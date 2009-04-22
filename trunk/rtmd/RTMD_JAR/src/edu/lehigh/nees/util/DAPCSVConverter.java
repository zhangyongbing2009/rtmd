package edu.lehigh.nees.util;

import java.awt.BorderLayout;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/********************************* 
 * DAPCSVConverter
 * <p>
 * Convert between Servotest DAP ASCII file and CSV file
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 22 Mar 06  T. Marullo  Initial
 * 
 ********************************/
public class DAPCSVConverter {
	
    /** Creates a new instance of DAPCSVConverter */
    public DAPCSVConverter() {    	
    }

    
    /** Convert from CSV to DAP ASCII */
    public void convertCSVtoDAP(String inputFileName, String outputFileName) {        
    	// Open output file
        PrintStream outputFile = FileHandler.openOutputFile(outputFileName);
        
        // Get file information
        int numLines = FileHandler.getNumberOfLines(inputFileName);
        int freq = Integer.parseInt(JOptionPane.showInputDialog(null,"What is the frequency of the CSV File?","DAPCSVConverter",JOptionPane.OK_OPTION));        
        
        // Create a progress bar
        JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(0,0,100,25);
		JFrame popup = new JFrame("CSVDecimator");
		popup.setBounds(0,0,200,70);
		popup.setLocationRelativeTo(null);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(progressBar,BorderLayout.CENTER);
		JLabel label = new JLabel("Converting");
		label.setHorizontalAlignment(JLabel.CENTER);
		popup.getContentPane().add(label,BorderLayout.NORTH);
        try {
        	// Open file 
            BufferedReader input = FileHandler.openInputFile(inputFileName);        
            int i;
            int j = 0;
            String s;
            try {	
            	// Initialize progress bar
            	progressBar.setMaximum(numLines);
            	progressBar.setMinimum(0);
            	progressBar.setValue(j);
            	popup.setVisible(true);
                // Write freq
                outputFile.println(freq);
                // Write Length
                outputFile.println((numLines-1));
                                
				// Read header
				s = input.readLine();
				String[] cols = s.split(",");
				// Write # of columns
				outputFile.println((cols.length));
				// Write headers
				for (i = 0; i < cols.length; i++) {
					if (cols[i].length() > 20) 
						outputFile.println(cols[i].substring(0,19));					
					else 
						outputFile.println(cols[i]);
				}
				// Write equal amount of blanks (no units)
				for (i = 0; i < cols.length; i++) 
					outputFile.println("units");					
				// Read data and replace "," with " "
                while ((s = input.readLine()) != null) {					
					outputFile.println(s.replace(',',' '));
					// Increment progress bar
					progressBar.setValue(j++);
				}                 
            } catch (Exception e) {popup.setVisible(false);}
            try {
                input.close();
                outputFile.close();
                popup.setVisible(false);
            } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}
        } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}
            
        JOptionPane.showMessageDialog(null,"Conversion Complete");
    }   
    
    public void convertCSVtoDAPNoPopup(String inputFileName, String outputFileName, int freq) {        
    	// Open output file
        PrintStream outputFile = FileHandler.openOutputFile(outputFileName);
        
        // Get file information
        int numLines = FileHandler.getNumberOfLines(inputFileName);           
        
        // Create a progress bar
        JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(0,0,100,25);
		JFrame popup = new JFrame("CSV to DAP Conversion");
		popup.setBounds(0,0,200,70);
		popup.setLocationRelativeTo(null);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(progressBar,BorderLayout.CENTER);
		JLabel label = new JLabel("Converting");
		label.setHorizontalAlignment(JLabel.CENTER);
		popup.getContentPane().add(label,BorderLayout.NORTH);
        try {
        	// Open file 
            BufferedReader input = FileHandler.openInputFile(inputFileName);        
            int i;
            int j = 0;
            String s;
            try {	
            	// Initialize progress bar
            	progressBar.setMaximum(numLines);
            	progressBar.setMinimum(0);
            	progressBar.setValue(j);
            	popup.setVisible(true);
                // Write freq
                outputFile.println(freq);
                // Write Length
                outputFile.println((numLines-1));
                                
				// Read header
				s = input.readLine();
				String[] cols = s.split(",");
				// Write # of columns
				outputFile.println((cols.length));
				// Write headers
				for (i = 0; i < cols.length; i++) {
					if (cols[i].length() > 20) 
						outputFile.println(cols[i].substring(0,19));					
					else 
						outputFile.println(cols[i]);
				}
				// Write equal amount of blanks (no units)
				for (i = 0; i < cols.length; i++) 
					outputFile.println("units");					
				// Read data and replace "," with " "
                while ((s = input.readLine()) != null) {					
					outputFile.println(s.replace(',',' '));
					// Increment progress bar
					progressBar.setValue(j++);
				}                 
            } catch (Exception e) {popup.setVisible(false);}
            try {
                input.close();
                outputFile.close();
                popup.setVisible(false);
            } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}
        } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}      
        
        popup.setVisible(false);
    }   
    
    /** Convert from DAP ASCII to CSV */
    public void convertDAPtoCSV(String inputFileName, String outputFileName) {        
    	// Open output file
        PrintStream outputFile = FileHandler.openOutputFile(outputFileName);                  
        
        // Create a progress bar
        JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(0,0,100,25);
		JFrame popup = new JFrame("DAP to CSV Conversion");
		popup.setBounds(0,0,200,70);
		popup.setLocationRelativeTo(null);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(progressBar,BorderLayout.CENTER);
		JLabel label = new JLabel("Converting");
		label.setHorizontalAlignment(JLabel.CENTER);
		popup.getContentPane().add(label,BorderLayout.NORTH);
        try {
        	// Open file 
            BufferedReader input = FileHandler.openInputFile(inputFileName);        
            int i;
            int j = 0;
            String s;
            try {	
            	// Throw away first 12 lines
            	for (i = 0; i < 12; i++)
            		input.readLine();
            	
				// Read number of lines
				int numLines = Integer.parseInt(input.readLine().trim());
								
				// Read header
				s = input.readLine();					
				// Write header
				outputFile.println(s);

				// Read and throw away units				
				input.readLine();					
												
				// Initialize progress bar
            	progressBar.setMaximum(numLines);
            	progressBar.setMinimum(0);
            	progressBar.setValue(j);
            	popup.setVisible(true);
				// Read data and replace " " with ","
                while ((s = input.readLine()) != null) {					
					outputFile.println(s);
					// Increment progress bar
					progressBar.setValue(j++);
				}                 
            } catch (Exception e) {e.printStackTrace(); popup.setVisible(false);}
            try {
                input.close();
                outputFile.close();
                popup.setVisible(false);
            } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}
        } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}
            
        JOptionPane.showMessageDialog(null,"Conversion Complete");
    }  
  
    public static void main(String[] args) {
    	DAPCSVConverter converter = new DAPCSVConverter();

    	if ((Integer.parseInt(JOptionPane.showInputDialog(null,"1- Convert CSV to DAP\n2- Convert DAP to CSV","1"))) == 1) {    	
	    	// Get the input file name
	    	String inputFileName;
	        if ((inputFileName = FileHandler.getFilePath("Open CSV File")) == null)
	        	System.exit(1);
	    	
	    	// Get the output file name
	    	String outputFileName;
	        if ((outputFileName = FileHandler.getFilePath("Open .txt DAP File")) == null)
	        	System.exit(1);
	
	    	converter.convertCSVtoDAP(inputFileName, outputFileName);
    	}
    	else {    	
	    	// Get the input file name    
    		String inputFileName;
	        if ((inputFileName = FileHandler.getFilePath("Open DAP ASCII File")) == null)
	        	System.exit(1);
	    	
	    	// Get the output file name
	        String outputFileName;
	        if ((outputFileName = FileHandler.getFilePath("Open CSV File")) == null)
	        	System.exit(1);
	
	    	converter.convertDAPtoCSV(inputFileName, outputFileName);
    	}
        System.exit(0);
    }    
}
