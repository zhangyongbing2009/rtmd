package edu.lehigh.nees.util.filefilter;

import java.io.File;

/********************************* 
 * HVSFileFilter
 * <p>
 * HVS FileFilter extension class
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 9 Aug 06  T. Marullo  Initial
 * 26 Jul 07  T. Marullo  Added getExtensionType and extended as from a new class
 *********************************/
public class HVSFileFilter extends FileFilterExtended {
	
	public boolean accept(File f)
	{
		if (f.isDirectory()) return true;
		String exten = getExtension(f);
		if (exten != null && exten.equalsIgnoreCase("hvs"))
				return true;
		return false;
	}

	public String getDescription()
	{ return "HVS files (*.hvs)"; }
			
	public String getExtensionType() {
		return ".hvs";
	}
}
    
    