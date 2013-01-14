package lib.ui.panels.base;

import com.sun.servicetag.SystemEnvironment;
import lib.types.*;
import lib.ui.utils.PanelUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

import static lib.utils.Logging.log;

/**
 * Basic type for all panels used by the visualiser.
 */
public abstract class Panel extends JPanel {

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

    protected Graphics2D g2d = null;

    protected PADDataHandler data = PADDataHandlerContainer.getInstance().get();

    /**
     * Initializes the widget.
     *
     * @param type Panel.type
     * @param width Widget's width resolution (in pixels)
     * @param height Widget's height resolution (in pixels)
     */
    public Panel(PAD.Type type, int width, int height) {
        this(type, width, height, true);
    }

    /**
     * Initializes the widget.
     *
     * @param type Panel.type
     * @param width Widget's width resolution (in pixels)
     * @param height Widget's height resolution (in pixels)
     */
    public Panel(PAD.Type type, int width, int height, boolean isRealTime) {
        setPreferredSize(new Dimension(width, height));
        this.type = type;
        this.label = PAD.getName(type);

        PanelUpdater.handle(this);
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
    public void customPaintComponent(Graphics2D g2d) {
        customPaintComponent();
    }

    public void customPaintComponent() {

    }

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

        g2d = (Graphics2D)graphics;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (!data.isEmpty()) {
            customPaintComponent((Graphics2D) graphics);
        } else {

            log("empty handler: %s", data);
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

    protected void draw(Shape shape) {
        draw(shape, g2d.getColor());
    }

    protected void draw(String string) {
    }

    protected void draw(Shape shape, Color color) {
        Color initColor = g2d.getColor();
        g2d.setColor(color);

        g2d.draw(shape);

        g2d.setColor(initColor);
    }

    protected Line2D line(Coords start, Coords end) {
        return new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
    }

    protected Line2D line(int startX, int startY, int endX, int endY) {
        return line(new Coords(startX, startY), new Coords(endX, endY));
    }

    protected void setColor(Color color) {
        g2d.setColor(color);
    }
}