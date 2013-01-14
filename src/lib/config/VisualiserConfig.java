package lib.config;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.File;

public class VisualiserConfig extends PadConfig {

    public VisualiserConfig(File configFile) {
        super(configFile);
    }

    public String getMode() {
        return get("mode");
    }

    public String getLayout() {
        return get("layout");
    }

    public boolean isRealTime() {
        return getMode().equals("auto");
    }

    public boolean isOneChannel() {
        return true;
    }
}