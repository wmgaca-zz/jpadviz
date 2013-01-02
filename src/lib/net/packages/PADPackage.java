package lib.net.packages;

import lib.types.PADState;
import lib.types.PADValue;

public class PADPackage extends lib.net.packages.base.Package {

    protected PADState state;

    public PADPackage() {
        super(Type.PAD);
    }

    public PADPackage(PADState padState) {
        this();
        state = padState;
    }

    public PADPackage(PADValue p, PADValue a, PADValue d) {
        this();

        state = new PADState(p, a, d);
    }

    public void setState(PADState value) {
        state = value;
    }

    public PADState getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("<PADPackage(state=%s)>", state.toString());
    }

    public static PADPackage getRandom() {
        return new PADPackage(PADState.getRandom());
    }

}
