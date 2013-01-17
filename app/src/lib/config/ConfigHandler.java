package lib.config;

import lib.utils.Utils;
import lib.exceptions.PADConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Contains basic functionalities for handling XML documents.
 */
public class ConfigHandler {

    /**
     * Document handle.
     */
    public Document doc = null;

    /**
     * Node values cache.
     */
    private Dictionary<String, String> nodeValues = new Hashtable<String, String>();

    /**
     * Default constructor.
     *
     * @param configFile File object representing XML config to be used.
     */
    public ConfigHandler(File configFile) {
        this.doc = ConfigHandler.parseDocument(configFile);

        if (this.doc == null) {
            Utils.exitOnException(new PADConfigException(), String.format("Config file not found: ", configFile.getPath()));
        }
    }

    /**
     * Get node value by node name.
     *
     * @param tagName Node name.
     * @return String containing value of the first node matching given name. If none: null is returned.
     */
    public String get(String tagName) {
        return this.getNodeValue(tagName);
    }

    /**
     * Get node value by node name parsed to an integer.
     *
     * @see #get(String)
     *
     * @param tagName Node name.
     * @return Integer containing value of the first node matching given name. If none: null is returned.
     */
    public int getInt(String tagName) {
        return Integer.parseInt(this.get(tagName));
    }

    /**
     * Get raw node value by node name.
     *
     * @param tagName Node name.
     * @return String containing node's first occurance's value. If none: null is returned.
     */
    private String getNodeValue(String tagName) {
        if (null == this.nodeValues.get(tagName)) {
            NodeList portNodes = null;

            try {
                portNodes = this.doc.getElementsByTagName(tagName).item(0).getChildNodes();
                try {
                    this.nodeValues.put(tagName, portNodes.item(0).getNodeValue());
                } catch (NullPointerException error) {
                    this.nodeValues.put(tagName, "");
                }
            } catch (NullPointerException error) {
                error.printStackTrace();
                this.nodeValues.put(tagName, null);
            }
        }

        return this.nodeValues.get(tagName);
    }

    /**
     * Crate a Document instance from a File object representing an XML object.
     *
     * @param configFile File object representing the XML config.
     * @return Document instace. If can't create: null is returned.
     */
    public static Document parseDocument(File configFile) {
        Document doc = null;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException error) {
            return null;
        }

        try {
            doc = docBuilder.parse(configFile.getPath());
        } catch (SAXException error) {
            return null;
        } catch (IOException error) {
            return null;
        }

        try {
            doc.getDocumentElement().normalize();
        } catch (NullPointerException error) {
            return null;
        }

        return doc;
    }

}
