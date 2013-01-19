package lib.net.packages.base;

import java.io.Serializable;

/**
 * Base class for all package types.
 */
public abstract class Package implements Serializable {

    /**
     * Package types.
     */
    public static enum Type {
        HANDSHAKE,
        EXPERIMENT_INFO,
        DATA,
        PAD,
        END
    }

    /**
     * Package type.
     */
    protected Type type;

    /**
     * Package creation time in milliseconds (see: System.currentTimeMillis).
     * Not used by the application itself, but can be used to benchmark the network throughput.
     */
    protected long creationTime;

    /**
     * Default constructor.
     *
     * @param type Package type.
     */
    public Package(Type type) {
        this.type = type;
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * @return Package type.
     */
    public Type getType() {
        return type;
    }

    /**
     * @return Package creation time.
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * @return Package string representation.
     */
    @Override
    public String toString() {
        return String.format("<Package(%s)>", type);
    }


}

