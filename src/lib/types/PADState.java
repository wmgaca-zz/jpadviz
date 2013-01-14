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

    protected int method;

    protected PADState() {
        this(null, null, null);
    }

    public PADState(PADValue p, PADValue a, PADValue d) {
        this(p, a, d, System.currentTimeMillis(), 1);
    }

    public PADState(PADValue p, PADValue a, PADValue d, long timestamp) {
        this(p, a, d, timestamp, 1);
    }

    public PADState(PADValue p, PADValue a, PADValue d, long timestamp, int method) {
        this.p = p;
        this.a = a;
        this.d = d;

        this.timestamp = timestamp;
        this.method = method;
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

    public int getMethod() {
        return method;
    }

    public void setMethod(int value) {
        method = value;
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
