package lib.utils;

import lib.types.PADState;
import lib.types.PADValue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import static lib.utils.Logging.log;

public class Utils {

    private static Random randomGenerator = null;

    public static class ReturnCode {
        public static int NORMAL = 0;
        public static int ERROR = -1;
    }

    public static void exitOnException(Exception error, String message) {
        log(message);

        if (null != error) {
            error.printStackTrace();
        }

        System.exit(ReturnCode.ERROR);
    }

    public static void exitOnException(Exception error) {
        Utils.exitOnException(error, "Something went terribly wrong.");
    }

    public static Random getRandomGenerator() {
        if (null == Utils.randomGenerator) {
            Utils.randomGenerator = new Random();
        }

        return Utils.randomGenerator;
    }

    public static float abs(float a) {
        if (a < 0) {
            return -1f * a;
        }
        return a;
    }

    public static ArrayList<PADValue> reverseSPVList(ArrayList<PADValue> list) {
        ArrayList<PADValue> reversed = new ArrayList<PADValue>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public static ArrayList<PADState> reversePSList(ArrayList<PADState> list) {
        ArrayList<PADState> reversed = new ArrayList<PADState>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public static Color getNegative(Color color) {
        return new Color(
                255 - color.getRed(),
                255 - color.getGreen(),
                255 - color.getBlue());
    }
}
