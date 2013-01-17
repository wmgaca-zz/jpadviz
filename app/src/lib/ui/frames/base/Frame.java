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

/**
 * Base for all application's frames.
 */
public abstract class Frame extends JFrame {

    /**
     * Default frame width. In pixels.
     */
    public static int DEFAULT_WIDTH = 800;

    /**
     * Default frame height. In pixels.
     */
    public static int DEFAULT_HEIGHT = 800;

    /**
     * Label config instance.
     */
    protected LabelConfig labelConfig;

    /**
     * Main panel container.
     */
    protected JPanel panelContainer;

    /**
     * Frame's panels.
     */
    protected ArrayList<Panel> panels = new ArrayList<Panel>();

    /**
     * Always on top flag.
     */
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

    /**
     * @return Label config.
     */
    public LabelConfig getLabelConfig() {
        return labelConfig;
    }

    protected void setLabelConfig(LabelConfig value) {
        labelConfig = value;
    }

    /**
     * Add components to a panel.
     *
     * @param container Component container
     * @param components Components to add
     */
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

    /**
     * Init frame
     */
    protected void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        initPanelContainer();
        setContentPane(panelContainer);
        setJMenuBar(MainMenu.getInstance(this));
    }

    /**
     * Handle menu actions, invoked by the menu object.
     *
     * @param action
     */
    public abstract void handleMenuAction(MainMenu.Action action);

    /**
     * Switch the "stay on top" option.
     */
    public void toggleStayOnTop() {
        setAlwaysOnTop(!isAlwaysOnTop());
    }

    /**
     * Remove all components.
     */
    protected void clearLayout() {
        panelContainer.removeAll();
    }

    /**
     * Init main panel container.
     */
    protected void initPanelContainer() {
        // Main container
        panelContainer = new JPanel();
        setBackground(Palette.white);
        panelContainer.setBackground(Palette.white);
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }
}