package lib.ui.frames;

import lib.types.LabelConfig;
import lib.types.Palette;
import lib.ui.Menu;
import lib.ui.frames.base.Frame;
import lib.ui.panels.LabelPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.util.ArrayList;

import static lib.utils.Logging.log;

public class MinimizedDynamicFrame extends lib.ui.frames.base.Frame {

    protected ArrayList<Frame> frames;

    public MinimizedDynamicFrame(ArrayList<Frame> frames, LabelConfig labelConfig) {
        super(labelConfig);
        this.frames = frames;
    }

    @Override
    protected void initPanelContainer() {
        // Main container
        panelContainer = new JPanel();
        setBackground(Palette.white);
        panelContainer.setBackground(Palette.white);
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // Top: LabelPanel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        addToContainer(topContainer, new LabelPanel(800, 50, labelConfig));

        // Assemble panelContainer
        addToContainer(panelContainer, topContainer);
    }

    @Override
    public void handleMenuAction(Menu.Action action) {
        switch (action) {
            case STAY_ON_TOP:
                log("Stay on top!!! 111 MIBB");
                log("frames %s: %s", frames.size(), frames);
                for (Frame frame : frames) {
                    log("Toggle frame %s", frame);
                    frame.setVisible(!frame.isVisible());
                }
                break;
            //setAlwaysOnTop(isAlwaysOnTop());
            //break;
            default:
                log("Unknown menu action.");
        }
    }
}