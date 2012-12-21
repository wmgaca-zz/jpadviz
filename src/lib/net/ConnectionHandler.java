package lib.net;

import lib.DataHandler;
import lib.types.ClientType;
import lib.types.PADState;
import lib.types.packages.*;
import lib.types.packages.base.Package;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
public class ConnectionHandler implements Runnable {

    private static List<ConnectionHandler> connectionHandlers = new ArrayList<ConnectionHandler>();
    private static List<ConnectionHandler> closeConnectionHandlers = new ArrayList<ConnectionHandler>();

    private Socket socket;
    private String name;
    private ClientType clientType;

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private DataHandler db = null;

    ConnectionHandler(Socket socket, String name, DataHandler db) {
        this.socket = socket;
        this.name = name;
        this.db = db;
    }

    private void endConnection() {
        ConnectionHandler.closeConnectionHandlers.add(this);

        if (null != in) {
            try {
                in.close();
            } catch (SocketException error) {
                 System.out.println("Socket already closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != out) {
            try {
                out.close();
            } catch (SocketException error) {
                System.out.println("Socket already closed");
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    private Package readPackage() {
        if (in == null) {
            return null;
        }

        Package data = null;

        try {
            data = (Package)in.readObject();
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
        Package data;

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            data = readPackage();

            if (null == data) {
                System.out.println("Null package received. Terminating.");
                endConnection();
                return;
            }

            if (data instanceof  HandshakePackage) {
                clientType = ((HandshakePackage)data).getClientType();

                ConnectionHandler.connectionHandlers.add(this);

                if (ClientType.DATA_SOURCE == clientType) {
                    listen();
                } else if (ClientType.VISUALISER == clientType) {
                    // ...
                } else {
                    // ...
                }
            } else if (data instanceof RequestDataPackage) {
                ArrayList<PADState> states = db.fetchAll(((RequestDataPackage) data).getResultSetId());
                for (PADState state : states) {
                    out.writeObject(new PADPackage(state));
                }
                endConnection();
            } else {
                System.out.println("Incorrect handshake. Terminating.");
                endConnection();
            }
        } catch (IOException error) {
            System.out.println(String.format("IOException on socket listen: %s", error.toString()));
            endConnection();
        }
    }

    public void listen() {
        Package data;

        long lastPackageTime = System.currentTimeMillis();
        long timeDelta;
        long currentTime;
        long packageTransferTime;

        // Create new session
        int sessionId = db.createSession(1, 1);

        // Create result set
        int resultSetId = db.createResultSet(sessionId);

        try {
            while ((data = (Package)in.readObject()) != null) {
                currentTime = System.currentTimeMillis();
                timeDelta = currentTime - lastPackageTime;
                lastPackageTime = currentTime;
                packageTransferTime = currentTime - data.getSendTime();

                System.out.println(String.format("%s %s %s [Package received]", timeDelta, packageTransferTime, name));

                if (data instanceof PADPackage) {
                    ConnectionHandler.broadcast(data);
                    db.insertPadValue((PADPackage) data, resultSetId);
                } else if (data instanceof EndPackage) {
                    endConnection();
                    return;
                }
            }
        } catch (SocketException error) {
            System.out.println("Ending [2]");
            this.endConnection();
            return;
        } catch (IOException error) {
            System.out.println("Ending [1]");
            error.printStackTrace();
            this.endConnection();
            return;
        } catch (ClassNotFoundException error ){
            error.printStackTrace();
        }

        this.endConnection();
    }

    public void send(Package data) {
        if (null == out) {
            System.out.println("Cannot write object. No output.");
            return;
        }

        try {
            System.out.println("Sending package (forward).");
            out.writeObject(data);
        } catch (SocketException error) {
            System.out.println("Socket error. Closing connection.");
            endConnection();
        } catch (IOException error) {
            System.out.println(String.format("IOException on socket writeObject: %s", error.toString()));
            error.printStackTrace();
        }

    }

    private static void broadcast(Package data) {
        for (ConnectionHandler handler : ConnectionHandler.connectionHandlers) {
            if (ClientType.VISUALISER == handler.clientType) {
                System.out.println("Forwarding package.");
                handler.send(data);
            }
        }

        for (ConnectionHandler handler : ConnectionHandler.closeConnectionHandlers) {
            ConnectionHandler.connectionHandlers.remove(handler);
        }
    }

    public static void handle(Socket socket, String name, DataHandler db) {
        Thread thread = new Thread(new ConnectionHandler(socket, name, db));
        thread.start();
    }
}
