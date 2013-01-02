package lib.ui.frames;

import lib.types.LabelConfig;
import lib.types.PAD;
import lib.types.Palette;
import lib.ui.Menu;
import lib.ui.frames.base.Frame;
import lib.ui.panels.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

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

    protected void clearLayout() {
        panelContainer.removeAll();
    }

    protected void setLayout(Layout layout) {
        if (layout == currentLayout) {
            return;
        }

        if (Layout.FULL == layout) {
            setFullLayout();
        } else if (Layout.LABEL == layout) {
            setMinimalLayout();
        } else if (Layout.RADAR == layout) {
            setMinimalLayout();
        }

        currentLayout = layout;
    }

    protected void setMinimalLayout() {
        clearLayout();

        setSize(new Dimension(1000, 200));

        // Top: LabelPanel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        addToContainer(topContainer, new LabelPanel(800, 50, labelConfig));

        // Assemble panelContainer
        addToContainer(panelContainer, topContainer);

        pack();
    }

    protected void setFullLayout() {
        clearLayout();

        setSize(new Dimension(1000, 800));

        // Top: LabelPanel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));

        addToContainer(topContainer, new LabelPanel(600, 200, labelConfig));
        addToContainer(topContainer, new MultipleRadarPanel(200, 200));

        // Middle left: SinglePADPanel for P, A, D
        JPanel middleLeftContainer = new JPanel();
        middleLeftContainer.setLayout(new BoxLayout(middleLeftContainer, BoxLayout.Y_AXIS));
        addToContainer(middleLeftContainer, new SinglePADPanel(PAD.Type.P, 600, 200));
        addToContainer(middleLeftContainer, new SinglePADPanel(PAD.Type.A, 600, 200));
        addToContainer(middleLeftContainer, new SinglePADPanel(PAD.Type.D, 600, 200));

        JPanel middleRightContainer = new JPanel();
        middleRightContainer.setLayout(new BoxLayout(middleRightContainer, BoxLayout.Y_AXIS));
        addToContainer(middleRightContainer, new SingleLabelPanel(PAD.Type.P, 200, 200));
        addToContainer(middleRightContainer, new SingleLabelPanel(PAD.Type.A, 200, 200));
        addToContainer(middleRightContainer, new SingleLabelPanel(PAD.Type.D, 200, 200));

        // Assemble middleContainer
        JPanel middleContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        addToContainer(middleContainer, middleLeftContainer);
        addToContainer(middleContainer, middleRightContainer);

        // Assemble panelContainer
        addToContainer(panelContainer, topContainer);
        addToContainer(panelContainer, middleContainer);

        pack();
    }

    @Override
    protected void initPanelContainer() {
        // Main container
        panelContainer = new JPanel();
        setBackground(Palette.white);
        panelContainer.setBackground(Palette.white);
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        setLayout(Layout.FULL);
    }

    @Override
    public void handleMenuAction(Menu.Action action) {
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