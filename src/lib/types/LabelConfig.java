package lib.types;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import lib.ConfigHandler;
import lib.Utils;
import lib.types.exceptions.PADConfigException;

public class LabelConfig {

    private List<Label> labels = new ArrayList<Label>();

    public LabelConfig(File labelConfigFile) {
        Document doc = ConfigHandler.parseDocument(labelConfigFile);
        if (doc == null) {
            Utils.exitOnException(new PADConfigException());
        }

        NodeList labelNodes = null;

        // Read the labels
        try {
            labelNodes = doc.getElementsByTagName("label");
        } catch (NullPointerException error) {
            Utils.exitOnException(error);
        }

        for (int i = 0; i < labelNodes.getLength(); ++i) {
            this.labels.add(Label.fromNode(labelNodes.item(0)));
        }
    }

    public List<Label> getLabels() {
        return this.labels;
    }

}
