package lib.types;

/**
 * Helper class for easier handling drawing margins in the panels.
 */
public class Margin {

    /**
     * Top margin in pixels.
     */
    public int top;

    /**
     * Bottom margin in pixels.
     */
    public int bottom;

    /**
     * Left margin in pixels.
     */
    public int left;

    /**
     * Right margin in pixels.
     */
    public int right;

    /**
     * Default constructor
     *
     * @param top Top margin in pixels
     * @param left Left margin in pixels
     * @param bottom Bottom margin in pixels
     * @param right Right margin in pixels
     */
    public Margin(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

}
