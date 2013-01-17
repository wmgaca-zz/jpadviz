package lib.forms;
import javax.swing.*;
import java.awt.*;

/**
 * Default application form
 */
public class MainWindowForm {

    /**
     * Form main panel.
     */
    private JPanel mainPanel;

    /**
     * Bit flag, if true: the application should end.
     */
    private boolean exitApp = false;

    /**
     * Default constructor, initializes the form with setting panel's size.
     */
    public MainWindowForm() {
        mainPanel.setPreferredSize(new Dimension(800, 600));
    }

    /**
     * @return Main panel getter.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * @return true the "application end" flag is on, false otherwise.
     */
    public boolean getExitApp() {
        return exitApp;
    }

}
