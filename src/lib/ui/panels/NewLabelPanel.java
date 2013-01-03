
package lib.ui.panels;

import lib.types.*;
import lib.types.Label;
import lib.ui.panels.base.BasePanel;
import lib.utils.Utils;
import static lib.utils.Logging.log;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class NewLabelPanel extends BasePanel {

    protected static NewLabelPanel instance = null;

    public static NewLabelPanel getInstance(int width, int height, LabelConfig labelConfig) {
        if (null == instance) {
            instance = new NewLabelPanel(width, height, labelConfig);
        }

        return instance;
    }

    protected LabelConfig labelConfig;

    public NewLabelPanel(int width, int height, LabelConfig labelConfig) {
        super(PAD.Type.PAD, width, height);

        this.labelConfig = labelConfig;
        this.margin.right = 50;
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        // Draw bottom Y line
        g2d.setColor(Palette.black);
        g2d.drawLine(margin.left, margin.top + getH(), getWidth() - margin.right, margin.top + getH());

        log("size: %s", data.getCurrentValues().size());

        for (PADState state : data.getCurrentValues()) {

            int x = getXForTime(state.getTimestamp(), data.getCurrentStartTime(), data.getCurrentEndTime());

            g2d.setColor(Palette.black);

            Label label = labelConfig.getMatchingLabel(state);

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
