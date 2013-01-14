package lib.types;

import lib.utils.Utils;
import java.util.ArrayList;

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

        autoTime();
    }

    public boolean isEmpty() {
        log("Size: %s", values.size());

        return values.isEmpty();
    }

    public void feed(PADState state) {

        synchronized (values) {
            log("FEED: %s", values.size());
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

    public ArrayList<PADState> getValuesPostCurrentBuffer() {
        if (0 == values.size()) {
            log("value.size == 0");
            return new ArrayList<PADState>();
        }

        PADState lastState = values.get(values.size() - 1);
        PADState bufferLastState = getLastState();

        if (lastState.getTimestamp() < bufferLastState.getTimestamp()) {
            return new ArrayList<PADState>();
        }

        return getValuesForTime(getCurrentEndTime() + 1, lastState.getTimestamp());
    }

    public ArrayList<PADValue> getValuesPreCurrentBuffer(PAD.Type type) {
        ArrayList<PADValue> values = new ArrayList<PADValue>();

        for (PADState state : getValuesPreCurrentBuffer()) {
            values.add(state.getPADValue(type));
        }

        return values;
    }

    public ArrayList<PADValue> getValuesPostCurrentBuffer(PAD.Type type) {
        ArrayList<PADValue> values = new ArrayList<PADValue>();

        for (PADState state : getValuesPostCurrentBuffer()) {
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

        startTime += 5 * (endTime - startTime) / 6;

        log("autoTime: %s - %s = %s", startTime, endTime, startTime - endTime);

        log("first: %s", values.get(0));
        log(" last: %s", values.get(values.size() - 1));
    }

    public void setWindow(int middle, int zoom) {
        if (0 == values.size()) {
            return;
        }

        long start = values.get(0).getTimestamp();
        long end = values.get(values.size() - 1).getTimestamp();

        long window = ((end - start) * zoom) / 100;
        long center = start + ((end - start) * middle) / 100;

        startTime = center - window / 2;
        endTime = center + window / 2;
    }

    protected static PADDataHandler instance = null;

    public static PADDataHandler getInstance(boolean isRealTime) {

        PADDataHandler handler = PADDataHandlerContainer.getInstance(isRealTime).get();

        //handler.autoTime();

        log("handler: %s", handler);

        return handler;

        /*
        if (null == instance) {
            instance = new PADDataHandler(isRealTime);

            if (isRealTime) {
                instance.autoTime();
            }
        }

        return instance;
        //*/
    }

    public static PADDataHandler getInstance() {
        return getInstance(true);
    }
}