package lib.types;

import java.util.HashMap;
import static lib.utils.Logging.log;

public class PAD {

    public static enum Type { P, A, D, PAD }

    private static HashMap<Type, String> names = new HashMap<Type, String>();

    static {
        names.put(Type.P, "P");
        names.put(Type.A, "A");
        names.put(Type.D, "D");
        names.put(Type.PAD, "PAD");
    }

    public static String getName(PAD.Type type) {
        log("type: %s", type.toString());
        log("got: %s", names.get(type));
        return PAD.names.get(type);
    }
}