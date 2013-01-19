package lib.net;

import lib.DataHandler;
import lib.types.Client;
import lib.types.PADState;
import lib.net.packages.*;
import lib.net.packages.base.Package;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static lib.utils.Logging.log;

/**
 * Handles connections at server's side.
 *
 * A new thread running ConnectionHandler is spawned to handle every new connection to the server.
 */
public class ConnectionHandler implements Runnable {

    /**
     * All instances of connection handlers.
     */
    protected static final ArrayList<ConnectionHandler> connectionHandlers = new ArrayList<ConnectionHandler>();

    /**
     * Connection's socket.
     */
    private Socket socket;

    /**
     * Connection name.
     */
    private String name;

    /**
     * Client type.
     */
    private Client client;

    /**
     * Object input stream (for reading data).
     */
    private ObjectInputStream input = null;

    /**
     * Object output stream (for writing data).
     */
    private ObjectOutputStream output = null;

    /**
     * Database handler.
     */
    private DataHandler db = null;

    /**
     * Default constructor.
     *
     * @param socket Connection's socket
     * @param name Connection name
     * @param db Database handler
     */
    ConnectionHandler(Socket socket, String name, DataHandler db) {
        this.socket = socket;
        this.name = name;
        this.db = db;
    }

    /**
     * End current connection
     */
    private void endConnection() {
        log("End connection for %s", name);

        removeHandler(this);

        if (null != input) {
            try {
                input.close();
            } catch (IOException e) {
                log("Input stream already closed.");
            }
        }

        if (null != output) {
            try {
                output.close();
            } catch (IOException e) {
                log("Output stream already closed.");
            }
        }
    }

    /**
     * Read next package.
     *
     * @return Package object
     */
    private Package readPackage() {
        if (input == null) {
            return null;
        }

        Package data;

        try {
            data = (Package) input.readObject();
        } catch (ClassNotFoundException error) {
            error.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return data;
    }

    /**
     * Entry point for all new class instances.
     */
    public void run() {
        Package data = null;

        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            data = readPackage();
        } catch (IOException error) {
            log("IOException on socket listen: %s", error.toString());
            endConnection();
        }

        if (null == data) {
            log("Null package received.");
            endConnection();
        } else if (data instanceof  HandshakePackage) {
            client = ((HandshakePackage)data).getClient();

            registerHandler(this);

            if (Client.DATA_SOURCE == client) {
                listen();
            } else if (Client.VISUALISER == client) {
                // ...
            } else {
                // ...
            }
        } else if (data instanceof RequestDataPackage) {
            int experimentId = ((RequestDataPackage)data).getExperimentId();
            ArrayList<Integer> methods = ((RequestDataPackage)data).getMethods();

            ArrayList<PADState> states = db.fetchAll(experimentId, methods);

            for (PADState state : states) {
                send(new PADPackage(state));
            }

            endConnection();
        } else {
            log("Incorrect handshake.");
            endConnection();
        }
    }

    /**
     * Listening mode: for interaction with the data source.
     */
    public void listen() {
        Package data;


        try {

            data = (Package)input.readObject();

            if (!(data instanceof ExperimentInfoPackage)) {
                log("Incorrect data, expecting experiment info package...");
                endConnection();
                return;
            }

            int experimentId = db.getExperimentIdByName(((ExperimentInfoPackage) data).getExperimentName());
            int methodId = db.getMethodIdByName(((ExperimentInfoPackage) data).getMethodName());

            // Create new session
            int sessionId = db.createSession(experimentId, methodId);

            // Create result set
            int resultSetId = db.createResultSet(sessionId);

            while ((data = (Package) input.readObject()) != null) {
                log("Package received: %s, %s", name, data);

                if (data instanceof PADPackage) {
                    ConnectionHandler.broadcast(data);
                    db.insertPadValue((PADPackage) data, resultSetId);
                } else if (data instanceof EndPackage) {
                    endConnection();
                    return;
                }
            }
        } catch (SocketException error) {
            log("Ending [2]");
            this.endConnection();
            return;
        } catch (IOException error) {
            log("Ending [1]");
            error.printStackTrace();
            this.endConnection();
            return;
        } catch (ClassNotFoundException error ){
            error.printStackTrace();
        }

        this.endConnection();
    }

    /**
     * Send package the the output stream.
     *
     * @param data Package to be sent.
     */
    public void send(Package data) {
        if (null == output) {
            log("Cannot write object. No output.");
            return;
        }

        try {
            output.writeObject(data);
        } catch (SocketException error) {
            log("Socket error. Closing connection.");
            endConnection();
        } catch (IOException error) {
            log("IOException on socket writeObject: %s", error.toString());
            error.printStackTrace();
        }
    }

    /**
     * Add a new handler to the handlers list.
     *
     * @param handler Handler object
     */
    protected static void registerHandler(ConnectionHandler handler) {
        synchronized (connectionHandlers) {
            if (!connectionHandlers.contains(handler)) {
                connectionHandlers.add(handler);
            }
        }
    }

    /**
     * Remove a handler from the handlers list (on connection end)
     *
     * @param handler Handler object to be removed.
     */
    protected static void removeHandler(ConnectionHandler handler) {
        synchronized (connectionHandlers) {
            if (connectionHandlers.contains(handler)) {
                connectionHandlers.remove(handler);
            }
        }
    }

    /**
     * Broadcast a package to all listening visualiser instances.
     *
     * @param data Package to be broadcasted
     */
    protected static void broadcast(Package data) {
        synchronized (connectionHandlers) {
            for (int i = 0; i < connectionHandlers.size(); ++i) {
                ConnectionHandler handler = connectionHandlers.get(i);
                if (Client.VISUALISER == handler.client) {
                    handler.send(data);
                }
            }
        }
    }

    /**
     * Handle a new socket connection: create and run a new ConnectionHandler instance.
     *
     * @param socket Connection's socket
     * @param name Connection name
     * @param db Database handler
     */
    public static void handle(Socket socket, String name, DataHandler db) {
        (new Thread(new ConnectionHandler(socket, name, db))).start();
    }
}
