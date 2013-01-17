package lib.net.packages;

import lib.types.Client;

/**
 * "Start communication" pacakge, sent by the client (visualiser od data source).
 */
public class HandshakePackage extends lib.net.packages.base.Package {

    /**
     * Client type.
     */
    protected Client client;

    /**
     * Default constructor.
     *
     * @param client Client type.
     */
    public HandshakePackage(Client client) {
        super(Type.HANDSHAKE);

        this.client = client;
    }

    /**
     * @return Client type.
     */
    public Client getClient() {
        return this.client;
    }
}
