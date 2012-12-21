package lib.config;

import lib.Utils;
import lib.types.exceptions.PADConfigException;
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

public class ConfigHandler {

    public Document doc = null;

    private Dictionary<String, String> nodeValues = new Hashtable<String, String>();

    public ConfigHandler(File configFile) {
        this.doc = ConfigHandler.parseDocument(configFile);

        if (this.doc == null) {
            Utils.exitOnException(new PADConfigException());
        }
    }

    public String get(String tagName) {
        return this.getNodeValue(tagName);
    }

    public int getInt(String tagName) {
        return Integer.parseInt(this.get(tagName));
    }

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
