package lib.ui.frames;

import lib.types.LabelConfig;
import lib.types.PAD;
import lib.types.Palette;
import lib.ui.Menu;
import lib.ui.panels.LabelPanel;
import lib.ui.panels.MultiplePADPanel;
import lib.ui.panels.SinglePADPanel;
import lib.ui.panels.SingleRadarPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MinimizedDynamicFrame extends lib.ui.frames.base.Frame {

    public MinimizedDynamicFrame(LabelConfig labelConfig) {
        super(labelConfig);
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
    public void handleMenuAction(Menu.MenuAction action) {
        switch (action) {
            case StayOnTop:
                System.out.println("Stay on top!!!");
                setAlwaysOnTop(!isAlwaysOnTop());
                break;
            default:
                System.out.println("Unknown menu action.");

        }
    }
}