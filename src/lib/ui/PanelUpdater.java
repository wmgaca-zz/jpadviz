package lib.ui;

public class PanelUpdater implements Runnable {
    BasicPanel panel;

    // In miliseconds
    int refreshRate = 100;

    PanelUpdater(BasicPanel panel, int refreshRate) {
        this(panel);

        this.refreshRate = refreshRate;
    }

    PanelUpdater(BasicPanel panel) {
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

    public static void handle(BasicPanel panel) {
        Thread thread = new Thread(new PanelUpdater(panel));
        thread.start();
    }
}
