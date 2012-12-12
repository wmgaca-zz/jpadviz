import lib.ServerConfig;
import lib.Utils;
import lib.types.ClientType;
import lib.types.packages.EndPackage;
import lib.types.packages.HandshakePackage;
import lib.types.packages.PADPackage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class PadDummyClient {
    public static ServerConfig config = PadServer.config;

    public static void main(String[] args) throws InterruptedException {
        String host = PadDummyClient.config.getHost();
        int port = PadDummyClient.config.getPort();

        try {
            Socket socket = new Socket(host, port);

            System.out.println(String.format("Connection established with %s:%s", host, port));

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new HandshakePackage(ClientType.DATA_SOURCE));

            for (int i = 0; i < 100; ++i) {
                out.writeObject(PADPackage.getRandom());
                Thread.sleep(1000);
            }

            out.writeObject(new EndPackage());

            socket.close();

        } catch (UnknownHostException error) {
            Utils.exitOnException(
                    error,
                    String.format("Unknown host: %s", host));
        } catch (IOException error) {
            Utils.exitOnException(
                    error,
                    String.format("No I/O"));
        }
    }
}
