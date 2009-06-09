package edu.lehigh.nees.util;

import java.io.*;

/********************************* 
 * xPCToCSVtoDAPHandler
 * <p>
 * Converts xPC files to both CSV and DAP quickly
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 18 Dec 07  T. Marullo  Initial
 *  1 Dec 08  T. Marullo  Added System.exit to resolve orphaned java process
 *  
 * 
 ********************************/
public class xPCToCSVtoDAPHandler {
	public static void main(String[] args) {
		new xPCToCSVtoDAPHandler(new File(args[0]));
		
		System.exit(0);
	}	
	
	public xPCToCSVtoDAPHandler(File xpcfile) {
		// Get the xPC xml file		
		String abspath = xpcfile.getAbsolutePath();
		String curpath = abspath.substring(0,abspath.lastIndexOf("\\"));
		
		// Convert RAW XPC data to a CSV file
		xPCDataConverter xpctocsv = new xPCDataConverter(abspath,"results.csv");
		xpctocsv.convertNoPopup();
		xpctocsv = null;
		
		// Convert CSV file to DAP file
		String csvfilename = new String(curpath + "\\results.csv");
		String txtfilename = new String(curpath + "\\results.txt");
		DAPCSVConverter csvtodap = new DAPCSVConverter();
		csvtodap.convertCSVtoDAPNoPopup(csvfilename, txtfilename, 1024);		
		csvtodap = null;	
	}
}
