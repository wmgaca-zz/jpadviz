import lib.DataHandler;
import lib.utils.Utils;
import lib.config.ServerConfig;
import lib.net.ConnectionHandler;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import static lib.utils.Logging.log;

/**
 * PAD server
 */
public class PadServer {

    /**
     * App working dir
     */
    public static String workingDir = System.getProperty("user.dir");

    /**
     * Config file
     */
    public static File  serverConfigFile = new File(PadServer.workingDir, "config/server-settings.xml");

    /**
     * Config object
     */
    public static ServerConfig config = new ServerConfig(PadServer.serverConfigFile);

    /**
     * Active connections count
     */
    public static int connections = 0;

    /**
     * DB handler
     */
    public static DataHandler db = null;

    /**
     * Entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        // Initialize application: read configuration files, set up things
        PadServer.init();

        // Run server
        PadServer.run();
    }

    /**
     * Set up the server
     */
    public static void init() {
        log("Initializing...");

        if (!PadServer.config.validate()) {
            Utils.exitOnException(null, "Server config file contains errors.");
        } else {
            log("Server config file validation: OK.");
        }

        log("Establishing connection to the database...");
        try {
            db = new DataHandler(PadServer.config.getDBConnectionString(),
                                 PadServer.config.getDBUser(),
                                 PadServer.config.getDBPassword());
        } catch (SQLException e) {
            Utils.exitOnException(null, String.format("Cannot connect to database: %s", PadServer.config.getDBConnectionString()));
        }
        log("Connected.");
    }

    /**
     * Server's logic
     */
    public static void run() {
        ServerSocket listener = null;

        try {
            listener = new ServerSocket(PadServer.config.getPort());
        } catch (BindException error) {
            Utils.exitOnException(
                    null,
                    String.format("Could not listen on port %s", PadServer.config.getPort()));
        } catch (IOException error) {
            Utils.exitOnException(
                    null,
                    String.format("Could not listen on port %s", PadServer.config.getPort()));
        }

        assert null != listener;

        log("Listening on port %s", PadServer.config.getPort());

        while (true) {
            Socket socket = null;

            try {
                socket = listener.accept();
            } catch (IOException error) {
                log("Could not accept on port %s", PadServer.config.getPort());
                continue;
            } catch (NullPointerException error) {
                log("Could not accept on port %s", PadServer.config.getPort());
                continue;
            }

            log("Handling new connection on %s.", PadServer.config.getPort());

            ConnectionHandler.handle(socket, String.format("Conn#%d", PadServer.connections), PadServer.db);
            ++PadServer.connections;
        }
    }
}