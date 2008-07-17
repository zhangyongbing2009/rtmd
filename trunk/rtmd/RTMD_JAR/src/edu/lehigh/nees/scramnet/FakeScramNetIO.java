package edu.lehigh.nees.scramnet;

import java.util.*;

/********************************* 
 * Fake SCRAMNet Java Driver
 * <p>
 * This class contains all the necessary functions to simulate read to and write from the SCRAMNet.
 * <p>
 * @author Tommy Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  5 Jul 06  T. Marullo   Initial Version
 *  
 */

/** Fake Scramnet Drivers <br> 
 * Address 0 contains a 1Hz unit sine wave for testing
 */
public class FakeScramNetIO implements Runnable
{
	private volatile List<Float> memorymap;
	private boolean isRunning;
	private static int SIZE = 3000;
	private Thread fakeSignal;
	
	
	/** Constructor */
	public FakeScramNetIO() {
		// Declare SIZE Object blockFs
		memorymap = Collections.synchronizedList(new ArrayList<Float>(SIZE));	
		// Float List    	    
    	for (int i = 0; i < SIZE; i++)
    		memorymap.add(i,0F);		
		fakeSignal = new Thread(this);
	}
	
    /** Initialized the Scramnet */
    public int initScramnet() {
    	fakeSignal.start();
    	return 0;
    }
    /** Unmaps the Scramnet */
    public int unmapScramnet() {
    	isRunning = false;
    	try{fakeSignal.join();}catch(Exception e){}
    	memorymap = null;    	
    	return 0;
    }
    /** Set the Memory mode: 0 = long, 1 = word, 2 = byte */
    public int setTransMode(int mode) {    	
    	return 0;
    }
    /** Read the Global Counter */
    public long readGlobalCounter() {    	
    	return Long.valueOf(Float.toString(memorymap.get(64)).split("\\.")[0]);
    }
    /** Read a floating point value from a Scramnet location */
    public float readSCR(int loc) {
    	return memorymap.get(loc);
    }
    /** Read an integer value from a Scramnet location */
    public int readSCRint(int loc) {
    	return Float.floatToIntBits(memorymap.get(loc));
    }
    /** Read a floating point value from the Scramnet and scale it */
    public float readSCRscaled(int loc, int scale) {
    	return memorymap.get(loc) * scale;
    }
    /** Read a block of data starting from a Scramnet base location */
    public int readSCRblock(int loc, float[] values, int length) {
    	ArrayList tempList = (ArrayList)memorymap.subList(loc,loc+length);
    	for (int i = 0; i < tempList.size(); i++) {
    		values[i] = memorymap.get(loc);
    	}
    	return 0;    	    	
    }
    /** Read a block of data from the Scramnet and scale them all */
    public int readSCRblockScaled(int loc, float[] values, int length, int[] scale) {
    	ArrayList tempList = (ArrayList)memorymap.subList(loc,loc+length);
    	for (int i = 0; i < tempList.size(); i++) {
    		values[i] = memorymap.get(loc) * scale[i];
    	}
    	return 0;  
    }
    /** Write a floating point value to a Scramnet location */
    public int writeSCR(int loc, float value) {
    	memorymap.set(loc,value);
    	return 0;
    }
    /** Write an integer value to a Scramnet location */
    public int writeSCRint(int loc, int value) {
    	memorymap.set(loc,Float.intBitsToFloat(value));
    	return 0;
    }
    /** Write a floating point value to a Scramnet location and scale it */
    public int writeSCRscaled(int loc, float value, int scale) {
    	memorymap.set(loc,value*scale);
    	return 0;
    }
    /** Write a block of data starting from a Scramnet base location */
    public int writeSCRblock(int loc, float[] value, int length) {
    	for (int i = 0; i < length; i++) {
    		memorymap.set(loc+i,value[i]);
    	}
    	return 0;
    }
    /** Write a block of data starting from a Scramnet base location and scale it */
    public int writeSCRblockScaled(int loc, float[] value, int length, int[] scale) {
    	for (int i = 0; i < length; i++) {
    		memorymap.set(loc+i,value[i]*scale[i]);
    	}
    	return 0;
    }
    /** Clear SCRAMNet Memory */
    public int clearSCR() {
    	memorymap.clear();
    	return 0;
    }

    /** Read a double value from a location on the DAQ system based on parameters */
    public double readDAQ(String loc, double gain, double Voffset, double Vslope, double EUoffset, double EUslope) {
		// Read 32 bit integer and shift 16 bits to the right
    	int val = Float.floatToIntBits(memorymap.get(Integer.parseInt(loc)));
        
        // Take 2's Complement if sign bit set to get negative value
        if (val > 0x7FFF)
	      val = -((val ^ 0xFFFF) + 1);
		
		// Equation to convert counts to double value
        double value = ((((((10000.0 / gain)/32768.0) * val) * Vslope) +Voffset) * EUslope) + EUoffset;
        return value;		
    }
	
    /** Read counts value from a location on the DAQ system */
    public int readDAQcounts(int loc) {
    	// Read 32 bit integer and shift 16 bits to the right
    	int val = Float.floatToIntBits(memorymap.get(loc));

        // Take 2's Complement if sign bit set to get negative value
        if (val > 0x7FFF)
            val = -((val ^ 0xFFFF) + 1);

        return val;
    }
       
    /** Scramnet workaround write to use 2 blocks (Not needed anymore) */
    public void writeSCRsplit(int loc, double value) {
        float val = (float)value;
        if (val == 0) {
            memorymap.set(loc, 0f);
            memorymap.set(loc+1, 0f);
        }
        else {
            try {
                String s = Integer.toHexString(Float.floatToIntBits(val));
                float m = Float.intBitsToFloat(Long.decode("0x"+s).intValue());
                if (writeSCR(loc, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
                String s2 = s.substring(2) + "00";
                m = Float.intBitsToFloat(Long.decode("0x"+s2).intValue());
                if (writeSCR(loc+1, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
            } catch (Exception e) {e.printStackTrace();}
        }  
    }
       
    /** Scramnet workaround write to use 2 blocks (Not needed anymore) */
    public void writeSCRscaledSplit(int loc, double value, int scale) {
        float val = (float)value/(float)scale;
        if (val == 0) {
        	memorymap.set(loc, 0f);
            memorymap.set(loc+1, 0f);
        }
        else {
            try {
                String s = Integer.toHexString(Float.floatToIntBits(val));
                float m = Float.intBitsToFloat(Long.decode("0x"+s).intValue());
                if (writeSCR(loc, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
                String s2 = s.substring(2) + "00";
                m = Float.intBitsToFloat(Long.decode("0x"+s2).intValue());
                if (writeSCR(loc+1, m) != 0) 
                    System.err.println("Scramnet Write Error at " + loc + " for value " + value);
            } catch (Exception e) {e.printStackTrace();}
        }  
    }
    
    // Fake signal
	public synchronized void run() {		
		float i = 0;
		isRunning = true;
		while (isRunning == true) {
			memorymap.set(0,(float)Math.sin(2*Math.PI*i/102.4));
			memorymap.set(64,i++);			
			try {Thread.sleep(10);} catch(Exception e) {e.printStackTrace();}
		}
	}
    
    public static void main(String[] args) {
    	FakeScramNetIO scr = new FakeScramNetIO();
    	
    	scr.initScramnet();
    	
    	System.out.println("GC = " + scr.readGlobalCounter());    	    	
    	System.out.println("Read address 0: " + scr.readSCR(0));
    	System.out.println("Read address 1: " + scr.readSCR(1));
    	System.out.println("Read address 1000: " + scr.readSCR(1000));
    	try {Thread.sleep(1000);} catch(Exception e) {e.printStackTrace();}
    	System.out.println("GC = " + scr.readGlobalCounter());
    	System.out.println("Read address 0: " + scr.readSCR(0));
    	System.out.println("Write 1.234 to address 1 ");
    	scr.writeSCR(1,1.234F);
    	System.out.println("Write 2.234 to address 1000 ");
    	scr.writeSCR(1000,2.234F);
    	System.out.println("Read address 1: " + scr.readSCR(1));
    	System.out.println("Read address 1000: " + scr.readSCR(1000));
    	try {Thread.sleep(1000);} catch(Exception e) {e.printStackTrace();}
    	System.out.println("GC = " + scr.readGlobalCounter());
    	System.out.println("Read address 0: " + scr.readSCR(0));
    	System.out.println("Read address 1: " + scr.readSCR(1));
    	System.out.println("Read address 1000: " + scr.readSCR(1000));
    	
    	System.out.println("Press any key to continue");
    	try {System.in.read();} catch(Exception e){}
    	scr.unmapScramnet();
    	System.exit(0);
    }
}



