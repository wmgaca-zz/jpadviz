package lib.config;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static lib.utils.Logging.log;

public class VisualiserConfig extends PadConfig {

    public VisualiserConfig(File configFile) {
        super(configFile);
    }

    public String getMode() {
        return get("mode");
    }

    public String getLayout() {
        return get("layout");
    }

    public boolean isRealTime() {
        return getMode().equals("auto");
    }

    public boolean isOneChannel() {
        return 1 == getChannels().size();
    }

    public boolean isMultiChannel() {
        return getChannels().size() > 1;
    }

    public int getExperimentId() {
        Node session = configHandler.doc.getElementsByTagName("session").item(0);
        return Integer.parseInt(session.getAttributes().getNamedItem("id").getNodeValue());
    }

    public ArrayList<Integer> getChannels() {
        Node session = configHandler.doc.getElementsByTagName("session").item(0);
        String channels = session.getAttributes().getNamedItem("channels").getNodeValue();

        ArrayList<Integer> channelsList = new ArrayList<Integer>();

        for (String channel : channels.split(",")) {
            try {
                channelsList.add(Integer.parseInt(channel.trim()));
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return channelsList;
    }
}