package lib.types;

import java.io.Serializable;

public class PADState implements Serializable {

    protected float p;
    protected float a;
    protected float d;
    protected float cp;
    protected float ca;
    protected float cd;

    public PADState() {
    }

    public PADState(float p, float a, float d, float cp, float ca, float cd) {
        this();

        this.p = p;
        this.a = a;
        this.d = d;

        this.cp = cp;
        this.ca = ca;
        this.cd = cd;
    }

    public float getP() {
        return this.p;
    }

    public float getCP() {
        return this.cp;
    }

    public float getA() {
        return this.a;
    }

    public float getCA() {
        return this.ca;
    }

    public float getD() {
        return this.d;
    }

    public float getCD() {
        return this.cd;
    }

    public void setP(float p, float cp) {
        this.p = p;
        this.cp = cp;
    }

    public void setA(float a, float ca) {
        this.a = a;
        this.ca = ca;
    }

    public void setD(float d, float cd) {
        this.d = d;
        this.cd = cd;
    }

    @Override
    public String toString() {
        return String.format(
                "<PADState(p=%s/%s,a=%s/%s,d=%s/%s)", this.p, this.cp, this.a, this.ca, this.d, this.cd);
    }
}
