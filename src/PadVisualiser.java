import lib.Utils;
import lib.forms.MainWindowForm;
import lib.types.ClientType;
import lib.types.LabelConfig;
import lib.types.packages.*;
import lib.types.packages.Package;
import lib.ui.DynamicFrame;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class PadVisualiser {

    private static String workingDir = System.getProperty("user.dir");
    private static File labelConfigFile = new File(PadVisualiser.workingDir, "config/labels.xml");

    private MainWindowForm mainForm = new MainWindowForm();
    private LabelConfig labelConfig = null;

    private DynamicFrame frame;

    private String serverHost;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInput inStream;

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

    private void closeNetworking() {
        try {
            this.socket.close();
        } catch (IOException error) {
            Utils.exitOnException(error, "Cannot close connection.");
        }
    }

    private boolean send(Package data) {
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

    private Package read() {
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


    private void dispatchPackage(Package data) {
        if (data instanceof PADPackage) {
            PADPackage pad = (PADPackage)data;
        } else if (data instanceof DataPackage) {
            DataPackage dat = (DataPackage)data;
        }

        System.out.println(String.format("Package: %s", data));
    }

    private void initUI() {

        this.frame = new DynamicFrame(this.labelConfig);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);

        /*
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.setVisible(true);
                }
           });
          //*/

        //JFrame frame = new JFrame("MyForm");
        /*
        JFrame frame = new DynamicFrame();
        frame.setContentPane(this.mainForm.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.getGraphics().drawLine(0, 0, 5, 5);
        //*/
    }

    private void updateUI() {
        try {

            this.frame.feed(PADPackage.getRandom().getState());

            Thread.sleep(Utils.getRandomGenerator().nextInt(2500) + 500);

            System.out.println("UpdateUI!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //this.setupNetworking();
        this.initUI();

        Package data;

        while (!this.mainForm.getExitApp()) {
            this.updateUI();
        }

        this.send(new HandshakePackage(ClientType.VISUALISER));

        while (!this.mainForm.getExitApp() && (data = this.read()) != null) {
            this.dispatchPackage(data);
            this.updateUI();
        }

        this.send(new EndPackage());


        this.closeNetworking();
    }
}
