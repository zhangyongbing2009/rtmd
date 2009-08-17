package edu.lehigh.nees.xml;

/********************************* 
 * XMLScramnetConfig
 * <p>
 * SCRAMNet XML configuration utility
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 26 Jun 06  T. Marullo  Added Node inner class for Visualizations using SCRAMNet
 * 						  Removed Controller Limits since they are no longer needed
 * 06 Jul 06  T. Marullo  Added units to SCRNodeconfig
 * 13 Jul 06  T. Marullo  Changed Scan to Stream 
 * 28 Jul 06  T. Marullo  Changed Node Name to Node ID
 * 17 Aug 09  T. Marullo  Changed gain from int to double
 * 
 ********************************/
public class XMLScramnetConfig {
    private boolean isOn = false;
    private int numCtrlBlocks;
    private int numSimBlocks;
    private int numNodeBlocks;   
    private SCRCtrlconfig[] scrCtrl;
    private SCRSimconfig[] scrSim;
    private SCRNodeconfig[] scrNode;   
    
    /** Creates a new instance of XMLScramnetConfig */
    public XMLScramnetConfig() {
    }
    
    // Getters and Setters
    /** Set the Object on */
    public void setIsOn(boolean bool) {
        isOn = bool;
    }
    /** Get isOn flag */
    public boolean getIsOn() {
        return isOn;
    }
    
    
    //////////// Ctrl Functions ///////////////
    /** Set number of controller blocks */
    public void setnumCtrlBlocks(int i) {
        numCtrlBlocks = i;
        scrCtrl = new SCRCtrlconfig[numCtrlBlocks];
        for (int j = 0; j < numCtrlBlocks; j++)
            scrCtrl[j] = new SCRCtrlconfig();
    }
    /** Get number of controller blocks */
    public int getnumCtrlBlocks() {
        return numCtrlBlocks;
    }   
    /** Set offset for controller block */
    public void setCtrlOffset(int blockID, String s) {
        scrCtrl[blockID].offset = s;
    }
    /** Get offset for controller block */
    public String getCtrlOffset(int blockID) {
        return scrCtrl[blockID].offset;
    }   
    /** Get Block ID from offset for controller block */
    public int getCtrlBlockIDFromOffset(String offset) throws Exception {
        for (int i = 0; i < numCtrlBlocks; i++) {
            if (getCtrlOffset(i).equals(offset))
                return i;
        }
        throw (new Exception("Offset " + offset + " Does Not Exist"));
    }
    /** Set Stream flag for controller block */
    public void setCtrlStream(int blockID, String s) {
        scrCtrl[blockID].stream = s;
    }
    /** Get Stream flag for controller block */
    public String getCtrlStream(int blockID) {
        return scrCtrl[blockID].stream;
    }
    /** Get Stream flag from offset for controller block */
    public String getCtrlStreamFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getCtrlBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrCtrl[blockID].stream;
    }
    /** Set Description for controller block */
    public void setCtrlDescription(int blockID, String s) {
        scrCtrl[blockID].description = s;
    }
    /** Get Description for controller block */
    public String getCtrlDescription(int blockID) {
        return scrCtrl[blockID].description;
    }
    /** Get Description from offset for controller block */
    public String getCtrlDescriptionFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getCtrlBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrCtrl[blockID].description;
    }
    /** Set Units for controller block */
    public void setCtrlUnits(int blockID, String s) {
        scrCtrl[blockID].units = s;
    }
    /** Get Units for controller block */
    public String getCtrlUnits(int blockID) {
        return scrCtrl[blockID].units;
    }
    /** Get Units from offset for controller block */
    public String getCtrlUnitsFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getCtrlBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrCtrl[blockID].units;
    }
    /** Set scale for controller block */
    public void setCtrlScale(int blockID, double i) {
        scrCtrl[blockID].scale = i;
    }
    /** Get scale for controller block */
    public double getCtrlScale(int blockID) {
        return scrCtrl[blockID].scale;
    }
    /** Get scale from offset for controller block */
    public double getCtrlScaleFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getCtrlBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrCtrl[blockID].scale;
    }
    /** Set Minimum Simulation Limit for controller block */
    public void setLowerLimit(int blockID, double d) {
        scrCtrl[blockID].lowerLimit = d;
    }
    /** Get Minimum Simulation Limit for controller block */
    public double getLowerLimit(int blockID) {
        return scrCtrl[blockID].lowerLimit;
    }
    /** Get Minimum Simulation Limit from offset for controller block */
    public double getLowerLimitFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getCtrlBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrCtrl[blockID].lowerLimit;
    }
    /** Set Maximum Simulation Limit for controller block */
    public void setUpperLimit(int blockID, double d) {
        scrCtrl[blockID].upperLimit = d;
    }
    /** Get Maximum Simulation Limit for controller block */
    public double getUpperLimit(int blockID) {
        return scrCtrl[blockID].upperLimit;
    }
    /** Get Maximum Simulation Limit from offset for controller block */
    public double getUpperLimitFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getCtrlBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrCtrl[blockID].upperLimit;
    }
    //////////////////////////////////////////////////
    
    //////////// Sim Functions ///////////////
    /** Set number of Simulation Blocks */
    public void setnumSimBlocks(int i) {
        numSimBlocks = i;
        scrSim = new SCRSimconfig[numSimBlocks];
        for (int j = 0; j < numSimBlocks; j++)
            scrSim[j] = new SCRSimconfig();
    }
    /** Get number of Simulation Blocks */
    public int getnumSimBlocks() {
        return numSimBlocks;
    }   
    /** Set offset for simulation block */    
    public void setSimOffset(int blockID, String s) {
        scrSim[blockID].offset = s;
    }
    /** Get offset for simulation block */
    public String getSimOffset(int blockID) {
        return scrSim[blockID].offset;
    }   
    /** Get Block ID from offset for simulation block */
    public int getSimBlockIDFromOffset(String offset) throws Exception {
        for (int i = 0; i < numSimBlocks; i++) {
            if (getCtrlOffset(i).equals(offset))
                return i;
        }
        throw (new Exception("Offset " + offset + " Does Not Exist"));
    }
    /** Set name for simulation block */
    public void setSimName(int blockID, String s) {
        scrSim[blockID].name = s;
    }
    /** Get name for simulation block */
    public String getSimName(int blockID) {
        return scrSim[blockID].name;
    }
    /** Get name from offset for simulation block */
    public String getSimNameFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getSimBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrSim[blockID].name;
    }
    ///////////////////////////////////
    
    
    //////////// Node Functions ///////////////
    /** Set number of Node Blocks */
    public void setnumNodeBlocks(int i) {
        numNodeBlocks = i;
        scrNode = new SCRNodeconfig[numNodeBlocks];
        for (int j = 0; j < numNodeBlocks; j++)
        	scrNode[j] = new SCRNodeconfig();
    }
    /** Get number of Node Blocks */
    public int getnumNodeBlocks() {
        return numNodeBlocks;
    }       
    /** Set ID for Node block */
    public void setNodeID(int blockID, String s) {
    	scrNode[blockID].id = s;
    }
    /** Get ID for Node block */
    public String getNodeID(int blockID) {
        return scrNode[blockID].id;
    }
    /** Set Constraint ID for Node block */
    public void setNodeConstraintID(int blockID, String s) {
    	scrNode[blockID].constraintid = s;
    }
    /** Get Constraint ID for Node block */
    public String getNodeConstraintID(int blockID) {
        return scrNode[blockID].constraintid;
    }
    /** Get Constraint ID for Node block by id */
    public String getNodeConstraintIDFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeConstraintID(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set X Displacement offset for Node block */    
    public void setNodeDXOffset(int blockID, String s) {
        scrNode[blockID].dxoffset = s;
    }
    /** Get X Displacement offset for Node block */
    public String getNodeDXOffset(int blockID) {
        return scrNode[blockID].dxoffset;
    }   
    /** Get X Displacement offset for Node block by id */
    public String getNodeDXOffsetFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeDXOffset(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set Y Displacement offset for Node block */    
    public void setNodeDYOffset(int blockID, String s) {
        scrNode[blockID].dyoffset = s;
    }
    /** Get Y Displacement offset for Node block */
    public String getNodeDYOffset(int blockID) {
        return scrNode[blockID].dyoffset;
    }   
    /** Get Y Displacement offset for Node block by id */
    public String getNodeDYOffsetFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeDYOffset(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set Z Displacement offset for Node block */    
    public void setNodeDZOffset(int blockID, String s) {
        scrNode[blockID].dzoffset = s;
    }
    /** Get Z Displacement offset for Node block */
    public String getNodeDZOffset(int blockID) {
        return scrNode[blockID].dzoffset;
    }   
    /** Get Z Displacement offset for Node block by id */
    public String getNodeDZOffsetFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeDZOffset(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set X Rotation offset for Node block */    
    public void setNodeTXOffset(int blockID, String s) {
        scrNode[blockID].txoffset = s;
    }
    /** Get X Rotation offset for Node block */
    public String getNodeTXOffset(int blockID) {
        return scrNode[blockID].txoffset;
    }   
    /** Get X Rotation offset for Node block by id */
    public String getNodeTXOffsetFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeTXOffset(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set Y Rotation offset for Node block */    
    public void setNodeTYOffset(int blockID, String s) {
        scrNode[blockID].tyoffset = s;
    }
    /** Get Y Rotation offset for Node block */
    public String getNodeTYOffset(int blockID) {
        return scrNode[blockID].tyoffset;
    }   
    /** Get Y Rotation offset for Node block by id */
    public String getNodeTYOffsetFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeTYOffset(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set Z Rotation offset for Node block */    
    public void setNodeTZOffset(int blockID, String s) {
        scrNode[blockID].tzoffset = s;
    }
    /** Get Z Rotation offset for Node block */
    public String getNodeTZOffset(int blockID) {
        return scrNode[blockID].tzoffset;
    }   
    /** Get Z Rotation offset for Node block by id */
    public String getNodeTZOffsetFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeTZOffset(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set Displacement units for Node block */
    public void setNodeDUnits(int blockID, String s) {
    	scrNode[blockID].dunits = s;
    }
    /** Get Displacement units for Node block */
    public String getNodeDUnits(int blockID) {
        return scrNode[blockID].dunits;
    }
    /** Get Displacement units for Node block by id */
    public String getNodeDUnitsFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeDUnits(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    /** Set Rotation units for Node block */
    public void setNodeTUnits(int blockID, String s) {
    	scrNode[blockID].tunits = s;
    }
    /** Get Rotation units for Node block */
    public String getNodeTUnits(int blockID) {
        return scrNode[blockID].tunits;
    }
    /** Get Rotation units for Node block by id */
    public String getNodeTUnitsFromName(String name) throws Exception {
        for (int i = 0; i < numNodeBlocks; i++) {
            if (getNodeID(i).equals(name))
                return getNodeTUnits(i);
        }
        throw (new Exception("Node " + name + " Does Not Exist"));
    }
    ///////////////////////////////////
    
    
    /** Print config */
    public void print() {
        System.out.println("SCRAMNet On? " + isOn);
        if (isOn) {
            for (int i = 0; i < numCtrlBlocks; i++)
                System.out.println("CtrlBlock " + scrCtrl[i].offset + " Stream = " + scrCtrl[i].stream + " Description = " + scrCtrl[i].description + " Units " + scrCtrl[i].units + " Scale " + scrCtrl[i].scale + " LowerLimit " + scrCtrl[i].lowerLimit + " UpperLimit " + scrCtrl[i].upperLimit);
            for (int i = 0; i < numSimBlocks; i++)
                System.out.println("SimBlock " + scrSim[i].offset + " Name = " + scrSim[i].name);
            for (int i = 0; i < numNodeBlocks; i++)
                System.out.println("NodeBlock " + i + " ID = " + scrNode[i].id + " ConstraintID = " + scrNode[i].constraintid + " DXoffset = " + scrNode[i].dxoffset + " DYoffset = " + scrNode[i].dyoffset + " DZoffset = " + scrNode[i].dzoffset + " Displacement Units = " + scrNode[i].dunits + " TXoffset = " + scrNode[i].txoffset + " TYoffset = " + scrNode[i].tyoffset + " TZoffset = " + scrNode[i].tzoffset + " Rotation Units = " + scrNode[i].tunits);
        }
    }
    
}

/** Controller SCRAMNet configuration inner class for control and telepresence
 * 
 * @author Tommy
 *
 */
class SCRCtrlconfig {
    public String offset;    
    public String stream;
    public String description;
    public String units;
    public double scale;
    public double lowerLimit;
    public double upperLimit;   
    
    public SCRCtrlconfig() {
    }
}

/** Simulation Data SCRAMNet configuration inner class for telepresence
 * 
 * @author Tommy
 *
 */
class SCRSimconfig {
    public String offset;
    public String name;    
    
    public SCRSimconfig() {
    }
}

/** Node SCRAMNet configuration inner class for visualization and telepresence
 * 
 * @author Tommy
 *
 */
class SCRNodeconfig {
    public String dxoffset;	// X Displacement
    public String dyoffset;	// Y Displacement
    public String dzoffset;	// Z Displacement
    public String txoffset; // X Rotation
    public String tyoffset; // Y Rotation
    public String tzoffset; // Z Rotation
    public String id;
    public String constraintid; // Constraint ID
    public String dunits  ; // Units for Displacement
    public String tunits  ; // Units for Rotation
    
    public SCRNodeconfig() {
    }
}
