package lib.types.packages;

import lib.types.PackageType;

public class DataPackage extends Package {

    protected String message;

    public DataPackage(String message) {
        super(PackageType.DATA);

        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
