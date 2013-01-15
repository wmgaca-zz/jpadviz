package lib.net.packages;

import lib.types.Client;

public class HandshakePackage extends lib.net.packages.base.Package {

    protected Client client;

    public HandshakePackage(Client client) {
        super(Type.HANDSHAKE);

        this.client = client;
    }

    public Client getClient() {
        return this.client;
    }
}
