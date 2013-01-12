package lib.ui.menus;

import lib.utils.Utils;
import lib.ui.frames.base.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class MainMenu {

    public enum Action {
        STAY_ON_TOP,
        LAYOUT_FULL,
        LAYOUT_MIN_LABEL,
        LAYOUT_MIN_RADAR
    }

    private static JMenuBar instance = null;

    public static JMenuBar getInstance(final Frame frame) {
        if (instance == null) {
            synchronized (MainMenu.class) {
                if (instance == null) {
                    instance = MainMenu.getMenuBar(frame);
                }
            }
        }

        return instance;
    }

    private static JMenu getFileMenu(final Frame frame) {
        JMenu file = new JMenu("File");

        file.setMnemonic(KeyEvent.VK_F);

        // Exit
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(Utils.ReturnCode.NORMAL);
            }
        });

        file.add(exitMenuItem);

        return file;
    }

    private static JMenu getViewMenu(final Frame frame) {
        JMenu view = new JMenu("View");

        view.setMnemonic(KeyEvent.VK_V);

        // Stay on top
        JCheckBoxMenuItem stayOnTop = new JCheckBoxMenuItem("Stay on top");
        stayOnTop.setState(false);
        stayOnTop.setMnemonic(KeyEvent.VK_T);
        stayOnTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.handleMenuAction(Action.STAY_ON_TOP);
                // Revert menu item's selection
                ((JCheckBoxMenuItem) actionEvent.getSource()).setState(
                    (((JCheckBoxMenuItem) actionEvent.getSource()).getState())
                );
            }
        });

        // Layout
        JMenu layout = new JMenu("Layout");
        layout.setMnemonic(KeyEvent.VK_L);

        final JCheckBoxMenuItem fullLayout = new JCheckBoxMenuItem("Full");
        final JCheckBoxMenuItem minLabelLayout = new JCheckBoxMenuItem("Label");
        final JCheckBoxMenuItem minRadarLayout = new JCheckBoxMenuItem("Radar");
        fullLayout.setState(true);
        fullLayout.setMnemonic(KeyEvent.VK_F);
        fullLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.handleMenuAction(Action.LAYOUT_FULL);
                // Revert menu item's selection
                ((JCheckBoxMenuItem) actionEvent.getSource()).setState(
                        (((JCheckBoxMenuItem) actionEvent.getSource()).getState())
                );
                fullLayout.setState(true);
                minLabelLayout.setState(false);
                minRadarLayout.setState(false);
            }
        });

        minLabelLayout.setState(false);
        minLabelLayout.setMnemonic(KeyEvent.VK_F);
        minLabelLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.handleMenuAction(Action.LAYOUT_MIN_LABEL);
                // Revert menu item's selection
                ((JCheckBoxMenuItem) actionEvent.getSource()).setState(
                        (((JCheckBoxMenuItem) actionEvent.getSource()).getState())
                );
                fullLayout.setState(false);
                minLabelLayout.setState(true);
                minRadarLayout.setState(false);
            }
        });

        minRadarLayout.setState(false);
        minRadarLayout.setMnemonic(KeyEvent.VK_F);
        minRadarLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.handleMenuAction(Action.LAYOUT_MIN_RADAR);
                // Revert menu item's selection
                ((JCheckBoxMenuItem) actionEvent.getSource()).setState(
                        (((JCheckBoxMenuItem) actionEvent.getSource()).getState())
                );
                fullLayout.setState(false);
                minLabelLayout.setState(false);
                minRadarLayout.setState(true);
            }
        });

        layout.add(fullLayout);
        layout.add(minLabelLayout);
        layout.add(minRadarLayout);

        view.add(stayOnTop);
        view.add(layout);

        return view;
    }

    private static JMenuBar getMenuBar(final Frame frame) {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(getFileMenu(frame));
        menuBar.add(getViewMenu(frame));

        return menuBar;
    }

    private MainMenu() {
    }



}
