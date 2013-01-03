package lib.config;

import java.io.File;

import static lib.utils.Logging.log;

public class ServerConfig extends PadConfig {

    public ServerConfig(File configFile) {
        super(configFile);
    }

    public int getPort() {
        return getInt("port");
    }

    public String getHost() {
        return get("host");
    }

    public String getDBConnectionString() {
        return get("db_string");
    }

    public String getDBUser() {
        return get("db_user");
    }

    public String getDBPassword() {
        return get("db_password");
    }

    public boolean validate() {
        String[] fields = new String[] {
                "port", "host", "db_string", "db_user", "db_password"
        };

        boolean result = true;

        for (String field : fields) {
            log(field);
            if (null == get(field)) {
                log("Config value missing: %s", field);
                result = false;
            }
        }

        return result;
    }
}