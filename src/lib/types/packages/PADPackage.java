package lib.types.packages;

import lib.Utils;
import lib.types.PADState;
import lib.types.PackageType;

public class PADPackage extends Package {

    protected PADState state;

    public PADPackage() {
        super(PackageType.PAD);
    }

    public PADPackage(float p, float a, float d, float cp, float ca, float cd) {
        this();

        this.state = new PADState(p, a, d, cp, ca, cd);
    }

    public void setState(PADState value) {
        this.state = value;
    }

    public PADState getState() {
        return this.state;
    }

    @Override
    public String toString() {
        return String.format("<PADPackage(state=%s)>", this.state.toString());
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
