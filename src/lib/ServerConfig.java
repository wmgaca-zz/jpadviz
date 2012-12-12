package lib;

import java.io.File;

public class ServerConfig extends PadConfig {

    public ServerConfig(File configFile) {
        super(configFile);
    }

    public int getPort() {
        return this.getInt("port");
    }

    public String getHost() {
        return this.get("host");
    }

    public boolean validate() {
        String[] fields = new String[] {
                "port",
                "host"
        };

        boolean result = true;

        for (String field : fields) {
            if (null == this.configHandler.get(field)) {
                System.out.println(
                        String.format("Config value missing: %s", field));
                result = false;
            }
        }

        return result;
    }
}