package lib.ui.frames;

import lib.types.LabelConfig;
import lib.types.PAD;
import lib.types.Palette;
import lib.ui.Menu;
import lib.ui.panels.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class FullDynamicFrame extends lib.ui.frames.base.Frame {

    public FullDynamicFrame(LabelConfig labelConfig) {
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
        addToContainer(topContainer, new MultiplePADPanel(200, 200));
        addToContainer(topContainer, new LabelPanel(600, 200, labelConfig));

        // Middle left: SingleRadarPanel
        JPanel middleLeftContainer = new JPanel();
        middleLeftContainer.setLayout(new BoxLayout(middleLeftContainer, BoxLayout.Y_AXIS));
        addToContainer(middleLeftContainer, new SingleRadarPanel(PAD.Type.P, 200, 200));
        addToContainer(middleLeftContainer, new SingleRadarPanel(PAD.Type.A, 200, 200));
        addToContainer(middleLeftContainer, new SingleRadarPanel(PAD.Type.D, 200, 200));

        // Middle right: SinglePADPanel for P, A, D
        JPanel middleRightContainer = new JPanel();
        middleRightContainer.setLayout(new BoxLayout(middleRightContainer, BoxLayout.Y_AXIS));
        addToContainer(middleRightContainer, new SinglePADPanel(PAD.Type.P, 600, 200));
        addToContainer(middleRightContainer, new SinglePADPanel(PAD.Type.A, 600, 200));
        addToContainer(middleRightContainer, new SinglePADPanel(PAD.Type.D, 600, 200));

        // Assemble middleContainer
        JPanel middleContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        addToContainer(middleContainer, middleLeftContainer);
        addToContainer(middleContainer,  middleRightContainer);

        // Assemble panelContainer
        addToContainer(panelContainer, topContainer);
        addToContainer(panelContainer, middleContainer);

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