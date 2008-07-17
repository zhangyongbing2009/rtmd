package edu.lehigh.nees.xml;

/********************************* 
 * XMLDAQConfig
 * <p>
 * DAQ XML configuration utility
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 
 ********************************/
public class XMLDAQConfig {	
    private SCRDaqconfig[] scrDaq;
    private int numDaqBlocks;
   
    
    /** Creates a new instance of XMLDAQConfig */
    public XMLDAQConfig() {
    }
    
    // Getters and Setters
    //////////// DAQ Functions ///////////////
    /** Set number of DAQ blocks */
    public void setnumDaqBlocks(int i) {
        numDaqBlocks = i;
        scrDaq = new SCRDaqconfig[numDaqBlocks];
        for (int j = 0; j < numDaqBlocks; j++)
            scrDaq[j] = new SCRDaqconfig();
    }
    /** Get number of DAQ blocks */
    public int getnumDaqBlocks() {
        return numDaqBlocks;
    }   
    /** Set offset for DAQ block */
    public void setDaqOffset(int blockID, String s) {
        scrDaq[blockID].offset = s;
    }
    /** Get offset for DAQ block */
    public String getDaqOffset(int blockID) {
        return scrDaq[blockID].offset;
    }   
    /** Get Block ID from offset for DAQ block */
    public int getDaqBlockIDFromOffset(String offset) throws Exception {
        for (int i = 0; i < numDaqBlocks; i++) {
            if (getDaqOffset(i).equals(offset))
                return i;
        }
        throw (new Exception("Offset " + offset + " Does Not Exist"));
    }
    
    /** Set gain for DAQ block */
    public void setDaqGain(int blockID, double d) {
        scrDaq[blockID].gain = d;
    }
    /** Get gain for DAQ block */
    public double getDaqGain(int blockID) {
        return scrDaq[blockID].gain;
    }
    /** Get gain from offset for DAQ block */
    public double getDaqGainFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getDaqBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrDaq[blockID].gain;
    }
    /** Set Voffset for DAQ block */
    public void setDaqVoffset(int blockID, double d) {
        scrDaq[blockID].Voffset = d;
    }
    /** Get Voffset for DAQ block */
    public double getDaqVoffset(int blockID) {
        return scrDaq[blockID].Voffset;
    }
    /** Get gain from Voffset for DAQ block */
    public double getDaqVoffsetFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getDaqBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrDaq[blockID].Voffset;
    }
    /** Set Vslope for DAQ block */
    public void setDaqVslope(int blockID, double d) {
        scrDaq[blockID].Vslope = d;
    }
    /** Get Vslope for DAQ block */
    public double getDaqVslope(int blockID) {
        return scrDaq[blockID].Vslope;
    }
    /** Get gain from Vslope for DAQ block */
    public double getDaqVslopeFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getDaqBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrDaq[blockID].Vslope;
    }
    /** Set EUoffset for DAQ block */
    public void setDaqEUoffset(int blockID, double d) {
        scrDaq[blockID].EUoffset = d;
    }
    /** Get EUoffset for DAQ block */
    public double getDaqEUoffset(int blockID) {
        return scrDaq[blockID].EUoffset;
    }
    /** Get gain from EUoffset for DAQ block */
    public double getDaqEUoffsetFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getDaqBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrDaq[blockID].EUoffset;
    }
    /** Set EUslope for DAQ block */
    public void setDaqEUslope(int blockID, double d) {
        scrDaq[blockID].EUslope = d;
    }
    /** Get EUslope for DAQ block */
    public double getDaqEUslope(int blockID) {
        return scrDaq[blockID].EUslope;
    }
    /** Get gain from EUslope for DAQ block */
    public double getDaqEUslopeFromOffset(String offset) {
        int blockID = -1;
        try {
            blockID = getDaqBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
        return scrDaq[blockID].EUslope;
    }
    /** Set name for DAQ block */
    public void setDaqName(int blockID, String n) {
    	scrDaq[blockID].name = n;
    }
    /** Get name for DAQ block */
    public String getDaqName(int blockID) {
    	return scrDaq[blockID].name;
    }
    /** Get name for DAQ block from offset */
    public String getDaqName(String offset) {
    	int blockID = -1;
        try {
            blockID = getDaqBlockIDFromOffset(offset);
        } catch (Exception e) {e.printStackTrace();}
    	return scrDaq[blockID].name;
    }
    
    ///////////////////////////////////
    
    
    /** Print config */
    public void print() {        
        System.out.println("DAQ");        
        for (int i = 0; i < numDaqBlocks; i++)
            System.out.println("DaqBlock " + scrDaq[i].offset + " Name= " + scrDaq[i].name + " Gain= " + scrDaq[i].gain + " Voffset= " + scrDaq[i].Voffset + " Vslope= " + scrDaq[i].Vslope + " EUoffset= " + scrDaq[i].EUoffset + " EUslope= " + scrDaq[i].EUslope);     
    }   
}

class SCRDaqconfig {
    public String offset;    
    public String channelID;  
    public double gain;
    public double Voffset;
    public double Vslope;
    public double EUslope;
    public double EUoffset;
    public String name;
    
    public SCRDaqconfig() {
    }
}

