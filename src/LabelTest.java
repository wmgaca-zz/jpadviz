import lib.types.LabelConfig;

import java.io.File;

public class LabelTest {

    public static File labelConfigFile = new File(PadServer.workingDir, "config/labels.xml");

    public static void main(String[] args) {
        System.out.println("Loading file...");
        LabelConfig labelConfig = new LabelConfig(LabelTest.labelConfigFile);
        System.out.println("File loaded...");
    }

}
