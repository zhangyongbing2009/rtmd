package edu.lehigh.nees.util;

import java.awt.BorderLayout;
import java.io.*;
import java.nio.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import edu.lehigh.nees.xml.*;
import edu.lehigh.nees.util.filefilter.*;

/********************************* 
 * xPCDataConverter
 * <p>
 * Convert xPC Binary Files to CSV File using xPC XML file
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 15 Mar 06  T. Marullo  Initial
 * 27 Apr 06  T. Marullo  Added path to convert for flexible XPC.DAT locations
 * 28 Apr 06  T. Marullo  Changed xpc data file loading to user selected rather than hardcoded
 *  6 Apr 07  T. Marullo  Added units to header
 * 
 ********************************/
public class xPCDataConverter {

	/** Create a new xPCDataConverter Object *
	 * 
	 * @param xmlfile_ xPC XML File
	 * @param outfile_ CSV Output file
	 */
	public xPCDataConverter(String xmlfile_, String outfile_) {					
		out = null;	
		xmlfile = xmlfile_;
		outfile = outfile_;
		isDone = false;
	}
	
	public xPCDataConverter() {
		// Get the input file name
    	String xmlFileName;
        if ((xmlFileName = FileHandler.getFilePath("Open xPC XML File", new XMLFileFilter())) == null) {    	        	
        	return;
        }
        
    	// Get the output file name
    	String outputFileName;
        if ((outputFileName = FileHandler.getFilePath("Open Output CSV File", new CSVFileFilter())) == null) {
        	return;
        }
        
        // Create the conveter object
        xPCDataConverter xpc = new xPCDataConverter(xmlFileName,outputFileName);		
		xpc.convert();		
	}
	
	/** Convert the Data files to CSV format */
	public void convert() {	
		// Open XML file
		XMLxPCConfig xml = (new ReadxPCXMLConfig(new File(xmlfile))).getxPCConfig();		
		
		// Set Data File array based on how many read blocks there are (10 reads per block)
		data = new DataInputStream[(xml.getnumxPCReadBlocks()-1)/10 + 1];
		for (int i = 0; i < data.length; i++) {
			data[i] = null;			
		}
		
		// Open each Data File and seek ahead 512 bytes
		for (int i = 0; i < data.length; i++) {
			try {      
				//data[i] = new DataInputStream(new BufferedInputStream(new FileInputStream(new String(pathOfData + "XPC" + (i+1) + ".DAT"))));
				data[i] = new DataInputStream(new BufferedInputStream(new FileInputStream(FileHandler.getFilePath("Open xPC Data File " + (i+1)))));
				data[i].skipBytes(512);
			} catch (Exception e) {e.printStackTrace();}
		}
			
		// Open Output File
		try {
			out = new PrintStream(new FileOutputStream(new File(outfile)));
		} catch (Exception e) {e.printStackTrace();}
		
		// Write Header
		String s = "";
		for (int i = 0; i < xml.getnumxPCReadBlocks(); i++) {
			s = s + xml.getxPCReadName(i) + " (" + xml.getxPCReadUnits(i) + "),";
		}
		out.println(s + "Time");
		
		// Create a progress bar
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(0,0,100,25);
		JFrame popup = new JFrame();
		popup.setBounds(0,0,200,70);
		popup.setLocationRelativeTo(null);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(progressBar,BorderLayout.CENTER);
		JLabel label = new JLabel("Converting");
		label.setHorizontalAlignment(JLabel.CENTER);
		popup.getContentPane().add(label,BorderLayout.NORTH);
		popup.setVisible(true);
        progressBar.setIndeterminate(true);
		
		// Go through all xPC Read Blocks and get data from files		
		int i = 0;
		while (isDone == false) {
			int fileID = -1;
			s = "";	
			for (i = 0; i < xml.getnumxPCReadBlocks(); i++) {
				// Increment file ID on each 10th data block				
				if (i % 10 == 0)
					fileID++;
				if (xml.getxPCReadisDAQ(i).equals("false"))
					// Sim-Ctrl Block
					s = s + (getData(data[fileID])*xml.getxPCReadGain(i)) + ",";
				else {
					// DAQ Block needs to be converted to a 16-Bit 2-Cs Int					
					double daqval = getData(data[fileID]);
					daqval = daqval/65536;
					if (daqval > 0x7FFF)
						daqval = -(((int)daqval ^ 0xFFFF) + 1);
					daqval = ((((((10000.0 / xml.getxPCReadGain(i))/32768.0) * daqval) * xml.getxPCReadVslope(i)) + xml.getxPCReadVoffset(i)) * xml.getxPCReadEUslope(i)) + xml.getxPCReadEUoffset(i);					
					s = s + daqval + ",";
				}
			}			
			
			// Throw away all Time columns except last file
			for (i = 0; i < data.length-1; i++) {
				getData(data[i]);
			}			
			// Skip the last line because it's garbage numbers
			if (isDone != true)
				out.println(s + getData(data[i]));
		}
		
		// Close files
		for (i = 0; i < data.length; i++) {
			try {data[i].close();} catch (Exception e) {e.printStackTrace();}
		}
		out.close();
		
		popup.setVisible(false);
		
		JOptionPane.showMessageDialog(null,"Conversion Complete");
	}
	
	/** Convert the Data files to CSV format assuming all XPC files are in the path */
	public void convertNoPopup() {	
		// Open XML file
		File xmlFILE = new File(xmlfile);
		XMLxPCConfig xml = (new ReadxPCXMLConfig(xmlFILE)).getxPCConfig();	
		String abspath = xmlFILE.getAbsolutePath();
		String pathOfData = abspath.substring(0,abspath.lastIndexOf("\\"));		
		
		// Set Data File array based on how many read blocks there are (10 reads per block)
		data = new DataInputStream[(xml.getnumxPCReadBlocks()-1)/10 + 1];
		for (int i = 0; i < data.length; i++) {
			data[i] = null;			
		}
		
		// Open each Data File and seek ahead 512 bytes
		for (int i = 0; i < data.length; i++) {
			try {      
				data[i] = new DataInputStream(new BufferedInputStream(new FileInputStream(new String(pathOfData + "\\XPC" + (i+1) + ".DAT"))));
				System.out.println("Processed " + new String(pathOfData + "\\XPC" + (i+1) + ".DAT"));
				data[i].skipBytes(512);
			} catch (Exception e) {e.printStackTrace();}
		}
			
		// Open Output File
		try {
			out = new PrintStream(new FileOutputStream(new File(pathOfData + "\\" + outfile)));
		} catch (Exception e) {e.printStackTrace();}
		
		// Write Header
		String s = "";
		for (int i = 0; i < xml.getnumxPCReadBlocks(); i++) {
			s = s + xml.getxPCReadName(i) + " (" + xml.getxPCReadUnits(i) + "),";
		}
		out.println(s + "Time");
		
		// Create a progress bar
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(0,0,100,25);
		JFrame popup = new JFrame();
		popup.setBounds(0,0,200,70);
		popup.setLocationRelativeTo(null);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(progressBar,BorderLayout.CENTER);
		JLabel label = new JLabel("Converting");
		label.setHorizontalAlignment(JLabel.CENTER);
		popup.getContentPane().add(label,BorderLayout.NORTH);
		popup.setVisible(true);
        progressBar.setIndeterminate(true);
		
		// Go through all xPC Read Blocks and get data from files		
		int i = 0;
		while (isDone == false) {
			int fileID = -1;
			s = "";	
			for (i = 0; i < xml.getnumxPCReadBlocks(); i++) {
				// Increment file ID on each 10th data block				
				if (i % 10 == 0)
					fileID++;
				if (xml.getxPCReadisDAQ(i).equals("false"))
					// Sim-Ctrl Block
					s = s + (getData(data[fileID])*xml.getxPCReadGain(i)) + ",";
				else {
					// DAQ Block needs to be converted to a 16-Bit 2-Cs Int					
					double daqval = getData(data[fileID]);
					daqval = daqval/65536;
					if (daqval > 0x7FFF)
						daqval = -(((int)daqval ^ 0xFFFF) + 1);
					daqval = ((((((10000.0 / xml.getxPCReadGain(i))/32768.0) * daqval) * xml.getxPCReadVslope(i)) + xml.getxPCReadVoffset(i)) * xml.getxPCReadEUslope(i)) + xml.getxPCReadEUoffset(i);					
					s = s + daqval + ",";
				}
			}			
			
			// Throw away all Time columns except last file
			for (i = 0; i < data.length-1; i++) {
				getData(data[i]);
			}			
			// Skip the last line because it's garbage numbers
			if (isDone != true)
				out.println(s + getData(data[i]));
		}
		
		// Close files
		for (i = 0; i < data.length; i++) {
			try {data[i].close();} catch (Exception e) {e.printStackTrace();}
		}
		out.close();
		
		popup.setVisible(false);			
	}
	
	/** Read Binary Data into String (double) format *
	 * 
	 * @param data Data File
	 * @return Data value
	 */
	private double getData(DataInputStream data) {
		byte b[] = new byte[8];
		double value;
	     
	    try {
	    	for (int i = 0; i < 8; i++) 
	    		b[7-i] = data.readByte();	    		
    		ByteBuffer buf = ByteBuffer.wrap(b);
    		value = buf.getDouble();
    		return value;
    	} catch (Exception e) {isDone = true;} // Set isDone if end of file
	    
		return (double)9999999;					     
	}
	
	public static void main(String[] args) 
	 {
		new xPCDataConverter();
		System.exit(0);		
	 }
	
	// Variables
	private boolean isDone;
	private DataInputStream data[];
	private PrintStream out;
	private String outfile;
	private String xmlfile;
}