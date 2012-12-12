package lib;

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

}
