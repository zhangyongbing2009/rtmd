package edu.lehigh.nees.util.filefilter;

import java.io.File;

/********************************* 
 * XMLFileFilter
 * <p>
 * XML FileFilter extension class
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 12 Jul 06  T. Marullo  Initial
 * 26 Jul 07  T. Marullo  Added getExtensionType and extended as from a new class
 *********************************/
public class XMLFileFilter extends FileFilterExtended  {

	public boolean accept(File f)
	{
		if (f.isDirectory()) return true;
		String exten = getExtension(f);
		if (exten != null && exten.equalsIgnoreCase("xml"))
				return true;
		return false;
	}
	
	public String getDescription()
	{ return "XML files (*.xml)"; }
		
	public String getExtensionType() {
		return ".xml";
	}
}
    
    