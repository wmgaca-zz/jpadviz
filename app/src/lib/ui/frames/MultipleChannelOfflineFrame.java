package lib.ui.frames;

import lib.types.LabelConfig;
import lib.types.PAD;
import lib.types.PADDataHandlerContainer;
import lib.ui.frames.base.Frame;
import lib.ui.menus.MainMenu;
import lib.ui.panels.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static lib.utils.Logging.log;

public class MultipleChannelOfflineFrame extends Frame {

    public static int DEFAULT_ZOOM = 10;
    public static int DEFAULT_WINDOW = 50;

    public enum Layout {
        FULL
    }

    protected Layout currentLayout;

    public MultipleChannelOfflineFrame(LabelConfig labelConfig) {
        super(labelConfig);
    }

    protected void setLayout(Layout layout) {
        if (layout == currentLayout) {
            return;
        }

        clearLayout();

        if (Layout.FULL == layout) {
            setFullLayout();
        }

        pack();

        currentLayout = layout;
    }

    protected void setFullLayout() {
        setSize(new Dimension(1000, 800));

        // King of the hill,
        // Top of the heap,
        // A number one,
        // Top of the list [Controls container]
        //
        // It's up to you
        // New York, New York!
        JPanel controlsContainer = new JPanel();

        JLabel zoomLabel = new JLabel("Zoom:");
        JLabel windowLabel = new JLabel("Window:");

        final JSlider currentZoomSlider = new JSlider(JSlider.HORIZONTAL, 5, 100, DEFAULT_ZOOM);
        final JSlider currentWindowSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, DEFAULT_WINDOW);

        currentZoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                int middle = currentWindowSlider.getValue();
                int zoom = ((JSlider)event.getSource()).getValue();

                PADDataHandlerContainer.getInstance().setWindow(middle, zoom);
            }
        });

        currentWindowSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                int middle = ((JSlider)event.getSource()).getValue();
                int zoom = currentZoomSlider.getValue();

                PADDataHandlerContainer.getInstance().setWindow(middle, zoom);
            }
        });

        PADDataHandlerContainer.getInstance().setWindow(currentWindowSlider.getValue(), currentZoomSlider.getValue());

        addToContainer(
                controlsContainer,
                zoomLabel, currentZoomSlider,
                windowLabel, currentWindowSlider);

        /*

        final JTextArea timeStartTextArea = new JTextArea(String.valueOf(PADDataHandler.getInstance().getCurrentStartTime()));
        timeStartTextArea.setSize(new Dimension(100, 20));

        timeStartTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                log("value: %s", timeStartTextArea.getText());
            }
        });

        timeStartTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                log("Typed");
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                log("Pressed");
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                log("Released");
            }
        });

        JTextArea timeEndTextArea = new JTextArea(String.valueOf(PADDataHandler.getInstance().getCurrentEndTime()));
        timeEndTextArea.setSize(new Dimension(100, 20));

        JLabel timeSpanLabel = new JLabel(String.format("%s - %s", PADDataHandler.getInstance().getCurrentStartTime(), PADDataHandler.getInstance().getCurrentEndTime()));
        addToContainer(controlsContainer, timeStartTextArea, timeEndTextArea);
        //*/

        // Top: LabelPanel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));

        addToContainer(topContainer, new MultipleLabelPanel(600, 300, labelConfig),
                                     new MultipleMethodsPanel(200, 200));

        // Middle left: SinglePADPanel for P, A, D
        JPanel middleLeftContainer = new JPanel();
        middleLeftContainer.setLayout(new BoxLayout(middleLeftContainer, BoxLayout.Y_AXIS));
        addToContainer(middleLeftContainer, new MultiplePADPanel(PAD.Type.P, 600, 200),
                                            new MultiplePADPanel(PAD.Type.A, 600, 200),
                                            new MultiplePADPanel(PAD.Type.D, 600, 200));
                                            //new PADPanel(PAD.Type.A, 600, 200),
                                            //new PADPanel(PAD.Type.D, 600, 200));

        //*
        JPanel middleRightContainer = new JPanel();
        middleRightContainer.setLayout(new BoxLayout(middleRightContainer, BoxLayout.Y_AXIS));
        addToContainer(middleRightContainer, new MultipleValuePanel(PAD.Type.P, 200, 200),
                                             new MultipleValuePanel(PAD.Type.A, 200, 200),
                                             new MultipleValuePanel(PAD.Type.D, 200, 200));
        //*/

        // Assemble middleContainer
        JPanel middleContainer = new JPanel();
        //topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        addToContainer(middleContainer, middleLeftContainer,
                                        middleRightContainer);
        //*/

        // Assemble panelContainer
        addToContainer(panelContainer, controlsContainer,
                                       topContainer,
                                       middleContainer);
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
            default:
                log("Unknown menu action.");
                break;
        }
    }
}