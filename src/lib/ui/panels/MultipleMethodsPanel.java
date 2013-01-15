package lib.ui.panels;

import lib.types.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static lib.utils.Logging.log;

public class MultipleMethodsPanel extends lib.ui.panels.base.Panel {

    public MultipleMethodsPanel(int width, int height) {
        super(PAD.Type.PAD, width, height);
    }

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
