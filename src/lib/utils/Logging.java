package lib.utils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Logging {

    private static Logger logger = null;

    private static Logger getLogger() {
        if (null == logger) {
            BasicConfigurator.configure();
            logger = Logger.getLogger("");
        }
        return logger;
    }

    public static void log(String message, Object... formatArgs) {
        getLogger().info(String.format(message, formatArgs));
    }

}
