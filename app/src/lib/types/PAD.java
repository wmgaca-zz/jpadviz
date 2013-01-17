package lib.types;

import java.util.HashMap;
import static lib.utils.Logging.log;

/**
 * Helper class for easier PAD values manipulation.
 */
public class PAD {

    /**
     * Visualisation types: P, A, D for a single metrics chart
     * and PAD for chart showing all of the state's metrics.
     */
    public static enum Type {
        P,
        A,
        D,
        PAD }

    /**
     * Type -> Name mapping.
     */
    private static HashMap<Type, String> names = new HashMap<Type, String>();

    static {
        names.put(Type.P, "P");
        names.put(Type.A, "A");
        names.put(Type.D, "D");
        names.put(Type.PAD, "PAD");
    }

    /**
     * Text representation of a type.
     *
     * @param type Type
     * @return Type's text representation.
     */
    public static String getName(PAD.Type type) {
        return PAD.names.get(type);
    }
}