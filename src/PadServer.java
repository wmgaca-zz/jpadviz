import lib.DataHandler;
import lib.Utils;
import lib.config.ServerConfig;
import lib.net.ConnectionHandler;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class PadServer {

    public static String workingDir = System.getProperty("user.dir");
    public static File serverConfigFile = new File(PadServer.workingDir, "config/server-settings.xml");
    public static ServerConfig config = new ServerConfig(PadServer.serverConfigFile);
    public static int connections = 0;
    public static DataHandler db = null;

    public static void main(String[] args) {
        // Initialize application: read configuration files, set up things
        PadServer.init();

        // Run server
        PadServer.run();
    }

    public static void init() {
        System.out.println("Initializing...");

        if (!PadServer.config.validate()) {
            Utils.exitOnException(null, "Server config file contains errors.");
        } else {
            System.out.println("Server config file validation: OK.");
        }

        try {
            db = new DataHandler(PadServer.config.getDBConnectionString(),
                                 PadServer.config.getDBUser(),
                                 PadServer.config.getDBPassword());
        } catch (SQLException e) {
            Utils.exitOnException(e, "Cannot connect to database.");
        }
    }

    public static void run() {
        ServerSocket listener = null;

        try {
            listener = new ServerSocket(PadServer.config.getPort());
        } catch (BindException error) {
            Utils.exitOnException(
                    error,
                    String.format("Could not listen on port %s", PadServer.config.getPort()));
        } catch (IOException error) {
            Utils.exitOnException(
                    error,
                    String.format("Could not listen on port %s", PadServer.config.getPort()));
        }

        System.out.println(String.format("Listening on port %s", PadServer.config.getPort()));

        while (true) {
            Socket socket;

            try {
                socket = listener.accept();
            } catch (IOException error) {
                System.out.println(String.format("Could not accept on port %s", PadServer.config.getPort()));
                continue;
            } catch (NullPointerException error) {
                System.out.println(String.format("Could not accept on port %s", PadServer.config.getPort()));
                continue;
            }

            System.out.println(String.format("Handling new connection on %s.", PadServer.config.getPort()));

            ConnectionHandler.handle(socket, String.format("Conn#%d", PadServer.connections), PadServer.db);
            ++PadServer.connections;
        }
    }
}