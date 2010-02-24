package edu.lehigh.nees.IntegratedControl.SCCBF;

import edu.lehigh.nees.util.filefilter.CSVFileFilter;
import edu.lehigh.nees.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/********************************* 
 * CSVDecimator
 * <p>
 * Decimate a CSV file
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 22 Mar 06  T. Marullo  Initial
 *  6 Apr 07  T. Marullo  Converted to Applet
 *  6 Nov 08  T. Marullo  Allowed to run as Application
 * 
 ********************************/
public class SCCBFdataProcessor extends JFrame implements ActionListener {
	
    private static final long serialVersionUID = 3492250886789702570L;
    
	/** Creates a new instance of SCCBFdataProcessor */
    public SCCBFdataProcessor() { 
    	super("SC-CBF Data Processor");
    	
    	// Initialize the decimate variable to 1 lines (no decimating) 
        decimate = 1;
        linesToSkip = 0;       
        init();
        this.setVisible(true);
    }
    
    public void init() {    	
    	this.setSize(225,270);
    	this.getContentPane().setLayout(null);    	
    	this.setLocationRelativeTo(null);
    	
    	// CSV File 
    	openCSVFileButton = new JButton("Open CSV File");
    	openCSVFileButton.setBounds(10,10,200,25);
    	openCSVFileButton.addActionListener(this);
    	openCSVFileButton.setActionCommand("open");
    	this.getContentPane().add(openCSVFileButton);
    	
    	// Analyze
    	analyzeButton = new JButton("Analyze CSV File");
    	analyzeButton.setBounds(10,40,200,25);
    	analyzeButton.setActionCommand("analyze");
    	analyzeButton.addActionListener(this);
    	this.getContentPane().add(analyzeButton);
    	
    	// Decimation value
    	JLabel label = new JLabel("Decimation value: ");
    	label.setBounds(10,70,150,25);
    	this.getContentPane().add(label);
    	decimateTextField = new JTextField("1");    	
    	decimateTextField.setBounds(150,70,50,25);
    	decimateTextField.setHorizontalAlignment(JTextField.RIGHT);    	    	
    	this.getContentPane().add(decimateTextField);
    	
    	// Lines to skip value
    	label = new JLabel("Initial lines to skip: ");
    	label.setBounds(10,100,150,25);
    	this.getContentPane().add(label);
    	linesToSkipTextField = new JTextField("1");    	
    	linesToSkipTextField.setBounds(150,100,50,25);
    	linesToSkipTextField.setHorizontalAlignment(JTextField.RIGHT);    	
    	this.getContentPane().add(linesToSkipTextField);
    	
    	// Columns to keep
    	label = new JLabel("Columns to keep: ");
    	label.setBounds(10,130,150,25);
    	this.getContentPane().add(label);
    	columnsToIncludeTextField = new JTextField("");    	
    	columnsToIncludeTextField.setBounds(150,130,50,25);
    	columnsToIncludeTextField.setHorizontalAlignment(JTextField.RIGHT);    	
    	this.getContentPane().add(columnsToIncludeTextField);
    	
    	// Output File
    	openOutputFileButton = new JButton("Set Output File");
    	openOutputFileButton.setBounds(10,160,200,25);
    	openOutputFileButton.setActionCommand("set");
    	openOutputFileButton.addActionListener(this);
    	this.getContentPane().add(openOutputFileButton);
    	
    	// Decimate
    	decimateButton = new JButton("Decimate");
    	decimateButton.setBounds(10,190,200,25);
    	decimateButton.setActionCommand("decimate");
    	decimateButton.addActionListener(this);
    	this.getContentPane().add(decimateButton);    	       
    }
    
    /** Action Event handler for this class *
     */
    public void actionPerformed(ActionEvent arg0) {    	
    	if (arg0.getActionCommand().equals("open"))	
    		inputFileName = FileHandler.getFilePath("Open Input CSV File",new CSVFileFilter());
    	
    	if (arg0.getActionCommand().equals("analyze"))	
    		analyze();
    	
    	if (arg0.getActionCommand().equals("set"))
    		outputFileName = FileHandler.getFilePath("Set Output CSV File",new CSVFileFilter());
    		
		if (arg0.getActionCommand().equals("decimate"))	
			decimate();
	}      

    /** Read the length of the CSV file and set decimation value *
     */
    private void analyze() {
    	if (inputFileName == null) {
    		JOptionPane.showMessageDialog(null,"Open CSV file first");
    		return;
    	}
    	
        // Check if the CSV file has a header and skip it when decimating
    	if (JOptionPane.showConfirmDialog(null,"Does this input file have a header?","CSVDecimator",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
    		linesToSkip = 1;
    	else
    		linesToSkip = 0;
    	linesToSkipTextField.setText(Integer.toString(linesToSkip));
    	
    	// Get number of columns
    	int numColumns = FileHandler.getNumberOfColumns(inputFileName);
    	columnsToInclude = "";
    	for (int i = 0; i < numColumns-1; i++)
    		columnsToInclude += Integer.toString(i+1) + ",";    	
    	columnsToInclude += Integer.toString(numColumns);
    	columnsToIncludeTextField.setText(columnsToInclude);
    	
    	// Get number of lines
		numLines = FileHandler.getNumberOfLines(inputFileName);
		decimate = Integer.parseInt(JOptionPane.showInputDialog(null,"File has " + numLines + " lines.  Excel supports 32000 max lines for graphs.\nRecommended Decimation value = " + (1+(long)((double)numLines/(double)32000)),(1+(long)((double)numLines/(double)32000))));
		decimateTextField.setText(Integer.toString(decimate));
		
		wasAnalyzed = true;
    }
    
    /** Decimate the file and only include certain columns *
     * 
     * @param columns comma delimited list of columns to include 
     */
    public void decimate() {
    	if (inputFileName == null || outputFileName == null || wasAnalyzed == false) {
    		JOptionPane.showMessageDialog(null,"You must open and set the CSV files and analyze the input file first");
    		return;
    	}
    	
    	decimate = Integer.parseInt(decimateTextField.getText());
    	linesToSkip = Integer.parseInt(linesToSkipTextField.getText());
    	columnsToInclude = columnsToIncludeTextField.getText().trim();    	
    	
    	// Get columns
    	String[] cols = columnsToInclude.split(",");
    	    	
    	// Open output file
        PrintStream outputFile = FileHandler.openOutputFile(outputFileName);
        
        // Create a progress bar
        JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(0,0,100,25);
		JFrame popup = new JFrame("CSVDecimator");
		popup.setBounds(0,0,200,70);
		popup.setLocationRelativeTo(null);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(progressBar,BorderLayout.CENTER);
		JLabel label = new JLabel("Decimating");
		label.setHorizontalAlignment(JLabel.CENTER);
		popup.getContentPane().add(label,BorderLayout.NORTH);
        try {
        	// Open file 
            BufferedReader input = FileHandler.openInputFile(inputFileName);        
            int i = 0;
            String s;
            String s_new;
            String[] ss;
            try {	
            	// Initialize progress bar
            	progressBar.setMaximum(numLines);
            	progressBar.setMinimum(0);
            	progressBar.setValue(i);
            	popup.setVisible(true);
            	// Read each line
                while ((s = input.readLine()) != null) {
                	s_new = "";
                	// Split into each column
                	ss = s.split(",");
                	// Number of lines to skip
                    if (i < linesToSkip) {                       
                    	for (int k = 0; k < cols.length; k++)
                    		s_new = s_new + ss[Integer.parseInt(cols[k].trim())-1] + ",";
                    	outputFile.println(s_new);
                    }
                    else {
                    	// Output the modulus of the decimation value
                        if (i % decimate == 0) {                    
                        	for (int k = 0; k < cols.length; k++)
                        		s_new = s_new + ss[Integer.parseInt(cols[k].trim())-1] + ",";
                        	outputFile.println(s_new);
                        }                
                    }
                    i++;   
                    progressBar.setValue(i);
                }           
            } catch (Exception e) {popup.setVisible(false);}
            try {
                input.close();
                outputFile.close();
                popup.setVisible(false);
            } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}
        } catch (Exception e) {e.printStackTrace();popup.setVisible(false);}
            
        JOptionPane.showMessageDialog(null,"Decimation Complete");
    }            
    
    public static void main(String args[]) {
    	try {
	    	// Load file once
	    	String inputFileName = FileHandler.getFilePath("Open Input CSV File",new CSVFileFilter());
	    	BufferedReader input = FileHandler.openInputFile(inputFileName);        
	    	int numLines = FileHandler.getNumberOfLines(inputFileName);	    		    
	    	
	    	// Get step column
	    	int stepColumn = 196;
	    	
	    	// Get path
	    	String path = inputFileName.substring(0,inputFileName.lastIndexOf("\\")+1);
	    	
	    	// Load each output files and column data
	    	PrintStream plasticSlides = FileHandler.openOutputFile(path + "Plastic Slides.csv");
	    	int[] plasticSlidesCols = {1,2,3,4,5,6,7,8,196,219};		    	
	    	PrintStream substructureDisplacements = FileHandler.openOutputFile(path + "Substructure Displacements.csv");
	    	int[] substructureDisplacementsCols = {83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,107,108,109,110,196,219};	    	
	    	PrintStream actuatorDisp = FileHandler.openOutputFile(path + "Actuator_North_South Column Displacements.csv");
	    	int[] actuatorDispCols = {9,13,17,21,203,205,207,209,211,212,213,214,215,216,217,218,196,219};	    	
	    	PrintStream middle = FileHandler.openOutputFile(path + "Middle Floor LVDTs.csv");
	    	int[] middleCols = {10,11,12,14,15,16,18,19,20,22,23,24,196,219};	    	
	    	PrintStream subFriction = FileHandler.openOutputFile(path + "Substructure_Friction Device Load Cells.csv");
	    	int[] subFrictionCols = {25,26,27,28,29,30,31,32,33,34,196,219};	    	
	    	PrintStream actuatorForces = FileHandler.openOutputFile(path + "Actuator Forces_PT Load Cells.csv");
	    	int[] actuatorForcesCols = {35,36,37,38,39,40,204,206,208,210,196,219};	    	
	    	PrintStream fullBridges = FileHandler.openOutputFile(path + "Full Bridges.csv");
	    	int[] fullBridgesCols = {41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,196,219};	    	
	    	PrintStream groundStrain = FileHandler.openOutputFile(path + "Ground Level Strain Gauges.csv");
	    	int[] groundStrainCols = {67,68,69,70,71,72,73,74,75,76,125,126,127,128,129,130,131,132,133,134,151,152,153,154,196,219};	    	
	    	PrintStream floor1strain = FileHandler.openOutputFile(path + "Floor 1 Strain Gauges.csv");
	    	int[] floor1strainCols = {77,78,79,80,81,82,111,112,113,114,115,116,117,118,119,120,121,122,123,124,196,219};
	    	PrintStream floor3strain = FileHandler.openOutputFile(path + "Floor 3 Strain Gauges.csv");
	    	int[] floor3strainCols = {135,136,137,138,139,140,141,142,143,144,177,178,179,180,181,182,183,184,185,186,196,219};
	    	PrintStream floor4strain = FileHandler.openOutputFile(path + "Floor 4 Strain Gauges.csv");
	    	int[] floor4strainCols = {145,146,147,148,149,150,163,164,165,166,167,168,169,170,171,172,173,174,176,196,219};	    	
	    	PrintStream convergedData = FileHandler.openOutputFile(path + "Converged Data.csv");	    	
	    	
	    	// Read data
	    	int i = 0;
	    	int k = 0;
	    	int step = 0;	    	
            String s;            
            String s_new;
            String s_prev = "";
            String[] cols;            
            // Read each line
            while ((s = input.readLine()) != null) {
            	System.out.println("Line " + i++ + " / " + numLines);
            	// Split into each column
            	cols = s.split(",");            	
            	
            	// Plastic Slides
            	s_new = "";
            	for (k = 0; k < plasticSlidesCols.length; k++)
            		s_new = s_new + cols[plasticSlidesCols[k]-1] + ",";
            	plasticSlides.println(s_new);
            	
            	// Substructure Displacements
            	s_new = "";
            	for (k = 0; k < substructureDisplacementsCols.length; k++)
            		s_new = s_new + cols[substructureDisplacementsCols[k]-1] + ",";
            	substructureDisplacements.println(s_new);
            	
            	// Actuator and Floor Displacements 
            	s_new = "";
            	for (k = 0; k < actuatorDispCols.length; k++)
            		s_new = s_new + cols[actuatorDispCols[k]-1] + ",";
            	actuatorDisp.println(s_new);
            	
            	// Middle LVDTs
            	s_new = "";
            	for (k = 0; k < middleCols.length; k++)
            		s_new = s_new + cols[middleCols[k]-1] + ",";
            	middle.println(s_new);
            	
            	// Substructure Friction Devices
            	s_new = "";
            	for (k = 0; k < subFrictionCols.length; k++)
            		s_new = s_new + cols[subFrictionCols[k]-1] + ",";
            	subFriction.println(s_new);
            	
            	// Actuator Forces
            	s_new = "";
            	for (k = 0; k < actuatorForcesCols.length; k++)
            		s_new = s_new + cols[actuatorForcesCols[k]-1] + ",";
            	actuatorForces.println(s_new);
            	
            	// Full Bridges
            	s_new = "";
            	for (k = 0; k < fullBridgesCols.length; k++)
            		s_new = s_new + cols[fullBridgesCols[k]-1] + ",";
            	fullBridges.println(s_new);
            	
            	// Ground Strain Gauges
            	s_new = "";
            	for (k = 0; k < groundStrainCols.length; k++)
            		s_new = s_new + cols[groundStrainCols[k]-1] + ",";
            	groundStrain.println(s_new);
            	
            	// Floor 1 Strain
            	s_new = "";
            	for (k = 0; k < floor1strainCols.length; k++)
            		s_new = s_new + cols[floor1strainCols[k]-1] + ",";
            	floor1strain.println(s_new);
            	
            	// Floor 3 Strain
            	s_new = "";
            	for (k = 0; k < floor3strainCols.length; k++)
            		s_new = s_new + cols[floor3strainCols[k]-1] + ",";
            	floor3strain.println(s_new);
            	
            	// Floor 4 Strain
            	s_new = "";
            	for (k = 0; k < floor4strainCols.length; k++)
            		s_new = s_new + cols[floor4strainCols[k]-1] + ",";
            	floor4strain.println(s_new);
            	
            	// Converged Data            	
            	if (i == 1) {
            		convergedData.println(s);
            		s_prev = s;
            	}
            	else {            		
        			if (step != (int)(Double.parseDouble(cols[stepColumn-1]))) {
        				convergedData.println(s_prev);
        				step++;
            		}
            	}    
            	
            	// Save previous line for Convergence data
            	s_prev = s;
            }
	    	
	    	// Close all files
	    	input.close();	    	
	    	plasticSlides.close();	    	
	    	substructureDisplacements.close();	    	
	    	actuatorDisp.close();	    	    
	    	middle.close();
	    	subFriction.close();
	    	actuatorForces.close();
	    	fullBridges.close();
	    	groundStrain.close();
	    	floor1strain.close();
	    	floor3strain.close();
	    	floor4strain.close();	    		  	    	
	    	convergedData.close();
	    	
	    	JOptionPane.showMessageDialog(null,"Process Completed");
	    	
    	} catch (Exception e) {e.printStackTrace();}
    	
    	System.exit(0);
    }

    
    // Variables    
    private int numLines;     
    private boolean wasAnalyzed = false;
    private JButton openCSVFileButton;
    private String inputFileName;    
    private JButton analyzeButton;
    private JButton openOutputFileButton;
    private String outputFileName;
    private JTextField decimateTextField;
    private int decimate;
    private JTextField linesToSkipTextField;
    private int linesToSkip;
    private JTextField columnsToIncludeTextField;
    private String columnsToInclude;
    private JButton decimateButton;
}
