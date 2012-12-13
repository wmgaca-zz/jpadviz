package lib.ui;

import lib.types.PAD;
import lib.types.Palette;
import lib.types.SinglePADValue;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class SinglePADPanel extends BasicSinglePanel {

    public SinglePADPanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        int len = values.size();

        if (0 == len) {
            return;
        }

        g2d.setColor(Palette.black);

        // Draw X line
        g2d.drawLine(margin.left, getCenterY(), getWidth() - margin.right, getCenterY());

        // Draw Y line
        g2d.drawLine(margin.left, getHeight() - margin.bottom, margin.left, margin.top);

        SinglePADValue value = values.get(len - 1);

        // Draw the value
        g2d.drawString(
                String.format("%s: %.2f (%.2f)", label, value.value, value.certainty),
                this.getWidth() - 90, 20);

        int currentX;
        int currentY;
        int lastX = margin.left;
        int lastY = getCenterY();

        SinglePADValue lastValue = new SinglePADValue(0f, 0f, System.currentTimeMillis());

        for (int i = 0; i < values.size(); ++i) {
            SinglePADValue currentValue = values.get(i);

            currentX = margin.left + ((i + 1) * this.getW()) / len;
            currentY = getCenterY() + (int)(this.getH() * currentValue.value / 2) * -1;

            g2d.setColor(Palette.transparent);
            Polygon cert = new Polygon();
            cert.addPoint(lastX, lastY);
            cert.addPoint(currentX, currentY);
            cert.addPoint(currentX, margin.top + getH());
            cert.addPoint(lastX, margin.top + getH());
            cert.addPoint(lastX, lastY);
            g2d.drawPolygon(cert);

            Color currentColor = (currentValue.value > 0) ? Palette.green : Palette.red;
            Color lastColor = (lastValue.value > 0) ? Palette.green : Palette.red;

            g2d.setColor(Palette.getTransparent(Palette.green, currentValue.certainty));
            GradientPaint gp = new GradientPaint(
                    lastX, 0, Palette.getTransparent(lastColor, lastValue.certainty),
                    currentX, 0, Palette.getTransparent(currentColor, currentValue.certainty));
            g2d.setPaint(gp);
            g2d.fill(cert);

            g2d.setColor(Palette.blue);
            g2d.drawLine(lastX, lastY, currentX, currentY);

            lastX = currentX;
            lastY = currentY;
            lastValue = currentValue;
        }

        for (int i = 0; i < this.values.size(); ++i) {
            SinglePADValue currentValue = this.values.get(i);

            currentX = margin.left + ((i + 1) * getW()) / len;
            currentY = getCenterY() + (int)(getH() * currentValue.value / 2) * -1;

            g2d.setColor(Palette.blue);
            Ellipse2D circle = new Ellipse2D.Double(currentX - 3, currentY - 4, 6f, 6f);
            g2d.fill(circle);
            g2d.draw(circle);

            g2d.setColor(Palette.black);
            g2d.drawString(
                    String.format("%.1f", currentValue.value),
                    currentX - 5, currentY - 10);

            g2d.drawString(
                    String.format("%.1f", currentValue.certainty),
                    currentX,
                    currentY + (this.getHeight() - currentY) / 2
            );
        }
    }

}
