package lib.ui.frames;

import lib.types.LabelConfig;
import lib.types.Palette;
import lib.ui.Menu;
import lib.ui.frames.base.Frame;
import lib.ui.panels.LabelPanel;

import javax.swing.*;

public class SimpleDynamicFrame extends Frame {

    public SimpleDynamicFrame(LabelConfig labelConfig) {
        super(labelConfig);
    }

    @Override
    protected void initPanelContainer() {
        // Main container
        panelContainer = new JPanel();
        setBackground(Palette.white);
        addToContainer(panelContainer, new LabelPanel(400, 200, labelConfig));
    }

    @Override
    public void handleMenuAction(Menu.MenuAction action) {

    }
}