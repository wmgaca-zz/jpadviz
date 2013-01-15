package lib.types;

import lib.utils.Utils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

import static lib.utils.Logging.log;

public class Label {

    public String name;
    public Color color = Color.lightGray;

    public float pMin;
    public float pMax;
    public float aMin;
    public float aMax;
    public float dMin;
    public float dMax;

    public Label() {
        name = "Unknown";

        pMin = aMin = dMin = -1.0f;
        pMax = aMax = dMax = 1.0f;
    }

    public Label(String name) {
        this();

        this.name = name;
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
        return String.format("<Label(name=%s,p=[%s,%s],a=[%s,%s],d=[%s,%s])>",
                             name, pMin, pMax, aMin, aMax, dMin, dMax);
    }

    public void setPMax(String value) {
        pMax = Float.parseFloat(value);
    }

    public void setPMin(String value) {
        pMin = Float.parseFloat(value);
    }

    public void setAMax(String value) {
        this.aMax = Float.parseFloat(value);
    }

    public void setAMin(String value) {
        this.aMin = Float.parseFloat(value);
    }

    public void setDMax(String value) {
        this.dMax = Float.parseFloat(value);
    }

    public void setDMin(String value) {
        this.dMin = Float.parseFloat(value);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color value) {
        color = value;
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

    public float calculateDistance(PADState state) {
        float centerP = this.pMin + (this.pMax - this.pMin) / 2;
        float centerA = this.pMin + (this.aMax - this.aMin) / 2;
        float centerD = this.pMin + (this.dMax - this.dMin) / 2;

        return Utils.abs(state.getP().getValue() - centerP) + Utils.abs(state.getA().getValue() - centerA) + Utils.abs(state.getD().getValue() - centerD);
    }

}
