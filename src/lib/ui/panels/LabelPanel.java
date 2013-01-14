package lib.ui.panels;

import lib.types.*;
import lib.types.Label;
import lib.ui.panels.base.Panel;
import lib.utils.Utils;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static lib.utils.Logging.log;

public class LabelPanel extends Panel {

    protected static LabelPanel instance = null;

    public static LabelPanel getInstance(int width, int height, LabelConfig labelConfig) {
        if (null == instance) {
            instance = new LabelPanel(width, height, labelConfig);
        }

        return instance;
    }

    protected LabelConfig labelConfig;

    public LabelPanel(int width, int height, LabelConfig labelConfig) {
        super(PAD.Type.PAD, width, height);

        this.labelConfig = labelConfig;
        this.margin.right = 50;
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        log("label customPaintComponent!!!!");
        // Draw bottom Y line
        g2d.setColor(Palette.black);
        g2d.drawLine(margin.left, margin.top + getH(), getWidth() - margin.right, margin.top + getH());

        for (PADState state : data.getCurrentValues()) {
            int x = getXForTime(state.getTimestamp(), data.getCurrentStartTime(), data.getCurrentEndTime());

            g2d.setColor(Palette.black);

            Label label = labelConfig.getMatchingLabel(state);

            g2d.setFont(new Font("Arial", Font.PLAIN, (int)(state.getP().getCertainty() * 20f) + 5));

            int labelWidth = (int) g2d.getFontMetrics().getStringBounds(label.getName(), g2d).getWidth();
            int labelHeight = (int) g2d.getFontMetrics().getStringBounds(label.getName(), g2d).getHeight();

            int border = 2;

            g2d.setColor(label.getColor());
            Rectangle2D rect = new Rectangle.Double(x, getCenterY() - labelHeight, labelWidth + 2 * border, labelHeight + 2 * border);
            g2d.fill(rect);
            g2d.draw(rect);
            g2d.setColor(Utils.getNegative(label.getColor()));
            g2d.drawString(label.getName(), x + border, getCenterY() + border);
        }
    }
}
