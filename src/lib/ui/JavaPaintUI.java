package lib.ui;

import lib.types.LabelConfig;
import lib.types.PAD;
import lib.types.PADState;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;

public class JavaPaintUI extends JFrame {

    private JPanel container;
    private ArrayList<BasicPanel> panels = new ArrayList<BasicPanel>();

    private LabelConfig labelConfig;

    public void feed(PADState state) {
        for (BasicPanel panel : panels ) {
            panel.feed(state);
            panel.repaint();
        }
        container.repaint();
    }

    public JavaPaintUI(LabelConfig labelConfig) {
        this.labelConfig = labelConfig;

        this.initComponents();
    }

    private void addToContaner(BasicPanel panel) {
        panels.add(panel);
        container.add(panel);
    }

    private void initComponents() {
        // we want a custom Panel2, not a generic JPanel!
        //this.panel = new SinglePADPanel("P", 300, 200);

        this.setSize(new Dimension(800, 600));

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        addToContaner(new SinglePADPanel(PAD.Type.P, 400, 400));
        addToContaner(new LabelPanel(400, 200, labelConfig));
        addToContaner(new SingleRadarPanel(PAD.Type.P, 400, 400));
        container.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        this.setContentPane(container);


        //this.singeRadarPanel =

        //this.labelPanel = ;

        //this.labelPanel.setBackground(new java.awt.Color(255, 255, 255));
        //this.singeRadarPanel.setBackground(new java.awt.Color(255, 255, 255));

        //this.labelPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        //this.singeRadarPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // add the component to the frame to see it!
        //this.setContentPane(this.labelPanel);
        //this.setContentPane(this.singeRadarPanel);
        // be nice to testers..
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }


}