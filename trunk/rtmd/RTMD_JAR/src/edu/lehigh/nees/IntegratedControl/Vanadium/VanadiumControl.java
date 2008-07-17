package edu.lehigh.nees.IntegratedControl.Vanadium;

import edu.lehigh.nees.scramnet.*;

/********************************* 
 * VanadiumControl
 * <p>
 * Control Algorithm and GUI for the Vanadium Steel Project
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 29 Sep 06  T. Marullo  Initial 
 * 
 ********************************/
public class VanadiumControl {
/**
 * Initialize SCRAMNet
 * Create Main GUI Form
 * Create Signals GUI Form
 */
	
	/** Constructor */
	public VanadiumControl() {
		// Initialize SCRAMNet Device
		scr = new ScramNetIO();
		scr.initScramnet();
		
		// Create MainGUI in a polite manner
		//javax.swing.SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
            	maingui = new MainGUI(scr);
        		maingui.setVisible(true);
        //    }
        //});			
		
		// Start the signal acquisition
		new Thread(maingui).start();
	}
	
	/** Main */	 
	public static void main(String[] args) {
		new VanadiumControl();
	}

	/** Variables */
	MainGUI maingui;
	ScramNetIO scr;
}