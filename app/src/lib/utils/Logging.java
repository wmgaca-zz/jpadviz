package lib.utils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Logging utilitis
 */
public class Logging {

    /**
     * Logger instance
     */
    private static Logger logger = null;

    /**
     * Acquire a logger.
     *
     * @return Logger instances
     */
    private static Logger getLogger() {
        if (null == logger) {
            BasicConfigurator.configure();
            logger = Logger.getLogger("");
        }
        return logger;
    }

    /**
     * Log a message.
     * Format arguments will be used in place of '%s', '%f', etc.
     *
     * @param message Message to be printed
     * @param formatArgs Format arguments
     */
    public static void log(String message, Object... formatArgs) {
        getLogger().info(String.format(message, formatArgs));
    }

}
