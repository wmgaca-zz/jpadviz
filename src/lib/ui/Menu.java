package lib.ui;

import com.sun.servicetag.SystemEnvironment;
import lib.Utils;
import lib.ui.frames.base.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class Menu {

    public enum MenuAction {
        StayOnTop
    }

    private static volatile JMenuBar instance = null;

    public static JMenuBar getInstance(final Frame frame) {
        if (instance == null) {
            synchronized (Menu.class) {
                if (instance == null) {
                    instance = Menu.getMenuBar(frame);
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
                frame.handleMenuAction(MenuAction.StayOnTop);
                // Revert menu item's selection
                ((JCheckBoxMenuItem) actionEvent.getSource()).setState(
                    (((JCheckBoxMenuItem) actionEvent.getSource()).getState())
                );
            }
        });

        view.add(stayOnTop);

        return view;
    }

    private static JMenuBar getMenuBar(final Frame frame) {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(getFileMenu(frame));
        menuBar.add(getViewMenu(frame));

        return menuBar;
    }

    private Menu() {

    }



}
