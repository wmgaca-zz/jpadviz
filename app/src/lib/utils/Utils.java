package lib.utils;

import lib.types.PADState;
import lib.types.PADValue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import static lib.utils.Logging.log;

/**
 * Helper class.
 */
public class Utils {

    /**
     * Random generator instance.
     */
    private static Random randomGenerator = null;

    public static class ReturnCode {
        public static int NORMAL = 0;
        public static int ERROR = -1;
    }

    /**
     * Log the exception and an error message and shut down the application.
     *
     * @param error Exception
     * @param message Additional message
     */
    public static void exitOnException(Exception error, String message) {
        log(message);

        if (null != error) {
            error.printStackTrace();
        }

        System.exit(ReturnCode.ERROR);
    }

    /**
     * Log the exception and shut down the application.
     *
     * @param error Exception
     */
    public static void exitOnException(Exception error) {
        Utils.exitOnException(error, "Something went terribly wrong.");
    }

    /**
     * @return An instance of a random generator.
     */
    public static Random getRandomGenerator() {
        if (null == Utils.randomGenerator) {
            Utils.randomGenerator = new Random();
        }

        return Utils.randomGenerator;
    }

    /**
     * Float abs.
     *
     * @param a Float value
     * @return  Abs
     */
    public static float abs(float a) {
        if (a < 0) {
            return -1f * a;
        }
        return a;
    }

    /**
     * Reverse a PADValue list
     *
     * @param list List to reverse
     * @return Reversed list
     */
    public static ArrayList<PADValue> reverseSPVList(ArrayList<PADValue> list) {
        ArrayList<PADValue> reversed = new ArrayList<PADValue>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    /**
     * Reverse PADState list
     *
     * @param list List to reverse
     * @return Reversed list
     */
    public static ArrayList<PADState> reversePSList(ArrayList<PADState> list) {
        ArrayList<PADState> reversed = new ArrayList<PADState>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    /**
     * Get a color negative
     *
     * @param color Color
     * @return Color negative
     */
    public static Color getNegative(Color color) {
        return new Color(
                255 - color.getRed(),
                255 - color.getGreen(),
                255 - color.getBlue());
    }
}
