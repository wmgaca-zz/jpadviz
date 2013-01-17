package lib.types;

import lib.utils.Utils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

import static lib.utils.Logging.log;

/**
 * Representation of a single label.
 */
public class Label {

    /**
     * Label name.
     */
    public String name;

    /**
     * Label color.
     */
    public Color color = Color.lightGray;

    /**
     * Minimum P value.
     */
    public float pMin;

    /**
     * Maximum P value.
     */
    public float pMax;

    /**
     * Minimum A value.
     */
    public float aMin;

    /**
     * Maximum A value.
     */
    public float aMax;

    /**
     * Minimum D value.
     */
    public float dMin;

    /**
     * Maximum D value.
     */
    public float dMax;

    /**
     * Default constructor: creates an empty label with a maximum value range.
     */
    public Label() {
        name = "Unknown";

        pMin = aMin = dMin = -1.0f;
        pMax = aMax = dMax = 1.0f;
    }

    /**
     * Initializes a label instance with a name.
     *
     * @param name
     */
    public Label(String name) {
        this();

        this.name = name;
    }

    /**
     * @return Label string representation
     */
    public String toString() {
        return String.format("<Label(name=%s,p=[%s,%s],a=[%s,%s],d=[%s,%s])>",
                             name, pMin, pMax, aMin, aMax, dMin, dMax);
    }

    /**
     * @param value Maximum P value
     */
    public void setPMax(String value) {
        pMax = Float.parseFloat(value);
    }

    /**
     * @param value Minimum P value
     */
    public void setPMin(String value) {
        pMin = Float.parseFloat(value);
    }

    /**
     * @param value Maximum A value
     */
    public void setAMax(String value) {
        this.aMax = Float.parseFloat(value);
    }

    /**
     * @param value Minimum A value
     */
    public void setAMin(String value) {
        this.aMin = Float.parseFloat(value);
    }

    /**
     * @param value Maximum D value
     */
    public void setDMax(String value) {
        this.dMax = Float.parseFloat(value);
    }

    /**
     * @param value Minimum D value
     */
    public void setDMin(String value) {
        this.dMin = Float.parseFloat(value);
    }

    /**
     * @return Label name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Label color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param value Label color
     */
    public void setColor(Color value) {
        color = value;
    }

    /**
     * For config parsing purpose: return attribute's value from a node
     * @param attrNode Node objecet
     * @param attrName Attribute name
     * @return Attribute's value
     */
    private static String getAttrFromNode(Node attrNode, String attrName) {
        if (!attrNode.hasAttributes()) {
            return null;
        }

        Node node = attrNode.getAttributes().getNamedItem(attrName);
        return (node != null) ? node.getNodeValue() : null;
    }

    /**
     * Create a label from document's node.
     *
     * @param labelNode Document node representing a label
     * @return Label instance
     */
    public static Label fromNode(Node labelNode) {
        Label label = new Label(getAttrFromNode(labelNode, "name"));

        String hexColor = getAttrFromNode(labelNode, "color");
        if (null == hexColor) {
            label.setColor(Color.gray);
        } else {
            label.setColor(Color.decode(hexColor));
        }

        NodeList attrNodeList = labelNode.getChildNodes();

        for (int i = 0; i < attrNodeList.getLength(); ++i) {
            Node attrNode = attrNodeList.item(i);

            if (attrNode.getNodeName().toLowerCase().equals("p")) {
                label.setPMax(getAttrFromNode(attrNode, "max"));
                label.setPMin(getAttrFromNode(attrNode, "min"));
            } else if (attrNode.getNodeName().toLowerCase().equals("a")) {
                label.setAMax(getAttrFromNode(attrNode, "max"));
                label.setAMin(getAttrFromNode(attrNode, "min"));
            } else if (attrNode.getNodeName().toLowerCase().equals("d")) {
                label.setDMax(getAttrFromNode(attrNode, "max"));
                label.setDMin(getAttrFromNode(attrNode, "min"));
            }
        }

        return label;
    }

    /**
     * Check if a given PAD state matches the label.
     *
     * @param state PAD state
     * @return true if the state matches the label, false otherwise
     */
    public boolean match(PADState state) {
        if (state.getP().getValue() > pMax || state.getP().getValue() < pMin) {
            return false;
        } else if (state.getA().getValue() > aMax || state.getA().getValue() < aMin) {
            return false;
        } else if (state.getD().getValue() > dMax || state.getD().getValue() < dMin) {
            return false;
        }

        return true;
    }


}
