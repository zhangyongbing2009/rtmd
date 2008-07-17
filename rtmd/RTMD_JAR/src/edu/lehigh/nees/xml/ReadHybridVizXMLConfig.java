package edu.lehigh.nees.xml;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/********************************* 
 * ReadHybridVizXMLConfig
 * <p>
 * Reads and parses the HybridViz XML Configuration
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 28 Jul 06  T. Marullo  Initial
 * 
 ********************************/
public class ReadHybridVizXMLConfig{
    DocumentBuilderFactory docBuilderFactory;
    DocumentBuilder docBuilder;
    Document doc;
    
    /** Constructor.  Reads an XML configuration file
     * and prepares it to be parsed by the available functions
     */
    public ReadHybridVizXMLConfig(File filename) {
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
    
    /** Retrieve the HybridViz node list */
    public XMLHybridVizConfig getHybridVizConfig() {   
    	XMLHybridVizConfig XMLconfig = new XMLHybridVizConfig();
        
        // Get HybridViz Configuration
        try { 
            NodeList HybridViz = doc.getElementsByTagName("HybridViz");
            Element HybridVizList = (Element)HybridViz.item(0);                                                      
            NodeList TestSetup = HybridVizList.getElementsByTagName("TestSetup");
            Element TestSetupList = (Element)TestSetup.item(0);
            NodeList Structure = TestSetupList.getElementsByTagName("Structure");
            Element StructureList = (Element)Structure.item(0);
            NodeList Node = StructureList.getElementsByTagName("Node");            
            XMLconfig.setnumNodes(Node.getLength());
            for (int i = 0; i < Node.getLength(); i++) {
                XMLconfig.setID(i,((Element)Node.item(i)).getAttribute("id").trim());                
                XMLconfig.setConstraintID(i,((Element)Node.item(i)).getAttribute("cid").trim());
                XMLconfig.setDOFDX(i,Boolean.parseBoolean(((Element)Node.item(i)).getAttribute("dofX").trim()));
                XMLconfig.setDOFDY(i,Boolean.parseBoolean(((Element)Node.item(i)).getAttribute("dofY").trim()));
                XMLconfig.setDOFDZ(i,Boolean.parseBoolean(((Element)Node.item(i)).getAttribute("dofZ").trim()));
                XMLconfig.setDOFTX(i,Boolean.parseBoolean(((Element)Node.item(i)).getAttribute("dofTX").trim()));
                XMLconfig.setDOFTY(i,Boolean.parseBoolean(((Element)Node.item(i)).getAttribute("dofTY").trim()));
                XMLconfig.setDOFTZ(i,Boolean.parseBoolean(((Element)Node.item(i)).getAttribute("dofTZ").trim()));
            }  
            NodeList Grid = TestSetupList.getElementsByTagName("Grid");            
            XMLconfig.setDUnits(((Element)Grid.item(0)).getAttribute("dUnits").trim());
            XMLconfig.setTUnits(((Element)Grid.item(0)).getAttribute("tUnits").trim());                       
        } catch (NullPointerException e) {e.printStackTrace();}            
        return XMLconfig;
    }
                
    
    /** Print the XML configuration */
    public void print() {        
    	getHybridVizConfig().print();
    }
    
    public static void main (String argv []){
        ReadHybridVizXMLConfig xml = new ReadHybridVizXMLConfig(new File("c:/Documents and Settings/Owner/Desktop/viz.xml"));
        
        XMLHybridVizConfig config1 = xml.getHybridVizConfig();
        config1.print();
    }
}

