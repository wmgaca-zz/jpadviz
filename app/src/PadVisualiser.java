import lib.types.PADDataHandlerContainer;
import lib.ui.frames.MultipleChannelOfflineFrame;
import lib.ui.frames.SingleChannelOnlineFrame;
import lib.ui.frames.SingleChannelOfflineFrame;
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

/**
 * PAD visualiser
 */
public class PadVisualiser {

    /**
     * App working dir
     */
    protected static String workingDir = System.getProperty("user.dir");

    /**
     * Label config file
     */
    protected static File labelConfigFile = new File(PadVisualiser.workingDir, "config/labels.xml");

    /**
     * Visualiser config file
     */
    protected static File configFile = new File(PadVisualiser.workingDir, "config/visualiser-settings.xml");

    /**
     * Visualiser config handler
     */
    protected VisualiserConfig config = new VisualiserConfig(configFile);

    /**
     * Label config handler
     */
    protected LabelConfig labelConfig = null;

    /**
     * Main form
     */
    protected MainWindowForm mainForm = new MainWindowForm();

    /**
     * Main frame
     */
    protected Frame frame;

    /**
     * Server host
     */
    protected String serverHost;

    /**
     * Server port
     */
    protected int serverPort;

    /**
     * Connection socket
     */
    protected Socket socket;

    /**
     * Output stream (for sending packages to server)
     */
    protected ObjectOutputStream output;

    /**
     * Input stream (for reading packages sent by the server)
     */
    protected ObjectInput input;


    /**
     * PAD states handler
     */
    protected PADDataHandlerContainer dataHandlerContainer;

    /**
     * Entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        // Read the labels
        LabelConfig labelConfig = new LabelConfig(labelConfigFile);

        PadVisualiser padVisualiser = new PadVisualiser(labelConfig);
        padVisualiser.run();
    }

    /**
     * @param labelConfig Label config object
     */
    public PadVisualiser(LabelConfig labelConfig) {
        this.labelConfig = labelConfig;
    }

    /**
     * Configure networking, connect to the server and set up streams for writing and reading
     */
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
            Utils.exitOnException(e, "Cannot get I/O");
        }

        log("Network set up.");
    }

    /**
     * Tear down server connection
     */
    protected void closeNetworking() {
        try {
            this.socket.close();
        } catch (IOException error) {
            Utils.exitOnException(error, "Cannot close connection.");
        }
    }

    /**
     * Send a package to the server
     *
     * @param data Package
     * @return true on success, false otherwise
     */
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

    /**
     * Read a package from server
     *
     * @return Package
     */
    protected Package read() {
        Package data = null;

        try {
            data = (Package) input.readObject();
        } catch (ClassNotFoundException error) {
            log("Package type not recognized: %s", data);
        } catch (EOFException e) {
            return null;
        } catch (IOException error) {
            error.printStackTrace();
            log("Cannot read package...");
            return null;
        }

        return data;
    }

    /**
     * Package handling logic
     *
     * @param data
     */
    protected void dispatchPackage(Package data) {
        if (data instanceof PADPackage) {
            dataHandlerContainer.feed(((PADPackage)data).getState());

            if (!config.isRealTime()) {
                dataHandlerContainer.get().setWindow(SingleChannelOfflineFrame.DEFAULT_WINDOW, SingleChannelOfflineFrame.DEFAULT_ZOOM);
            }
        }
    }

    /**
     * Init user interface for real time mode
     */
    protected void initRealTimeUI() {
        frame = new SingleChannelOnlineFrame(labelConfig);
    }


    /**
     * Init user interface for off-line mode
     */
    protected void initLoadUI() {
        if (config.isOneChannel()) {
            frame = new SingleChannelOfflineFrame(labelConfig);
        } else if (config.isMultiChannel()) {
            frame = new MultipleChannelOfflineFrame(labelConfig);
        }
    }

    /**
     * Run the app in real time mode
     */
    protected void runRealTime() {
        send(new HandshakePackage(Client.VISUALISER));
        Package data;
        while (!mainForm.getExitApp() && (data = read()) != null) {
            log("got package...");
            dispatchPackage(data);
        }
        send(new EndPackage());
    }

    /**
     * Run the app in off-line mode
     */
    protected void runLoad() {
        log("Sending request package...");

        send(new RequestDataPackage(config.getExperimentId(), config.getChannels())); //config.getExperimentId(), config.getCId()));
        Package data;

        while (!mainForm.getExitApp() && (data = read()) != null) {
            dispatchPackage(data);
        }
        log("Data loaded.");
    }

    /**
     * Run the app
     */
    public void run() {
        setupNetworking();

        if (config.isRealTime()) {
            dataHandlerContainer = PADDataHandlerContainer.getInstance(true);
            initRealTimeUI();
            runRealTime();
        } else {
            dataHandlerContainer = PADDataHandlerContainer.getInstance(false);
            dataHandlerContainer.init(config.getChannels());

            initLoadUI();

            runLoad();

            log("timeAutoTuning...");

            dataHandlerContainer.autoTime();
        }

        closeNetworking();
    }
}
