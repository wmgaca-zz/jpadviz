package lib.types;

import java.awt.*;

public class Palette {
    public static Color transparent = new Color(0, 0, 0, 0f);
    public static Color blue = new Color(0, 0, 255);
    public static Color red = new Color(255, 0, 0);
    public static Color green = new Color(0, 255, 0);
    public static Color black = new Color(0, 0, 0);
    public static Color grey = Color.gray;
    public static Color white = Color.white;

    public static Color getTransparent(Color color, float alpha) {
        return new Color(color.getRed(),
                         color.getGreen(),
                         color.getBlue(),
                         (int)(alpha * 255));
    }
}
