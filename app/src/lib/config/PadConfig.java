package lib.config;

import java.io.File;

abstract class PadConfig {

    protected ConfigHandler configHandler;

    public PadConfig(File configFile) {
        configHandler = new ConfigHandler(configFile);
    }

    protected String get(String tagName) {
        return configHandler.get(tagName);
    }

    protected int getInt(String tagName) {
        return configHandler.getInt(tagName);
    }
}
