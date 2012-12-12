package lib.types;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Label {

    public String name;

    public float pMin;
    public float pMax;
    public float aMin;
    public float aMax;
    public float dMin;
    public float dMax;

    public Label() {
        this.name = "Unknown";

        this.pMin = this.aMin = this.dMin = -1.0f;
        this.pMax = this.aMax = this.dMax = 1.0f;
    }

    public Label(String name) {
        this();

        if (name != null) {
            this.name = name;
        }
    }

    public Label(String name, float pMin, float pMax, float aMin, float aMax, float dMin, float dMax) {
        this(name);

        this.pMin = pMin;
        this.pMax = pMax;
        this.aMin = aMin;
        this.aMax = aMax;
        this.dMin = dMin;
        this.dMax = dMax;
    }

    public String toString() {
        return String.format(
                "<Label(name=%s,p=[%s,%s],a=[%s,%s],d=[%s,%s])>",
                this.name, this.pMin, this.pMax, this.aMin, this.aMax, this.dMin, this.dMax);
    }

    public void setPMax(String value) {
        if (value == null) {
            return;
        }

        this.pMax = Float.parseFloat(value);
    }

    public void setPMin(String value) {
        if (value == null) {
            return;
        }

        this.pMin = Float.parseFloat(value);
    }

    public void setAMax(String value) {
        if (value == null) {
            return;
        }

        this.aMax = Float.parseFloat(value);
    }

    public void setAMin(String value) {
        if (value == null) {
            return;
        }

        this.aMin = Float.parseFloat(value);
    }

    public void setDMax(String value) {
        if (value == null) {
            return;
        }

        this.dMax = Float.parseFloat(value);
    }

    public void setDMin(String value) {
        if (value == null) {
            return;
        }

        this.dMin = Float.parseFloat(value);
    }

    private static String getAttrFromNode(Node attrNode, String attrName) {
        if (!attrNode.hasAttributes()) {
            return null;
        }

        Node node = attrNode.getAttributes().getNamedItem(attrName);
        return (node != null) ? node.getNodeValue() : null;
    }

    public static Label fromNode(Node labelNode) {
        Label label = new Label(getAttrFromNode(labelNode, "name"));

        NodeList attrNodeList = labelNode.getChildNodes();

        for (int i = 0; i < attrNodeList.getLength(); ++i) {
            Node attrNode = attrNodeList.item(i);

            if (attrNode.getNodeName().toLowerCase().equals("p")) {
                label.setPMax(Label.getAttrFromNode(attrNode, "max"));
                label.setPMin(Label.getAttrFromNode(attrNode, "min"));
            } else if (attrNode.getNodeName().toLowerCase().equals("a")) {
                label.setAMax(Label.getAttrFromNode(attrNode, "max"));
                label.setAMin(Label.getAttrFromNode(attrNode, "min"));
            } else if (attrNode.getNodeName().toLowerCase().equals("d")) {
                label.setDMax(Label.getAttrFromNode(attrNode, "max"));
                label.setDMin(Label.getAttrFromNode(attrNode, "min"));
            }
        }

        return label;
    }

}
