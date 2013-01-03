package lib.ui.panels.base;

import lib.utils.Utils;
import lib.types.PAD;
import lib.types.PADState;

import java.awt.*;
import java.util.ArrayList;
import static lib.utils.Logging.log;

public abstract class MultiplePanel extends Panel {

    protected ArrayList<PADState> values = new ArrayList<PADState>();

    public MultiplePanel(int width, int height) {
        this(width, height, true);
    }

    public MultiplePanel(int width, int height, boolean isRealTime) {
        super(PAD.Type.PAD, width, height, isRealTime);
    }

    @Override
    protected void feedState(PADState state) {
        this.values.add(state);
    }

    @Override
    public abstract void customPaintComponent(Graphics2D g2d);

    public ArrayList<PADState> getValuesForTime(long startTime, long endTime) {
        ArrayList<PADState> list = new ArrayList<PADState>();

        boolean found = false;

        for (PADState state: Utils.reversePSList(values)) {
            if (state.getTimestamp() >= startTime && state.getTimestamp() <= endTime) {
                list.add(state);
                found = true;
            } else if (found) {
                break;
            }
        }

        return Utils.reversePSList(list);
    }

    public ArrayList<PADState> getValuesForCurrentBuffer() {

        return getValuesForTime(getCurrentStartTime(), getCurrentEndTime());
    }

    public void autoTime() {
        if (!isRealTime && values.size() > 0) {
            startTime = values.get(0).getTimestamp();
            endTime = startTime + buffer;
        }
    }
}