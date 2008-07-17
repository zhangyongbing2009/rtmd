package edu.lehigh.nees.util.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/********************************* 
 * FileFilterExtended
 * <p>
 * Abstract class for extended FileFilter so it has a method to return the extension
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br> 
 * 26 Jul 07  T. Marullo  Initial
 *********************************/
public abstract class FileFilterExtended extends FileFilter {
			
	/** Accept the file name based on extension (should override) */
	public boolean accept(File f)
	{
		if (f.isDirectory()) return true;
		String exten = getExtension(f);
		if (exten != null && exten.equalsIgnoreCase("*"))
				return true;
		return false;
	}
	
	/** Shows the description of the file types in the dialog box */
	public abstract String getDescription();
	
	/** Get extension of a file */
	public String getExtension(File f)
	{
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1)
			ext = s.substring(i+1).toLowerCase();
		return ext;
	}
	
	/** Returns the extension for the file */
	public abstract String getExtensionType();
}
