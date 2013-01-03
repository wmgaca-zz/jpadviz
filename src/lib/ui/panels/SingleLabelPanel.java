package lib.ui.panels;

import lib.types.PAD;
import lib.types.PADState;
import lib.types.PADValue;
import lib.types.Palette;
import lib.ui.panels.base.BasePanel;
import lib.ui.panels.base.SinglePanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SingleLabelPanel extends BasePanel {

    public SingleLabelPanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {

        PADValue current = data.getLastValue(type);

        // Colored background for Positiveness
        if (PAD.Type.P == type) {
            Color color = (current.getValue() > 0) ? Palette.green : Palette.red;
            color = Palette.getTransparent(color, current.getCertainty());

            g2d.setColor(color);

            Rectangle2D rect = new Rectangle2D.Double(margin.left, margin.top, getW(), getH());
            g2d.draw(rect);
            g2d.fill(rect);

        }

        g2d.setColor(Palette.black);
        String s = String.format("%.2f / %.2f", current.getValue(), current.getCertainty());
        g2d.setFont(new Font("Arial", Font.PLAIN, 30));

        int sLen = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        g2d.drawString(s, getCenterX() - sLen / 2, getCenterY());
    }
}
