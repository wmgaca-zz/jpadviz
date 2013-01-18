package lib.ui.panels;

import lib.types.*;

import java.awt.*;
import java.util.Map;

/**
 * Widget shows used method and assigned colors.
 */
public class MultipleMethodsPanel extends lib.ui.panels.base.Panel {

    /**
     * @param width Width
     * @param height Height
     */
    public MultipleMethodsPanel(int width, int height) {
        super(PAD.Type.PAD, width, height);
    }

    /**
     * {@inheritDoc}
     *
     * @param g2d Graphics object to be drawn
     */
    @Override
    public void customPaintComponent(Graphics2D g2d) {
        Map<Integer, PADDataHandler> map = PADDataHandlerContainer.getInstance().getAll();

        g2d.setFont(new Font("Arial", Font.PLAIN, 18));

        int i = 0;
        for (Map.Entry<Integer, PADDataHandler> entry : map.entrySet()) {
            g2d.setColor(entry.getValue().getColor());
            String s = String.format("Method #%s", entry.getKey());

            g2d.drawString(s, margin.left, margin.top + (i * 20) + 10);

            i += 1;
        }


    }
}
