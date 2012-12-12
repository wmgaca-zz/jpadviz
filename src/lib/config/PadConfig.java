package lib.config;

import java.io.File;

abstract class PadConfig {

    protected ConfigHandler configHandler;

    public PadConfig(File configFile) {
        this.configHandler = new ConfigHandler(configFile);
    }

    protected String get(String tagName) {
        return this.configHandler.get(tagName);
    }

    protected int getInt(String tagName) {
        return this.configHandler.getInt(tagName);
    }
}
