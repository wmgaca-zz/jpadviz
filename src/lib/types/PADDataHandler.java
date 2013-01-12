package lib.types;

import lib.ui.panels.base.Panel;
import lib.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import static lib.utils.Logging.log;

public class PADDataHandler {

    protected final ArrayList<PADState> values = new ArrayList<PADState>();

    protected boolean isRealTime;

    /**
     * In seconds.
     */
    protected long buffer = 15;

    /**
     * In milliseconds.
     */
    protected long startTime = 0 ;

    /**
     * In milliseconds.
     */
    protected long endTime = 0;

    public PADDataHandler(boolean isRealTime) {
        this.isRealTime = isRealTime;
    }

    public PADDataHandler() {
        this(true);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void feed(PADState state) {
        synchronized (values) {
            values.add(state);
        }
    }

    public boolean matchTime(PADState state) {
        return matchTime(state, startTime, endTime);
    }

    public boolean matchTime(PADState state, long start, long end) {
        return state.getTimestamp() >= start && state.getTimestamp() <= end;
    }

    protected ArrayList<PADState> getValuesForTime(long start, long end) {
        ArrayList<PADState> list = new ArrayList<PADState>();

        boolean found = false;

        synchronized (values) {
            for (PADState state : values) {
                if (matchTime(state, start, end)) {
                    list.add(state);
                    found = true;
                } else if (found) {
                    break;
                }
            }
        }

        return list;
    }

    public ArrayList<PADState> getCurrentValues() {
        return getValuesForTime(getCurrentStartTime(), getCurrentEndTime());
    }

    public ArrayList<PADValue> getCurrentValues(PAD.Type type) {
        ArrayList<PADValue> values = new ArrayList<PADValue>();

        for (PADState state : getValuesForTime(getCurrentStartTime(), getCurrentEndTime())) {
            values.add(state.getPADValue(type));
        }

        return values;
    }

    public ArrayList<PADState> getValuesPreCurrentBuffer() {
        return getValuesForTime(0, getCurrentStartTime() - 1);
    }

    public ArrayList<PADValue> getValuesPreCurrentBuffer(PAD.Type type) {
        ArrayList<PADValue> values = new ArrayList<PADValue>();

        for (PADState state : getValuesPreCurrentBuffer()) {
            values.add(state.getPADValue(type));
        }

        return values;
    }

    public PADState getLastState() {
        if (isRealTime) {
            if (!values.isEmpty()) {
                return values.get(values.size() - 1);
            }
        } else {
            for (PADState state : Utils.reversePSList(values)) {
                if (matchTime(state)) {
                    return state;
                }
            }
        }

        return null;
    }

    public PADValue getLastValue(PAD.Type type) {
        PADState lastState = getLastState();

        return (null == lastState) ? null : lastState.getPADValue(type);
    }

    public long getCurrentStartTime() {
        if (isRealTime) {
            return getCurrentEndTime() - buffer * 1000;
        } else {
            return startTime;
        }
    }

    public long getCurrentEndTime() {
        if (isRealTime) {
            return System.currentTimeMillis();
        } else {
            return endTime;
        }
    }

    public void autoTime() {
        if (isRealTime) {
            return;
        } else if (isEmpty()) {
            startTime = 0;
            endTime = 0;
        }

        endTime = values.get(0).getTimestamp();
        startTime = endTime - buffer * 1000;
        if (0 > startTime) {
            startTime = 0;
        }

        startTime = values.get(0).getTimestamp();
        endTime = values.get(values.size() - 1).getTimestamp();

        log("autoTime: %s - %s = %s", startTime, endTime, startTime - endTime);

        log("first: %s", values.get(0));
        log(" last: %s", values.get(values.size() - 1));
    }

    protected static PADDataHandler instance = null;

    public static PADDataHandler getInstance(boolean isRealTime) {
        if (null == instance) {
            instance = new PADDataHandler(isRealTime);

            if (isRealTime) {
                instance.autoTime();
            }
        }

        log("isRealTime: %s", instance.isRealTime);

        return instance;
    }

    public static PADDataHandler getInstance() {
        return getInstance(true);
    }
}