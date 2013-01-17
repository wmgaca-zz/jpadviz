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

/**
 * Frame for offline multiple channel mode.
 */
public class MultipleChannelOfflineFrame extends Frame {

    /**
     * Default zoom
     */
    public static int DEFAULT_ZOOM = 10;

    /**
     * Default window
     */
    public static int DEFAULT_WINDOW = 50;

    /**
     * Possible views
     */
    public enum Layout {
        FULL
    }

    /**
     * Current view
     */
    protected Layout currentLayout;

    public MultipleChannelOfflineFrame(LabelConfig labelConfig) {
        super(labelConfig);
    }

    /**
     * Set view
     *
     * @param layout View to be set
     */
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

    /**
     * Set view to full layout (all panels present).
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initPanelContainer() {
        super.initPanelContainer();

        setLayout(Layout.FULL);
    }

    /**
     * {@inheritDoc}
     *
     * @param action Menu action
     */
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