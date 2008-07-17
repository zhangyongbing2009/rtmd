package edu.lehigh.nees.xml;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import Jama.*;

/********************************* 
 * ReadXMLConfig
 * <p>
 * Reads and parses the XML Configuration file
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 12 May 06  T. Marullo  Removed SCRAMNet CTRL Limits (not needed)
 * 06 Jul 06  T. Marullo  Added SCRNodeconfig to SCRAMNet configuration
 * 28 Jul 06  T. Marullo  Changed Node Name to Node ID and added Node Units
 * 
 ********************************/
public class ReadXMLConfig{
    DocumentBuilderFactory docBuilderFactory;
    DocumentBuilder docBuilder;
    Document doc;
    
    /** Constructor.  Reads an XML configuration file
     * and prepares it to be parsed by the available functions
     */
    public ReadXMLConfig(File filename) {
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
    
    /** Retrieve the Scramnet configuration into an XMLScramnetConfig object <br>
     *  Data arranged by input order.  Can be accessed by offset
     */
    public XMLScramnetConfig getScramnetConfig() {   
        XMLScramnetConfig XMLconfig = new XMLScramnetConfig();
        
        // Get Scramnet Configuration
        try { 
        	NodeList NEESsim = doc.getElementsByTagName("NEESsim");
            Element NEESsimList = (Element)NEESsim.item(0);
            NodeList scramnet = NEESsimList.getElementsByTagName("Scramnet");
            Element scramnetList = (Element)scramnet.item(0);     
            XMLconfig.setIsOn(scramnetList.getAttribute("ON").equals("true")); 
            // Only parse if the XML is set ON="true"
            if (XMLconfig.getIsOn()) {
                // Get the Controller Configuration             
                NodeList ctrlblock = scramnetList.getElementsByTagName("CtrlBlock");            
                XMLconfig.setnumCtrlBlocks(ctrlblock.getLength());
                for (int i = 0; i < ctrlblock.getLength(); i++) {
                    XMLconfig.setCtrlOffset(i,((Element)ctrlblock.item(i)).getAttribute("Offset").trim());
                    XMLconfig.setCtrlStream(i,((Element)ctrlblock.item(i)).getAttribute("Stream").trim());
                    XMLconfig.setCtrlDescription(i,((Element)ctrlblock.item(i)).getAttribute("Description").trim());
                    XMLconfig.setCtrlUnits(i,((Element)ctrlblock.item(i)).getAttribute("Units").trim());
                    XMLconfig.setCtrlScale(i,Integer.parseInt(((Element)ctrlblock.item(i)).getAttribute("Scale")));                
                    XMLconfig.setLowerLimit(i,Double.parseDouble(((Element)ctrlblock.item(i)).getAttribute("LowerLimit")));
                    XMLconfig.setUpperLimit(i,Double.parseDouble(((Element)ctrlblock.item(i)).getAttribute("UpperLimit")));                    
                }
                // Get the Simulation Data Configuration             
                NodeList simblock = scramnetList.getElementsByTagName("SimBlock");            
                XMLconfig.setnumSimBlocks(simblock.getLength());
                for (int i = 0; i < simblock.getLength(); i++) {
                    XMLconfig.setSimOffset(i,((Element)simblock.item(i)).getAttribute("Offset").trim());
                    XMLconfig.setSimName(i,((Element)simblock.item(i)).getAttribute("Name").trim());
                }
                // Get the Node Data Configuration             
                NodeList nodeblock = scramnetList.getElementsByTagName("NodeBlock");            
                XMLconfig.setnumNodeBlocks(nodeblock.getLength());
                for (int i = 0; i < nodeblock.getLength(); i++) {                    
                    XMLconfig.setNodeID(i,((Element)nodeblock.item(i)).getAttribute("ID").trim());
                    XMLconfig.setNodeConstraintID(i,((Element)nodeblock.item(i)).getAttribute("ConstraintID").trim());
                    XMLconfig.setNodeDXOffset(i,((Element)nodeblock.item(i)).getAttribute("DXOffset").trim());
                    XMLconfig.setNodeDYOffset(i,((Element)nodeblock.item(i)).getAttribute("DYOffset").trim());
                    XMLconfig.setNodeDZOffset(i,((Element)nodeblock.item(i)).getAttribute("DZOffset").trim());
                    XMLconfig.setNodeTXOffset(i,((Element)nodeblock.item(i)).getAttribute("TXOffset").trim());
                    XMLconfig.setNodeTYOffset(i,((Element)nodeblock.item(i)).getAttribute("TYOffset").trim());
                    XMLconfig.setNodeTZOffset(i,((Element)nodeblock.item(i)).getAttribute("TZOffset").trim());
                    XMLconfig.setNodeDUnits(i,((Element)nodeblock.item(i)).getAttribute("DUnits").trim());
                    XMLconfig.setNodeTUnits(i,((Element)nodeblock.item(i)).getAttribute("TUnits").trim());
                }
            }
           
        } catch (NullPointerException e) {e.printStackTrace();}            
        return XMLconfig;
    }
    
    /** Retrieve the Test configuration into an XMLIntegratorConfig object      
     */
    public XMLIntegratorConfig getIntegratorConfig() {               
        XMLIntegratorConfig XMLconfig = new XMLIntegratorConfig();        
        
        try {
            // Start at TestMethod
        	NodeList NEESsim = doc.getElementsByTagName("NEESsim");
            Element NEESsimList = (Element)NEESsim.item(0);
            NodeList testMethod = NEESsimList.getElementsByTagName("Integrator");
            Element testMethodList = (Element)testMethod.item(0);            
            XMLconfig.setIsOn(testMethodList.getAttribute("ON").equals("true")); 
            // Only parse if the XML is set ON="true"
            if (XMLconfig.getIsOn()) {
                // Alpha Method
                try {
                    // Get the Test Type                              
                    NodeList testMethodType = testMethodList.getElementsByTagName("Alpha");                
                    // Alpha Method                
                    XMLconfig.settestType(testMethodType.item(0).getNodeName());
                    // Get Alpha Method Parameters                
                    Element Alpha = (Element)testMethodType.item(0);
                    // History
                    Element HistoryFile = (Element)(Alpha.getElementsByTagName("History")).item(0);
                    XMLconfig.sethistoryFile(HistoryFile.getAttribute("value"));
                    // DOF
                    Element DOF = (Element)(Alpha.getElementsByTagName("DOF")).item(0);
                    XMLconfig.setnumDOF(Integer.parseInt(DOF.getAttribute("value")));                           
                    int numDOF = XMLconfig.getnumDOF();
                    // Steps
                    Element Steps = (Element)(Alpha.getElementsByTagName("Steps")).item(0);
                    XMLconfig.setsteps(Integer.parseInt(Steps.getAttribute("value")));
                    // Iterations
                    Element Iterations = (Element)(Alpha.getElementsByTagName("Iterations")).item(0);
                    XMLconfig.setiterations(Integer.parseInt(Iterations.getAttribute("value")));
                    // Control
                    Element Control = (Element)(Alpha.getElementsByTagName("Control")).item(0);
                    XMLconfig.setControl(Control.getAttribute("value"));
                    // Units
                    Element Units = (Element)(Alpha.getElementsByTagName("Units")).item(0);
                    XMLconfig.setUnits(Units.getAttribute("value"));
                    // Delta_T
                    Element DeltaT = (Element)(Alpha.getElementsByTagName("DeltaT")).item(0);
                    XMLconfig.setdeltaT(Double.parseDouble(DeltaT.getAttribute("value")));
                    // Alpha Constant
                    Element AlphaConstant = (Element)(Alpha.getElementsByTagName("AlphaConstant")).item(0);
                    XMLconfig.setalpha(Double.parseDouble(AlphaConstant.getAttribute("value")));
                    // Mass Matrix must be formed by converting from string to double
                    Element MassMatrix = (Element)(Alpha.getElementsByTagName("MassMatrix")).item(0);
                    String[] newMassMatrixString = MassMatrix.getAttribute("value").split(",");                
                    Matrix matrix = new Matrix(numDOF,numDOF);                
                    for (int i = 0; i < numDOF; i++) {
                        for (int j = 0; j < numDOF; j++)
                            matrix.set(i,j,Double.parseDouble(newMassMatrixString[i*numDOF+j]));                                            
                    } 
                    XMLconfig.setM(matrix);
                    // Stiffness Matrix must be formed by converting from string to double
                    Element StiffnessMatrix = (Element)(Alpha.getElementsByTagName("StiffnessMatrix")).item(0);
                    String[] newStiffnessMatrixString = StiffnessMatrix.getAttribute("value").split(",");                  
                    matrix = new Matrix(numDOF,numDOF);
                    for (int i = 0; i < numDOF; i++) {
                        for (int j = 0; j < numDOF; j++)
                            matrix.set(i,j,Double.parseDouble(newStiffnessMatrixString[i*numDOF+j]));
                    }      
                    XMLconfig.setK(matrix);
                    // Dampening Matrix must be formed by converting from string to double
                    Element DampeningMatrix = (Element)(Alpha.getElementsByTagName("DampeningMatrix")).item(0);
                    String[] newDampeningMatrixString = DampeningMatrix.getAttribute("value").split(",");                               
                    matrix = new Matrix(numDOF,numDOF);
                    for (int i = 0; i < numDOF; i++) {
                        for (int j = 0; j < numDOF; j++)
                            matrix.set(i,j,Double.parseDouble(newDampeningMatrixString[i*numDOF+j]));                        
                    }       
                    XMLconfig.setC(matrix);      
                    // Hybrid Section
                    NodeList Hybrid = doc.getElementsByTagName("Hybrid");
                    Element HybridList = (Element)Hybrid.item(0);            
                    XMLconfig.setHybridOn(HybridList.getAttribute("ON").equals("true"));                     
                    // Only parse if the Hybrid is set ON="true"
                    if (XMLconfig.getHybridOn()) {                        
                        // Experimental DOF Flags
                        Element Experimental = (Element)(HybridList.getElementsByTagName("Experimental")).item(0);                        
                        String[] newExperimentalString = Experimental.getAttribute("value").split(",");
                        boolean[] newExperimentalFlags = new boolean[numDOF];
                        for (int i = 0; i < numDOF; i++)
                            newExperimentalFlags[i] = newExperimentalString[i].equals("true");
                        XMLconfig.setHybridE(newExperimentalFlags);
                        // Analytical DOF Flags
                        Element Analytical = (Element)(HybridList.getElementsByTagName("Analytical")).item(0);                        
                        String[] newAnalyticalString = Analytical.getAttribute("value").split(",");
                        boolean[] newAnalyticalFlags = new boolean[numDOF];
                        for (int i = 0; i < numDOF; i++)
                            newAnalyticalFlags[i] =  newAnalyticalString[i].equals("true");
                        XMLconfig.setHybridA(newAnalyticalFlags);
                        // Analytical Stiffness Matrix must be formed by converting from string to double
                        Element AnalyticalStiffnessMatrix = (Element)(HybridList.getElementsByTagName("AnalyticalStiffnessMatrix")).item(0);
                        String[] newAnalyticalStiffnessMatrixString = AnalyticalStiffnessMatrix.getAttribute("value").split(",");                  
                        matrix = new Matrix(numDOF,numDOF);
                        for (int i = 0; i < numDOF; i++) {
                            for (int j = 0; j < numDOF; j++)
                                matrix.set(i,j,Double.parseDouble(newAnalyticalStiffnessMatrixString[i*numDOF+j]));
                        }      
                        XMLconfig.setKA(matrix);
                    }
                } catch (NullPointerException e) {}
                // Newmark Method
                try {
                    // Get the Test Type                              
                    NodeList testMethodType = testMethodList.getElementsByTagName("Newmark");                
                    // Newmark Method                
                    XMLconfig.settestType(testMethodType.item(0).getNodeName());
                    // Get Newmark Method Parameters                
                    Element Newmark = (Element)testMethodType.item(0);
                    // History
                    Element HistoryFile = (Element)(Newmark.getElementsByTagName("History")).item(0);
                    XMLconfig.sethistoryFile(HistoryFile.getAttribute("value"));
                    // DOF
                    Element DOF = (Element)(Newmark.getElementsByTagName("DOF")).item(0);
                    XMLconfig.setnumDOF(Integer.parseInt(DOF.getAttribute("value")));                           
                    int numDOF = XMLconfig.getnumDOF();
                    // Steps
                    Element Steps = (Element)(Newmark.getElementsByTagName("Steps")).item(0);
                    XMLconfig.setsteps(Integer.parseInt(Steps.getAttribute("value")));
                    // Control
                    Element Control = (Element)(Newmark.getElementsByTagName("Control")).item(0);
                    XMLconfig.setControl(Control.getAttribute("value"));
                    // Units
                    Element Units = (Element)(Newmark.getElementsByTagName("Units")).item(0);
                    XMLconfig.setUnits(Units.getAttribute("value"));
                    // Delta_T
                    Element DeltaT = (Element)(Newmark.getElementsByTagName("DeltaT")).item(0);
                    XMLconfig.setdeltaT(Double.parseDouble(DeltaT.getAttribute("value")));
                    // Alpha Constant
                    Element AlphaConstant = (Element)(Newmark.getElementsByTagName("AlphaConstant")).item(0);
                    XMLconfig.setalpha(Double.parseDouble(AlphaConstant.getAttribute("value")));
                    // Mass Matrix must be formed by converting from string to double
                    Element MassMatrix = (Element)(Newmark.getElementsByTagName("MassMatrix")).item(0);
                    String[] newMassMatrixString = MassMatrix.getAttribute("value").split(",");                
                    Matrix matrix = new Matrix(numDOF,numDOF);                
                    for (int i = 0; i < numDOF; i++) {
                        for (int j = 0; j < numDOF; j++)
                            matrix.set(i,j,Double.parseDouble(newMassMatrixString[i*numDOF+j]));                                            
                    } 
                    XMLconfig.setM(matrix);                    
                    // Dampening Matrix must be formed by converting from string to double
                    Element DampeningMatrix = (Element)(Newmark.getElementsByTagName("DampeningMatrix")).item(0);
                    String[] newDampeningMatrixString = DampeningMatrix.getAttribute("value").split(",");                               
                    matrix = new Matrix(numDOF,numDOF);
                    for (int i = 0; i < numDOF; i++) {
                        for (int j = 0; j < numDOF; j++)
                            matrix.set(i,j,Double.parseDouble(newDampeningMatrixString[i*numDOF+j]));                        
                    }       
                    XMLconfig.setC(matrix);     
                    // Hybrid Section
                    NodeList Hybrid = doc.getElementsByTagName("Hybrid");
                    Element HybridList = (Element)Hybrid.item(0);            
                    XMLconfig.setHybridOn(HybridList.getAttribute("ON").equals("true"));       
                    if (XMLconfig.getHybridOn()) {                        
                        // Experimental DOF Flags
                        Element Experimental = (Element)(HybridList.getElementsByTagName("Experimental")).item(0);                        
                        String[] newExperimentalString = Experimental.getAttribute("value").split(",");
                        boolean[] newExperimentalFlags = new boolean[numDOF];
                    	for (int i = 0; i < numDOF; i++)
                            newExperimentalFlags[i] = newExperimentalString[i].equals("true");
                        XMLconfig.setHybridE(newExperimentalFlags);
                        // Analytical DOF Flags
                        Element Analytical = (Element)(HybridList.getElementsByTagName("Analytical")).item(0);                        
                        String[] newAnalyticalString = Analytical.getAttribute("value").split(",");
                        boolean[] newAnalyticalFlags = new boolean[numDOF];
                        for (int i = 0; i < numDOF; i++)
                            newAnalyticalFlags[i] =  newAnalyticalString[i].equals("true");
                        XMLconfig.setHybridA(newAnalyticalFlags);
                        // Analytical Stiffness Matrix must be formed by converting from string to double
                        Element AnalyticalStiffnessMatrix = (Element)(HybridList.getElementsByTagName("AnalyticalStiffnessMatrix")).item(0);
                        String[] newAnalyticalStiffnessMatrixString = AnalyticalStiffnessMatrix.getAttribute("value").split(",");                  
                        matrix = new Matrix(numDOF,numDOF);
                        for (int i = 0; i < numDOF; i++) {
                            for (int j = 0; j < numDOF; j++)
                                matrix.set(i,j,Double.parseDouble(newAnalyticalStiffnessMatrixString[i*numDOF+j]));
                        }      
                        XMLconfig.setKA(matrix);
                    }
                } catch (NullPointerException e) {}
                // Effective Force 
                try {
                    // Get the Test Type                              
                    NodeList testMethodType = testMethodList.getElementsByTagName("EFF");                       
                    XMLconfig.settestType(testMethodType.item(0).getNodeName());
                    // Get Predefined History Parameters                                                        
                    Element EFF = (Element)testMethodType.item(0);
                    // History
                    Element HistoryFile = (Element)(EFF.getElementsByTagName("History")).item(0);
                    XMLconfig.sethistoryFile(HistoryFile.getAttribute("value"));
                    // DOF
                    Element DOF = (Element)(EFF.getElementsByTagName("DOF")).item(0);
                    XMLconfig.setnumDOF(Integer.parseInt(DOF.getAttribute("value")));                                               
                    // Steps
                    Element Steps = (Element)(EFF.getElementsByTagName("Steps")).item(0);
                    XMLconfig.setsteps(Integer.parseInt(Steps.getAttribute("value")));
                    // Control
                    Element Control = (Element)(EFF.getElementsByTagName("Control")).item(0);
                    XMLconfig.setControl(Control.getAttribute("value"));
                    // Units
                    Element Units = (Element)(EFF.getElementsByTagName("Units")).item(0);
                    XMLconfig.setUnits(Units.getAttribute("value"));
                } catch (NullPointerException e) {}
                // Predefined History File
                try {
                    // Get the Test Type                              
                    NodeList testMethodType = testMethodList.getElementsByTagName("Predefined");                       
                    XMLconfig.settestType(testMethodType.item(0).getNodeName());
                    // Get Predefined History Parameters                                                        
                    Element Predefined = (Element)testMethodType.item(0);
                    // History
                    Element HistoryFile = (Element)(Predefined.getElementsByTagName("History")).item(0);
                    XMLconfig.sethistoryFile(HistoryFile.getAttribute("value"));
                    // DOF
                    Element DOF = (Element)(Predefined.getElementsByTagName("DOF")).item(0);
                    XMLconfig.setnumDOF(Integer.parseInt(DOF.getAttribute("value")));                                               
                    // Steps
                    Element Steps = (Element)(Predefined.getElementsByTagName("Steps")).item(0);
                    XMLconfig.setsteps(Integer.parseInt(Steps.getAttribute("value")));
                    // Control
                    Element Control = (Element)(Predefined.getElementsByTagName("Control")).item(0);
                    XMLconfig.setControl(Control.getAttribute("value"));
                    // Units
                    Element Units = (Element)(Predefined.getElementsByTagName("Units")).item(0);
                    XMLconfig.setUnits(Units.getAttribute("value"));
                } catch (NullPointerException e) {}               
            }            
        } catch (NullPointerException e) {e.printStackTrace();}            
        return XMLconfig;
    }
    
    /** Retrieve the Kinematics configuration into an XMLKinematicsConfig object
     */
    public XMLKinematicsConfig getKinematicsConfig() {   
        XMLKinematicsConfig XMLconfig = new XMLKinematicsConfig();        
        int actID;
        int devID;
        
        // Get Kinematics Configuration            
        try { 
            NodeList kinematics = doc.getElementsByTagName("Kinematics");
            Element kinematicsList = (Element)kinematics.item(0);
            XMLconfig.setIsOn(kinematicsList.getAttribute("ON").equals("true")); 
            // Only parse if the XML is set ON="true"
            if (XMLconfig.getIsOn()) {
                // Is Incremental or Total Kinematics?
                XMLconfig.setType(kinematicsList.getAttribute("Type").trim());
                if (XMLconfig.getType().equals("Incremental")) { 
                    // Incremental Kinematics
                    // Get the SPN parameters for each SPN 
                    NodeList SPN = kinematicsList.getElementsByTagName("SPN");                                                            
                    XMLconfig.setSPNx(Double.parseDouble(((Element)SPN.item(0)).getAttribute("X")));
                    XMLconfig.setSPNy(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Y")));
                    XMLconfig.setSPNz(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Z")));
                    XMLconfig.setLength(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Length"))); 
                    XMLconfig.setHeight(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Height")));

                    // Get the Actuator parameters
                    NodeList Actuator = kinematicsList.getElementsByTagName("Actuator");
                    // How many actuators are there?
                    XMLconfig.setnumActuators(Actuator.getLength());
                    for (int j = 0; j < Actuator.getLength(); j++) {
                        // Get each Actuator ID
                        actID = Integer.parseInt(((Element)Actuator.item(j)).getAttribute("ID")); //Actuator ID
                        // Get each Actuator coordinates                    
                        XMLconfig.setASNx(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("ASNx"))); 
                        XMLconfig.setASNy(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("ASNy"))); 
                        XMLconfig.setASNz(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("ASNz")));
                        XMLconfig.setAFNx(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("AFNx"))); 
                        XMLconfig.setAFNy(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("AFNy"))); 
                        XMLconfig.setAFNz(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("AFNz"))); 
                        XMLconfig.setActLength(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("Length"))); 
                    }
                    // Get the Device parameters
                    NodeList Device = kinematicsList.getElementsByTagName("Device");
                    // How many devices are there?
                    XMLconfig.setnumDevices(Device.getLength());
                    for (int j = 0; j < Device.getLength(); j++) {
                        // Get each Device ID
                        devID = Integer.parseInt(((Element)Device.item(j)).getAttribute("ID")); //Device ID
                        // Get each Device coordinates                    
                        XMLconfig.setMSNx(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MSNx"))); 
                        XMLconfig.setMSNy(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MSNy"))); 
                        XMLconfig.setMSNz(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MSNz"))); 
                        XMLconfig.setMFNx(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNx"))); 
                        XMLconfig.setMFNy(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNy"))); 
                        XMLconfig.setMFNz(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNz"))); 
                        XMLconfig.setDevLength(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("Length")));                         
                    }                    
                }
                else if (XMLconfig.getType().equals("Total")) { 
                    // Total Kinematics
                    // Get the SPN parameters for the SPN 
                    NodeList SPN = kinematicsList.getElementsByTagName("SPN");                                                
                    XMLconfig.setSPNx(Double.parseDouble(((Element)SPN.item(0)).getAttribute("X")));
                    XMLconfig.setSPNy(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Y")));
                    XMLconfig.setSPNz(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Z")));
                    XMLconfig.setLength(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Length"))); 
                    XMLconfig.setHeight(Double.parseDouble(((Element)SPN.item(0)).getAttribute("Height")));

                    // Get the Actuator parameters
                    NodeList Actuator = kinematicsList.getElementsByTagName("Actuator");
                    // How many actuators are there?
                    XMLconfig.setnumActuators(Actuator.getLength());
                    for (int j = 0; j < Actuator.getLength(); j++) {
                        // Get each Actuator ID
                        actID = Integer.parseInt(((Element)Actuator.item(j)).getAttribute("ID")); //Actuator ID
                        // Get each Actuator coordinates                    
                        XMLconfig.setASNx(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("ASNx"))); 
                        XMLconfig.setASNy(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("ASNy"))); 
                        XMLconfig.setASNz(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("ASNz"))); 
                        XMLconfig.setAFNx(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("AFNx"))); 
                        XMLconfig.setAFNy(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("AFNy"))); 
                        XMLconfig.setAFNz(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("AFNz"))); 
                        XMLconfig.setActLength(actID,Double.parseDouble(((Element)Actuator.item(j)).getAttribute("Length")));                     
                    }
                    // Get the Device parameters
                    NodeList Device = kinematicsList.getElementsByTagName("Device");
                    // How many devices are there?
                    XMLconfig.setnumDevices(Device.getLength());
                    for (int j = 0; j < Device.getLength(); j++) {
                        // Get each Device ID
                        devID = Integer.parseInt(((Element)Actuator.item(j)).getAttribute("ID")); //Device ID
                        // Get each Device coordinates                    
                        XMLconfig.setMSNx(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MSNx"))); 
                        XMLconfig.setMSNy(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MSNy"))); 
                        XMLconfig.setMSNz(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MSNz")));
                        XMLconfig.setMFNax(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNax"))); 
                        XMLconfig.setMFNay(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNay"))); 
                        XMLconfig.setMFNaz(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNaz"))); 
                        XMLconfig.setMFNbx(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNbx"))); 
                        XMLconfig.setMFNby(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNby"))); 
                        XMLconfig.setMFNbz(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNbz"))); 
                        XMLconfig.setMFNlocalx(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNlocalx"))); 
                        XMLconfig.setMFNlocaly(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNlocaly"))); 
                        XMLconfig.setMFNlocalz(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("MFNlocalz"))); 
                        XMLconfig.setDevLengthA(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("LengthA"))); 
                        XMLconfig.setDevLengthB(devID,Double.parseDouble(((Element)Device.item(j)).getAttribute("LengthB")));                                             
                    }      
                }                
            }
        } catch (NullPointerException e) {e.printStackTrace();}  
        return XMLconfig;
    }
    
    /** Retrieve the Ramp configuration into an XMLRampConfig object
     */
    public XMLRampConfig getRampConfig() {  
        XMLRampConfig XMLconfig = new XMLRampConfig();
        int actID;
        
        // Get the Ramp Generators for each Actuator              
        try { 
            NodeList rampGenerator = doc.getElementsByTagName("RampGenerator");
            Element rampGeneratorList = (Element)rampGenerator.item(0);           
            XMLconfig.setIsOn(rampGeneratorList.getAttribute("ON").equals("true")); 
            // Only parse if the XML is set ON="true"
            if (XMLconfig.getIsOn()) {            
                // Get each Actuator Ramp
                NodeList actuatorRamps = rampGeneratorList.getElementsByTagName("Actuator");            
                XMLconfig.setnumActuators(actuatorRamps.getLength());
                for(int i = 0; i < actuatorRamps.getLength(); i++) {
                    actID = Integer.parseInt(((Element)actuatorRamps.item(i)).getAttribute("ID"));
                    XMLconfig.setrampType(actID,((Element)actuatorRamps.item(i)).getAttribute("Ramp").trim());                                    
                    XMLconfig.setUseFeedback(actID,((Element)actuatorRamps.item(i)).getAttribute("UseFeedback").trim()); 
                }
                NodeList Ticks = rampGeneratorList.getElementsByTagName("Ticks");
                XMLconfig.setticks(Integer.parseInt(((Element)Ticks.item(0)).getAttribute("value").trim()));                
            }
        } catch (NullPointerException e) {e.printStackTrace();}
        return XMLconfig;
    }
      
    /** Print the XML configuration */
    public void print() {        
        getIntegratorConfig().print();        
        getKinematicsConfig().print();        
        getRampConfig().print();
        getScramnetConfig().print();    
    }
    
    public static void main (String argv []){
        ReadXMLConfig xml = new ReadXMLConfig(new File("C:/test.xml"));
        
        XMLIntegratorConfig config1 = xml.getIntegratorConfig();
        config1.print();
        
        XMLKinematicsConfig config2 = xml.getKinematicsConfig();
        config2.print();
        
        XMLRampConfig config3 = xml.getRampConfig();
        config3.print();
        
        XMLScramnetConfig config4 = xml.getScramnetConfig();
        config4.print();               
    }
}

