package edu.lehigh.nees.xml;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/********************************* 
 * ReadDAQXMLConfig
 * <p>
 * Reads and parsed the DAQ XML Configuration file
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 
 ********************************/
public class ReadDAQXMLConfig{
    DocumentBuilderFactory docBuilderFactory;
    DocumentBuilder docBuilder;
    Document doc;
    
    /** Constructor.  Reads an XML configuration file
     * and prepares it to be parsed by the available functions
     */
    public ReadDAQXMLConfig(File filename) {
        try {

            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse (filename);

            // normalize text representation
            doc.getDocumentElement().normalize ();            
            
            // normalize text representation
            doc.getDocumentElement().normalize ();
        } catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        } catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        } catch (Throwable t) {
        t.printStackTrace ();
        }
    }
    
    /** Retrieve the DAQ SCRAMNet order and data */
    public XMLDAQConfig getDAQConfig() {   
        XMLDAQConfig XMLconfig = new XMLDAQConfig();
        
        // Get DAQ Configuration
        try { 
            NodeList ChannelSet = doc.getElementsByTagName("channelSet");
            Element ChannelSetList = (Element)ChannelSet.item(0);                                         
            // Get the DAQ Configuration             
            NodeList daqblock = ChannelSetList.getElementsByTagName("channel");            
            XMLconfig.setnumDaqBlocks(daqblock.getLength());
            for (int i = 0; i < daqblock.getLength(); i++) {
                XMLconfig.setDaqOffset(i,((Element)daqblock.item(i)).getAttribute("location").trim());
                XMLconfig.setDaqGain(i,Double.parseDouble(((Element)daqblock.item(i)).getAttribute("Gain").trim()));
                XMLconfig.setDaqVoffset(i,Double.parseDouble(((Element)daqblock.item(i)).getAttribute("VoltageOffset").trim()));
                XMLconfig.setDaqVslope(i,Double.parseDouble(((Element)daqblock.item(i)).getAttribute("VoltageSlope").trim()));
                XMLconfig.setDaqEUoffset(i,Double.parseDouble(((Element)daqblock.item(i)).getAttribute("EUOffset").trim()));
                XMLconfig.setDaqEUslope(i,Double.parseDouble(((Element)daqblock.item(i)).getAttribute("EUSlope").trim()));
                XMLconfig.setDaqName(i,((Element)daqblock.item(i)).getAttribute("name").trim());
            }            
           
        } catch (NullPointerException e) {e.printStackTrace();}            
        return XMLconfig;
    }
                
    
    /** Print the XML configuration */
    public void print() {        
        getDAQConfig().print();
    }
    
    public static void main (String argv []){
        ReadDAQXMLConfig xml = new ReadDAQXMLConfig(new File("c:/Documents and Settings/Tommy/Desktop/daq.xml"));
        
        XMLDAQConfig config1 = xml.getDAQConfig();
        config1.print();
    }
}

