package lib.ui.frames.base;

import lib.types.LabelConfig;
import lib.types.PADState;
import lib.types.Palette;
import lib.ui.Menu;
import lib.ui.panels.base.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Frame extends JFrame {

    public static int DEFAULT_WIDTH = 800;
    public static int DEFAULT_HEIGHT = 800;

    protected LabelConfig labelConfig;
    protected JPanel panelContainer;

    protected ArrayList<Panel> panels = new ArrayList<Panel>();

    protected boolean alwaysOnTop = false;

    public Frame(LabelConfig labelConfig) {
        this(labelConfig, new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public Frame(LabelConfig labelConfig, Dimension size) {
        // Set labels configuration
        setLabelConfig(labelConfig);

        // Set window size
        setSize(size);

        // Init layout & components
        init();

        // Always on top...
        setAlwaysOnTop(alwaysOnTop);

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
        getContentPane().setBackground(Palette.white);

        for (Panel panel : panels) {
            panel.feed(state);
        }

        panelContainer.repaint();
    }

    protected void addToContainer(JPanel container, Component panel) {
        container.add(panel);

        if (panel instanceof Panel) {
            if (!panels.contains((Panel)panel)) {
                panels.add((Panel)panel);
            }
        }
    }

    protected void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        initPanelContainer();
        setContentPane(panelContainer);
        setJMenuBar(Menu.getInstance(this));
    }

    protected abstract void initPanelContainer();

    public void handleMenuAction(Menu.Action action) {
        switch (action) {
            case STAY_ON_TOP:
                toggleStayOnTop();
                break;
        }
    }

    public void toggleStayOnTop() {
        setAlwaysOnTop(!isAlwaysOnTop());
    }

}