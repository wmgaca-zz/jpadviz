package lib.ui.panels;

import lib.utils.Utils;
import lib.types.Label;
import lib.types.LabelConfig;
import lib.types.PADState;
import lib.types.Palette;
import lib.ui.Margin;
import lib.ui.PanelUpdater;
import lib.ui.panels.base.MultiplePanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class LabelPanel extends MultiplePanel {

    protected static LabelPanel instance = null;

    public static LabelPanel getInstance(int width, int height, LabelConfig labelConfig) {
        return getInstance(width, height, labelConfig, true);
    }

    public static LabelPanel getInstance(int width, int height, LabelConfig labelConfig, boolean isRealTime) {
        if (null == instance) {
            instance = new LabelPanel(width, height, labelConfig, isRealTime);
        }

        return instance;
    }

    protected LabelConfig labelConfig;

    protected int buffer = 15;

    public LabelPanel(int width, int height, LabelConfig labelConfig) {
        this(width, height, labelConfig, true);
    }

    public LabelPanel(int width, int height, LabelConfig labelConfig, boolean isRealTime) {
        super(width, height, isRealTime);

        this.margin = new Margin(10, 10, 10, 50);

        this.labelConfig = labelConfig;
        PanelUpdater.handle(this);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        if (0 == values.size()) {
            return;
        }

        // Draw bottom Y line
        g2d.setColor(Palette.black);
        g2d.drawLine(margin.left, margin.top + getH(), getWidth() - margin.right, margin.top + getH());

        for (PADState state : getValuesForCurrentBuffer()) {
            int x = getXForTime(state.getTimestamp(), getCurrentStartTime(), getCurrentEndTime());

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
