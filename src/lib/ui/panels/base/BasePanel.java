package lib.ui.panels.base;

import lib.types.PAD;
import lib.types.PADDataHandler;
import lib.types.PADState;
import lib.ui.Margin;
import lib.ui.NewPanelUpdater;

import javax.swing.*;
import java.awt.*;

/**
 * Basic type for all panels used by the visualiser.
 */
public abstract class BasePanel extends JPanel {

    /**
     * Data type visualised by the widget.
     */
    protected PAD.Type type;

    /**
     * Widget's label.
     */
    protected String label;

    /**                              x
     * Drawing margins.
     */
    protected Margin margin = new Margin(10, 10, 10, 10);

    protected PADDataHandler data = PADDataHandler.getInstance();

    /**
     * Initializes the widget.
     *
     * @param type Panel.type
     * @param width Widget's width resolution (in pixels)
     * @param height Widget's height resolution (in pixels)
     */
    public BasePanel(PAD.Type type, int width, int height) {
        this(type, width, height, true);
    }

    /**
     * Initializes the widget.
     *
     * @param type Panel.type
     * @param width Widget's width resolution (in pixels)
     * @param height Widget's height resolution (in pixels)
     */
    public BasePanel(PAD.Type type, int width, int height, boolean isRealTime) {
        setPreferredSize(new Dimension(width, height));
        this.type = type;
        this.label = PAD.getName(type);

        NewPanelUpdater.handle(this);
    }

    /**
     */
    public final void update() {
        repaint();
    }

    /**
     * Widget's drawing logic. This method is called by Panel.paintComponent.
     *
     * @param g2d Graphics object to be drawn
     */
    public abstract void customPaintComponent(Graphics2D g2d);

    /**
     * Widget's drawing logic.
     *
     * @see #paintComponent(java.awt.Graphics)
     *
     * @param graphics Graphics object to be drawn
     */
    @Override
    final public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D)graphics;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (!data.isEmpty()) {
            customPaintComponent((Graphics2D) graphics);
        }
    }

    /**
     * Drawing area width.
     *
     * @return Drawing area width in pixels.
     */
    public int getW() {
        return getWidth() - margin.left - margin.right;
    }

    /**
     * Drawing area height.
     *
     * @return Drawing area height in pixels.
     */
    public int getH() {
        return getHeight() - margin.top - margin.bottom;
    }

    /**
     * X coordinate of the drawing area's center.
     *
     * @see #getW()
     *
     * @return X coordinate of the drawing area's center.
     */
    public int getCenterX() {
        return margin.left + getW() / 2;
    }

    /**
     * Y coordinate of the drawing area's center.
     *
     * @see #getH()
     *
     * @return Y coordinate of the drawing area's center.
     */
    public int getCenterY() {
        return margin.top + getH() / 2;
    }

    /**
     * Event's X coordinate based on time of its occurrence.
     *
     * @param time Event's time stamp in milliseconds.
     * @param startTime Drawing area's start time.
     * @param endTime Drawing area's end time.
     *
     * @return Event's X coordinate.
     */
    final public int getXForTime(long time, long startTime, long endTime) {
        double timeSpan = (double)(endTime - startTime);
        double relativeTime = (double)(time - startTime);
        return margin.left + (int)((relativeTime / timeSpan) * (double)getW());
    }
}