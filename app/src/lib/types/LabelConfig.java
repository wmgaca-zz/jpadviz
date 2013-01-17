package lib.types;

import lib.utils.Utils;
import lib.config.ConfigHandler;
import lib.exceptions.PADConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Handling of label information
 */
public class LabelConfig {

    /**
     * List of used labels.
     */
    private List<Label> labels = new ArrayList<Label>();

    /**
     * Default constructor.
     *
     * @param labelConfigFile Label XML config
     */
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

    /**
     * @return All labels
     */
    public List<Label> getLabels() {
        return this.labels;
    }

    /**
     * Get labels matching given state.
     *
     * @param state PAD state
     * @return Labels matching given state
     */
    public Label getMatchingLabel(PADState state) {
        for (Label label : this.labels) {
              if (label.match(state)) {
                  return label;
              }
        }

        return new Label("Empty");
    }

}
