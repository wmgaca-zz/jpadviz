package lib.types;

/**
 * Class for easy handling a set of coordinates attributes.
 */
public class Coords {

    /**
     * X coordinate.
     */
    protected int x;

    /**
     * Y coordinate.
     */
    protected int y;

    /**
     * Default constructor
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * @param value X coorinate
     */
    public void setX(int value) {
        x = value;
    }

    /**
     * @return Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @param value Y coordinate
     */
    public void setY(int value) {
        y = value;
    }

}
