package edu.lehigh.nees.util;

import java.io.*;
import javax.swing.*;
import edu.lehigh.nees.util.filefilter.FileFilterExtended;

/********************************* 
 * FileHandler
 * <p>
 * Methods commonly used to handle files
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 22 Mar 06  T. Marullo  Initial
 * 15 Jun 06  T. Marullo  Changed JFileChooser to EFileChooser for its enhancements
 * 29 Jun 06  T. Marullo  Allowed both JFileChooser and EFileChooser where the function ends with an 'E' for the enhanced version
 * 12 Jul 06  T. Marullo  Added FileFilter class as input for graphic Open/Saves
 * 28 Jul 06  T. Marullo  Added Recent Path saving
 *  6 Apr 07  T. Marullo  Added getNumberOfColumns for a CSV file
 * 26 Jul 07  T. Marullo  Changed FileFilter to FileFilterExtended
 ********************************/
public class FileHandler {		
	private static String recentPath = System.getProperty("user.home");
	
	public static void setPath (String path) {
		FileHandler.recentPath = path;
	}
	
	/** Get number of columns from a CSV file
	 * 
	 * @return number of columns or -1 if file not found
	 */
	public static int getNumberOfColumns(String filename) {  		
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
            int i = -1;
            try {
                String[] ss = input.readLine().split(",");
                i = ss.length;
            } catch (Exception e) {e.printStackTrace();};            
            try {
                input.close();                
            } catch (Exception e) {e.printStackTrace();}
            return i;
        } catch (Exception e) {e.printStackTrace();}
        return -1;
    }
	
	/** Read the number of lines a file has *
	 * 
	 * @return number of lines or -1 if file not found
	 */
	public static int getNumberOfLines(String filename) {  
		ProgressPopup progressBar = new ProgressPopup("Converting", "Getting File Length");
		progressBar.setIndeterminate(true);
		Thread progressBarThread = new Thread(progressBar);
		progressBarThread.start();		
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));        
            int i = 0;   
            try {
                while (input.readLine() != null) 
                    i++;
            } catch (Exception e) {e.printStackTrace();progressBar.setIsDone(true);};            
            try {
                input.close();                
            } catch (Exception e) {e.printStackTrace();progressBar.setIsDone(true);}
            progressBar.setIsDone(true);
            return i;            
        } catch (Exception e) {e.printStackTrace();progressBar.setIsDone(true);}
        return -1;
    }
	
	/** Open a file to write to with a Graphical file chooser *
	 *  
	 * @param title Title of dialog box 
	 * @return PrintStream object 
	 */
    public static PrintStream chooseOutputFile(String title) {
        // Open file
        try{
        	// Choose a file
            JFileChooser fc = new JFileChooser(recentPath);        	
            int returnVal = fc.showDialog(null,title);
            // If the file dialog box was cancelled, return null
            if (returnVal == JFileChooser.APPROVE_OPTION) { 
            	FileHandler.recentPath = fc.getSelectedFile().getAbsolutePath();
                return (new PrintStream(new FileOutputStream(fc.getSelectedFile())));                
            }
            else
            	return null;            
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
	/** Open a file to write to with a Graphical file chooser and file filter *
	 *  
	 * @param title Title of dialog box 
	 * @param filter File Filter
	 * @return PrintStream object 
	 */
    public static PrintStream chooseOutputFile(String title, FileFilterExtended filter) {
        // Open file
        try{
        	// Choose a file
            JFileChooser fc = new JFileChooser(recentPath);   
            fc.setFileFilter(filter);
            int returnVal = fc.showDialog(null,title);
            // If the file dialog box was cancelled, return null
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	FileHandler.recentPath = fc.getSelectedFile().getAbsolutePath();            
            	if (fc.getSelectedFile().exists())
            		return (new PrintStream(new FileOutputStream(fc.getSelectedFile())));
            	else
            		return (new PrintStream(new FileOutputStream(fc.getSelectedFile() + filter.getExtensionType())));
            }
            else
            	return null;            
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }        
    
	/** Open a file to write to *
	 *  
	 * @param filename 
	 * @return PrintStream object 
	 */
    public static PrintStream openOutputFile(String filename) {
        // Open file
        try{
        	return (new PrintStream(new FileOutputStream(new File(filename))));                       
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }        
    }
    
    /** Open a file to read from with a Graphical file chooser *
     * 
     * @param title Title of dialog box
     * @return BufferedReader object
     */
    public static BufferedReader chooseInputFile(String title) {
        // Open file
        try{
            JFileChooser fc = new JFileChooser(recentPath);        	
            int returnVal = fc.showDialog(null,title);
            // If the file dialog box was cancelled, return null
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	FileHandler.recentPath = fc.getSelectedFile().getAbsolutePath();
                return (new BufferedReader(new InputStreamReader(new FileInputStream(fc.getSelectedFile()))));
            }
            else
            	return null;                        
        } catch (Exception e) {
            System.err.println(e);
        } 
        return null;
    }
    
    /** Open a file to read from with a Graphical file chooser and a File filter *
     * 
     * @param title Title of dialog box
     * @param filter File filter
     * @return BufferedReader object
     */
    public static BufferedReader chooseInputFile(String title, FileFilterExtended filter) {
        // Open file
        try{
            JFileChooser fc = new JFileChooser(recentPath);  
            fc.setFileFilter(filter);
            int returnVal = fc.showDialog(null,title);
            // If the file dialog box was cancelled, return null
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	FileHandler.recentPath = fc.getSelectedFile().getAbsolutePath();
            	return (new BufferedReader(new InputStreamReader(new FileInputStream(fc.getSelectedFile()))));            	
            }
            else
            	return null;                        
        } catch (Exception e) {
            System.err.println(e);
        } 
        return null;
    }
        
	/** Open a file to read from *
	 *  
	 * @param filename 
	 * @return BufferedReader object 
	 */
    public static BufferedReader openInputFile(String filename) {
        // Open file
        try{
        	return (new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)))));                       
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }        
    }
    
    /** Get the path to a selected File *
     * 
     * @param title Title of dialog
     * @return String filename
     */
    public static String getFilePath(String title) {
        // Open file
        try{
            JFileChooser fc = new JFileChooser(recentPath);        	
            int returnVal = fc.showDialog(null,title);
            // If the file dialog box was cancelled, return null
            File file = null;
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
        		FileHandler.recentPath = fc.getSelectedFile().getAbsolutePath();
    			file = fc.getSelectedFile();
        	}
        	else
        		return null;
            return file.getAbsolutePath();
        } catch (Exception e) {
            System.err.println(e);
        } 
        return null;
    }
    
    /** Get the path to a selected File with a File filter *
     * 
     * @param title Title of dialog
     * @param filter File filter
     * @return String filename
     */
    public static String getFilePath(String title, FileFilterExtended filter) {
        // Open file
        try{
            JFileChooser fc = new JFileChooser(recentPath);   
            fc.setFileFilter(filter);
            int returnVal = fc.showDialog(null,title);
            // If the file dialog box was cancelled, return null
            File file = null;
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
        		FileHandler.recentPath = fc.getSelectedFile().getAbsolutePath();
    			file = fc.getSelectedFile();
        	}
        	else
        		return null;
        	if (file.exists())
        		return file.getAbsolutePath();
        	else
        		return file.getAbsolutePath() + filter.getExtensionType();
        } catch (Exception e) {
            System.err.println(e);
        } 
        return null;
    }        
}