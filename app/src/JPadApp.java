import com.sun.servicetag.SystemEnvironment;
import lib.types.LabelConfig;
import java.io.File;
import java.io.StringReader;

public class JPadApp {

    /**
     * App working dir
     */
    protected static String workingDir = System.getProperty("user.dir");

    /**
     * Label config file
     */
    protected static File labelConfigFile = new File(PadVisualiser.workingDir, "config/labels.xml");

    public static void main(String[] args) {
        LabelConfig labelConfig = new LabelConfig(labelConfigFile);

        String cmd;
        if (args.length > 0) {
            cmd = args[0].toLowerCase();
        } else {
            cmd = "";
        }

        if (cmd.equals("server") || cmd.equals("-s") || cmd.equals("--server")) {
            PadServer.init();
            PadServer.run();
        } else if (cmd.equals("mock") || cmd.equals("-m") || cmd.equals("--mock")) {
            try {
                PadDummyClient.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (cmd.equals("visualiser") || cmd.equals("-v") || cmd.equals("--visualiser")) {
            (new PadVisualiser(labelConfig)).run();
        } else {
            System.out.println(
                "Usage:\n\n" +
                "    java -jar jpadapp [option]\n\n" +
                "Options:\n\n" +
                "    server, -s, --server\n" +
                "        Run the PadServer\n\n" +
                "    visualiser, -v, --visualiser\n" +
                "        Run the PadVisualiser\n\n" +
                "    mock, -m, --mock\n" +
                "        Run the mock client.");
        }

    }

}
