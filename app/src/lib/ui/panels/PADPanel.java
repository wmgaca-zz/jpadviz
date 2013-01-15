package lib.ui.panels;

import lib.types.*;

import static lib.utils.Logging.log;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class PADPanel extends lib.ui.panels.base.Panel {

    public PADPanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    private void drawPolygon(int x, int y, int prevX, int prevY, PADValue currentValue, PADValue prevValue, Graphics2D g2d) {
        g2d.setColor(Palette.transparent);

        // Assemble polygon
        Polygon certaintyPolygon = new Polygon();
        certaintyPolygon.addPoint(prevX, prevY);
        certaintyPolygon.addPoint(x, y);
        certaintyPolygon.addPoint(x, margin.top + getH());
        certaintyPolygon.addPoint(prevX, margin.top + getH());
        certaintyPolygon.addPoint(prevX, prevY);

        // Draw polygon
        g2d.setColor(Palette.getTransparent(Palette.green, 0f));
        g2d.draw(certaintyPolygon);

        // Compute gradient's color, set gradient as paint & fill the polygon
        Color currentColor = (currentValue.value > 0) ? Palette.green : Palette.red;
        Color prevColor = (prevValue.value > 0) ? Palette.green : Palette.red;
        g2d.setPaint(new GradientPaint(prevX, 0, Palette.getTransparent(prevColor, prevValue.certainty),
                                       x, 0, Palette.getTransparent(currentColor, currentValue.certainty)));
        g2d.fill(certaintyPolygon);

        // Draw top line in blue
        g2d.setColor(Palette.blue);
        g2d.drawLine(prevX, prevY, x, y);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        ArrayList<PADValue> values = data.getCurrentValues(type);

        if (0 == values.size()) {
            return;
        }

        PADValue last = values.get(values.size() - 1);

        // Draw -1, 0 and 1 Y lines
        g2d.setColor(Palette.black);
        g2d.drawLine(margin.left, margin.top, getWidth() - margin.right, margin.top);
        g2d.drawLine(margin.left, getCenterY(), getWidth() - margin.right, getCenterY());
        g2d.drawLine(margin.left, margin.top + getH(), getWidth() - margin.right, margin.top + getH());

        // Draw the value
        g2d.drawString(
                String.format("%s: %.2f (%.2f)", label, last.getValue(), last.getCertainty()),
                this.getWidth() - 90, 25);


        PADValue prevValue = null;

        if (values.size() == 0) {
            return;
        }

        int prevX = 0;
        int prevY = 0;

        // Compute start point's Y at X = 0
        ArrayList<PADValue> preBuffer = data.getValuesPreCurrentBuffer(type);
        ArrayList<PADValue> postBuffer = data.getValuesPostCurrentBuffer(type);

        if (0 != preBuffer.size()) {
            PADValue firstValue = values.get(0);
            prevValue = preBuffer.get(preBuffer.size() - 1);

            float firstValueY = getYForValue(firstValue.getValue());
            float prevValueY = getYForValue(prevValue.getValue());

            long distance = firstValue.getTimestamp() - prevValue.getTimestamp(); // distance between points
            long lost = data.getCurrentStartTime() - firstValue.getTimestamp(); // distance out of borders

            float deltaY = (firstValueY - prevValueY) * (float)lost / (float)distance;

            prevY = (int)((float)firstValueY + deltaY);
            prevX = margin.left;
        }

        long currentStartTime = data.getCurrentStartTime();
        long currentEndTime = data.getCurrentEndTime();

        // Draw gradient background
        for (PADValue pad : values) {
            //int x = getXForTime(padValue.timestamp, startTime, currentTime);

            int x = getXForTime(pad.getTimestamp(), currentStartTime, currentEndTime);
            int y = getYForValue(pad.getValue());

            if (null != prevValue) {
                drawPolygon(x, y, prevX, prevY, pad, prevValue, g2d);
            } else {
                drawPolygon(x, y, margin.left, y, pad, pad, g2d);
            }

            prevX = x;
            prevY = y;
            prevValue = pad;
        }

        if (0 != postBuffer.size() && prevValue != null) {
            PADValue nextValue = postBuffer.get(0);

            float nextValueY = getYForValue(nextValue.getValue());

            long distance = nextValue.getTimestamp() - prevValue.getTimestamp(); // distance between points
            long present = data.getCurrentEndTime() - last.getTimestamp();

            float deltaY = (nextValueY - prevY) * (float)present / (float)distance;

            Coords next = new Coords(margin.left + getW(),
                    (int)(prevY + deltaY));

            drawPolygon(next.getX(), next.getY(), prevX, prevY, prevValue, prevValue, g2d);
        } else {
            drawPolygon(margin.left + getW(), prevY, prevX, prevY, prevValue, prevValue, g2d);
        }


        // Draw points and labels
        for (PADValue pad : values) {
            int x = getXForTime(pad.timestamp, currentStartTime, currentEndTime);
            int y = getYForValue(pad.value);

            // Point
            g2d.setColor(Palette.blue);
            Ellipse2D circle = new Ellipse2D.Double(x - 3, y - 4, 6f, 6f);
            g2d.fill(circle);
            g2d.draw(circle);

            // Labels
            g2d.setColor(Palette.black);
            g2d.drawString(String.format("%.1f", pad.getValue()), x - 5, y - 10);
            // TODO
            g2d.drawString(String.format("%.1f", pad.getCertainty()), x, this.getHeight() - 15);
        }
    }

    /**
     * Event's Y coordinate based on its value (larger value -> lower Y coordinate -> higher on the screen)
     *
     * @param value Event's value.
     *
     * @return Event's Y coordinate.
     */
    final public int getYForValue(float value) {
        return getCenterY() + (int)(getH() * value / 2) * -1;
    }

}
