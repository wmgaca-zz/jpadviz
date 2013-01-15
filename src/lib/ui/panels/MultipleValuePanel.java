package lib.ui.panels;

import lib.types.*;

import java.awt.*;
import java.util.ArrayList;
import static lib.utils.Logging.log;

public class MultipleValuePanel extends lib.ui.panels.base.Panel {

    public MultipleValuePanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        ArrayList<PADDataHandler> list = PADDataHandlerContainer.getInstance().getList();

        g2d.setFont(new Font("Arial", Font.PLAIN, 18));

        for (int i = 0; i < list.size(); ++i) {
            PADDataHandler handler = list.get(i);
            PADValue current = handler.getLastValue(type);

            if (null == current) {
                continue;
            }

            g2d.setColor(handler.getColor());

            String s = String.format("%,2f / %.2f", current.getValue(), current.getCertainty());
            g2d.drawString(s, margin.left, margin.top + (i * 20) + 10);
        }
    }
}
