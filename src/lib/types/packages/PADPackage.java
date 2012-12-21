package lib.types.packages;

import lib.Utils;
import lib.types.PADState;
import lib.types.PackageType;

public class PADPackage extends lib.types.packages.base.Package {

    protected PADState state;

    public PADPackage() {
        super(PackageType.PAD);
    }

    public PADPackage(PADState padState) {
        this();
        state = padState;
    }

    public PADPackage(float p, float a, float d, float cp, float ca, float cd) {
        this();

        state = new PADState(p, a, d, cp, ca, cd);
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
        PADPackage pad = new PADPackage(
                Utils.getRandomGenerator().nextFloat() * 2.0f - 1.0f,
                Utils.getRandomGenerator().nextFloat() * 2.0f - 1.0f,
                Utils.getRandomGenerator().nextFloat() * 2.0f - 1.0f,
                Utils.getRandomGenerator().nextFloat(),
                Utils.getRandomGenerator().nextFloat(),
                Utils.getRandomGenerator().nextFloat());

        return pad;
    }

}
