package lib.types;

import java.util.ArrayList;

public class PADDataHandler {

    protected ArrayList<PADState> values = new ArrayList<PADState>();

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
        values.add(state);
    }

    protected ArrayList<PADState> getValuesForTime(long start, long end) {
        ArrayList<PADState> list = new ArrayList<PADState>();

        boolean found = false;

        for (PADState state : values) {
            if (state.getTimestamp() >= start && state.getTimestamp() <= end) {
                list.add(state);
                found = true;
            } else if (found) {
                break;
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

    public PADState getLastValue() {
        if (isRealTime) {
            if (!values.isEmpty()) {
                return values.get(values.size() - 1);
            }
        }

        return null;
    }

    public PADValue getLastValue(PAD.Type type) {
        return getLastValue().getPADValue(type);
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
        if (!isRealTime || isEmpty()) {
            return;
        }

        endTime = values.get(0).getTimestamp();

        startTime = endTime - buffer * 1000;
        if (0 > startTime) {
            startTime = 0;
        }
    }

    protected static PADDataHandler instance = null;

    public static PADDataHandler getInstance(boolean isRealTime) {
        if (null == instance) {
            instance = new PADDataHandler(isRealTime);
        }

        return instance;
    }

    public static PADDataHandler getInstance() {
        return getInstance(true);
    }
}