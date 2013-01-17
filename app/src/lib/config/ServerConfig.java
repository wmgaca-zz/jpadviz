package lib.config;

import java.io.File;

import static lib.utils.Logging.log;

/**
 * PadServer config handler.
 */
public class ServerConfig extends PADConfig {

    /**
     * Default constructor.
     *
     * @param configFile Server's XML config.
     */
    public ServerConfig(File configFile) {
        super(configFile);
    }

    /**
     * @return Server port.
     */
    public int getPort() {
        return getInt("port");
    }

    /**
     * @return Server host.
     */
    public String getHost() {
        return get("host");
    }

    /**
     * @return Database connection string.
     */
    public String getDBConnectionString() {
        return get("db_string");
    }

    /**
     * @return Database username.
     */
    public String getDBUser() {
        return get("db_user");
    }

    /**
     * @return Database password.
     */
    public String getDBPassword() {
        return get("db_password");
    }

    /**
     * Validate config file contents.
     *
     * @return true if config file was validated successfully, false otherwise.
     */
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