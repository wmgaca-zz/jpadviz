package lib.types;

public class SinglePADValue {
    public float value = 0;
    public float certainty = 0;
    public long timestamp = 0;

    public SinglePADValue(float value, float certainty, long timestamp) {
        this.value = value;
        this.certainty = certainty;
        this.timestamp = timestamp;
    }
}
