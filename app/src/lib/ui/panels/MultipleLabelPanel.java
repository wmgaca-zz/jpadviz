package lib.ui.panels;

import lib.types.Label;
import lib.types.*;
import lib.ui.panels.base.Panel;
import lib.utils.Utils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import static lib.utils.Logging.log;

public class MultipleLabelPanel extends Panel {

    protected static MultipleLabelPanel instance = null;

    public static MultipleLabelPanel getInstance(int width, int height, LabelConfig labelConfig) {
        if (null == instance) {
            instance = new MultipleLabelPanel(width, height, labelConfig);
        }

        return instance;
    }

    protected LabelConfig labelConfig;

    public MultipleLabelPanel(int width, int height, LabelConfig labelConfig) {
        super(PAD.Type.PAD, width, height);

        this.labelConfig = labelConfig;
        //this.margin.right = 50;
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
        ArrayList<PADDataHandler> handlers = PADDataHandlerContainer.getInstance().getList();

        // Draw bottom Y line
        g2d.setColor(Palette.black);
        draw(line(margin.left, margin.top, getW(), margin.top), Palette.black);

        int y = 40;

        for (PADDataHandler handler : handlers) {

            for (PADState state : handler.getCurrentValues()) {
                int x = getXForTime(state.getTimestamp(), handler.getCurrentStartTime(), handler.getCurrentEndTime());

                g2d.setColor(Palette.black);

                Label label = labelConfig.getMatchingLabel(state);

                g2d.setFont(new Font("Arial", Font.PLAIN, (int)(state.getP().getCertainty() * 20f) + 5));

                int labelWidth = (int) g2d.getFontMetrics().getStringBounds(label.getName(), g2d).getWidth();
                int labelHeight = (int) g2d.getFontMetrics().getStringBounds(label.getName(), g2d).getHeight();

                int border = 2;

                g2d.setColor(handler.getColor());
                Rectangle2D rect = new Rectangle.Double(x, y - (labelHeight + 2 * border) / 2, labelWidth + 2 * border, labelHeight + 2 * border);
                g2d.fill(rect);
                g2d.setColor(Palette.white); //Utils.getNegative(handler.getColor()));
                g2d.draw(rect);
                g2d.drawString(label.getName(), x + border, y + labelHeight/2);


            }

            y += 40;


        }


    }
}
