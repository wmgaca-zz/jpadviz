package lib.ui.frames;

import lib.types.LabelConfig;
import lib.types.PAD;
import lib.ui.menus.MainMenu;
import lib.ui.frames.base.Frame;
import lib.ui.panels.*;

import javax.swing.*;
import java.awt.*;

import static lib.utils.Logging.log;

public class DynamicFrame extends Frame {

    public enum Layout {
        FULL,
        LABEL,
        RADAR
    }

    protected Layout currentLayout;

    public DynamicFrame(LabelConfig labelConfig) {
        super(labelConfig);
    }

    protected void setLayout(Layout layout) {
        if (layout == currentLayout) {
            return;
        }

        clearLayout();

        if (Layout.FULL == layout) {
            setFullLayout();
        } else if (Layout.LABEL == layout) {
            setMinimalLabelLayout();
        } else if (Layout.RADAR == layout) {
            setMinimalRadarLayout();
        }

        pack();

        currentLayout = layout;
    }

    protected void setMinimalRadarLayout() {
        setSize(new Dimension(200, 200));

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        addToContainer(topContainer, RadarPanel.getInstance(200, 200));
        addToContainer(topContainer, new StatePanel(200, 200));

        addToContainer(panelContainer, topContainer);
    }

    protected void setMinimalLabelLayout() {
        setSize(new Dimension(1000, 200));

        // Top: LabelPanel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        //addToContainer(topContainer, new LabelPanel(800, 50, labelConfig));
        addToContainer(topContainer, LabelPanel.getInstance(800, 50, labelConfig),
                       new StatePanel(200, 200));

        // Assemble panelContainer
        addToContainer(panelContainer, topContainer);
    }

    protected void setFullLayout() {
        setSize(new Dimension(1000, 800));

        // Top: LabelPanel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));

        //addToContainer(topContainer, new LabelPanel(600, 200, labelConfig));
        addToContainer(topContainer, LabelPanel.getInstance(600, 300, labelConfig));
        //addToContainer(topContainer, new MultipleRadarPanel(200, 200));
        addToContainer(topContainer, RadarPanel.getInstance(200, 200));

        // Middle left: SinglePADPanel for P, A, D
        JPanel middleLeftContainer = new JPanel();
        middleLeftContainer.setLayout(new BoxLayout(middleLeftContainer, BoxLayout.Y_AXIS));
        addToContainer(middleLeftContainer, new MultiplePADPanel(600, 200));
        addToContainer(middleLeftContainer, new PADPanel(PAD.Type.A, 600, 200));
        addToContainer(middleLeftContainer, new PADPanel(PAD.Type.D, 600, 200));

        JPanel middleRightContainer = new JPanel();
        middleRightContainer.setLayout(new BoxLayout(middleRightContainer, BoxLayout.Y_AXIS));
        addToContainer(middleRightContainer, new ValuePanel(PAD.Type.P, 200, 200));
        addToContainer(middleRightContainer, new ValuePanel(PAD.Type.A, 200, 200));
        addToContainer(middleRightContainer, new ValuePanel(PAD.Type.D, 200, 200));

        // Assemble middleContainer
        JPanel middleContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        addToContainer(middleContainer, middleLeftContainer);
        addToContainer(middleContainer, middleRightContainer);

        // Assemble panelContainer
        addToContainer(panelContainer, topContainer);
        addToContainer(panelContainer, middleContainer);
    }

    @Override
    protected void initPanelContainer() {
        super.initPanelContainer();

        setLayout(Layout.FULL);
    }

    @Override
    public void handleMenuAction(MainMenu.Action action) {
        switch (action) {
            case STAY_ON_TOP:
                toggleStayOnTop();
                break;
            case LAYOUT_FULL:
                setLayout(Layout.FULL);
                break;
            case LAYOUT_MIN_RADAR:
                setLayout(Layout.RADAR);
                break;
            case LAYOUT_MIN_LABEL:
                setLayout(Layout.LABEL);
                break;
            default:
                log("Unknown menu action.");
                break;
        }
    }
}