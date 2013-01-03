package lib.ui.panels.base;

import lib.utils.Utils;
import lib.types.PAD;
import lib.types.PADState;
import lib.types.PADValue;

import java.awt.*;
import java.util.ArrayList;

public abstract class SinglePanel extends lib.ui.panels.base.Panel {

    protected ArrayList<PADValue> values = new ArrayList<PADValue>();

    public SinglePanel(PAD.Type type, int width, int height) {
        super(type, width, height, true);
    }

    public SinglePanel(PAD.Type type, int width, int height, boolean isRealTime) {
        super(type, width, height, isRealTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void feedState(PADState state) {
        values.add(state.getPADValue(type));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void customPaintComponent(Graphics2D g2d);

    public ArrayList<PADValue> getValuesForTime(long startTime, long endTime) {
        ArrayList<PADValue> list = new ArrayList<PADValue>();

        boolean found = false;

        for (PADValue value : Utils.reverseSPVList(values)) {
            if (value.timestamp >= startTime && value.timestamp <= endTime) {
                list.add(value);
                found = true;
            } else if (found) {
                break;
            }
        }

        return Utils.reverseSPVList(list);
    }

    public ArrayList<PADValue> getValuesForCurrentBuffer() {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - buffer * 1000;

        return getValuesForTime(startTime, endTime);
    }

    public ArrayList<PADValue> getValuesPreCurrentBuffer() {
        long startTime = 0;
        long endTime = System.currentTimeMillis() - buffer * 1000;

        return getValuesForTime(startTime, endTime);
    }

    public void autoTime() {
        if (!isRealTime && values.size() > 0) {
            startTime = values.get(0).getTimestamp();
            endTime = startTime + buffer;
        }
    }
}
