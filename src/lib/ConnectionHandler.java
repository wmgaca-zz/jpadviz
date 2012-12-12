package lib;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import lib.types.*;
import lib.types.packages.*;
import lib.types.packages.Package;
public class ConnectionHandler implements Runnable {

    private static List<ConnectionHandler> connectionHandlers = new ArrayList<ConnectionHandler>();
    private static List<ConnectionHandler> closeConnectionHandlers = new ArrayList<ConnectionHandler>();

    private Socket socket;
    private String name;
    private ClientType clientType;

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    ConnectionHandler(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    private void endConnection() {
        ConnectionHandler.closeConnectionHandlers.add(this);

        if (null != this.in) {
            try {
                this.in.close();
            } catch (SocketException error) {
                 System.out.println("Socket already closed");
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        if (null != this.out) {
            try {
                this.out.close();
            } catch (SocketException error) {
                System.out.println("Socket already closed");
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    private Package readPackage() {
        if (this.in == null) {
            return null;
        }

        Package data = null;

        try {
            data = (Package)this.in.readObject();
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
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.out = new ObjectOutputStream(this.socket.getOutputStream());

            data = this.readPackage();

            if (null == data) {
                System.out.println("Null package received. Terminating.");
                this.endConnection();
                return;
            }

            if (!(data instanceof HandshakePackage)) {
                System.out.println("Incorrect handshake. Terminating.");
                this.endConnection();
                return;
            }

            this.clientType = ((HandshakePackage)data).getClientType();

            ConnectionHandler.connectionHandlers.add(this);

            if (ClientType.DATA_SOURCE == this.clientType) {
                this.listen();
            } else if (ClientType.VISUALISER == this.clientType) {
                // ...
            } else {
                // ...
            }

        } catch (IOException error) {
            System.out.println(String.format("IOException on socket listen: %s", error.toString()));
            this.endConnection();
            return;
        }
    }

    public void listen() {
        Package data;

        long lastPackageTime = System.currentTimeMillis();
        long timeDelta;
        long currentTime;
        long packageTransferTime;

        try {
            while ((data = (Package)this.in.readObject()) != null) {
                currentTime = System.currentTimeMillis();
                timeDelta = currentTime - lastPackageTime;
                lastPackageTime = currentTime;
                packageTransferTime = currentTime - data.getSendTime();

                System.out.println(String.format("%s %s %s [Package received]", timeDelta, packageTransferTime, this.name));

                if (data instanceof DataPackage || data instanceof PADPackage) {
                    ConnectionHandler.broadcast(data);
                } else if (data instanceof EndPackage) {
                    this.endConnection();
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
        if (null == this.out) {
            System.out.println("Cannot write object. No output.");
            return;
        }

        try {
            System.out.println("Sending package (forward).");
            this.out.writeObject(data);
        } catch (SocketException error) {
            System.out.println("Socket error. Closing connection.");
            this.endConnection();
            return;
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

    public static void handle(Socket socket, String name) {
        Thread thread = new Thread(new ConnectionHandler(socket, name));
        thread.start();
    }
}
