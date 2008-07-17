package edu.lehigh.nees.xml;

/********************************* 
 * XMLHybridVizConfig
 * <p>
 * HybridViz XML configuration utility
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 28 Jul 06  T. Marullo  Initial
 * 
 ********************************/
public class XMLHybridVizConfig {   
    private int numNodes;
    private String[] id;
    private String[] cid;    
    private boolean[] dofX;
    private boolean[] dofY;
    private boolean[] dofZ;
    private boolean[] dofTX;
    private boolean[] dofTY;
    private boolean[] dofTZ;
    private String dunits;
    private String tunits;
    
     
    /** Creates a new instance of XMLRampConfig */
    public XMLHybridVizConfig() {
    }
    
    // Getters and Setters    
    /** Set number of Nodes */
    public void setnumNodes(int i) {
        numNodes = i;
        id = new String[numNodes];   
        cid = new String[numNodes];        
        dofX = new boolean[numNodes];
        dofY = new boolean[numNodes];
        dofZ = new boolean[numNodes];
        dofTX = new boolean[numNodes];
        dofTY = new boolean[numNodes];
        dofTZ = new boolean[numNodes];        
    }
    /** Get number of Nodes */
    public int getnumNodes() {
        return numNodes;
    }
    /** Set Node ID */    
    public void setID(int nodeID, String ID) {
        id[nodeID] = ID;
    }
    /** Get Node ID */
    public String getID(int nodeID) {
        return id[nodeID];
    }
    /** Set Node Constraint ID */    
    public void setConstraintID(int nodeID, String CID) {
        cid[nodeID] = CID;
    }
    /** Get Node Constraint ID */
    public String getConstraintID(int nodeID) {
        return cid[nodeID];
    }
    /** Set Node DOF DX State */    
    public void setDOFDX(int nodeID, boolean on) {
        dofX[nodeID] = on;
    }
    /** Get Node DOF DX State */
    public boolean getDOFDX(int nodeID) {
        return dofX[nodeID];
    }
    /** Set Node DOF DY State */    
    public void setDOFDY(int nodeID, boolean on) {
        dofY[nodeID] = on;
    }
    /** Get Node DOF DY State */
    public boolean getDOFDY(int nodeID) {
        return dofY[nodeID];
    }
    /** Set Node DOF DZ State */    
    public void setDOFDZ(int nodeID, boolean on) {
        dofZ[nodeID] = on;
    }
    /** Get Node DOF DZ State */
    public boolean getDOFDZ(int nodeID) {
        return dofZ[nodeID];
    }
    /** Set Node DOF TX State */    
    public void setDOFTX(int nodeID, boolean on) {
        dofTX[nodeID] = on;
    }
    /** Get Node DOF TX State */
    public boolean getDOFTX(int nodeID) {
        return dofTX[nodeID];
    }
    /** Set Node DOF TY State */    
    public void setDOFTY(int nodeID, boolean on) {
        dofTY[nodeID] = on;
    }
    /** Get Node DOF TY State */
    public boolean getDOFTY(int nodeID) {
        return dofTY[nodeID];
    }
    /** Set Node DOF TZ State */    
    public void setDOFTZ(int nodeID, boolean on) {
        dofTZ[nodeID] = on;
    }
    /** Get Node DOF TZ State */
    public boolean getDOFTZ(int nodeID) {
        return dofTZ[nodeID];
    }
    /** Set Displacement Units */    
    public void setDUnits(String units) {
        dunits = units;
    }
    /** Get Displacement Units */
    public String getDUnits() {
        return dunits;
    }
    /** Set Rotation Units */    
    public void setTUnits(String units) {
        tunits = units;
    }
    /** Get Rotation Units */
    public String getTUnits() {
        return tunits;
    }
    
    
    /** Print Configuration */
    public void print() {
        System.out.println("HybridViz DUnits=" + dunits + " TUnits=" + tunits);         
        for (int i = 0; i < numNodes; i++) {
            System.out.println("Node " + i + " id=" + id[i] + " ConstraintID=" + cid[i] + " dofX=" + dofX[i] + " dofY=" + dofY[i] + " dofZ=" + dofZ[i] + " dofTX=" + dofTX[i] + " dofTY=" + dofTY[i] + " dofTZ=" + dofTZ[i]);               
        }        
    }
    
}
