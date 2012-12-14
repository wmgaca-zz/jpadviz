package lib;

import lib.types.PADState;
import lib.types.SinglePADValue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Utils {

    private static Random randomGenerator = null;

    public static class ReturnCode {
        public static int ERROR = -1;
    }

    public static void exitOnException(Exception error, String message) {
        System.err.println(message);

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

    public static ArrayList<SinglePADValue> reverseSPVList(ArrayList<SinglePADValue> list) {
        ArrayList<SinglePADValue> reversed = new ArrayList<SinglePADValue>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public static ArrayList<PADState> reversePSList(ArrayList<PADState> list) {
        ArrayList<PADState> reversed = new ArrayList<PADState>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public static Color getNegative(Color color) {
        System.out.println("Color: " + color);
        return new Color(
                255 - color.getRed(),
                255 - color.getGreen(),
                255 - color.getBlue());
    }
}
