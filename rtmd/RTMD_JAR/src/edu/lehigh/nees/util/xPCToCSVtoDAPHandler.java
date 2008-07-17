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
 * 18 Dec 08  T. Marullo  Initial
 *  
 * 
 ********************************/
public class xPCToCSVtoDAPHandler {
	public static void main(String[] args) {		
		File xpcfile = new File(args[0]);
		String abspath = xpcfile.getAbsolutePath();
		String curpath = abspath.substring(0,abspath.lastIndexOf("\\"));
		System.out.println(abspath);	
		System.out.println(curpath);
		xPCDataConverter xpctocsv = new xPCDataConverter(abspath,"results.csv");
		xpctocsv.convertNoPopup();
		
		System.out.println("CSV Conversion Done");
		
		String csvfilename = new String(curpath + "\\results.csv");
		String txtfilename = new String(curpath + "\\results.txt");
		DAPCSVConverter csvtodap = new DAPCSVConverter();
		csvtodap.convertCSVtoDAPNoPopup(csvfilename, txtfilename, 1024);
	}
}
