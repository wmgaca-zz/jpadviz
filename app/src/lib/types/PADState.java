package lib.types;

import java.io.Serializable;

/**
 * Class for handling PAD state information.
 */
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

    /**
     * Method used for data collection
     */
    protected int method;

    /**
     * Create an empty state.
     */
    protected PADState() {
        this(null, null, null);
    }

    /**
     * Create a new state, init with given values, current time and default method.
     *
     * @param p P value
     * @param a A value
     * @param d D value
     */
    public PADState(PADValue p, PADValue a, PADValue d) {
        this(p, a, d, System.currentTimeMillis(), 1);
    }

    /**
     * Create a new state, init with given values, time and default method.
     *
     * @param p P value
     * @param a A value
     * @param d D value
     * @param timestamp Timestamp
     */

    public PADState(PADValue p, PADValue a, PADValue d, long timestamp) {
        this(p, a, d, timestamp, 1);
    }

    /**
     * Create a new state, init with given values, time and method.
     *
     * @param p P value
     * @param a A value
     * @param d D value
     * @param timestamp State's timestamp
     * @param method Method used to collect the data
     */
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

    /**
     * @param type Value type
     * @return Given value from the state
     */
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

    /**
     * @return State's string representation
     */
    @Override
    public String toString() {
        return String.format("<PADState(p=%s,a=%s,d=%s)", p, a, d);
    }

    /**
     * @return A new state with random metric values
     */
    public static PADState getRandom() {
        return new PADState(PADValue.getRandom(), PADValue.getRandom(), PADValue.getRandom());
    }
}
