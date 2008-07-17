package edu.lehigh.nees.IntegratedControl.Vanadium;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Properties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.lehigh.nees.scramnet.*;
import edu.lehigh.nees.util.CSVWriter;
import edu.lehigh.nees.util.FileHandler;
import edu.lehigh.nees.util.filefilter.XMLFileFilter;
import edu.lehigh.nees.xml.ReadDAQXMLConfig;

/********************************* 
 * MainGUI
 * <p>
 * Main GUI for the Vanadium Steel Project
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 29 Sep 06  T. Marullo  Initial 
 * 25 Oct 06  T. Marullo  Removed Minimum Increment Size and connected the actuators to the increment spinners
 * 
 ********************************/
public class MainGUI extends JFrame implements Runnable, ActionListener, ChangeListener  {
	private int DEBUG = 0;
	
	/** Load Properties File **/
	private void loadProperties() {
		properties = new Properties();		
		try {
	        properties.load(new FileInputStream("vanadium.properties"));
	        
	        // Ask user if they would like to use the existing properties
	        // And show the properties
	        String s = "";
	        s = s + "EAST_DISP_CMD_ADDR:  " + properties.getProperty("EAST_DISP_CMD_ADDR") + "\n";	        	       
	        s = s + "WEST_DISP_CMD_ADDR:  " + properties.getProperty("WEST_DISP_CMD_ADDR") + "\n";
	        s = s + "EAST_DISP_FBK_ADDR:  " + properties.getProperty("EAST_DISP_FBK_ADDR") + "\n";
	        s = s + "EAST_LOAD_FBK_ADDR:  " + properties.getProperty("EAST_LOAD_FBK_ADDR") + "\n";
	        s = s + "EAST_LOAD_SCALE:  " + properties.getProperty("EAST_LOAD_SCALE") + "\n";
	        s = s + "WEST_DISP_FBK_ADDR:  " + properties.getProperty("WEST_DISP_FBK_ADDR") + "\n";
	        s = s + "WEST_LOAD_FBK_ADDR:  " + properties.getProperty("WEST_LOAD_FBK_ADDR") + "\n";
	        s = s + "WEST_LOAD_SCALE:  " + properties.getProperty("WEST_LOAD_SCALE") + "\n";
	        s = s + "DISP_SCALE:  " + properties.getProperty("DISP_SCALE") + "\n";
	        //s = s + "MINIMUM_INCREMENT_SIZE:  " + properties.getProperty("MINIMUM_INCREMENT_SIZE") + "\n";
	        s = s + "DEBUG:  " + properties.getProperty("DEBUG") + "\n";
	        	        
	        int response = JOptionPane.showConfirmDialog(null,"Would you like to use the existing properties?\n" + s,"Confirm", JOptionPane.YES_NO_OPTION);
	        if (response == JOptionPane.YES_OPTION) {
	        	// The user wants to use the current Properties so set them
	        	EAST_DISP_CMD_ADDR = Integer.parseInt(properties.getProperty("EAST_DISP_CMD_ADDR"));	        	       
		        WEST_DISP_CMD_ADDR = Integer.parseInt(properties.getProperty("WEST_DISP_CMD_ADDR"));
		        EAST_DISP_FBK_ADDR = Integer.parseInt(properties.getProperty("EAST_DISP_FBK_ADDR"));
		        EAST_LOAD_FBK_ADDR = Integer.parseInt(properties.getProperty("EAST_LOAD_FBK_ADDR"));
		        EAST_LOAD_SCALE = Integer.parseInt(properties.getProperty("EAST_LOAD_SCALE"));
		        WEST_DISP_FBK_ADDR = Integer.parseInt(properties.getProperty("WEST_DISP_FBK_ADDR"));
		        WEST_LOAD_FBK_ADDR = Integer.parseInt(properties.getProperty("WEST_LOAD_FBK_ADDR"));
		        WEST_LOAD_SCALE = Integer.parseInt(properties.getProperty("WEST_LOAD_SCALE"));
		        DISP_SCALE = Integer.parseInt(properties.getProperty("DISP_SCALE"));
		        //MINIMUM_INCREMENT_SIZE = Double.parseDouble(properties.getProperty("MINIMUM_INCREMENT_SIZE"));
		        DEBUG = Integer.parseInt(properties.getProperty("DEBUG"));
	        }
	        else 
	        	// Set new properties
	        	setProperties();	        	        	        
	    } catch (IOException e) {
	    	// No properties file exists, so create sone.
	    	setProperties();	    	
	    }	    	   
	}
	
	/** Set properties */
	private void setProperties() {
		// No properties set, get user input
    	EAST_DISP_CMD_ADDR = Integer.parseInt(JOptionPane.showInputDialog("East Actuator Disp. Command Address?",1));
    	properties.setProperty("EAST_DISP_CMD_ADDR",Integer.toString(EAST_DISP_CMD_ADDR));
    	WEST_DISP_CMD_ADDR = Integer.parseInt(JOptionPane.showInputDialog("West Actuator Disp. Command Address?",6));
    	properties.setProperty("WEST_DISP_CMD_ADDR",Integer.toString(WEST_DISP_CMD_ADDR));
    	EAST_DISP_FBK_ADDR = Integer.parseInt(JOptionPane.showInputDialog("East Actuator Disp. Feedback Address?",66));
    	properties.setProperty("EAST_DISP_FBK_ADDR",Integer.toString(EAST_DISP_FBK_ADDR));
    	EAST_LOAD_FBK_ADDR = Integer.parseInt(JOptionPane.showInputDialog("East Actuator Load Feedback Address?",67));
    	properties.setProperty("EAST_LOAD_FBK_ADDR",Integer.toString(EAST_LOAD_FBK_ADDR));
    	EAST_LOAD_SCALE = Integer.parseInt(JOptionPane.showInputDialog("East Actuator Load Scale?",2000));
    	properties.setProperty("EAST_LOAD_SCALE",Integer.toString(EAST_LOAD_SCALE));
    	WEST_DISP_FBK_ADDR = Integer.parseInt(JOptionPane.showInputDialog("West Actuator Disp. Feedback Address?",68));
    	properties.setProperty("WEST_DISP_FBK_ADDR",Integer.toString(WEST_DISP_FBK_ADDR));
    	WEST_LOAD_FBK_ADDR = Integer.parseInt(JOptionPane.showInputDialog("West Actuator Load Feedback Address?",69));
    	properties.setProperty("WEST_LOAD_FBK_ADDR",Integer.toString(WEST_LOAD_FBK_ADDR));
    	WEST_LOAD_SCALE = Integer.parseInt(JOptionPane.showInputDialog("West Actuator Load Scale?",2000));
    	properties.setProperty("WEST_LOAD_SCALE",Integer.toString(WEST_LOAD_SCALE));
    	DISP_SCALE = Integer.parseInt(JOptionPane.showInputDialog("Actuator Displacement Scale?",500));
    	properties.setProperty("DISP_SCALE",Integer.toString(DISP_SCALE));
    	//MINIMUM_INCREMENT_SIZE = Double.parseDouble(JOptionPane.showInputDialog("Minimum Increment Size (inches)?",0.001));
    	//properties.setProperty("MINIMUM_INCREMENT_SIZE",Double.toString(MINIMUM_INCREMENT_SIZE));
    	DEBUG = Integer.parseInt(JOptionPane.showInputDialog("Debug? (0=no, 1=yes)",0));
    	properties.setProperty("DEBUG",Integer.toString(DEBUG));
    	
    	
    	// Write properties file.
	    try {
	        properties.store(new FileOutputStream("vanadium.properties"), null);
	    } catch (IOException e2) {}
	}
	
	/** Constructor 
	 * @param scr SCRAMNet driver
	 **/
	public MainGUI(ScramNetIO scr) {
		super("RTMD: Vanadium Steel");  
		loadProperties();
		
		// Initialize the GUI
		init();
        
        // Get SCRAMNet device
        this.scr = scr;
        
        // Get the DAQ SCRAMNet information        
        useDAQ = JOptionPane.showConfirmDialog(null,"Would you like to include the DAQ SCRAMnet information?","Confirm", JOptionPane.YES_NO_OPTION);
        // See if the user wants to include the DAQ information
        String[] header;
        int i = 0;
        if (useDAQ == JOptionPane.YES_OPTION) {
        	daqxml = new ReadDAQXMLConfig(new File(FileHandler.getFilePath("Open DAQ SCRAMNet XML File", new XMLFileFilter())));
        	header = new String[10 + daqxml.getDAQConfig().getnumDaqBlocks()];
        	sampleSet = new double[10 + daqxml.getDAQConfig().getnumDaqBlocks()];
        	for (i = 0; i < daqxml.getDAQConfig().getnumDaqBlocks(); i++) {
        		header[i] = daqxml.getDAQConfig().getDaqName(i);
        	}
        }
        else {
        	// Just use the Control/Sim signals
        	header = new String[10];
        	sampleSet = new double[10];
        }
                	
        // Initialize the CSV File Writer and the header
        csvfile = new CSVWriter();
        Date date = new Date();        
        csvfile.open("Vanadium_" + date.getTime() + ".csv");                
        header[i++] = "East Disp. Cmd (in)";
        header[i++] = "East Displacement (in)";
        header[i++] = "East Load (kips)"; 
        header[i++] = "West Disp. Cmd (in)";
        header[i++] = "West Displacement (in)";
        header[i++] = "West Load (kips)";
        header[i++] = "Total Load (kips)";
        header[i++] = "Step";
        header[i++] = "Increment";
        header[i++] = "Event Count";
        csvfile.writeHeader(header);                
        
        // Create the Decimal Formatter
        decimal = new DecimalFormat("0.000");
        
        // Set variable defaults
    	//setCurEastPosition(0);
    	//setCurWestPosition(0);
    	setEastDisp(mmToInches(scr.readFloat(EAST_DISP_FBK_ADDR)*DISP_SCALE));
    	setWestDisp(mmToInches(scr.readFloat(WEST_DISP_FBK_ADDR)*DISP_SCALE));
    	setEastLoad(kNToKips(scr.readFloat(EAST_LOAD_FBK_ADDR)*EAST_LOAD_SCALE));
    	setWestLoad(kNToKips(scr.readFloat(WEST_LOAD_FBK_ADDR)*WEST_LOAD_SCALE));
    	setTotalLoad(eastLoad + westLoad);	
    	setEastExtend(false);
    	setWestExtend(true);
    	setStep(1);
    	setIncrement(1);
    	tolerance = 0.0;
    	eastIncrement = 0.0;
    	westIncrement = 0.0;
    	steps = 1;
    	increments = 1;
    	timeperincrement = 0.0;    	    	
    	executeOnce = false;
    	executeAuto = false;
    	stopAuto = false;
    	pauseAuto= false;    	
    	setEastATensionActuator(false);
    	setWestATensionActuator(true);
    	count = 0; // Overall count of every "event" or actuator move.   
    	isPostBuckling = false;
    	
	}
	
	/** Initialize the GUI */
	protected void init() {
		this.setSize(400, 620); 
        this.setLocationRelativeTo(null);       
        this.getContentPane().setLayout(null);
        this.setResizable(false);                
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                   
        
        // Title Label
        JLabel label = new JLabel("RTMD Vanadium Steel");
        Font font = label.getFont();
        label.setFont(new Font(font.getFontName(),Font.ITALIC,16));
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(0, 0, 200, 30);
        this.getContentPane().add(label);
        
        // East Actuator
        label = new JLabel("East Actuator");
        font = label.getFont();
        label.setFont(new Font(font.getFontName(),Font.BOLD,16));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(25, 40, 150, 30);
        this.getContentPane().add(label); 
        
        // West Actuator
        label = new JLabel("West Actuator");
        font = label.getFont();
        label.setFont(new Font(font.getFontName(),Font.BOLD,16));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(225, 40, 150, 30);
        this.getContentPane().add(label);
        
        // East Actuator displacement and load
        // displacement
        label = new JLabel("Disp.");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 70, 50, 30);
        this.getContentPane().add(label);
        eastDispTextField = new JTextField();
        eastDispTextField.setBounds(50,70,100,30);
        eastDispTextField.setHorizontalAlignment(JTextField.RIGHT);
        eastDispTextField.setEditable(false);
        this.getContentPane().add(eastDispTextField);
        label = new JLabel(" in");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(150, 70, 30, 30);
        this.getContentPane().add(label);
        // Current Command
        label = new JLabel("Cmd.");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 110, 50, 30);
        this.getContentPane().add(label);
        eastCurDispTextField = new JTextField();
        eastCurDispTextField.setBounds(50,110,100,30);
        eastCurDispTextField.setHorizontalAlignment(JTextField.RIGHT);
        eastCurDispTextField.setEditable(false);
        this.getContentPane().add(eastCurDispTextField);
        label = new JLabel(" in");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(150, 110, 30, 30);
        this.getContentPane().add(label);
        // load
        label = new JLabel("Load");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 150, 50, 30);
        this.getContentPane().add(label);
        eastLoadTextField = new JTextField();
        eastLoadTextField.setBounds(50,150,100,30);
        eastLoadTextField.setHorizontalAlignment(JTextField.RIGHT);
        eastLoadTextField.setEditable(false);
        this.getContentPane().add(eastLoadTextField);
        label = new JLabel(" kips");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(150, 150, 30, 30);
        this.getContentPane().add(label);
        
        // West Actuator displacement and force
        // displacement
        label = new JLabel("Disp.");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(200, 70, 50, 30);
        this.getContentPane().add(label);
        westDispTextField = new JTextField();
        westDispTextField.setBounds(250,70,100,30);
        westDispTextField.setHorizontalAlignment(JTextField.RIGHT);
        westDispTextField.setEditable(false);
        this.getContentPane().add(westDispTextField);
        label = new JLabel(" in");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(350, 70, 30, 30);
        this.getContentPane().add(label);
        // Current Command
        label = new JLabel("Cmd.");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(200, 110, 50, 30);
        this.getContentPane().add(label);
        westCurDispTextField = new JTextField();
        westCurDispTextField.setBounds(250,110,100,30);
        westCurDispTextField.setHorizontalAlignment(JTextField.RIGHT);
        westCurDispTextField.setEditable(false);
        this.getContentPane().add(westCurDispTextField);
        label = new JLabel(" in");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(350, 110, 30, 30);
        this.getContentPane().add(label);
        // load
        label = new JLabel("Load");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(200, 150, 50, 30);
        this.getContentPane().add(label);
        westLoadTextField = new JTextField();
        westLoadTextField.setBounds(250,150,100,30);
        westLoadTextField.setHorizontalAlignment(JTextField.RIGHT);
        westLoadTextField.setEditable(false);
        this.getContentPane().add(westLoadTextField);
        label = new JLabel(" kips");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(350, 150, 30, 30);
        this.getContentPane().add(label);
        
        // Tare Button (Sets command = feedback)
        label = new JLabel("C=D");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(180, 90, 30, 15);
        this.getContentPane().add(label);
        tareButton = new JButton();        
        tareButton.setBounds(180,105,30,10);
        tareButton.setActionCommand("Tare");
        tareButton.addActionListener(this);
        this.getContentPane().add(tareButton);
        
        // Total Load
        label = new JLabel("Total Load");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(50, 190, 100, 30);
        this.getContentPane().add(label);
        totalLoadTextField = new JTextField();
        totalLoadTextField.setBounds(150,190,100,30);
        totalLoadTextField.setEditable(false);
        totalLoadTextField.setHorizontalAlignment(JTextField.RIGHT);               
        this.getContentPane().add(totalLoadTextField);
        label = new JLabel(" kips");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(250, 190, 30, 20);
        this.getContentPane().add(label);                
                
        // Total Load Direction indicator
        totalLoadDirectionLabel = new JLabel();                 
        totalLoadDirectionLabel.setHorizontalAlignment(JLabel.CENTER);
        totalLoadDirectionLabel.setFont(new Font(totalLoadDirectionLabel.getFont().getFontName(), Font.BOLD, 14));
        totalLoadDirectionLabel.setBounds(280,190,40,30);
        this.getContentPane().add(totalLoadDirectionLabel);
        
        // Tolerance
        label = new JLabel("Tolerance");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(50, 220, 100, 20);
        this.getContentPane().add(label);
        SpinnerModel tolerancespinnermodel = new SpinnerNumberModel((double)0,(double)0, (double)1000, (double)0.5);
        toleranceSpinner = new JSpinner();
        toleranceSpinner.setBounds(175,220,75,30);
        toleranceSpinner.setModel(tolerancespinnermodel);      
        toleranceSpinner.addChangeListener(this);
        this.getContentPane().add(toleranceSpinner);
        label = new JLabel(" kips");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(250, 220, 30, 20);
        this.getContentPane().add(label);
        
        // East Actuator Movement options
        label = new JLabel("Extend");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(70, 260, 100, 20);
        this.getContentPane().add(label);
        eastExtendRadioButton = new JRadioButton();
        eastExtendRadioButton.setBounds(50,260,20,20);   
        eastExtendRadioButton.setSelected(true);
        eastExtendRadioButton.setActionCommand("eastExtend");
        eastExtendRadioButton.addActionListener(this);
        this.getContentPane().add(eastExtendRadioButton);
        label = new JLabel("Retract");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(70, 280, 100, 20);
        this.getContentPane().add(label);
        eastRetractRadioButton = new JRadioButton();
        eastRetractRadioButton.setBounds(50,280,20,20);
        eastRetractRadioButton.setActionCommand("eastRetract");
        eastRetractRadioButton.addActionListener(this);
        this.getContentPane().add(eastRetractRadioButton);
        // Create button group
        eastMovementButtonGroup = new ButtonGroup();
        eastMovementButtonGroup.add(eastExtendRadioButton);
        eastMovementButtonGroup.add(eastRetractRadioButton);            
        
        // West Actuator Movement options
        label = new JLabel("Extend");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(270, 260, 100, 20);
        this.getContentPane().add(label);
        westExtendRadioButton = new JRadioButton();
        westExtendRadioButton.setBounds(250,260,20,20);
        westExtendRadioButton.setSelected(true);
        westExtendRadioButton.setActionCommand("westExtend");
        westExtendRadioButton.addActionListener(this);
        this.getContentPane().add(westExtendRadioButton);
        label = new JLabel("Retract");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(270, 280, 100, 20);
        this.getContentPane().add(label);
        westRetractRadioButton = new JRadioButton();
        westRetractRadioButton.setBounds(250,280,20,20);
        westRetractRadioButton.setActionCommand("westRetract");
        westRetractRadioButton.addActionListener(this);
        this.getContentPane().add(westRetractRadioButton);
        // Create button group
        westMovementButtonGroup = new ButtonGroup();
        westMovementButtonGroup.add(westExtendRadioButton);
        westMovementButtonGroup.add(westRetractRadioButton);
        
        // Spinner model in inches
        double min = 0;
        double max = 1;
        double step = 0.001;
        double initValue = 0;
        SpinnerModel topAspinnermodel = new SpinnerNumberModel(initValue, min, max, step);
        SpinnerModel botAspinnermodel = new SpinnerNumberModel(initValue, min, max, step);
        
        // East Actuator increment Spinner
        label = new JLabel("Increment");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 310, 100, 30);
        this.getContentPane().add(label);
        eastIncrementSpinner = new JSpinner();
        eastIncrementSpinner.setBounds(100,310,50,30);
        eastIncrementSpinner.setValue(0);
        eastIncrementSpinner.setModel(topAspinnermodel);
        eastIncrementSpinner.addChangeListener(this);
        this.getContentPane().add(eastIncrementSpinner);
        label = new JLabel(" in");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(150, 310, 30, 30);
        this.getContentPane().add(label);
                      
        // West Actuator increment Spinner
        label = new JLabel("Increment");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(200, 310, 100, 30);
        this.getContentPane().add(label);
        westIncrementSpinner = new JSpinner();
        westIncrementSpinner.setBounds(300,310,50,30);
        westIncrementSpinner.setValue(0);
        westIncrementSpinner.setModel(botAspinnermodel);
        westIncrementSpinner.addChangeListener(this);
        this.getContentPane().add(westIncrementSpinner);
        label = new JLabel(" in");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(350, 310, 30, 30);
        this.getContentPane().add(label);
        
        // Execute One Step button
        executeOneButton = new JButton("Move Once");
        executeOneButton.setBounds(125,350,150,30);
        executeOneButton.setActionCommand("ExecuteOne");
        executeOneButton.addActionListener(this);
        this.getContentPane().add(executeOneButton);
        
        // Automation Section
        label = new JLabel("Automation");
        font = label.getFont();
        label.setFont(new Font(font.getFontName(),Font.BOLD,16));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(150, 380, 100, 30);
        this.getContentPane().add(label);
        
        // East Actuator Tension/Compression Label
        eastActuatorLabel = new JLabel("[]");        
        font = eastActuatorLabel.getFont();
        eastActuatorLabel.setFont(new Font(font.getFontName(),Font.BOLD,12));
        eastActuatorLabel.setHorizontalAlignment(JLabel.CENTER);
        eastActuatorLabel.setBounds(25, 390, 150, 30);
        this.getContentPane().add(eastActuatorLabel); 
        
        // West Actuator Tension/Compression Label
        westActuatorLabel = new JLabel("[]");
        font = eastActuatorLabel.getFont();
        westActuatorLabel.setFont(new Font(font.getFontName(),Font.BOLD,12));
        westActuatorLabel.setHorizontalAlignment(JLabel.CENTER);
        westActuatorLabel.setBounds(225, 390, 150, 30);
        this.getContentPane().add(westActuatorLabel);
        
        // Switch Actuators Button
        switchActuatorsButton = new JButton("< >");
        switchActuatorsButton.setBounds(175,405,55,20);
        switchActuatorsButton.setHorizontalAlignment(JLabel.CENTER);
        switchActuatorsButton.setActionCommand("Switch");
        switchActuatorsButton.addActionListener(this);
        this.getContentPane().add(switchActuatorsButton);
        
        // Steps
        label = new JLabel("Steps");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(20, 430, 50, 30);
        this.getContentPane().add(label);
        SpinnerModel stepsspinnermodel = new SpinnerNumberModel(1, 1, 1000, 1);
        stepsSpinner = new JSpinner();
        stepsSpinner.setBounds(70,430,60,30);
        stepsSpinner.setModel(stepsspinnermodel);
        stepsSpinner.addChangeListener(this);
        this.getContentPane().add(stepsSpinner);
        
        // Pre/Post Buckling Algorithm Selector
        label = new JLabel("Pre-Buckle");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(40, 470, 100, 20);
        this.getContentPane().add(label);
        preBucklingRadioButton = new JRadioButton();
        preBucklingRadioButton.setBounds(20,470,20,20);
        preBucklingRadioButton.setActionCommand("prebuckle");
        preBucklingRadioButton.addActionListener(this);
        preBucklingRadioButton.setSelected(true);
        this.getContentPane().add(preBucklingRadioButton);
        label = new JLabel("Post-Buckle");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(40, 490, 100, 20);
        this.getContentPane().add(label);
        postBucklingRadioButton = new JRadioButton();
        postBucklingRadioButton.setBounds(20,490,20,20);
        postBucklingRadioButton.setActionCommand("postbuckle");
        postBucklingRadioButton.addActionListener(this);
        this.getContentPane().add(postBucklingRadioButton);
        // Create button group
        bucklingButtonGroup = new ButtonGroup();
        bucklingButtonGroup.add(preBucklingRadioButton);
        bucklingButtonGroup.add(postBucklingRadioButton);
        
        // Increments
        label = new JLabel("Increments");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(160, 430, 80, 30);
        this.getContentPane().add(label);
        SpinnerModel incrementsspinnermodel = new SpinnerNumberModel(1, 1, 1000, 1);
        incrementsSpinner = new JSpinner();
        incrementsSpinner.setBounds(240,430,60,30);
        incrementsSpinner.setModel(incrementsspinnermodel);
        incrementsSpinner.addChangeListener(this);
        this.getContentPane().add(incrementsSpinner);
        
        // Time per step
        label = new JLabel("Time Per Increment");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(120, 470, 120, 30);
        this.getContentPane().add(label);
        SpinnerModel timespinnermodel = new SpinnerNumberModel((double)1.0,(double)0.2, (double)300, (double)0.1);
        timeperincrementSpinner = new JSpinner();
        timeperincrementSpinner.setBounds(240,470,60,30);
        timeperincrementSpinner.setModel(timespinnermodel);
        timeperincrementSpinner.addChangeListener(this);
        this.getContentPane().add(timeperincrementSpinner);
        label = new JLabel(" seconds");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(275, 470, 100, 30);
        this.getContentPane().add(label);
        
        // Execute Button
        executeButton = new JButton("Execute");
        executeButton.setBounds(25,510,100,30);
        executeButton.setActionCommand("ExecuteAuto");
        executeButton.addActionListener(this);
        this.getContentPane().add(executeButton);
        
        // Stop Button
        stopButton = new JButton("Stop");
        stopButton.setBounds(150,510,100,30);
        stopButton.setActionCommand("Stop");
        stopButton.addActionListener(this);
        this.getContentPane().add(stopButton);
        
        // Pause Button
        pauseToggleButton = new JToggleButton("Pause");
        pauseToggleButton.setBounds(275,510,100,30);
        pauseToggleButton.setActionCommand("Pause");
        pauseToggleButton.addActionListener(this);
        this.getContentPane().add(pauseToggleButton);
        
        // Steps completed
        label = new JLabel("Step");
        font = label.getFont();
        label.setFont(new Font(font.getFontName(),1,16));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(40, 550, 100, 30);
        this.getContentPane().add(label);
        stepTextField = new JTextField();
        stepTextField.setBounds(115,550,50,30);
        stepTextField.setHorizontalAlignment(JTextField.RIGHT);
        stepTextField.setEditable(false);
        this.getContentPane().add(stepTextField);        
        
        // Increments completed
        label = new JLabel("Increment");
        font = label.getFont();
        label.setFont(new Font(font.getFontName(),1,16));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(175, 550, 100, 30);
        this.getContentPane().add(label);
        incrementTextField = new JTextField();
        incrementTextField.setBounds(275,550,50,30);
        incrementTextField.setHorizontalAlignment(JTextField.RIGHT);
        incrementTextField.setEditable(false);
        this.getContentPane().add(incrementTextField);
	}
	
	/** Actions for all objects except spinners */
	public void actionPerformed(ActionEvent e) {
		// East Extend/Retract options
		if (e.getActionCommand().equals("eastExtend"))
			setEastExtend(true);
		else if (e.getActionCommand().equals("eastRetract"))
			setEastExtend(false);
		
		// West Extend/Retract options
		if (e.getActionCommand().equals("westExtend"))
			setWestExtend(true);
		else if (e.getActionCommand().equals("westRetract"))
			setWestExtend(false);		
		
		// Execute Once button
		if (e.getActionCommand().equals("ExecuteOne")) {
			if (DEBUG==1) System.out.println("ExecuteOnce");
			executeOnce = true;
		}
		
		// Execute button
		if (e.getActionCommand().equals("ExecuteAuto")) {
			executeAuto = true;
		}		
		
		// Stop button
		if (e.getActionCommand().equals("Stop")) {
			stopAuto = true;
		}
		
		// Pause button
		if (e.getActionCommand().equals("Pause")) {
			if (pauseAuto) {
				pauseAuto = false;
				pauseToggleButton.setText("Pause");
			}
			else {
				pauseAuto = true;
				pauseToggleButton.setText("Continue");
			}
		}
		
		// Switch Actuator button
		if (e.getActionCommand().equals("Switch")) {
			if (isWestATensionActuator()) {
				setWestATensionActuator(false);
				setEastATensionActuator(true);
			}
			else {
				setWestATensionActuator(true);
				setEastATensionActuator(false);
			}
		}
		
		// Tare sets command = feedback
		if (e.getActionCommand().equals("Tare")) {
			setCurEastPosition(getEastDisp());
			setCurWestPosition(getWestDisp());
		}
		
		// Pre/post bucking
		if (e.getActionCommand().equals("prebuckle")) {
			isPostBuckling = false;			
		}
		if (e.getActionCommand().equals("postbuckle")) {
			isPostBuckling = true;
		}
	}
	
	/** Update spinners */
	public void stateChanged(ChangeEvent e) {
		// Set the variables for all spinners
		setTolerance((Double)toleranceSpinner.getValue());
		setEastIncrement((Double)eastIncrementSpinner.getValue());				
		setWestIncrement((Double)westIncrementSpinner.getValue());
		setSteps((Integer)stepsSpinner.getValue());
		setIncrements((Integer)incrementsSpinner.getValue());
		setTimeperincrement((Double)timeperincrementSpinner.getValue()); 		
	}
	
	/** Move Actuators one increment based on if they should extend or retract */
	protected void moveActuators() {
		//	Get the East Actuator Command and amplitude and Execute
		if (isEastExtend()) {
			// Extend actuator (negative command)
			// Add the increment to the current displacement then convert from inches to mm and scale					
			setCurEastPosition(getCurEastPosition() - getEastIncrement());
			if (DEBUG==1) System.out.println("Extend East " + (curEastPosition));
			//scr.writeSCRscaled(EAST_DISP_CMD_ADDR,(float)inchesTomm(getCurEastPosition()),DISP_SCALE);
		}
		else {
			// Retract actuator (positive command)
			// Add the increment to the current displacement then convert from inches to mm and scale
			setCurEastPosition(getCurEastPosition() + getEastIncrement());
			if (DEBUG==1) System.out.println("Retract East " + (curEastPosition));
			//scr.writeSCRscaled(EAST_DISP_CMD_ADDR,(float)inchesTomm(getCurEastPosition()),DISP_SCALE);
		}
		
		// Get the West Actuator Command and amplitude and Execute
		if (isWestExtend()) {
			// Extend actuator (negative command)
			// Add the increment to the current displacement then convert from inches to mm and scale
			setCurWestPosition(getCurWestPosition() - getWestIncrement());
			if (DEBUG==1) System.out.println("Extend West " + (curWestPosition));
			//scr.writeSCRscaled(WEST_DISP_CMD_ADDR,(float)inchesTomm(getCurWestPosition()),DISP_SCALE);
		}
		else {
			// Retract actuator (positive command)
			// Add the increment to the current displacement then convert from inches to mm and scale
			setCurWestPosition(getCurWestPosition() + getWestIncrement());
			if (DEBUG==1) System.out.println("Retract West " + (curWestPosition));
			//scr.writeSCRscaled(WEST_DISP_CMD_ADDR,(float)inchesTomm(getCurWestPosition()),DISP_SCALE);
		}		
	
		// Sample data just for the Sim PC on each step	
		sampleData();
		
		// This is an event so increment the count and push onto scramnet
		count++;
		scr.writeInt(61,count);		 
	}
	
	/** Test loop that runs as its own thread.  It will constantly update the GUI
	 *  and check if an Execute button has been pressed and act on it.
	 */	
	public void run() {		
		setStep(1);
		setIncrement(1);
		long globalCounter = scr.readGlobalCounter();
		
		while (true) {						
			// Get Actuator Feedback			
			setEastDisp(mmToInches(scr.readFloat(EAST_DISP_FBK_ADDR)*DISP_SCALE));
	    	setWestDisp(mmToInches(scr.readFloat(WEST_DISP_FBK_ADDR)*DISP_SCALE));
	    	setEastLoad(kNToKips(scr.readFloat(EAST_LOAD_FBK_ADDR)*EAST_LOAD_SCALE));
	    	setWestLoad(kNToKips(scr.readFloat(WEST_LOAD_FBK_ADDR)*WEST_LOAD_SCALE));
	    	setTotalLoad(eastLoad + westLoad);
	    	
	    	// Get the Actuator commands
	    	setCurEastPosition(mmToInches(scr.readFloat(EAST_DISP_CMD_ADDR)*DISP_SCALE));
	    	setCurWestPosition(mmToInches(scr.readFloat(WEST_DISP_CMD_ADDR)*DISP_SCALE));
								
	    	// Check tolerance against total load
	    	// Change totalLoad background to RED when exceeded
	    	if (Math.abs(getTotalLoad()) > getTolerance()) 
	    		totalLoadTextField.setBackground(Color.RED); 
	    	else
	    		totalLoadTextField.setBackground(Color.GREEN);
	    		    	
	    	// Check if Move Once has been hit
	    	if (executeOnce) {
	    		executeOnce = false;
	    		setEastIncrement((Double)eastIncrementSpinner.getValue());				
	    		setWestIncrement((Double)westIncrementSpinner.getValue());
	    		moveActuators();
	    	}
	    	
	    	// Wait for Pause Button and sleep for 250ms
	    	while (pauseAuto) {try {Thread.sleep((int)(getTimeperincrement()*250));} catch (Exception e) {}}
	    	
	    	//	Check if Execute Auto has been hit
	    	if (executeAuto) {
	    		// Disable Buttons and Spinners
	    		tareButton.setEnabled(false);
	    		toleranceSpinner.setEnabled(false);
	    		westExtendRadioButton.setEnabled(false);
	    		westRetractRadioButton.setEnabled(false);
	    		eastExtendRadioButton.setEnabled(false);
	    		eastRetractRadioButton.setEnabled(false);
	    		executeButton.setEnabled(false);
	    		executeOneButton.setEnabled(false);
	    		switchActuatorsButton.setEnabled(false);
	    		stepsSpinner.setEnabled(false);
	    		incrementsSpinner.setEnabled(false);
	    		timeperincrementSpinner.setEnabled(false);
	    		eastIncrementSpinner.setEnabled(false);
	    		westIncrementSpinner.setEnabled(false);
	    		preBucklingRadioButton.setEnabled(false);
	    		postBucklingRadioButton.setEnabled(false);
	    		
	    		// Pre buckling stage
	    		if (!isPostBuckling) {
		    		// if this is the first increment of the step, figure out how
		    		// the actuators should respond based on Total Load	    		
		    		if (getIncrement() == 1) {
		    			if (DEBUG==1) System.out.println("Step = " + getStep() + ", Ft = " + getTotalLoad() + ", Tol = " + getTolerance());
			    		// Total Load greater than positive tolerance, move compression actuator only
						if (getTotalLoad() > getTolerance()) {
							// if East Actuator is Tension, set to 0
							if (isEastATensionActuator()) {		
								if (DEBUG==1) System.out.println("Condition 1: East = 0");
								setEastIncrement(0.0);		
							}
							// Otherwise, set to minimum increment size
							else {
								if (DEBUG==1) System.out.println("Condition 1: East = Min");
								setEastIncrement((Double)eastIncrementSpinner.getValue());
							}
							// if West Actuator is Tension, set to 0
							if (isWestATensionActuator()) {
								if (DEBUG==1) System.out.println("Condition 1: West = 0");
								setWestIncrement(0.0);		
							}
							else {					
								// Otherwise, set to minimum increment size
								if (DEBUG==1) System.out.println("Condition 1: West = Min");
								setWestIncrement((Double)westIncrementSpinner.getValue());
							}
						}
						// Total Load less than negative tolerance, move tension actuator only
						else if (getTotalLoad() < -getTolerance()) {	
							//if East Actuator is Tension, set to minimum increment size
							if (isEastATensionActuator()) {
								if (DEBUG==1) System.out.println("Condition 2: East = Min");
								setEastIncrement((Double)eastIncrementSpinner.getValue());
							}
							// Otherwise, set to 0
							else {												
								if (DEBUG==1) System.out.println("Condition 2: East = 0");
								setEastIncrement(0.0);
							}
							// if West Actuator is Tension, set to minimum increment size
							if (isWestATensionActuator()) {
								if (DEBUG==1) System.out.println("Condition 2: West = Min");
								setWestIncrement((Double)westIncrementSpinner.getValue());
							}
							else {					
								// Otherwise, set to 0
								if (DEBUG==1) System.out.println("Condition 2: West = 0");
								setWestIncrement(0.0);	
							}
						}
						// Within tolerance, move both actuators
						else {	    	
							if (DEBUG==1) System.out.println("Condition 3: Both = Min");
							setEastIncrement((Double)eastIncrementSpinner.getValue());				
				    		setWestIncrement((Double)westIncrementSpinner.getValue());
						}
		    		}
	    		}
	    		// Post Buckling stage
	    		else {
	    			// if this is the first increment of the step, figure out how
		    		// the actuators should respond based on Total Load	    		
		    		if (getIncrement() == 1) {
		    			if (DEBUG==1) System.out.println("Step = " + getStep() + ", Ft = " + getTotalLoad() + ", Tol = " + getTolerance());
			    		// Total load is greater than the tolerance
						if (getTotalLoad() > getTolerance()) {
							// Total load is 2x greater than the tolerance
							if (getTotalLoad() > getTolerance() * 2) {
								if (DEBUG==1) System.out.println("East E2, West R1");
								setEastExtend(true);
								setEastIncrement((Double)eastIncrementSpinner.getValue()*2);				
								setWestIncrement((Double)westIncrementSpinner.getValue());
							}
							else {
								if (DEBUG==1) System.out.println("East E1, West R1");
								setEastExtend(true);
								setEastIncrement((Double)eastIncrementSpinner.getValue());				
								setWestIncrement((Double)westIncrementSpinner.getValue());
							}
						}
						// Total load is less than the tolerance
						else if (getTotalLoad() < -getTolerance()) {
							// Total load is 2x less than the tolerance
							if (getTotalLoad() < -getTolerance() * 2) {
								if (DEBUG==1) System.out.println("East R2, West R1");
								setEastExtend(false);
								setEastIncrement((Double)eastIncrementSpinner.getValue()*2);				
								setWestIncrement((Double)westIncrementSpinner.getValue());
							}
							else {
								if (DEBUG==1) System.out.println("East R1, West R1");
								setEastExtend(false);
								setEastIncrement((Double)eastIncrementSpinner.getValue());				
								setWestIncrement((Double)westIncrementSpinner.getValue());
							}
						}									    	
						// Within tolerance, retract the west actuator
						else {	    			
							if (DEBUG==1) System.out.println("East 0, West R1");
							setEastIncrement(0);				
				    		setWestIncrement((Double)westIncrementSpinner.getValue());
						}
		    		}
	    		}
	    		
	    		// Check if step is less than the number of set steps
	    		// Also check if the stop button wasn't hit
	    		if ( (getStep() <= getSteps()) && (!stopAuto) ) {	    			
	    			if ( (getIncrement() <= getIncrements()) && (!stopAuto) ) {
	    				// move actuators
	    				moveActuators();
	    				
	    				// Next increment
	    				setIncrement(getIncrement() + 1);
		    	    	// Wait time per step - 100 ms
		    			//try {Thread.sleep((int)(getTimeperincrement()*1000));} catch (Exception e) {}	    				
		    			while (globalCounter+(int)(getTimeperincrement()*1000) > scr.readGlobalCounter()) ;
		    	    	globalCounter = scr.readGlobalCounter();
	    			}
	    			// End of step, reset increment
	    			else {	    					    						    				    					    		
	    				setStep(getStep() + 1);
	    				setIncrement(1);
	    				stopAuto = false;
	    			}
	    		}
	    		else {	    		
		    		// Enable Buttons and Spinners
	    			tareButton.setEnabled(true);
	    			toleranceSpinner.setEnabled(true);
	    			westExtendRadioButton.setEnabled(true);
		    		westRetractRadioButton.setEnabled(true);
		    		eastExtendRadioButton.setEnabled(true);
		    		eastRetractRadioButton.setEnabled(true);
		    		executeButton.setEnabled(true);
		    		executeOneButton.setEnabled(true);
		    		switchActuatorsButton.setEnabled(true);
		    		stepsSpinner.setEnabled(true);
		    		incrementsSpinner.setEnabled(true);
		    		timeperincrementSpinner.setEnabled(true);
		    		eastIncrementSpinner.setEnabled(true);
		    		westIncrementSpinner.setEnabled(true);
		    		preBucklingRadioButton.setEnabled(true);
		    		postBucklingRadioButton.setEnabled(true);
		    		// Reset flags and counter
		    		executeAuto = false;
		    		stopAuto = false;
		    		setStep(1);
		    		setIncrement(1);
	    		}
	    	}	    		    	    	   
	    	
	    	// Write displacement information to SCRAMNet for Visualization
	    	scr.writeFloat(1010,(float)getEastDisp());
	    	scr.writeFloat(1020,(float)getWestDisp());
	    	
	    	// Write step and increment information to SCRAMNet 
	    	scr.writeFloat(2000,(float)getStep());
	    	scr.writeFloat(2001,(float)getIncrement());	    	
	    	scr.writeFloat(2002,(float)getTotalLoad()); // Total Axial Force
	    	if (useDAQ == JOptionPane.YES_OPTION) {
		    	double LVDT17 = scr.readDAQ(daqxml.getDAQConfig().getDaqOffset(16),
						 					daqxml.getDAQConfig().getDaqGain(16),
						 					daqxml.getDAQConfig().getDaqVoffset(16),
						 					daqxml.getDAQConfig().getDaqVslope(16),
						 					daqxml.getDAQConfig().getDaqEUoffset(16),
						 					daqxml.getDAQConfig().getDaqEUslope(16));
		    	
		    	double LVDT18 = scr.readDAQ(daqxml.getDAQConfig().getDaqOffset(17),
						 					daqxml.getDAQConfig().getDaqGain(17),
						 					daqxml.getDAQConfig().getDaqVoffset(17),
						 					daqxml.getDAQConfig().getDaqVslope(17),
						 					daqxml.getDAQConfig().getDaqEUoffset(17),
						 					daqxml.getDAQConfig().getDaqEUslope(17));
		    	scr.writeFloat(2003,(float)((LVDT17+LVDT18)/2)); // Truss shortening
		    	scr.writeFloat(2004,(float)(((Math.abs(this.getEastLoad())+Math.abs(this.getWestLoad()))/2)* 120)); // Moment
		    	scr.writeFloat(2005,(float)((LVDT18-LVDT17)/42)); // Rotation
	    	}
	    	
	    	// Wait ~100 ms
			//try {Thread.sleep(100);} catch (Exception e) {}		    	
	    	while (globalCounter+100 > scr.readGlobalCounter()) ;	    	
	    	globalCounter = scr.readGlobalCounter();
		}
	}
	
	/** Sample the data from the DAQ and Controller and save to CSV file */
	protected synchronized void sampleData() {
		int i = 0;
		if (daqxml != null) {			
			for (i = 0; i < daqxml.getDAQConfig().getnumDaqBlocks(); i++) {		 
				 sampleSet[i] = scr.readDAQ(daqxml.getDAQConfig().getDaqOffset(i),
						 					daqxml.getDAQConfig().getDaqGain(i),
						 					daqxml.getDAQConfig().getDaqVoffset(i),
						 					daqxml.getDAQConfig().getDaqVslope(i),
						 					daqxml.getDAQConfig().getDaqEUoffset(i),
						 					daqxml.getDAQConfig().getDaqEUslope(i) );										
			 }
		}
		sampleSet[i++] = getCurEastPosition();
		sampleSet[i++] = getEastDisp();
		sampleSet[i++] = getEastLoad();
		sampleSet[i++] = getCurWestPosition();
		sampleSet[i++] = getWestDisp();
		sampleSet[i++] = getWestLoad();
		sampleSet[i++] = getTotalLoad();		
		sampleSet[i++] = getStep();
		sampleSet[i++] = getIncrement();
		sampleSet[i++] = getCount();
		
		csvfile.write(sampleSet);
	}
	
	protected double mmToInches(double mm) {
		return mm/25.4;
	}
	
	protected double inchesTomm(double inches) {
		return inches*25.4;
	}
	
	protected double kNToKips(double kN) {
		return kN/4.448;
	}
	
	protected double kipsTokN(double kips) {
		return kips*4.448;
	}
	

	protected double getCurEastPosition() {
		return mmToInches(scr.readFloat(EAST_DISP_CMD_ADDR)*DISP_SCALE);
	}

	protected void setCurEastPosition(double curEastPosition) {
		scr.writeFloat(EAST_DISP_CMD_ADDR,(float)inchesTomm(curEastPosition)/DISP_SCALE);
		eastCurDispTextField.setText(decimal.format(curEastPosition));
	}
	
	protected double getCurWestPosition() {
		return mmToInches(scr.readFloat(WEST_DISP_CMD_ADDR)*DISP_SCALE);
	}

	protected void setCurWestPosition(double curWestPosition) {
		scr.writeFloat(WEST_DISP_CMD_ADDR,(float)inchesTomm(curWestPosition)/DISP_SCALE);
		westCurDispTextField.setText(decimal.format(curWestPosition));
	}
	
	public double getWestDisp() {
		return westDisp;
	}

	public void setWestDisp(double westDisp) {
		this.westDisp = westDisp;		
		westDispTextField.setText(decimal.format(westDisp));
	}

	public boolean isWestExtend() {
		return westExtend;
	}

	public void setWestExtend(boolean westExtend) {
		this.westExtend = westExtend;		
		westExtendRadioButton.setSelected(westExtend);
		westRetractRadioButton.setSelected(!westExtend);
		isWestATensionActuator = !westExtend;
		if (isWestATensionActuator) {			
			westActuatorLabel.setText("[ Tension ]");
		}
		else {				
			westActuatorLabel.setText("[ Compression ]");
		}
	}

	public double getWestIncrement() {
		return westIncrement;
	}

	public void setWestIncrement(double westIncrement) {
		this.westIncrement = westIncrement;		
	}

	public double getWestLoad() {
		return westLoad;
	}

	public void setWestLoad(double westLoad) {
		this.westLoad = westLoad;	
		westLoadTextField.setText(decimal.format(westLoad));
	}

	public boolean isPause() {
		return pauseAuto;
	}

	public void setPause(boolean pause) {
		this.pauseAuto = pause;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
		stepTextField.setText(Integer.toString(step));
	}
	
	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
		incrementTextField.setText(Integer.toString(increment));
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	public int getIncrements() {
		return increments;
	}

	public void setIncrements(int increments) {
		this.increments = increments;
	}

	public boolean isStop() {
		return stopAuto;
	}

	public void setStop(boolean stop) {
		this.stopAuto = stop;
	}

	public double getTimeperincrement() {
		return timeperincrement;
	}

	public void setTimeperincrement(double timeperincrement) {
		this.timeperincrement = timeperincrement;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public double getEastDisp() {
		return eastDisp;
	}

	public void setEastDisp(double eastDisp) {
		this.eastDisp = eastDisp;
		eastDispTextField.setText(decimal.format(eastDisp));
	}

	public boolean isEastExtend() {
		return eastExtend;
	}

	public void setEastExtend(boolean eastExtend) {
		this.eastExtend = eastExtend;
		eastExtendRadioButton.setSelected(eastExtend);
		eastRetractRadioButton.setSelected(!eastExtend);
		isEastATensionActuator = !eastExtend;
		if (isEastATensionActuator) {			
			eastActuatorLabel.setText("[ Tension ]");
		}
		else {			
			eastActuatorLabel.setText("[ Compression ]");
		}
	}

	public double getEastIncrement() {
		return eastIncrement;
	}

	public void setEastIncrement(double eastIncrement) {
		this.eastIncrement = eastIncrement;
	}

	public double getEastLoad() {
		return eastLoad;
		
	}

	public void setEastLoad(double eastLoad) {
		this.eastLoad = eastLoad;
		eastLoadTextField.setText(decimal.format(eastLoad));
	}

	public double getTotalLoad() {
		return totalLoad;
	}

	public void setTotalLoad(double totalLoad) {
		previousTotalLoad = this.totalLoad;
		this.totalLoad = totalLoad;
		totalLoadTextField.setText(decimal.format(totalLoad));
		if (totalLoad > previousTotalLoad)
			totalLoadDirectionLabel.setText("+");
		else
			totalLoadDirectionLabel.setText("-");
	}
	
	public boolean isEastATensionActuator() {
		return isEastATensionActuator;
	}

	public void setEastATensionActuator(boolean isEastATensionActuator) {
		this.isEastATensionActuator = isEastATensionActuator;
		if (isEastATensionActuator) {
			eastActuatorLabel.setText("[ Tension ]");
			setEastExtend(false);	
			setWestExtend(true);
		}
		else {
			eastActuatorLabel.setText("[ Compression ]");
			setEastExtend(true);
			setWestExtend(false);
		}
	}

	public boolean isWestATensionActuator() {
		return isWestATensionActuator;
	}

	public void setWestATensionActuator(boolean isWestATensionActuator) {
		this.isWestATensionActuator = isWestATensionActuator;
		if (isWestATensionActuator) {
			westActuatorLabel.setText("[ Tension ]");
			setWestExtend(false);
			setEastExtend(true);
		}
		else {
			westActuatorLabel.setText("[ Compression ]");
			setWestExtend(true);
			setEastExtend(false);
		}
	}	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
		
	/** Variables */
	private static final long serialVersionUID = 1191119216960235810L;	
	private ScramNetIO scr;
	private DecimalFormat decimal;
	private int EAST_DISP_CMD_ADDR;
	private int WEST_DISP_CMD_ADDR;
	private int EAST_DISP_FBK_ADDR;
	private int EAST_LOAD_FBK_ADDR;
	private int WEST_DISP_FBK_ADDR;
	private int WEST_LOAD_FBK_ADDR;
	private int DISP_SCALE;
	private int EAST_LOAD_SCALE;
	private int WEST_LOAD_SCALE;
	//private double MINIMUM_INCREMENT_SIZE;
	private Properties properties;		
	private JTextField eastDispTextField;
	private double eastDisp;
	private JTextField westDispTextField;
	private double westDisp;
	private JTextField eastCurDispTextField;
	private double curEastPosition;
	private JTextField westCurDispTextField;
	private double curWestPosition;
	private JTextField eastLoadTextField;
	private double eastLoad;
	private JTextField westLoadTextField;
	private double westLoad;
	private JTextField totalLoadTextField;
	private double totalLoad;
	private JSpinner toleranceSpinner;
	private double tolerance; 
	private JRadioButton eastExtendRadioButton;	
	private JRadioButton eastRetractRadioButton;
	private boolean eastExtend;
	private JRadioButton westExtendRadioButton;
	private JRadioButton westRetractRadioButton;
	private boolean westExtend;
	private ButtonGroup eastMovementButtonGroup;
	private ButtonGroup westMovementButtonGroup;
	private JSpinner eastIncrementSpinner;
	private double eastIncrement;
	private JSpinner westIncrementSpinner;
	private double westIncrement;
	private JButton executeOneButton;
	private JSpinner stepsSpinner;
	private int steps;
	private JSpinner incrementsSpinner;
	private int increments;
	private JSpinner timeperincrementSpinner;
	private double timeperincrement;
	private JButton executeButton;
	private JButton stopButton;	
	private JToggleButton pauseToggleButton;	
	private JTextField stepTextField;
	private int step;
	private JTextField incrementTextField;
	private int increment;
	private boolean executeOnce;
	private boolean executeAuto;
	private boolean stopAuto;
	private boolean pauseAuto;
	private boolean isEastATensionActuator;
	private JLabel eastActuatorLabel;
	private boolean isWestATensionActuator;
	private JLabel westActuatorLabel;
	private JButton switchActuatorsButton;
	private int useDAQ;
	private ReadDAQXMLConfig daqxml;
	private double[] sampleSet;
	private CSVWriter csvfile;
	private int count;
	private JButton tareButton;
	private JLabel totalLoadDirectionLabel;	
	private double previousTotalLoad;
	private JRadioButton preBucklingRadioButton;
	private JRadioButton postBucklingRadioButton;
	private boolean isPostBuckling;
	private ButtonGroup bucklingButtonGroup;

}