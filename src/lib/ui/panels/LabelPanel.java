package lib.ui.panels;

import lib.Utils;
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

    protected LabelConfig labelConfig;

    protected int buffer = 15;

    public LabelPanel(int width, int height, LabelConfig labelConfig) {
        super(width, height);

        this.margin = new Margin(10, 10, 10, 50);

        this.labelConfig = labelConfig;
        PanelUpdater.handle(this);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        int len = this.values.size();

        if (0 == len) {
            return;
        }

        long endTime = System.currentTimeMillis();
        long startTime = endTime - buffer * 1000;

        // Draw bottom Y line
        g2d.setColor(Palette.black);
        g2d.drawLine(margin.left, margin.top + getH(), getWidth() - margin.right, margin.top + getH());

        PADState last = this.values.get(len - 1);
        // Draw the value
        g2d.drawString(
                String.format("%.2f, %.2f, %.2f", last.getP(), last.getA(), last.getD()),
                this.getWidth() - 110, 20);

        for (PADState state : getValuesForCurrentBuffer()) {
            int x = getXForTime(state.getTimestamp(), startTime, endTime);

            g2d.setColor(Palette.black);
            //g2d.drawLine(x, margin.top + getH(), x, margin.top + getH() / 2);

            Label label = this.labelConfig.getMatchingLabel(state);

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

        /*
        for (PADState currentState : this.values) {
            int currentX;

            if (len == 1) {
                currentX = getCenterX();
            } else {
                currentX = margin.left + (int) (((double) (currentState.getTimestamp() - firstTime) / timespan) * (double) getW());
            }
            g2d.setColor(Palette.black);
            g2d.drawLine(currentX, margin.top + getH(), currentX, margin.top + getH() / 2);

            Label label = this.labelConfig.getMatchingLabel(currentState);

            int sWidth = (int) g2d.getFontMetrics().getStringBounds(label.getName(), g2d).getWidth();
            int sHight = (int) g2d.getFontMetrics().getStringBounds(label.getName(), g2d).getHeight();

            int border = 2;

            g2d.setColor(Palette.blue);
            Rectangle2D rect = new Rectangle.Double(currentX, getCenterY() - sHight, sWidth + 2 * border, sHight + 2 * border);
            g2d.fill(rect);
            g2d.draw(rect);
            g2d.setColor(Palette.white);
            g2d.drawString(label.getName(), currentX + border, getCenterY() + border);
        }

        //*/
    }
}
