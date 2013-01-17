package lib.config;

import java.io.File;

/**
 * Base class for all config file handlers.
 */
abstract class PADConfig {

    /**
     * ConfigHandler instance handles the document and provides basic methods for its manipulation.
     */
    protected ConfigHandler configHandler;

    /**
     * Default constructor.
     *
     * @param configFile XML config file object.
     */
    public PADConfig(File configFile) {
        configHandler = new ConfigHandler(configFile);
    }

    /**
     * Get node value by node name.
     *
     * @param tagName Node name.
     * @return String containing value of the first node matching given name. If none: null is returned.
     */
    protected String get(String tagName) {
        return configHandler.get(tagName);
    }

    /**
     * Get node value by node name parsed to an integer.
     *
     * @see #get(String)
     *
     * @param tagName Node name.
     * @return Integer containing value of the first node matching given name. If none: null is returned.
     */
    protected int getInt(String tagName) {
        return configHandler.getInt(tagName);
    }
}
