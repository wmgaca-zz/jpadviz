package lib.net.packages;

public class RequestDataPackage extends lib.net.packages.base.Package {

    protected int resultSetId;

    public RequestDataPackage(int resultSetId) {
        super(Type.DATA);

        this.resultSetId = resultSetId;
    }

    public int getResultSetId() {
        return resultSetId;
    }

}
