package lib.ui.panels;

import lib.types.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Map;

import static lib.utils.Logging.log;

public class MultiplePADPanel extends lib.ui.panels.base.Panel {

    public MultiplePADPanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    protected void drawSingle(PADDataHandler handler) {
        drawSingle(handler, handler.getColor());
    }

    protected void drawSingle(PADDataHandler handler, Color color) {
        ArrayList<PADValue> values = handler.getCurrentValues(type);

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


        PADValue prevValue = null;

        if (values.size() == 0) {
            return;
        }

        prev = new Coords(0, 0);

        // Compute start point's Y at X = 0
        ArrayList<PADValue> preBuffer = handler.getValuesPreCurrentBuffer(type);

        if (0 != preBuffer.size()) {
            PADValue firstValue = values.get(0);
            prevValue = preBuffer.get(preBuffer.size() - 1);

            float firstValueY = getYForValue(firstValue.getValue());
            float prevValueY = getYForValue(prevValue.getValue());

            long distance = firstValue.getTimestamp() - prevValue.getTimestamp(); // distance between points
            long lost = handler.getCurrentStartTime() - firstValue.getTimestamp(); // distance out of borders

            float deltaY = (firstValueY - prevValueY) * (float)lost / (float)distance;

            prev = new Coords(margin.left,
                              (int)((float)firstValueY + deltaY));
        }

        long currentEndTime = handler.getCurrentEndTime();
        long currentStartTime = handler.getCurrentStartTime();

        for (PADValue pad : values) {
            current = new Coords(getXForTime(pad.getTimestamp(), currentStartTime, currentEndTime),
                                 getYForValue(pad.getValue()));

            if (null != prevValue) {
                draw(line(current, prev), color);
            } else {
                draw(line(current, new Coords(margin.left, current.getY())), color);
            }

            prev = current;
            prevValue = pad;
        }

        // Compute end point's Y
        ArrayList<PADValue> postBuffer = handler.getValuesPostCurrentBuffer(type);
        if (0 != postBuffer.size() && prevValue != null) {
            PADValue nextValue = postBuffer.get(0);

            float lastValueY = prev.getY();
            float nextValueY = getYForValue(nextValue.getValue());

            long distance = nextValue.getTimestamp() - prevValue.getTimestamp(); // distance between points
            long present = handler.getCurrentEndTime() - last.getTimestamp();

            float deltaY = (nextValueY - lastValueY) * (float)present / (float)distance;

            Coords next = new Coords(margin.left + getW(),
                    (int)(lastValueY + deltaY));

            draw(line(prev, next), color);
        } else {
            draw(line(prev, new Coords(margin.left + getW(), prev.getY())), color);
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
        for (Map.Entry<Integer, PADDataHandler> entry : PADDataHandlerContainer.getInstance().getAll().entrySet()) {
            drawSingle(entry.getValue());
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
