package lib.ui.panels;

import lib.types.Coords;
import lib.types.PAD;
import lib.types.PADValue;
import lib.types.Palette;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class MultiplePADPanel extends lib.ui.panels.base.Panel {

    public MultiplePADPanel(int width, int height) {
        super(PAD.Type.PAD, width, height);
    }

    protected void drawSingle(PAD.Type type, Color color) {
        ArrayList<PADValue> values = data.getCurrentValues(type);

        if (0 == values.size()) {
            return;
        }

        Coords current = null;
        Coords prev = null;

        PADValue last = values.get(values.size() - 1);

        // Draw -1, 0 and 1 Y lines
        setColor(Palette.black);
        draw(line(margin.left, margin.top, getWidth() - margin.right, margin.top), Palette.black);
        draw(line(margin.left, getCenterY(), getWidth() - margin.right, getCenterY()), Palette.black);
        draw(line(margin.left, margin.top + getH(), getWidth() - margin.right, margin.top + getH()), Palette.black);

        // Draw the value
        g2d.drawString(
                String.format("%s: %.2f (%.2f)", label, last.getValue(), last.getCertainty()),
                this.getWidth() - 90, 25);


        PADValue prevValue = null;

        if (values.size() == 0) {
            return;
        }

        prev = new Coords(0, 0);

        // Compute start point's Y at X = 0
        ArrayList<PADValue> preBuffer = data.getValuesPreCurrentBuffer(type);

        if (0 != preBuffer.size()) {
            PADValue firstValue = values.get(0);
            prevValue = preBuffer.get(preBuffer.size() - 1);

            float firstValueY = getYForValue(firstValue.getValue());
            float prevValueY = getYForValue(prevValue.getValue());

            long distance = firstValue.getTimestamp() - prevValue.getTimestamp(); // distance between points
            long lost = data.getCurrentStartTime() - firstValue.getTimestamp(); // distance out of borders

            float deltaY = (firstValueY - prevValueY) * (float)lost / (float)distance;

            prev = new Coords(margin.left,
                              (int)((float)firstValueY + deltaY));
        }

        long currentStartTime = data.getCurrentStartTime();
        long currentEndTime = data.getCurrentEndTime();

        // Draw gradient background
        for (PADValue pad : values) {
            current = new Coords(getXForTime(pad.getTimestamp(), currentStartTime, currentEndTime),
                                 getYForValue(pad.getValue()));

            if (null != prevValue) {
                draw(line(current, prev), color);
            }

            prev = current;
            prevValue = pad;
        }


        // Draw points and labels
        for (PADValue pad : values) {
            int x = getXForTime(pad.timestamp, currentStartTime, currentEndTime);
            int y = getYForValue(pad.value);

            // Point
            g2d.setColor(color);
            Ellipse2D circle = new Ellipse2D.Double(x - 3, y - 4, 6f, 6f);
            g2d.fill(circle);
            g2d.draw(circle);

            // Labels
            g2d.setColor(color);
            g2d.drawString(String.format("%.1f", pad.getValue()), x - 5, y - 10);
            // TODO
            //g2d.drawString(String.format("%.1f", pad.getCertainty()), x, this.getHeight() - 15);
        }
    }

    @Override
    public void customPaintComponent() {
        drawSingle(PAD.Type.P, Palette.red);
        drawSingle(PAD.Type.A, Palette.black);
        drawSingle(PAD.Type.D, Palette.blue);
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
