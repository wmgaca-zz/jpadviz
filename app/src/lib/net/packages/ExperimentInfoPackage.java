package lib.net.packages;

import lib.types.Client;

/**
 * "Start communication" pacakge, sent by the client (visualiser od data source).
 */
public class ExperimentInfoPackage extends lib.net.packages.base.Package {

    /**
     * Client type.
     */
    protected Client client;

    /**
     * Experiment name
     */
    protected String experimentName;

    /**
     * Method name
     */
    protected String methodName;

    /**
     * Default constructor.
     *
     * @param experimentName Experiment name
     */
    public ExperimentInfoPackage(String experimentName, String methodName) {
        super(Type.EXPERIMENT_INFO);

        this.experimentName = experimentName;
        this.methodName = methodName;
    }



    /**
     * @return Experiment's name
     */
    public String getExperimentName() {
        return experimentName;
    }

    /**
     * @return Method name
     */
    public String getMethodName() {
        return methodName;
    }
}
