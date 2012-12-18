package lib.types.packages;

import lib.types.ClientType;
import lib.types.PackageType;

public class HandshakePackage extends lib.types.packages.base.Package {

    protected ClientType clientType;

    public HandshakePackage(ClientType clientType) {
        super(PackageType.HANDSHAKE);

        this.clientType = clientType;
    }

    public ClientType getClientType() {
        return this.clientType;
    }
}
