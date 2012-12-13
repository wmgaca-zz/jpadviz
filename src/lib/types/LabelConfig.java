package lib.types;

import lib.Utils;
import lib.config.ConfigHandler;
import lib.types.exceptions.PADConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            this.labels.add(Label.fromNode(labelNodes.item(i)));
        }
    }

    public List<Label> getLabels() {
        return this.labels;
    }

    public Label getMatchingLabel(PADState state) {
        List<Label> matching = new ArrayList<Label>();

        for (Label label : this.labels) {
              if (label.match(state)) {
                  matching.add(label);
              }
        }

        if (0 == matching.size()) {
            return new Label("Empty");
        }

        Label result = null;
        float distance = 0;
        for (Label label : matching) {
            if (null == result) {
                result = label;
                distance = label.calculateDistance(state);
            } else {
                if (distance > label.calculateDistance(state)) {
                    result = label;
                    distance = label.calculateDistance(state);
                }
            }
        }

        return result;
    }

}
