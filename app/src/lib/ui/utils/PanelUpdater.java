package lib.ui.utils;

import lib.ui.panels.base.Panel;

import java.util.ArrayList;

public class PanelUpdater implements Runnable {

    /**
     * Updater's thread reference.
     */
    protected static Thread updater = null;

    /**
     * List of panels handled by updater.
     */
    final protected static ArrayList<Panel> panels = new ArrayList<Panel>();

    /**
     * Panels refresh rate in milliseconds.
     */
    protected static int fps = 100;

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (true) {
            synchronized (panels) {
                for (Panel panel : panels) {
                    panel.update();
                }
            }

            try {
                Thread.sleep(1000 / fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle a panel by the updater.
     *
     * @param panel
     */
    public static void handle(Panel panel) {
        synchronized (panels) {
            if (!panels.contains(panel)) {
                panels.add(panel);
            }
        }

        if (null == updater) {
            updater = new Thread(new PanelUpdater());
            updater.start();
        }
    }
}