package lib;

import javax.swing.*;
import javax.swing.border.*;

public class JavaPaintUI extends JFrame {

    //private SinglePADPanel panel;
    private SingleRadarPanel panel;

    public void feed(float value, float certainty) {
        this.panel.feed(value, certainty);
        //this.panel.repaint();
        //this.repaint();
        System.out.println("Repainted...");
    }

    public JavaPaintUI() {
        this.initComponents();
    }

    private void initComponents() {
        // we want a custom Panel2, not a generic JPanel!
        //this.panel = new SinglePADPanel("P", 300, 200);
        this.panel = new SingleRadarPanel("P", 400, 400);
        this.panel.setBackground(new java.awt.Color(255, 255, 255));
        this.panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // add the component to the frame to see it!
        this.setContentPane(this.panel);
        // be nice to testers..
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }


}