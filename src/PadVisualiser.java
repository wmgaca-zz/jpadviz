import lib.ui.frames.DynamicFrame;
import lib.utils.Utils;
import lib.config.VisualiserConfig;
import lib.forms.MainWindowForm;
import lib.types.Client;
import lib.types.LabelConfig;
import lib.net.packages.*;
import lib.net.packages.base.Package;
import lib.ui.frames.base.Frame;
import java.io.*;
import java.net.Socket;

import static lib.utils.Logging.log;

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
    protected ObjectOutputStream output;
    protected ObjectInput input;

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
        serverHost = PadDummyClient.config.getHost();
        serverPort = PadDummyClient.config.getPort();

        log("Connecting to %s:%s", this.serverHost, this.serverPort);
        while (true) {
            try {
                socket = new Socket(serverHost, serverPort);
            } catch (IOException e) {
                log("Cannot connect. Retrying...");
                continue;
            }
            log("Connection established with %s:%s", this.serverHost, this.serverPort);
            break;
        }

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Utils.exitOnException(e);
            log("Cannot get I/O");
        }

        log("Network set up.");
    }

    protected void closeNetworking() {
        try {
            this.socket.close();
        } catch (IOException error) {
            Utils.exitOnException(error, "Cannot close connection.");
        }
    }

    protected boolean send(Package data) {
        if (null == this.output) {
            this.setupNetworking();
        }

        try {
            this.output.writeObject(data);
        } catch (IOException e) {
            log("Cannot send package: %s", data);
            return false;
        }

        return true;
    }

    protected Package read() {
        Package data = null;

        try {
            data = (Package) input.readObject();
        } catch (ClassNotFoundException error) {
            log("Package type not recognized: %s", data);
        } catch (IOException error) {
            error.printStackTrace();
            log("Cannot read package...");

            return null;
        }

        return data;
    }

    protected void dispatchPackage(Package data) {
        if (data instanceof PADPackage) {
            PADPackage pad = (PADPackage)data;
            frame.feed(((PADPackage) data).getState());
        }
    }

    protected void initUI() {
        frame = new DynamicFrame(labelConfig);
    }

    protected void runRealTime() {
        send(new HandshakePackage(Client.VISUALISER));
        Package data;
        while (!mainForm.getExitApp() && (data = read()) != null) {
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
        log("Fetched!");
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

    public void toggleLayout() {
        frame.setVisible(true);
    }
}
