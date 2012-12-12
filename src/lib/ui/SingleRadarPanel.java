package lib.ui;

import lib.types.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

public class SingleRadarPanel extends JPanel {

    private float value = 0f;
    private float certainty = 0f;

    private String label;

    private int currentArcStart = 90;
    private int arcLen = 10;

    SingleRadarPanel() {
        this.label = "Unknown";
        this.setPreferredSize(new Dimension(400, 400));

        PanelUpdater.handle(this);
    }

    SingleRadarPanel(String label, int width, int height) {
        this();
        this.label = label;
        this.setPreferredSize(new Dimension(width, height));
    }

    public void feed(float value, float certainty) {
        this.value = value;
        this.certainty = certainty;
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

        int centerX = left + width / 2;
        int centerY = top + height / 2;

        int radius = (width < height) ? width / 2 : height /2;

        Ellipse2D circle = new Ellipse2D.Double(centerX - radius, centerY - radius, 2f * radius, 2f * radius);
        Color color = (this.value > 0) ? Palette.green : Palette.red;
        g2d.setColor(Palette.getTransparent(color, this.certainty));
        g2d.fill(circle);
        g2d.draw(circle);

        g.setColor(Palette.grey);
        g2d.setStroke(new BasicStroke(5f));

        int arcSpeed = (int) (-10 * this.value);

        this.currentArcStart = (this.currentArcStart + arcSpeed) % 360;

        Arc2D arc = new Arc2D.Double(centerX - radius, centerY - radius, width, height, this.currentArcStart, this.arcLen, Arc2D.OPEN);
        g2d.draw(arc);

        // Draw the value
        g2d.setColor(Palette.black);
        String s = String.format("%s: %.2f (%.2f)", this.label, this.value, this.certainty);
        int sLen = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        g.drawString(s, centerX - sLen / 2, centerY);
    }
}
