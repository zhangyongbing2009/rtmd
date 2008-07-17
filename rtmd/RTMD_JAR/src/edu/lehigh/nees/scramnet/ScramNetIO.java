package edu.lehigh.nees.scramnet;

/********************************* 
 * SCRAMNet Java Driver
 * <p>
 * This class contains all the necessary functions to read to and write from the SCRAMNet.
 * <p>
 * @author Tommy Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo   Initial Version
 *  7 Mar 06  T. Marullo   Library Load is OS independant now
 *                         DAQ functions no longer change transaction mode
 *  8 Oct 07  T. Marullo   Major rework for native types
 */

public class ScramNetIO
{
    /** Initialized the Scramnet */
    public native int initScramnet();
    /** Unmaps the Scramnet */
    public native int unmapScramnet();
    /** Set the Memory mode: 0 = long, 1 = word, 2 = byte */
    public native int setTransMode(int mode);        
    
    /** Read an integer value from a Scramnet location */
    public native int readInt(int loc);    
    /** Read a long value from a Scramnet location */
    public native long readLong(int loc);
    /** Read a floating point value from a Scramnet location */
    public native float readFloat(int loc);
    /** Read a double value from a Scramnet location */
    public native double readDouble(int loc);
       
    /** Write an integer value to a Scramnet location */
    public native int writeInt(int loc, int value);
    /** Write a long value to a Scramnet location */
    public native int writeLong(int loc, long value);
    /** Write a floating point value to a Scramnet location */
    public native int writeFloat(int loc, float value);       
    /** Write a double value to a Scramnet location */
    public native int writeDouble(int loc, double value);
           
    /** Clear SCRAMNet Memory */
    public native int clear();
    /** Read the Global Counter */
    public native long readGlobalCounter();

    /** Load the library */
    static
    {
    	if(System.getProperty("os.name").indexOf("Win") != -1)
    		// Windows
    		System.load("c:/RTMD/lib/scramnetio.dll");
    	else
    		// Linux
    		System.load("/usr/lib/scramnetio.so");
    }
           
    /** Read a double value from a location on the DAQ system based on parameters */
    public double readDAQ(String loc, double gain, double Voffset, double Vslope, double EUoffset, double EUslope) {
		// Read 32 bit integer and shift 16 bits to the right
    	int val = readInt(Integer.parseInt(loc))/65536;        
        
        // Take 2's Complement if sign bit set to get negative value
        if (val > 0x7FFF)
	      val = -((val ^ 0xFFFF) + 1);
		
		// Equation to convert counts to double value
        double value = ((((((10000.0 / gain)/32768.0) * val) * Vslope) +Voffset) * EUslope) + EUoffset;
        return value;		
    }
	
    /** Read counts value from a location on the DAQ system */
    public int readDAQcounts(String loc) {
    	// Read 32 bit integer and shift 16 bits to the right
    	int val = readInt(Integer.parseInt(loc))/65536;  

        // Take 2's Complement if sign bit set to get negative value
        if (val > 0x7FFF)
            val = -((val ^ 0xFFFF) + 1);

        return val;
    }
       
    /** Scramnet workaround write to use 2 blocks (Not needed anymore) */
    public void writeSCRsplit(int loc, double value) {
        float val = (float)value;
        if (val == 0) {
            writeFloat(loc, 0f);
            writeFloat(loc+1, 0f);
        }
        else {
            try {
                String s = Integer.toHexString(Float.floatToIntBits(val));
                float m = Float.intBitsToFloat(Long.decode("0x"+s).intValue());
                if (writeFloat(loc, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
                String s2 = s.substring(2) + "00";
                m = Float.intBitsToFloat(Long.decode("0x"+s2).intValue());
                if (writeFloat(loc+1, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
            } catch (Exception e) {e.printStackTrace();}
        }  
    }
    
    /** Scramnet workaround write to use 2 blocks (Not needed anymore) */
    public void writeSCRscaledSplit(int loc, double value, int scale) {
        float val = (float)value/(float)scale;
        if (val == 0) {
        	writeFloat(loc, 0f);
        	writeFloat(loc+1, 0f);
        }
        else {
            try {
                String s = Integer.toHexString(Float.floatToIntBits(val));
                float m = Float.intBitsToFloat(Long.decode("0x"+s).intValue());
                if (writeFloat(loc, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
                String s2 = s.substring(2) + "00";
                m = Float.intBitsToFloat(Long.decode("0x"+s2).intValue());
                if (writeFloat(loc+1, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
            } catch (Exception e) {e.printStackTrace();}
        }  
    }
}



