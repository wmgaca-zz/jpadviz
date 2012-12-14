package lib.ui;

import lib.types.LabelConfig;
import lib.types.PAD;
public class DynamicFrame extends BasicFrame {

    public DynamicFrame(LabelConfig labelConfig) {
        super(labelConfig);
    }

    protected void initComponents() {
        addToContainer(new SinglePADPanel(PAD.Type.P, 400, 400));
        addToContainer(new LabelPanel(400, 200, labelConfig));
        addToContainer(new SingleRadarPanel(PAD.Type.P, 400, 400));
    }


}