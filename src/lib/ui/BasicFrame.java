package lib.ui;

import lib.types.LabelConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class BasicFrame extends JFrame {

    public static int DEFAULT_WIDTH = 800;
    public static int DEFAULT_HEIGHT = 800;

    protected LabelConfig labelConfig;
    protected JPanel panelContainer;
    private ArrayList<BasicPanel> panels = new ArrayList<BasicPanel>();

    public BasicFrame(LabelConfig labelConfig) {
        setLabelConfig(labelConfig);
        setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public BasicFrame(LabelConfig labelConfig, Dimension size) {
        this(labelConfig);

        setSize(size);
    }

    protected void setLabelConfig(LabelConfig value) {
        labelConfig = value;
    }

    public LabelConfig getLabelConfig() {
        return labelConfig;
    }

    protected void initComponents() {

    }

}
