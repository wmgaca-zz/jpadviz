import lib.utils.Utils;
import lib.config.ServerConfig;
import lib.types.Client;
import lib.net.packages.EndPackage;
import lib.net.packages.HandshakePackage;
import lib.net.packages.PADPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import static lib.utils.Logging.log;

/**
 * Sample data source implementation
 */
public class PadDummyClient {
    public static ServerConfig config = PadServer.config;

    /**
     * Entry point
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        String host = PadDummyClient.config.getHost();
        int port = PadDummyClient.config.getPort();

        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Socket socket = null;

        try {
            socket = new Socket(host, port);

            log("Connection established with %s:%s", host, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new HandshakePackage(Client.DATA_SOURCE));

            while (true) {
                out.writeObject(PADPackage.getRandom());
                Thread.sleep(Utils.getRandomGenerator().nextInt(2500) + 500);
            }

        } catch (UnknownHostException error) {
            Utils.exitOnException(
                    error,
                    String.format("Unknown host: %s", host));
        } catch (IOException error) {
            Utils.exitOnException(
                    error,
                    String.format("No I/O"));
        } finally {
            if (null != out) {
                try {
                    out.writeObject(new EndPackage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
