package lib.ui;

import lib.ui.panels.base.Panel;

public class PanelUpdater implements Runnable {
    Panel panel;

    // In miliseconds
    int refreshRate = 10;

    PanelUpdater(Panel panel, int refreshRate) {
        this(panel);

        this.refreshRate = refreshRate;
    }

    PanelUpdater(Panel panel) {
        this.panel = panel;
    }

    public void run() {
        while (true) {
            //System.out.println("Tick for repaint: " + panel.toString());
            panel.repaint();
            try {
                Thread.sleep(refreshRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void handle(Panel panel) {
        Thread thread = new Thread(new PanelUpdater(panel));
        thread.start();
    }
}
