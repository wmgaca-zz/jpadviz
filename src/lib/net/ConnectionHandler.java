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

public class ConnectionHandler implements Runnable {

    protected static final ArrayList<ConnectionHandler> connectionHandlers = new ArrayList<ConnectionHandler>();

    private Socket socket;
    private String name;
    private Client client;

    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;

    private DataHandler db = null;

    ConnectionHandler(Socket socket, String name, DataHandler db) {
        this.socket = socket;
        this.name = name;
        this.db = db;
    }

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
            ArrayList<PADState> states = db.fetchAll(((RequestDataPackage) data).getResultSetId());

            for (PADState state : states) {
                send(new PADPackage(state));
            }
        } else {
            log("Incorrect handshake.");
            endConnection();
        }
    }

    public void listen() {
        Package data;

        // Create new session
        int sessionId = db.createSession(1, 1);

        // Create result set
        int resultSetId = db.createResultSet(sessionId);

        try {
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

    protected static void registerHandler(ConnectionHandler handler) {
        synchronized (connectionHandlers) {
            if (!connectionHandlers.contains(handler)) {
                connectionHandlers.add(handler);
            }
        }
    }

    protected static void removeHandler(ConnectionHandler handler) {
        synchronized (connectionHandlers) {
            if (connectionHandlers.contains(handler)) {
                connectionHandlers.remove(handler);
            }
        }
    }

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

    public static void handle(Socket socket, String name, DataHandler db) {
        (new Thread(new ConnectionHandler(socket, name, db))).start();
    }
}
