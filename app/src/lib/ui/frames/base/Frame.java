package lib.ui.frames.base;

import lib.types.LabelConfig;
import lib.types.PADState;
import lib.types.Palette;
import lib.ui.menus.MainMenu;

import static lib.utils.Logging.log;

import javax.swing.*;
import javax.swing.border.BevelBorder;
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
        log("Frame.feed()");
        panelContainer.repaint();
    }

    protected void addToContainer(JPanel container, Component... components) {
        for (Component component : components) {
            container.add(component);

            if (component instanceof Panel) {
                if (!panels.contains((Panel)component)) {
                    panels.add((Panel)component);
                }
            }
        }
    }

    /*
    protected void addToContainer(JPanel container, Component panel) {
        container.add(panel);

        if (panel instanceof Panel) {
            if (!panels.contains((Panel)panel)) {
                panels.add((Panel)panel);
            }
        }
    } //*/

    protected void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        initPanelContainer();
        setContentPane(panelContainer);
        setJMenuBar(MainMenu.getInstance(this));
    }

    public abstract void handleMenuAction(MainMenu.Action action);

    public void toggleStayOnTop() {
        setAlwaysOnTop(!isAlwaysOnTop());
    }

    protected void clearLayout() {
        panelContainer.removeAll();
    }

    protected void initPanelContainer() {
        // Main container
        panelContainer = new JPanel();
        setBackground(Palette.white);
        panelContainer.setBackground(Palette.white);
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        //setLayout(Layout.FULL);
    }
}