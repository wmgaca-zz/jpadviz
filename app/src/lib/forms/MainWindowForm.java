package lib.forms;
import javax.swing.*;
import java.awt.*;

public class MainWindowForm {

    private JPanel mainPanel;
    private boolean exitApp = false;

    public MainWindowForm() {
        mainPanel.setPreferredSize(new Dimension(800, 600));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public boolean getExitApp() {
        return exitApp;
    }

}
