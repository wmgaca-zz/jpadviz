package lib.net.packages;

import java.util.ArrayList;

/**
 * Package for requesting a finished experiment's data set from the server.
 */
public class RequestDataPackage extends lib.net.packages.base.Package {

    /**
     * Experiments identifier.
     */
    protected int experimentId;

    /**
     * Required method's identifiers.
     */
    protected ArrayList<Integer> methods;

    /**
     * Initializes object with experiment identifier only.
     *
     * @param experimentId Experiment's identifier.
     */
    public RequestDataPackage(int experimentId) {
        this(experimentId, null);
    }

    /**
     * Default constructor
     *
     * @param experimentId Experiment's identifier.
     * @param methods Required method's identifiers.
     */
    public RequestDataPackage(int experimentId, ArrayList<Integer> methods) {
        super(Type.DATA);

        this.experimentId = experimentId;
        this.methods = methods;
    }

    /**
     * @return Experiment's identifier.
     */
    public int getExperimentId() {
        return experimentId;
    }

    /**
     * @return Experiment's methods.
     */
    public ArrayList<Integer> getMethods() {
        return methods;
    }

}
