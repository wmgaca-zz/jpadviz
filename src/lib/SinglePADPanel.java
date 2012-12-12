package lib;

import lib.types.Palette;
import lib.types.SinglePADValue;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.ArrayList;

public class SinglePADPanel extends JPanel {

    private List<SinglePADValue> values = new ArrayList<SinglePADValue>();
    private String label;

    SinglePADPanel() {
        this.label = "Unknown";
        this.setPreferredSize(new Dimension(200, 200));
    }

    SinglePADPanel(String label, int width, int height) {
        this();
        this.label = label;
        this.setPreferredSize(new Dimension(width, height));
    }

    public void feed(float value, float certainty) {
        this.values.add(new SinglePADValue(value, certainty));

        if (this.values.size() > 15) {
            this.values.remove(0);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int left = 10;
        int right = 10;
        int top = 10;
        int bottom = 10;

        int width = this.getWidth() - left - right;
        int height = this.getHeight() - top - bottom;

        int centerY = top + height / 2;

        g.setColor(Palette.black);

        // Draw X line
        g.drawLine(left, centerY, this.getWidth() - right, centerY);

        // Draw Y line
        g.drawLine(left, this.getHeight() - bottom, left, top);

        int points = this.values.size();
        SinglePADValue value = this.values.get(points - 1);

        // Draw the value
        g.drawString(
            String.format("%s: %.2f (%.2f)", this.label, value.value, value.certainty),
            this.getWidth() - 90, 20);

        int currentX;
        int currentY;
        int lastX = left;
        int lastY = centerY;

        SinglePADValue lastValue = new SinglePADValue(0f, 0f);

        for (int i = 0; i < this.values.size(); ++i) {
            SinglePADValue currentValue = this.values.get(i);

            currentX = left + ((i + 1) * width) / points;
            currentY = centerY + (int)(height * currentValue.value / 2) * -1;

            g.setColor(Palette.transparent);
            Polygon cert = new Polygon();
            cert.addPoint(lastX, lastY);
            cert.addPoint(currentX, currentY);
            cert.addPoint(currentX, top + height);
            cert.addPoint(lastX, top+ height);
            cert.addPoint(lastX, lastY);
            g.drawPolygon(cert);

            Color currentColor = (currentValue.value > 0) ? Palette.green : Palette.red;
            Color lastColor = (lastValue.value > 0) ? Palette.green : Palette.red;

            g.setColor(Palette.getTransparent(Palette.green, currentValue.certainty));
            GradientPaint gp = new GradientPaint(
                    lastX, 0, Palette.getTransparent(lastColor, lastValue.certainty),
                    currentX, 0, Palette.getTransparent(currentColor, currentValue.certainty));
            g2d.setPaint(gp);
            g2d.fill(cert);

            g.setColor(Palette.blue);
            g.drawLine(lastX, lastY, currentX, currentY);

            lastX = currentX;
            lastY = currentY;
            lastValue = currentValue;
        }

        for (int i = 0; i < this.values.size(); ++i) {
            SinglePADValue currentValue = this.values.get(i);

            currentX = left + ((i + 1) * width) / points;
            currentY = centerY + (int)(height * currentValue.value / 2) * -1;

            g.setColor(Palette.blue);
            Ellipse2D circle = new Ellipse2D.Double(currentX - 3, currentY - 4, 6f, 6f);
            g2d.fill(circle);
            g2d.draw(circle);

            //g.drawOval(currentX, currentY, 4, 4);


            g.setColor(Palette.black);
            g.drawString(
                    String.format("%.1f", currentValue.value),
                    currentX - 5, currentY - 10);

            g.drawString(
                    String.format("%.1f", currentValue.certainty),
                    currentX,
                    currentY + (this.getHeight() - currentY) / 2
            );
        }
    }
}
