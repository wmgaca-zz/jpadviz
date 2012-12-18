package lib.types.packages;

import lib.types.PackageType;

public class DataPackage extends lib.types.packages.base.Package {

    protected String message;

    public DataPackage(String message) {
        super(PackageType.DATA);

        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
