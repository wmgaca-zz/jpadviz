package lib.ui;

import lib.types.LabelConfig;
import lib.types.PADState;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;

public abstract class BasicFrame extends JFrame {

    public static int DEFAULT_WIDTH = 800;
    public static int DEFAULT_HEIGHT = 800;

    protected LabelConfig labelConfig;
    protected JPanel panelContainer;

    protected ArrayList<BasicPanel> panels = new ArrayList<BasicPanel>();

    public BasicFrame(LabelConfig labelConfig) {
        this(labelConfig, new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public BasicFrame(LabelConfig labelConfig, Dimension size) {
        // Set labels configuration
        setLabelConfig(labelConfig);

        // Set window size
        setSize(size);

        // Init layout & components
        init();

        // Size the window to match components layouts
        pack();
    }

    public LabelConfig getLabelConfig() {
        return labelConfig;
    }

    protected void setLabelConfig(LabelConfig value) {
        labelConfig = value;
    }

    public final void feed(PADState state) {
        for (BasicPanel panel : panels) {
            panel.feed(state);
        }
        panelContainer.repaint();
    }

    protected void addToContainer(BasicPanel panel) {
        panels.add(panel);
        panelContainer.add(panel);
    }

    protected void init() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initPanelContainer();
        initComponents();
        this.setContentPane(panelContainer);
    }

    protected void initPanelContainer() {
        panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.X_AXIS));
        panelContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }

    protected abstract void initComponents();

}
