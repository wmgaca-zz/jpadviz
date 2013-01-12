package lib.ui.panels;

import lib.types.PAD;
import lib.types.PADState;
import lib.types.PADValue;
import lib.types.Palette;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class StatePanel extends lib.ui.panels.base.Panel {

    public StatePanel(int width, int height) {
        super(PAD.Type.PAD, width, height);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        PADState current = data.getLastState();

        if (null == current) {
            return;
        }

        g2d.setColor(Palette.black);

        String p = String.format("P: %.2f (%.2f)", current.getP().getValue(), current.getP().getCertainty());
        String a = String.format("A: %.2f (%.2f)", current.getA().getValue(), current.getA().getCertainty());
        String d = String.format("D: %.2f (%.2f)", current.getD().getValue(), current.getD().getCertainty());

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));

        int sLen = (int)g2d.getFontMetrics().getStringBounds(p, g2d).getWidth();
        g2d.drawString(p, getCenterX() - sLen / 2, getCenterY() - 30);

        sLen = (int)g2d.getFontMetrics().getStringBounds(a, g2d).getWidth();
        g2d.drawString(a, getCenterX() - sLen / 2, getCenterY());

        sLen = (int)g2d.getFontMetrics().getStringBounds(d, g2d).getWidth();
        g2d.drawString(d, getCenterX() - sLen / 2, getCenterY() + 30);
    }
}
