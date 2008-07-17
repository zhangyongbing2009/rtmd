package edu.lehigh.nees.util.filefilter;

import java.io.File;

/********************************* 
 * TXTFileFilter
 * <p>
 * Text FileFilter extension class
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 12 Jul 06  T. Marullo  Initial
 * 26 Jul 07  T. Marullo  Added getExtensionType and extended as from a new class
 *********************************/
public class TXTFileFilter extends FileFilterExtended {
	public boolean accept(File f)
	{
		if (f.isDirectory()) return true;
		String exten = getExtension(f);
		if (exten != null && exten.equalsIgnoreCase("txt"))
				return true;
		return false;
	}

	public String getDescription()
	{ return "Text files (*.txt)"; }
	
	public String getExtensionType() {
		return ".txt";
	}
}