package lib.config;

import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;

/**
 * PadVisualiser config handler.
 */
public class VisualiserConfig extends PADConfig {

    /**
     * Default constructor.
     *
     * @param configFile Visualiser's XML config file.
     */
    public VisualiserConfig(File configFile) {
        super(configFile);
    }

    /**
     * @return Visualiser's mode.
     */
    public String getMode() {
        return get("mode");
    }

    /**
     * @return Visualiser's layout.
     */
    public String getLayout() {
        return get("layout");
    }

    /**
     * @see #getMode()
     *
     * @return true if the real time mode should be ran, false otherwise.
     */
    public boolean isRealTime() {
        return getMode().equals("auto");
    }

    /**
     * @see #isMultiChannel()
     *
     * @return true if the single-channel mode should be ran, false otherwise.
     */
    public boolean isOneChannel() {
        return 1 <= getChannels().size();
    }

    /**
     * @see #isOneChannel()
     *
     * @return false if the single-channel mode should be ran, true otherwise.
     */
    public boolean isMultiChannel() {
        return getChannels().size() > 1;
    }

    /**
     * @return Returns the id of experiment to be load.
     */
    public int getExperimentId() {
        Node session = configHandler.doc.getElementsByTagName("session").item(0);
        return Integer.parseInt(session.getAttributes().getNamedItem("id").getNodeValue());
    }

    /**
     * @return Return array of methods given in the config file.
     */
    public ArrayList<Integer> getChannels() {
        Node session = configHandler.doc.getElementsByTagName("session").item(0);
        String channels = session.getAttributes().getNamedItem("channels").getNodeValue();

        ArrayList<Integer> channelsList = new ArrayList<Integer>();

        for (String channel : channels.split(",")) {
            try {
                channelsList.add(Integer.parseInt(channel.trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return channelsList;
    }
}