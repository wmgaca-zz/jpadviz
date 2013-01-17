package lib.types;

import lib.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

import static lib.utils.Logging.log;

/***
 * A handler for experiment's single session data (PAD states)
 */
public class PADDataHandler {

    /**
     * Session PAD states
     */
    protected final ArrayList<PADState> values = new ArrayList<PADState>();

    /**
     * Is the application run in real time mode?
     */
    protected boolean isRealTime;

    /**
     * Current window's time span.
     * In seconds.
     */
    protected long buffer = 15;

    /**
     * Current window's start time.
     * In milliseconds.
     */
    protected long startTime = 0 ;

    /**
     * Current window's end time.
     * In milliseconds.
     */
    protected long endTime = 0;

    /**
     * Color used to draw the session data in multi-channel mode.
     */
    protected Color color;

    /**
     * @param isRealTime Real time mode?
     */
    public PADDataHandler(boolean isRealTime) {
        this.isRealTime = isRealTime;

        color = Palette.getNextColor();
    }

    /**
     * Default constructor, creates a new handler in real time mode.
     */
    public PADDataHandler() {
        this(true);

        autoTime();
    }

    /**
     * Is the data set empty?
     *
     * @return true if the data set is empty, false otherwise
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * Add a new state to the handler.
     *
     * @param state PAD state
     */
    public void feed(PADState state) {

        synchronized (values) {
            log("FEED: %s", values.size());
            values.add(state);
        }
    }

    /**
     * Check if a state matches current window's time span
     *
     * @param state PAD state
     * @return true if the match, false otherwise.
     */
    public boolean matchTime(PADState state) {
        return matchTime(state, startTime, endTime);
    }

    /**
     * Check if the state matches a time span
     *
     * @param state PAD state
     * @param start Start time
     * @param end End time
     * @return true if match, false otherwise
     */
    public boolean matchTime(PADState state, long start, long end) {
        return state.getTimestamp() >= start && state.getTimestamp() <= end;
    }

    /**
     * Get states for given window
     *
     * @param start Window's start time
     * @param end Window's end time
     * @return Matching PAD states
     */
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

    /**
     * Get states for current window
     *
     * @return Matching PAD states
     */
    public ArrayList<PADState> getCurrentValues() {
        return getValuesForTime(getCurrentStartTime(), getCurrentEndTime());
    }

    /**
     * Get PAD values extracted from the states for current window
     *
     * @param type Value type
     * @return Matching values
     */
    public ArrayList<PADValue> getCurrentValues(PAD.Type type) {
        ArrayList<PADValue> values = new ArrayList<PADValue>();

        for (PADState state : getValuesForTime(getCurrentStartTime(), getCurrentEndTime())) {
            values.add(state.getPADValue(type));
        }

        return values;
    }

    /**
     * Get PAD states prior to the current window
     *
     * @return Matching states
     */
    public ArrayList<PADState> getValuesPreCurrentBuffer() {
        return getValuesForTime(0, getCurrentStartTime() - 1);
    }

    /**
     * Get PAD states post current window
     *
     * @return Matching states
     */
    public ArrayList<PADState> getValuesPostCurrentBuffer() {
        if (0 == values.size()) {
            return new ArrayList<PADState>();
        }

        PADState lastState = values.get(values.size() - 1);
        PADState bufferLastState = getLastState();

        if (lastState.getTimestamp() < bufferLastState.getTimestamp()) {
            return new ArrayList<PADState>();
        }

        return getValuesForTime(getCurrentEndTime() + 1, lastState.getTimestamp());
    }

    /**
     * Get PAD values extracted from states prior to the current window
     *
     * @see #getValuesPreCurrentBuffer()
     * @param type Value type
     * @return Matching values
     */
    public ArrayList<PADValue> getValuesPreCurrentBuffer(PAD.Type type) {
        ArrayList<PADValue> values = new ArrayList<PADValue>();

        for (PADState state : getValuesPreCurrentBuffer()) {
            values.add(state.getPADValue(type));
        }

        return values;
    }

    /**
     * Get PAD values extracted from states post the current window
     *
     * @param type Value type
     * @return Matching values
     */
    public ArrayList<PADValue> getValuesPostCurrentBuffer(PAD.Type type) {
        ArrayList<PADValue> values = new ArrayList<PADValue>();

        for (PADState state : getValuesPostCurrentBuffer()) {
            values.add(state.getPADValue(type));
        }

        return values;
    }

    /**
     * @return The last state on the list
     */
    public PADState getReallyLastState() {
        if (values.isEmpty()) {
            return null;
        }

        return values.get(values.size() - 1);
    }

    /**
     * @return The first state on the list
     */
    public PADState getReallyFirstState() {
        if (values.isEmpty()) {
            return null;
        }

        return values.get(0);
    }

    /**
     * @return The last state matching current window
     */
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

    /**
     * @param type Value type
     * @return Value extracted from the last matching the current window
     */
    public PADValue getLastValue(PAD.Type type) {
        PADState lastState = getLastState();

        return (null == lastState) ? null : lastState.getPADValue(type);
    }


    /**
     * @return Start time of currently used window
     */
    public long getCurrentStartTime() {
        if (isRealTime) {
            return getCurrentEndTime() - buffer * 1000;
        } else {
            return startTime;
        }
    }

    /**
     * @return End time of the currently used window.
     */
    public long getCurrentEndTime() {
        if (isRealTime) {
            return System.currentTimeMillis();
        } else {
            return endTime;
        }
    }

    /**
     * Time auto tune.
     */
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
    }

    /**
     * Set window's start & end time
     *
     * @param middle Window's middle point time
     * @param zoom Value of range 0-100 representing window's percentage time span
     *             It's relative to the experiment's time span, e.g.:
     *              5: 5% of the experiment will be covered by the window
     *              50: 50% of the experiment will be covered by the window
     */
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

    /**
     * Get first handler's instance.
     *
     * TODO: refactor (used in single-channel mode)
     *
     * @param isRealTime
     * @return First handler's instance
     */
    public static PADDataHandler getInstance(boolean isRealTime) {

        PADDataHandler handler = PADDataHandlerContainer.getInstance(isRealTime).get();

        return handler;
    }

    /**
     * @return First instance's handler
     */
    public static PADDataHandler getInstance() {
        return getInstance(true);
    }

    /**
     * @param value Window start time
     */
    public void setStartTime(long value) {
        startTime = value;
    }

    /**
     * Window end time
     * @param value
     */
    public  void setEndTime(long value) {
        endTime = value;
    }

    /**
     * @return Color used to draw session-specific data.
     */
    public Color getColor() {
        return color;
    }
}
