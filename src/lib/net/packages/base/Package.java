package lib.net.packages.base;

import java.io.Serializable;

public abstract class Package implements Serializable {

    public static enum Type {
        HANDSHAKE,
        DATA,
        PAD,
        END
    }

    protected Type type;
    protected long creationTime;

    public Package(Type type) {
        this.type = type;
        this.creationTime = System.currentTimeMillis();
    }

    public Type getType() {
        return type;
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return String.format("<Package(%s)>", type);
    }


}

