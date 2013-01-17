package lib.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static lib.utils.Logging.log;

/**
 * Container for PADDataHandler for multi-channel usage.
 */
public class PADDataHandlerContainer {

    /**
     * Is the real time mode on?
     */
    protected boolean isRealTime;

    /**
     * List of all data handlers.
     */
    protected ArrayList<PADDataHandler> list = new ArrayList<PADDataHandler>();

    /**
     * Mapping session identifiers on data handling objects.
     */
    protected Map<Integer, PADDataHandler> map = new HashMap<Integer, PADDataHandler>();

    /**
     * Default container.
     */
    public PADDataHandlerContainer() {
        this(true);
    }

    /**
     * Initializes a new object with the is-real-time flag.
     *
     * @param isRealTime Is the app running in real time mode?
     */
    public PADDataHandlerContainer(boolean isRealTime) {
        this.isRealTime = isRealTime;
    }

    /**
     * Add a new data handler.
     *
     * @param method Method identifier
     * @param handler Data handler object
     */
    protected void add(int method, PADDataHandler handler) {
        assert !list.contains(handler);
        assert !map.containsKey(method);

        list.add(handler);
        map.put(method, handler);
    }

    /**
     * Init the object with a list of method identifiers.
     * The object should be initialized right after its created (app design drawback)
     *
     * TODO: refactor
     *
     * @param methods
     */
    public void init(List<Integer> methods) {
        for (Integer method : methods) {
            add(method, new PADDataHandler(isRealTime));
        }
    }

    /**
     * @return true if real time mode is on, false otherwise
     */
    public boolean getIsRealTime() {
        return isRealTime;
    }

    /**
     * @return All handlers
     */
    public Map<Integer, PADDataHandler> getAll() {
        return map;
    }

    /**
     * @return First handler on the list
     */
    public PADDataHandler get() {
        if (list.isEmpty()) {
            if (isRealTime) {
                log("Add empty");
                list.add(new PADDataHandler(isRealTime));
            } else {
                return null;
            }
        }

        return list.get(0);
    }

    /**
     * @return List of all present handlers.
     */
    public ArrayList<PADDataHandler> getList() {
        return list;
    }

    /**
     * @param method Method identifier
     * @return Handler by method's identifier
     */
    public PADDataHandler get(int method) {
        if (!map.containsKey(method)) {
            return null;
        }

        return map.get(method);
    }

    /**
     * Add a new PAD state to all of the handlers in the container
     *
     * @param state PAD state
     */
    public void feed(PADState state) {
        log("feeding container, method: %s", state.getMethod());
        if (isRealTime) {
            if (list.isEmpty()) {
                list.add(new PADDataHandler(isRealTime));
            }

            list.get(0).feed(state);
        } else {
            if (!map.containsKey(state.getMethod())) {
                map.put(state.getMethod(), new PADDataHandler(isRealTime));
            }

            map.get(state.getMethod()).feed(state);
        }
    }

    /**
     * Time auto tune all of the handlers.
     */
    public void autoTime() {
        if (isRealTime) {
            for (PADDataHandler handler : list) {
                handler.autoTime();
            }
        }
    }

    /**
     * Instance of the data handler container (should always be only one)
     */
    protected static PADDataHandlerContainer instance = null;

    /**
     * Get the instance of data handler container
     * This method should be used acquire object's instance
     *
     * @param isRealTime Real time flag
     * @return Container object
     */
    public static PADDataHandlerContainer getInstance(boolean isRealTime) {

        if (null == instance) {
            instance = new PADDataHandlerContainer(isRealTime);

            if (isRealTime) {
                instance.get();
                instance.autoTime();
            }
        }

        return instance;
    }

    /**
     * Get the instance of data handler container
     * This method should be used acquire object's instance
     *
     * @return Container object
     */
    public static PADDataHandlerContainer getInstance() {
        return getInstance(true);
    }

    /**
     * @return The start time of first PAD state in the container matching current window's time
     */

    public long getEdgeStartTime() {
        long start = -1;

        for (PADDataHandler handler : list) {
            PADState state = handler.getReallyFirstState();

            if (null == state) {
                continue;
            }

            if (start > state.getTimestamp() || start == -1) {
                start = state.getTimestamp();
            }
        }

        return (start != -1) ? start : 0;
    }

    /**
     * @return The end time of last PAD state in the container matching current window's time
     */
    public long getEdgeEndTime() {
        long end = 0;

        for (PADDataHandler handler : list) {
            PADState state = handler.getReallyLastState();

            if (null == state) {
                continue;
            }

            if (end < state.getTimestamp()) {
                end = state.getTimestamp();
            }
        }

        return end;
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
        if (map.isEmpty()) {
            return;
        }

        long start = getEdgeStartTime();
        long end = getEdgeEndTime();

        log("start=%s, end=%s", start, end);

        long window = ((end - start) * zoom) / 100;
        long center = start + ((end - start) * middle) / 100;

        for (PADDataHandler handler : list) {
            handler.setStartTime(center - window / 2);
            handler.setEndTime(center + window / 2);
        }
    }
}
