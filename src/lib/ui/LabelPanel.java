package lib.ui;

import lib.types.*;
import lib.types.Label;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class LabelPanel extends BasicMultiplePanel {

    protected LabelConfig labelConfig;

    LabelPanel(int width, int height, LabelConfig labelConfig) {
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

        long firstTime = this.values.get(0).getTimestamp();
        long lastTime = this.values.get(len - 1).getTimestamp();

        long timespan = this.values.get(len - 1).getTimestamp();
        if (len > 1) {
            timespan -= this.values.get(0).getTimestamp();
        }

        // Draw X & Y lines
        g2d.setColor(Palette.black);
        g2d.drawLine(margin.left, margin.top + getH(), this.getWidth() - margin.right, margin.top + getH());
        g2d.drawLine(margin.left, this.getHeight() - margin.bottom, margin.left, margin.top);

        PADState last = this.values.get(len - 1);
        // Draw the value
        g2d.drawString(
                String.format("%.2f, %.2f, %.2f", last.getP(), last.getA(), last.getD()),
                this.getWidth() - 110, 20);

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

    }
}
