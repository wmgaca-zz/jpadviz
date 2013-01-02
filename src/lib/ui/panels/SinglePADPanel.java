package lib.ui.panels;

import lib.types.PAD;
import lib.types.PADValue;
import lib.types.Palette;
import lib.ui.PanelUpdater;
import lib.ui.panels.base.SinglePanel;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class SinglePADPanel extends SinglePanel {

    public SinglePADPanel(PAD.Type type, int width, int height) {
        super(type, width, height);

        PanelUpdater.handle(this);
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
        int len = values.size();

        if (0 == len) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - buffer * 1000;
        PADValue lastValue = values.get(len - 1);

        // Draw -1, 0 and 1 Y lines
        g2d.setColor(Palette.black);
        g2d.drawLine(margin.left, margin.top, getWidth() - margin.right, margin.top);
        g2d.drawLine(margin.left, getCenterY(), getWidth() - margin.right, getCenterY());
        g2d.drawLine(margin.left, margin.top + getH(), getWidth() - margin.right, margin.top + getH());

        // Draw the value
        g2d.drawString(
                String.format("%s: %.2f (%.2f)", label, lastValue.value, lastValue.certainty),
                this.getWidth() - 90, 25);

        ArrayList<PADValue> padValues = getValuesForCurrentBuffer();
        PADValue prevValue = null;

        if (padValues.size() == 0) {
            return;
        }

        int prevX = 0;
        int prevY = 0;

        // Compute start point's Y at X = 0
        ArrayList<PADValue> preBuffer = getValuesPreCurrentBuffer();
        if (0 != preBuffer.size()) {
            PADValue firstValue = padValues.get(0);
            prevValue = preBuffer.get(preBuffer.size() - 1);

            float firstValueY = getYForValue(firstValue.value);
            float prevValueY = getYForValue(prevValue.value);

            long distance = firstValue.timestamp - prevValue.timestamp; // distance between points
            long lost = startTime - firstValue.timestamp; // distance out of borders

            float deltaY = (firstValueY - prevValueY) * (float)lost / (float)distance;

            prevY = (int)((float)firstValueY + deltaY);
            prevX = margin.left;
        }

        // Draw gradient background
        for (PADValue padValue : padValues) {
            int x = getXForTime(padValue.timestamp, startTime, currentTime);
            int y = getYForValue(padValue.value);

            if (null != prevValue) {
                drawPolygon(x, y, prevX, prevY, padValue, prevValue, g2d);
            }

            prevX = x;
            prevY = y;
            prevValue = padValue;
        }

        drawPolygon(margin.left + getW(), prevY, prevX, prevY, prevValue, prevValue, g2d);

        // Draw points and labels
        for (PADValue padValue : getValuesForCurrentBuffer()) {
            int x = getXForTime(padValue.timestamp, startTime, currentTime);
            int y = getYForValue(padValue.value);

            // Point
            g2d.setColor(Palette.blue);
            Ellipse2D circle = new Ellipse2D.Double(x - 3, y - 4, 6f, 6f);
            g2d.fill(circle);
            g2d.draw(circle);

            // Labels
            g2d.setColor(Palette.black);
            g2d.drawString(String.format("%.1f", padValue.value), x - 5, y - 10);
            // TODO
            g2d.drawString(String.format("%.1f", padValue.certainty), x, this.getHeight() - 15);
        }
    }

}
