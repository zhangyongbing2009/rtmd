package edu.lehigh.nees.xml;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/********************************* 
 * ReadxPCXMLConfig
 * <p>
 * Reads and parses the xPC XML Configuration file
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 10 Mar 06  T. Marullo  Initial
 *  7 Aug 07  T. Marullo  Added LowerLimit and UpperLimit to Control blocks for xPC
 * 
 ********************************/
public class ReadxPCXMLConfig{
    DocumentBuilderFactory docBuilderFactory;
    DocumentBuilder docBuilder;
    Document doc;
    
    /** Constructor.  Reads an XML configuration file
     * and prepares it to be parsed by the available functions
     */
    public ReadxPCXMLConfig(File filename) {
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
    
    /** Retrieve the xPC SCRAMNet order and data */
    public XMLxPCConfig getxPCConfig() {   
        XMLxPCConfig XMLconfig = new XMLxPCConfig();
        
        // Get xPC Configuration
        try { 
            NodeList ChannelSet = doc.getElementsByTagName("xPC");
            Element ChannelSetList = (Element)ChannelSet.item(0);                                         
            // Get the xPC Read Configuration             
            NodeList xpcreadblock = ChannelSetList.getElementsByTagName("xPCReadBlock");            
            XMLconfig.setnumxPCReadBlocks(xpcreadblock.getLength());
            for (int i = 0; i < xpcreadblock.getLength(); i++) {
                XMLconfig.setxPCReadLocation(i,((Element)xpcreadblock.item(i)).getAttribute("location").trim());
                XMLconfig.setxPCReadName(i,((Element)xpcreadblock.item(i)).getAttribute("name").trim());
                XMLconfig.setxPCReadUnits(i,((Element)xpcreadblock.item(i)).getAttribute("unit").trim());
                XMLconfig.setxPCReadGain(i,Double.parseDouble(((Element)xpcreadblock.item(i)).getAttribute("Gain").trim()));
                String isDAQ = ((Element)xpcreadblock.item(i)).getAttribute("isDAQ").trim();
                XMLconfig.setxPCReadisDAQ(i,isDAQ);     
                if (isDAQ.equals("true")) {
                	// DAQ Config	                
	                XMLconfig.setxPCReadVoffset(i,Double.parseDouble(((Element)xpcreadblock.item(i)).getAttribute("VoltageOffset").trim()));
	                XMLconfig.setxPCReadVslope(i,Double.parseDouble(((Element)xpcreadblock.item(i)).getAttribute("VoltageSlope").trim()));
	                XMLconfig.setxPCReadEUoffset(i,Double.parseDouble(((Element)xpcreadblock.item(i)).getAttribute("EUOffset").trim()));
	                XMLconfig.setxPCReadEUslope(i,Double.parseDouble(((Element)xpcreadblock.item(i)).getAttribute("EUSlope").trim()));
                }                                
            }            
            // Get the xPC Write Configuration             
            NodeList xpcwriteblock = ChannelSetList.getElementsByTagName("xPCWriteBlock");            
            XMLconfig.setnumxPCWriteBlocks(xpcwriteblock.getLength());
            for (int i = 0; i < xpcwriteblock.getLength(); i++) {
                XMLconfig.setxPCWriteLocation(i,((Element)xpcwriteblock.item(i)).getAttribute("location").trim());
                XMLconfig.setxPCWriteName(i,((Element)xpcwriteblock.item(i)).getAttribute("name").trim());
                XMLconfig.setxPCWriteUnits(i,((Element)xpcwriteblock.item(i)).getAttribute("unit").trim());
                XMLconfig.setxPCWriteGain(i,Double.parseDouble(((Element)xpcwriteblock.item(i)).getAttribute("Gain").trim()));
                String isCTRL = ((Element)xpcwriteblock.item(i)).getAttribute("isCTRL").trim();
                XMLconfig.setxPCWriteisCTRL(i,isCTRL);     
                if (isCTRL.equals("true")) {
                	XMLconfig.setxPCWriteLowerLimit(i,Double.parseDouble(((Element)xpcwriteblock.item(i)).getAttribute("lowerlimit").trim()));
                	XMLconfig.setxPCWriteUpperLimit(i,Double.parseDouble(((Element)xpcwriteblock.item(i)).getAttribute("upperlimit").trim()));
                }
            }      
        } catch (NullPointerException e) {e.printStackTrace();}            
        return XMLconfig;
    }
    
    /** Print the XML configuration */
    public void print() {        
        getxPCConfig().print();
    }
    
    public static void main (String argv []){
        ReadxPCXMLConfig xml = new ReadxPCXMLConfig(new File("C:/Documents and Settings/Tommy/My Documents/Eclipse Workspace/playpen/xPC.xml"));
                
    	XMLxPCConfig config1 = xml.getxPCConfig();
    	config1.print();
            
    }
}
