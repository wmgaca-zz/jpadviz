package lib.types;

import javafx.beans.property.MapProperty;
import lib.utils.Utils;
import org.jcp.xml.dsig.internal.MacOutputStream;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static lib.utils.Logging.log;

public class MultiplePADDataHandler {

    protected final Map<Integer, ArrayList<PADState>> values = new HashMap<Integer, ArrayList<PADState>>();

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

    public MultiplePADDataHandler(boolean isRealTime) {
        this.isRealTime = isRealTime;
    }

    public MultiplePADDataHandler() {
        this(true);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    protected void addState(PADState state) {
        addState(state, values);
    }

    protected static void addState(PADState state, Map<Integer, ArrayList<PADState>> map) {
        if (!map.containsKey(state.getMethod())) {
            map.put(state.getMethod(), new ArrayList<PADState>());
        }

        map.get(state.getMethod()).add(state);
    }

    public void feed(PADState state) {
        synchronized (values) {
            addState(state);
        }
    }

    public boolean matchTime(PADState state) {
        return matchTime(state, startTime, endTime);
    }

    public boolean matchTime(PADState state, long start, long end) {
        if (end == -1) {
            return state.getTimestamp() >= start;
        } else {
            return state.getTimestamp() >= start && state.getTimestamp() <= end;
        }
    }

    protected Map<Integer, ArrayList<PADState>> getValuesForTime(long start, long end) {
        Map<Integer, ArrayList<PADState>> map = new HashMap<Integer, ArrayList<PADState>>();

        synchronized (values) {
            for (Map.Entry<Integer, ArrayList<PADState>> entry : values.entrySet()) {
                boolean found = false;
                for (PADState state : entry.getValue()) {
                    if (matchTime(state, start, end)) {
                        addState(state, map);
                        found = true;
                    } else if (found) {
                        break;
                    }
                }
            }
        }

        return map;
    }

    protected Map<Integer, ArrayList<PADState>> getValuesForTime(long start) {
        return getValuesForTime(start, -1);
    }

    protected static ArrayList<PADValue> getValuesFromStates(ArrayList<PADState> states, PAD.Type type) {
        ArrayList<PADValue> padValues = new ArrayList<PADValue>();

        for (PADState state : states) {
            padValues.add(state.getPADValue(type));
        }

        return padValues;
    }

    protected static Map<Integer, ArrayList<PADValue>> getValuesFromStates(Map<Integer, ArrayList<PADState>> states, PAD.Type type) {
        Map<Integer, ArrayList<PADValue>> map = new HashMap<Integer, ArrayList<PADValue>>();

        for (Map.Entry<Integer, ArrayList<PADState>> entry : states.entrySet()) {
            map.put(entry.getKey(), getValuesFromStates(entry.getValue(), type));
        }

        return map;
    }

    public Map<Integer, ArrayList<PADState>> getCurrentValues() {
        return getValuesForTime(getCurrentStartTime(), getCurrentEndTime());
    }

    public Map<Integer, ArrayList<PADValue>> getCurrentValues(PAD.Type type) {
        Map<Integer, ArrayList<PADValue>> map = new HashMap<Integer, ArrayList<PADValue>>();

        for (Map.Entry<Integer, ArrayList<PADState>> entry : getCurrentValues().entrySet()) {
            map.put(entry.getKey(), getValuesFromStates(entry.getValue(), type));
        }

        return map;
    }

    public Map<Integer, ArrayList<PADState>> getValuesPreCurrentBuffer() {
        return getValuesForTime(0, getCurrentStartTime() - 1);
    }

    public Map<Integer, ArrayList<PADState>> getValuesPostCurrentBuffer() {
        if (0 == values.size()) {
            log("value.size == 0");
            return new HashMap<Integer, ArrayList<PADState>>();
        }

        return getValuesForTime(getCurrentEndTime() + 1);
    }

    public Map<Integer, ArrayList<PADValue>> getValuesPreCurrentBuffer(PAD.Type type) {
        Map<Integer, ArrayList<PADValue>> values = new HashMap<Integer, ArrayList<PADValue>>();

        for (Map.Entry<Integer, ArrayList<PADState>> entry : getValuesPreCurrentBuffer().entrySet()) {
            values.put(entry.getKey(), getValuesFromStates(entry.getValue(), type));
        }

        return values;
    }

    public Map<Integer, ArrayList<PADValue>> getValuesPostCurrentBuffer(PAD.Type type) {
        Map<Integer, ArrayList<PADValue>> values = new HashMap<Integer, ArrayList<PADValue>>();

        for (Map.Entry<Integer, ArrayList<PADState>> entry : getValuesPostCurrentBuffer().entrySet()) {
            values.put(entry.getKey(), getValuesFromStates(entry.getValue(), type));
        }

        return values;
    }

    public Map<Integer, PADState> getLastState() {
        Map<Integer, PADState> states = new HashMap<Integer, PADState>();

        if (values.isEmpty()) {
            return states;
        }

        if (isRealTime) {
            for (Map.Entry<Integer, ArrayList<PADState>> entry : values.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    states.put(entry.getKey(), entry.getValue().get(entry.getValue().size() - 1));
                } else {
                    states.put(entry.getKey(), null);
                }
            }
        } else {
            for (Map.Entry<Integer, ArrayList<PADState>> entry : values.entrySet()) {
                for (PADState state : Utils.reversePSList(entry.getValue())) {
                    if (matchTime(state)) {
                        states.put(entry.getKey(), state);
                        break;
                    }
                }

            }
        }

        return states;
    }

    public Map<Integer, PADValue> getLastValue(PAD.Type type) {
        Map<Integer, PADValue> padValues = new HashMap<Integer, PADValue>();

        for (Map.Entry<Integer, PADState> entry : getLastState().entrySet()) {
            padValues.put(entry.getKey(), entry.getValue().getPADValue(type));
        }

        return padValues;
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

    protected static MultiplePADDataHandler instance = null;

    public static MultiplePADDataHandler getInstance(boolean isRealTime) {
        if (null == instance) {
            instance = new MultiplePADDataHandler(isRealTime);

            if (isRealTime) {
                instance.autoTime();
            }
        }

        return instance;
    }

    public static MultiplePADDataHandler getInstance() {
        return getInstance(true);
    }
}