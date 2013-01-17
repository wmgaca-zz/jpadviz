package lib.net.packages;

import lib.types.PADState;
import lib.types.PADValue;

/**
 * Package for transmission of PAD states.
 */
public class PADPackage extends lib.net.packages.base.Package {

    /**
     * PAD state.
     */
    protected PADState state;

    /**
     * Default constructor.
     */
    public PADPackage() {
        super(Type.PAD);
    }

    /**
     * Initializes the package with a PAD state.
     *
     * @param padState PAD state.
     */
    public PADPackage(PADState padState) {
        this();
        state = padState;
    }

    /**
     * Initializes the package with a PAD state created of the p, a, and d values given.
     *
     * @param p PADState.p.value
     * @param a PADState.a.valule
     * @param d PADState.d.value
     */
    public PADPackage(PADValue p, PADValue a, PADValue d) {
        this();

        state = new PADState(p, a, d);
    }

    /**
     * @param value PAD state.
     */
    public void setState(PADState value) {
        state = value;
    }

    /**
     * @return Package's PAD state.
     */
    public PADState getState() {
        return state;
    }

    /**
     * @return Package's string representation.
     */
    @Override
    public String toString() {
        return String.format("<PADPackage(state=%s)>", state.toString());
    }

    /**
     * Random package generator.
     *
     * @return A random PAD package.
     */
    public static PADPackage getRandom() {
        return new PADPackage(PADState.getRandom());
    }

}
