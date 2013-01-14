package lib.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static lib.utils.Logging.log;

public class PADDataHandlerContainer {

    protected boolean isRealTime;

    protected ArrayList<PADDataHandler> list = new ArrayList<PADDataHandler>();
    protected Map<Integer, PADDataHandler> map = new HashMap<Integer, PADDataHandler>();

    public PADDataHandlerContainer() {
        this(true);
    }

    protected void add(int method, PADDataHandler handler) {
        assert !list.contains(handler);
        assert !map.containsKey(method);

        list.add(handler);
        map.put(method, handler);
    }

    public void init(List<Integer> methods) {
        for (Integer method : methods) {
            add(method, new PADDataHandler(isRealTime));
        }
    }

    public PADDataHandlerContainer(boolean isRealTime) {
        this.isRealTime = isRealTime;
    }

    public boolean getIsRealTime() {
        return isRealTime;
    }

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

    public PADDataHandler get(int method) {
        if (!map.containsKey(method)) {
            return null;
        }

        return map.get(method);
    }

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

    public void autoTime() {
        if (isRealTime) {
            for (PADDataHandler handler : list) {
                handler.autoTime();
            }
        }
    }

    protected static PADDataHandlerContainer instance = null;

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

    public static PADDataHandlerContainer getInstance() {
        return getInstance(true);
    }
}
