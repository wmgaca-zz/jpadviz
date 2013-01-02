package lib.types;

import java.io.Serializable;

public class PADState implements Serializable {

    /**
     * Pleasure
     */
    protected PADValue p;

    /**
     * Arousal
     */
    protected PADValue a;

    /**
     * Dominance
     */
    protected PADValue d;

    /**
     * Measurement timestamp.
     */
    protected long timestamp;

    protected PADState() {
        this.timestamp = System.currentTimeMillis();
    }

    public PADState(PADValue p, PADValue a, PADValue d) {
        this();

        this.p = p;
        this.a = a;
        this.d = d;
    }

    public PADState(PADValue p, PADValue a, PADValue d, long timestamp) {
        this(p, a, d);

        this.timestamp = timestamp;
    }

    public PADValue getP() {
        return p;
    }

    public void setP(PADValue p) {
        this.p = p;
    }

    public PADValue getA() {
        return a;
    }

    public void setA(PADValue a) {
        this.a = a;
    }

    public PADValue getD() {
        return d;
    }

    public void setD(PADValue d) {
        this.d = d;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long value) {
        this.timestamp = value;
    }

    public PADValue getPADValue(PAD.Type type) {
        switch (type) {
            case P:
                return p;
            case A:
                return a;
            case D:
                return d;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return String.format("<PADState(p=%s,a=%s,d=%s)", p, a, d);
    }

    public static PADState getRandom() {
        return new PADState(PADValue.getRandom(), PADValue.getRandom(), PADValue.getRandom());
    }
}
