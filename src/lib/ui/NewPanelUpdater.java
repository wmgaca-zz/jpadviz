package lib.ui;

import lib.ui.panels.base.BasePanel;
import lib.ui.panels.base.Panel;

import java.util.ArrayList;

public class NewPanelUpdater implements Runnable {

    /**
     * Updater's thread reference.
     */
    protected static Thread updater = null;

    /**
     * List of panels handled by updater.
     */
    final protected static ArrayList<BasePanel> panels = new ArrayList<BasePanel>();

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
                for (BasePanel panel : panels) {
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
    public static void handle(BasePanel panel) {
        synchronized (panels) {
            if (!panels.contains(panel)) {
                panels.add(panel);
            }
        }

        if (null == updater) {
            updater = new Thread(new NewPanelUpdater());
            updater.start();
        }
    }
}