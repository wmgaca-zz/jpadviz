package lib.ui;

import javax.swing.*;

public class PanelUpdater implements Runnable {
    JPanel panel;

    PanelUpdater(JPanel panel) {
        this.panel = panel;
    }

    public void run() {
        while (true) {
            this.panel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println("Tick...");
        }
    }

    public static void handle(JPanel panel) {
        Thread thread = new Thread(new PanelUpdater(panel));
        thread.start();
    }
}
