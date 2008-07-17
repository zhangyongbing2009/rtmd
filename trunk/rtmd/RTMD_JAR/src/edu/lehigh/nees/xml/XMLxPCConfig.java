package edu.lehigh.nees.xml;

/********************************* 
 * XMLxPCConfig
 * <p>
 * xPC XML configuration utility
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 10 Mar 06  T. Marullo  Initial
 * 15 Mar 07  T. Marullo  Removed scale and made it Gain
 *  7 Aug 07  T. Marullo  Added Lower and Upper limits and isCTRL
 * 
 ********************************/
public class XMLxPCConfig {
    private SCRxPCconfig[] scrxPCRead;
    private int numxPCReadBlocks;
    private SCRxPCconfig[] scrxPCWrite;
    private int numxPCWriteBlocks;
   
    
    /** Creates a new instance of XMLxPCConfig */
    public XMLxPCConfig() {
    }
    
    // Getters and Setters    
    /** Set number of xPC Read blocks */
    public void setnumxPCReadBlocks(int i) {
    	numxPCReadBlocks = i;
    	scrxPCRead = new SCRxPCconfig[numxPCReadBlocks];
        for (int j = 0; j < numxPCReadBlocks; j++)
        	scrxPCRead[j] = new SCRxPCconfig();
    }
    /** Get number of xPC Read blocks */
    public int getnumxPCReadBlocks() {
        return numxPCReadBlocks;
    }   
    
    /** Set number of xPC Write blocks */
    public void setnumxPCWriteBlocks(int i) {
    	numxPCWriteBlocks = i;
    	scrxPCWrite = new SCRxPCconfig[numxPCWriteBlocks];
        for (int j = 0; j < numxPCWriteBlocks; j++)
        	scrxPCWrite[j] = new SCRxPCconfig();
    }
    /** Get number of xPC Read blocks */
    public int getnumxPCWriteBlocks() {
        return numxPCWriteBlocks;
    }   
    
    /** Set location for xPC Read block */
    public void setxPCReadLocation(int blockID, String s) {
        scrxPCRead[blockID].location = s;
    }
    /** Get location for xPC Read block */
    public String getxPCReadLocation(int blockID) {
        return scrxPCRead[blockID].location;
    }
    
    /** Set location for xPC Write block */
    public void setxPCWriteLocation(int blockID, String s) {
        scrxPCWrite[blockID].location = s;
    }
    /** Get location for xPC Write block */
    public String getxPCWriteLocation(int blockID) {
        return scrxPCWrite[blockID].location;
    }   
    
    /** Set gain for xPC Read block */
    public void setxPCReadGain(int blockID, double d) {
        scrxPCRead[blockID].gain = d;
    }
    /** Get gain for xPC Read block */
    public double getxPCReadGain(int blockID) {
        return scrxPCRead[blockID].gain;
    }
    
    /** Set gain for xPC Write block */
    public void setxPCWriteGain(int blockID, double d) {
        scrxPCWrite[blockID].gain = d;
    }
    /** Get gain for xPC Write block */
    public double getxPCWriteGain(int blockID) {
        return scrxPCWrite[blockID].gain;
    }
       
    /** Set Voffset for xPC Read block */
    public void setxPCReadVoffset(int blockID, double d) {
        scrxPCRead[blockID].Voffset = d;
    }
    /** Get Voffset for xPC Read block */
    public double getxPCReadVoffset(int blockID) {
        return scrxPCRead[blockID].Voffset;
    }
    
    /** Set Vslope for xPC Read block */
    public void setxPCReadVslope(int blockID, double d) {
        scrxPCRead[blockID].Vslope = d;
    }
    /** Get Vslope for xPC Read block */
    public double getxPCReadVslope(int blockID) {
        return scrxPCRead[blockID].Vslope;
    }
    
    /** Set EUoffset for xPC Read block */
    public void setxPCReadEUoffset(int blockID, double d) {
        scrxPCRead[blockID].EUoffset = d;
    }
    /** Get EUoffset for xPC Read block */
    public double getxPCReadEUoffset(int blockID) {
        return scrxPCRead[blockID].EUoffset;
    }
    
    /** Set EUslope for xPC Read block */
    public void setxPCReadEUslope(int blockID, double d) {
        scrxPCRead[blockID].EUslope = d;
    }
    /** Get EUslope for xPC Read block */
    public double getxPCReadEUslope(int blockID) {
        return scrxPCRead[blockID].EUslope;
    }
    
    /** Set name for xPC Read block */
    public void setxPCReadName(int blockID, String n) {
    	scrxPCRead[blockID].name = n;
    }
    /** Get name for xPC Read block */
    public String getxPCReadName(int blockID) {
    	return scrxPCRead[blockID].name;
    }
    
    /** Set name for xPC Write block */
    public void setxPCWriteName(int blockID, String n) {
    	scrxPCWrite[blockID].name = n;
    }
    /** Get name for xPC Write block */
    public String getxPCWriteName(int blockID) {
    	return scrxPCWrite[blockID].name;
    }
    
    /** Set Units for xPC Read block */
    public void setxPCReadUnits(int blockID, String n) {
    	scrxPCRead[blockID].units = n;
    }
    /** Get Units for xPC Read block */
    public String getxPCReadUnits(int blockID) {
    	return scrxPCRead[blockID].units;
    }
    /** Set Units for xPC Write block */
    public void setxPCWriteUnits(int blockID, String n) {
    	scrxPCWrite[blockID].units = n;
    }
    /** Get Units for xPC Write block */
    public String getxPCWriteUnits(int blockID) {
    	return scrxPCWrite[blockID].units;
    }
    
    /** Set isDAQ for xPC Read block */
    public void setxPCReadisDAQ(int blockID, String n) {
    	scrxPCRead[blockID].isDAQ = n;
    }
    /** Get isDAQ for xPC Read block */
    public String getxPCReadisDAQ(int blockID) {
    	return scrxPCRead[blockID].isDAQ;
    }
    
    /** Set isCTRL for xPC Write block */
    public void setxPCWriteisCTRL(int blockID, String n) {
    	scrxPCWrite[blockID].isCTRL = n;
    }
    /** Get isCTRL for xPC Write block */
    public String getxPCWriteisCTRL(int blockID) {
    	return scrxPCWrite[blockID].isCTRL;
    }
    
    /** Set Lower Limit for xPC Write block */
    public void setxPCWriteLowerLimit(int blockID, double d) {
    	scrxPCWrite[blockID].lowerlimit = d;
    }
    /** Get Lower Limit for xPC Write block */
    public double getxPCWriteLowerLimit(int blockID) {
    	return scrxPCWrite[blockID].lowerlimit;
    }
    /** Set Upper Limit for xPC Write block */
    public void setxPCWriteUpperLimit(int blockID, double d) {
    	scrxPCWrite[blockID].upperlimit = d;
    }
    /** Get Upper Limit for xPC Write block */
    public double getxPCWriteUpperLimit(int blockID) {
    	return scrxPCWrite[blockID].upperlimit;
    }

    
    ///////////////////////////////////
    
    
    /** Print config */
    public void print() {
        System.out.println("xPC");        
        for (int i = 0; i < numxPCReadBlocks; i++) {
        	if (scrxPCRead[i].isDAQ.equals("true"))
        		System.out.println("xPCReadBlock " + scrxPCRead[i].location + " Name= " + scrxPCRead[i].name + " Units= " + scrxPCRead[i].units + " isDAQ= " + scrxPCRead[i].isDAQ + " Gain= " + scrxPCRead[i].gain + " Voffset= " + scrxPCRead[i].Voffset + " Vslope= " + scrxPCRead[i].Vslope + " EUoffset= " + scrxPCRead[i].EUoffset + " EUslope= " + scrxPCRead[i].EUslope);
        	else
        		System.out.println("xPCReadBlock " + scrxPCRead[i].location + " Name= " + scrxPCRead[i].name + " Units= " + scrxPCRead[i].units + " isDAQ= " + scrxPCRead[i].isDAQ + " Gain= " + scrxPCRead[i].gain);
        }
        for (int i = 0; i < numxPCWriteBlocks; i++)
        	System.out.println("xPCWriteBlock " + scrxPCWrite[i].location + " Name= " + scrxPCWrite[i].name + " Units= " + scrxPCWrite[i].units + " Gain= " + scrxPCWrite[i].gain + " Lower Limit= " + scrxPCWrite[i].lowerlimit + " Upper Limit= " + scrxPCWrite[i].upperlimit);
        
    }    
}

class SCRxPCconfig {
    public String location;    
    public String name;  
    public String units;    
    public String isDAQ;
    public String isCTRL;
    public double gain;    
    public double Voffset;
    public double Vslope;
    public double EUslope;
    public double EUoffset;
    public double lowerlimit;
    public double upperlimit;
    
    
    public SCRxPCconfig() {
    }
}

