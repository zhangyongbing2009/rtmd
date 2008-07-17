package edu.lehigh.nees.xml;

import java.io.FileOutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import edu.lehigh.nees.util.FileHandler;

/********************************* 
 * XSLTTransform
 * <p>
 * Transform XML using XML Stylesheet
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Mar 06  T. Marullo  Initial
 * 
 ********************************/
public class XSLTTransform {
	/** Create new XSLTTranform */
	public XSLTTransform() {		
	}
    
	/** Transform Source XML into Dest XML using XML Style Sheet *
	 * 
	 * @param xsl Style Sheet
	 * @param source Source XML
	 * @param dest Destination XML
	 */
	public void transform(String xsl, String source, String dest) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			System.out.println("Loading Stylesheet " + xsl);
			Transformer transformer = tFactory.newTransformer(new StreamSource(xsl));
			System.out.println("Transforming " + source + " into " + dest);
			transformer.transform(new StreamSource(source), new StreamResult(new FileOutputStream(dest)));
			System.out.println("Done");
		} catch (Exception e) {e.printStackTrace();}		
	}
	
	
	public static void main(String[] args) {	
		String stylesheetFileName;
		String xmlinputFileName;
		String xmloutputFileName;
		
		// Command line
		if (args.length == 3) {
			stylesheetFileName = args[0];	
			xmlinputFileName = args[1];
			xmloutputFileName = args[2];
		}
			
		// Graphical
		else {
			// Get the stylesheet file name    	
	        if ((stylesheetFileName = FileHandler.getFilePath("Open XML Stylesheet File")) == null)
	        	System.exit(1);
			
			// Get the input file name    	
	        if ((xmlinputFileName = FileHandler.getFilePath("Open Source XML File")) == null)
	        	System.exit(1);
	        
	    	// Get the output file name    	
	        if ((xmloutputFileName = FileHandler.getFilePath("Open Destination XML File")) == null)
	        	System.exit(1);
		}
        
        XSLTTransform t = new XSLTTransform();
		t.transform(stylesheetFileName,xmlinputFileName,xmloutputFileName);
	
		System.exit(0);
	}
}
