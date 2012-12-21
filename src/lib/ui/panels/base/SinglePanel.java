package lib.ui.panels.base;

import lib.Utils;
import lib.types.PAD;
import lib.types.PADState;
import lib.types.SinglePADValue;

import java.awt.*;
import java.util.ArrayList;

public abstract class SinglePanel extends lib.ui.panels.base.Panel {

    protected ArrayList<SinglePADValue> values = new ArrayList<SinglePADValue>();

    public SinglePanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void feedState(PADState state) {
        values.add(state.getSinglePADValue(type));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void customPaintComponent(Graphics2D g2d);

    public ArrayList<SinglePADValue> getValuesForTime(long startTime, long endTime) {
        ArrayList<SinglePADValue> list = new ArrayList<SinglePADValue>();

        boolean found = false;

        for (SinglePADValue value : Utils.reverseSPVList(values)) {
            if (value.timestamp >= startTime && value.timestamp <= endTime) {
                list.add(value);
                found = true;
            } else if (found) {
                break;
            }
        }

        return Utils.reverseSPVList(list);
    }

    public ArrayList<SinglePADValue> getValuesForCurrentBuffer() {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - buffer * 1000;

        return getValuesForTime(startTime, endTime);
    }

    public ArrayList<SinglePADValue> getValuesPreCurrentBuffer() {
        long startTime = 0;
        long endTime = System.currentTimeMillis() - buffer * 1000;

        return getValuesForTime(startTime, endTime);
    }
}
