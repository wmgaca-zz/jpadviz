package lib.types.packages;

import lib.types.PackageType;

import java.io.Serializable;

public abstract class Package implements Serializable {

    protected PackageType type;
    protected long sendTime;

    public Package(PackageType type) {
        this.type = type;
        this.sendTime = System.currentTimeMillis();
    }

    public PackageType getType() {
        return this.type;
    }

    public long getSendTime() {
        return this.sendTime;
    }

    @Override
    public String toString() {
        return String.format("<Package(%s)>", this.type);
    }
}

