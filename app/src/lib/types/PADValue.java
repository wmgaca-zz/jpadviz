package lib.types;

import lib.utils.Utils;

import java.io.Serializable;

/**
 * Reresents a singele state's value.
 */
public class PADValue implements Serializable {

    /**
     * Value: (-1f, 1f)
     */
    public float value = 0f;

    /**
     * Certainty: (0f, 1f)
     */
    public float certainty = 0f;

    /**
     * Timestamp in milliseconds.
     */
    public long timestamp = 0;

    /**
     * @param value Metric's value
     * @param certainty Metric's certainty
     * @param timestamp Metric's timestamp
     */
    public PADValue(float value, float certainty, long timestamp) {
        this.value = value;
        this.certainty = certainty;
        this.timestamp = timestamp;
    }

    /**
     * Create a single metric's value with given value and current time
     *
     * @param value Metric's value
     * @param certainty Metric's certainty
     */
    public PADValue(float value, float certainty) {
        this(value, certainty, System.currentTimeMillis());
    }

    /**
     * Metric's value and certainty setter.
     * @see #value
     * @see #certainty
     * @param value metric's value
     * @param certainty metric's certainty
     */
    public void set(float value, float certainty) {
        this.value = value;
        this.certainty = certainty;
    }

    /**
     * Returns metric's value
     * @see #value
     * @return metric's value
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets metric's value
     * @see #value
     * @param value metric's value
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Returns metric's certainty
     * @see #certainty
     * @return metric's certainty
     */
    public float getCertainty() {
        return certainty;
    }

    /**
     * Sets metric's certainty
     * @see #certainty
     * @param certainty metric's certainty
     */
    public void setCertainty(float certainty) {
        this.certainty = certainty;
    }

    /**
     * Returns metric's timestamp
     * @see #timestamp
     * @return metric's timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets metric's timestamp
     * @see #timestamp
     * @param timestamp metric's timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return Value's string representation.
     */
    @Override
    public String toString() {
        return String.format("<PadValue(%s,%s)", value, certainty);
    }

    /**
     * Get PADValue with random generated values.
     *
     * @return random PADValue object.
     */
    public static PADValue getRandom() {
        return new PADValue(Utils.getRandomGenerator().nextFloat() * 2.0f - 1.0f,
                            Utils.getRandomGenerator().nextFloat());
    }

}
