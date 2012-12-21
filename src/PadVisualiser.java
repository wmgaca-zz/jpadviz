import lib.Utils;
import lib.config.VisualiserConfig;
import lib.forms.MainWindowForm;
import lib.types.ClientType;
import lib.types.LabelConfig;
import lib.types.packages.*;
import lib.types.packages.base.Package;
import lib.ui.frames.FullDynamicFrame;
import lib.ui.frames.MinimizedDynamicFrame;
import lib.ui.frames.base.Frame;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class PadVisualiser {

    protected static String workingDir = System.getProperty("user.dir");
    protected static File labelConfigFile = new File(PadVisualiser.workingDir, "config/labels.xml");
    protected static File configFile = new File(PadVisualiser.workingDir, "config/visualiser-settings.xml");

    protected VisualiserConfig config = new VisualiserConfig(configFile);

    protected MainWindowForm mainForm = new MainWindowForm();
    protected LabelConfig labelConfig = null;

    protected Frame frame;

    protected String serverHost;
    protected int serverPort;
    protected Socket socket;
    protected ObjectOutputStream outStream;
    protected ObjectInput inStream;

    public static void main(String[] args) {

        // Read the labels
        LabelConfig labelConfig = new LabelConfig(labelConfigFile);


        PadVisualiser padVisualiser = new PadVisualiser(labelConfig);
        padVisualiser.run();
    }

    public PadVisualiser(LabelConfig labelConfig) {
        this.labelConfig = labelConfig;
    }

    public void setupNetworking() {
        this.serverHost = PadDummyClient.config.getHost();
        this.serverPort = PadDummyClient.config.getPort();

        System.out.println(String.format("Connecting to %s:%s", this.serverHost, this.serverPort));

        while (true) {
            try {
                this.socket = new Socket(this.serverHost, this.serverPort);
            } catch (IOException e) {
                System.out.println("Cannot connect. Retrying...");
                continue;
            }

            System.out.println(String.format("Connection established with %s:%s", this.serverHost, this.serverPort));
            break;
        }

        try {
            this.outStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.inStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            Utils.exitOnException(e);
        }
    }

    protected void closeNetworking() {
        try {
            this.socket.close();
        } catch (IOException error) {
            Utils.exitOnException(error, "Cannot close connection.");
        }
    }

    protected boolean send(Package data) {
        if (null == this.outStream) {
            this.setupNetworking();
        }

        try {
            this.outStream.writeObject(data);
        } catch (IOException e) {
            System.out.println(String.format("Cannot send package: %s", data));
            return false;
        }

        return true;
    }

    protected Package read() {
        Package data = null;

        try {
            data = (Package)this.inStream.readObject();
        } catch (ClassNotFoundException error) {
            System.out.println(String.format("Package type not recognized: %s", data));
        } catch (IOException error) {
            System.out.println("Cannot read package...");
            return null;
        }

        return data;
    }

    protected void dispatchPackage(Package data) {
        if (data instanceof PADPackage) {
            PADPackage pad = (PADPackage)data;
            frame.feed(((PADPackage) data).getState());
        }

        System.out.println(String.format("Package: %s", data));
    }

    protected void initUI() {
        if (config.getLayout().equals("min")) {
            frame = new MinimizedDynamicFrame(labelConfig);
        } else {
            frame = new FullDynamicFrame(labelConfig);
        }
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    protected void runRealTime() {
        send(new HandshakePackage(ClientType.VISUALISER));

        Package data;
        while (!this.mainForm.getExitApp() && (data = this.read()) != null) {
            dispatchPackage(data);
        }

        send(new EndPackage());
    }

    protected void runLoad() {
        send(new RequestDataPackage(-1));

        Package data;
        while (!this.mainForm.getExitApp() && (data = this.read()) != null) {
            dispatchPackage(data);
        }

        System.out.println("Fetched!");

        send(new EndPackage());
    }

    public void run() {
        this.setupNetworking();
        this.initUI();

        if (config.getMode().equals("auto")) {
            runRealTime();
        } else {
            runLoad();
        }

        this.closeNetworking();
    }
}
