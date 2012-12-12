import java.io.*;
import java.net.*;

import lib.ConnectionHandler;
import lib.ServerConfig;
import lib.Utils;

public class PadServer {

    public static String workingDir = System.getProperty("user.dir");
    public static File serverConfigFile = new File(PadServer.workingDir, "config/server-settings.xml");
    public static ServerConfig config = new ServerConfig(PadServer.serverConfigFile);
    public static int connections = 0;

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

            ConnectionHandler.handle(socket, String.format("Conn#%d", PadServer.connections));
            ++PadServer.connections;
        }
    }
}