package lib.net.packages;

import java.util.ArrayList;

public class RequestDataPackage extends lib.net.packages.base.Package {

    protected int experimentId;
    protected ArrayList<Integer> methods;

    public RequestDataPackage(int experimentId) {
        this(experimentId, null);
    }

    public RequestDataPackage(int experimentId, ArrayList<Integer> methods) {
        super(Type.DATA);

        this.experimentId = experimentId;
        this.methods = methods;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public ArrayList<Integer> getMethods() {
        return methods;
    }

}
