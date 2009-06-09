package edu.lehigh.nees.scramnet;

import java.util.*;

import edu.lehigh.nees.xml.XMLDAQConfig;

/********************************* 
 * Fake SCRAMNet Java Driver
 * <p>
 * This class contains all the necessary functions to simulate read to and write from the SCRAMNet.
 * <p>
 * @author Tommy Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  5 Jul 06  T. Marullo   Initial Version
 *  9 Jun 09  T. Marullo   Added a readDaq function for using DAQ XML file
 *  
 */

/** Fake Scramnet Drivers <br> 
 * Address 1000 contains a 1Hz unit sine wave for testing
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
    /** Read an integer value from a Scramnet location */
    public int readInt(int loc) {
    	return Float.floatToIntBits(memorymap.get(loc));
    }
    /** Read a long value from a Scramnet location */
    public long readLong(int loc) {
    	return (long)Float.floatToIntBits(memorymap.get(loc));
    }
    /** Read a floating point value from a Scramnet location */
    public float readFloat(int loc) {
    	return memorymap.get(loc);
    }
    /** Read a double value from a Scramnet location */
    public double readDouble(int loc) {
    	return (double)memorymap.get(loc);
    }
    /** Write an integer value to a Scramnet location */
    public int writeInt(int loc, int value) {
    	memorymap.set(loc,Float.intBitsToFloat(value));
    	return 0;
    }
    /** Write a long value to a Scramnet location */
    public int writeLong(int loc, long value) {
    	memorymap.set(loc,Float.intBitsToFloat((int)value));
    	return 0;
    }
    /** Write a floating point value to a Scramnet location */
    public int writeFloat(int loc, float value) {
    	memorymap.set(loc,value);
    	return 0;
    }
    /** Write a double value to a Scramnet location */
    public int writeDouble(int loc, double value) {
    	memorymap.set(loc,(float)value);
    	return 0;
    }
    

    /** Clear SCRAMNet Memory */
    public int clear() {
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
    
    /** Read a double value from a location using a DAQ XML config */
    public double readDAQ(String loc, XMLDAQConfig daqConfig) {
    	// Get params
    	double gain = daqConfig.getDaqGainFromOffset(loc);
    	double Voffset = daqConfig.getDaqVoffsetFromOffset(loc);
    	double Vslope = daqConfig.getDaqVslopeFromOffset(loc);
    	double EUoffset = daqConfig.getDaqEUoffsetFromOffset(loc);
    	double EUslope = daqConfig.getDaqEUslopeFromOffset(loc);    	
    	
    	return this.readDAQ(loc, gain, Voffset, Vslope, EUoffset, EUslope);
    }
	
    /** Read counts value from a location on the DAQ system */
    public int readDAQcounts(String loc) {
    	// Read 32 bit integer and shift 16 bits to the right
    	int val = Float.floatToIntBits(memorymap.get(Integer.parseInt(loc)));

        // Take 2's Complement if sign bit set to get negative value
        if (val > 0x7FFF)
            val = -((val ^ 0xFFFF) + 1);

        return val;
    }
       
    
    // Fake signal
	public synchronized void run() {		
		float i = 0;
		isRunning = true;
		while (isRunning == true) {
			writeFloat(100,(float)Math.sin(2*Math.PI*i/102.4));
			writeFloat(64,i++);			
			try {Thread.sleep(10);} catch(Exception e) {e.printStackTrace();}
		}
	}
    
    public static void main(String[] args) {
    	FakeScramNetIO scr = new FakeScramNetIO();
    	
    	scr.initScramnet();
    	
    	System.out.println("GC = " + scr.readGlobalCounter());    	    	
    	System.out.println("Read address 0: " + scr.readFloat(0));
    	System.out.println("Read address 1: " + scr.readFloat(1));
    	System.out.println("Read address 1000: " + scr.readFloat(1000));
    	try {Thread.sleep(1000);} catch(Exception e) {e.printStackTrace();}
    	System.out.println("GC = " + scr.readGlobalCounter());
    	System.out.println("Read address 0: " + scr.readFloat(0));
    	System.out.println("Write 1.234 to address 1 ");
    	scr.writeFloat(1,1.234F);
    	System.out.println("Write 2.234 to address 1000 ");
    	scr.writeFloat(1000,2.234F);
    	System.out.println("Read address 1: " + scr.readFloat(1));
    	System.out.println("Read address 1000: " + scr.readFloat(1000));
    	try {Thread.sleep(1000);} catch(Exception e) {e.printStackTrace();}
    	System.out.println("GC = " + scr.readGlobalCounter());
    	System.out.println("Read address 0: " + scr.readFloat(0));
    	System.out.println("Read address 1: " + scr.readFloat(1));
    	System.out.println("Read address 1000: " + scr.readFloat(1000));
    	
    	System.out.println("Press any key to continue");
    	try {System.in.read();} catch(Exception e){}
    	scr.unmapScramnet();
    	System.exit(0);
    }
}



