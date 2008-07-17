package edu.lehigh.nees.util.filefilter;

import java.io.File;

/********************************* 
 * CSVFileFilter
 * <p>
 * CSV FileFilter extension class
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  6 Apr 07  T. Marullo  Initial
 *  26 Jul 07  T. Marullo  Added getExtensionType and extended as from a new class
 *********************************/
public class CSVFileFilter extends FileFilterExtended {
	public boolean accept(File f)
	{
		if (f.isDirectory()) return true;
		String exten = getExtension(f);
		if (exten != null && exten.equalsIgnoreCase("csv"))
				return true;
		return false;
	}

	public String getDescription()
	{ return "CSV files (*.csv)"; }
	
	public String getExtensionType() {
		return ".csv";
	}
}
    
    