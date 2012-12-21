package lib.types.packages;

import lib.types.PackageType;

public class RequestDataPackage extends lib.types.packages.base.Package {

    protected int resultSetId;

    public RequestDataPackage(int resultSetId) {
        super(PackageType.DATA);

        this.resultSetId = resultSetId;
    }

    public int getResultSetId() {
        return resultSetId;
    }

}
