package lib.ui.panels;

import lib.types.PAD;
import lib.types.PADState;
import lib.types.PADValue;
import lib.types.Palette;
import lib.ui.panels.base.SinglePanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SingleLabelPanel extends SinglePanel {

    public SingleLabelPanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        if (values.size() == 0) {
            return;
        }

        PADValue currentValue = values.get(values.size() - 1);

        // Colored background for Positiveness
        if (PAD.Type.P == type) {
            Color color = (currentValue.getValue() > 0) ? Palette.green : Palette.red;
            color = Palette.getTransparent(color, currentValue.getCertainty());

            g2d.setColor(color);

            Rectangle2D rect = new Rectangle2D.Double(margin.left, margin.top, getW(), getH());
            g2d.draw(rect);
            g2d.fill(rect);

        }

        g2d.setColor(Palette.black);
        String s = String.format("%.2f / %.2f", currentValue.getValue(), currentValue.getCertainty());
        g2d.setFont(new Font("Arial", Font.PLAIN, 30));

        int sLen = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        g2d.drawString(s, getCenterX() - sLen / 2, getCenterY());

        //g2d.drawString(s, 20, margin.top + getH() / 2 + 10 );


    }
}
