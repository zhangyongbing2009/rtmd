package edu.lehigh.nees.scramnet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.text.DecimalFormat;
import edu.lehigh.nees.IntegratedControl.Ramp.ToZeroRampGenerator;

/********************************* 
 * SCRAMNet CommandZero
 * <p>
 * Zero the selected command blocks in SCRAMNet using a Ramp
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  2 Mar 06  T. Marullo   Initial
 * 21 Apr 09  T. Marullo   Fixed some sawtoothing effects.  Now runs smooth
 *  						
 ********************************/
public class CommandZero extends JFrame implements ActionListener {
 
	/** New CommandZero object */
	public CommandZero() {
		
		// SCRAMNet
		scr = new ScramNetIO();
		scr.initScramnet();				
		
		// Number Formatter
		format.setMaximumFractionDigits(4);
		
		// Initialize the timer counters
		currentTickCount = scr.readGlobalCounter();
		previousTickCount = scr.readGlobalCounter();
		
		init();			
	}
	  
	/** Initialize the Button Graphics */
	public void init() {	
		// Set form layout
		Container contentPane = getContentPane();
	    contentPane.setLayout(new BorderLayout());
	
	    // Window parameters
	    this.setSize(600,500);
	    this.setTitle("SCRAMNet Command Zero");
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setResizable(false);                
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
        // Set up UI Look and Feel
        try {            
            UIManager.setLookAndFeel(new com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel());
        } catch(Exception e) {System.out.println("Error setting Look and Feel: " + e);}

        // Set up arrays for all 30 blocks
        addressTextField = new JTextField[30];
        valueTextField = new JTextField[30];
        blockCheckBox = new JCheckBox[30];
        
        // Set up header       
        // left column
		label = new JLabel("Name");
	    label.setBounds(30,5,125,25);	    
	    this.getContentPane().add(label);
	    label = new JLabel("Address");
	    label.setBounds(160,5,55,25);
	    label.setHorizontalAlignment(JTextField.RIGHT);
	    this.getContentPane().add(label);
	    label = new JLabel("Value");
	    label.setBounds(220,5,55,25);
	    label.setHorizontalAlignment(JTextField.RIGHT);
	    this.getContentPane().add(label);
        // right column
		label = new JLabel("Name");
	    label.setBounds(330,5,125,25);	    
	    this.getContentPane().add(label);
	    label = new JLabel("Address");
	    label.setBounds(460,5,55,25);
	    label.setHorizontalAlignment(JTextField.RIGHT);
	    this.getContentPane().add(label);
	    label = new JLabel("Value");
	    label.setBounds(520,5,55,25);
	    label.setHorizontalAlignment(JTextField.RIGHT);
	    this.getContentPane().add(label);
		    
		
	    // Create Textfields
        for (int i = 0; i < 30; i++) {
        	addressTextField[i] = new JTextField(Integer.toString(i));
        	valueTextField[i] = new JTextField("0");
        	blockCheckBox[i] = new JCheckBox();

        	switch (i) {
        		case 0:	// Simulation Running
        			// Label
        			label = new JLabel("Simulation Running");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 1:	// Displacement Command 1
        			// Label
        			label = new JLabel("Displacement cmd 1");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 2:	// Velocity Command 1
        			// Label
        			label = new JLabel("Velocity cmd 1");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;        			
        		case 3:	// Acceleration Command 1
        			// Label
        			label = new JLabel("Accel cmd 1");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 4:	// Force Command 1
        			// Label
        			label = new JLabel("Force cmd 1");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 5:	// Force Derivative Command 1
        			// Label
        			label = new JLabel("Force Deriv cmd 1");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 6:	// Displacement Command 2
        			// Label
        			label = new JLabel("Displacement cmd 2");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 7:	// Velocity Command 2
        			// Label
        			label = new JLabel("Velocity cmd 2");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;        			
        		case 8:	// Acceleration Command 2
        			// Label
        			label = new JLabel("Accel cmd 2");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 9:	// Force Command 2
        			// Label
        			label = new JLabel("Force cmd 2");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 10:	// Force Derivative Command 2
        			// Label
        			label = new JLabel("Force Deriv cmd 2");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 11:	// Displacement Command 3
        			// Label
        			label = new JLabel("Displacement cmd 3");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 12:	// Velocity Command 3
        			// Label
        			label = new JLabel("Velocity cmd 3");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;        			
        		case 13:	// Acceleration Command 3
        			// Label
        			label = new JLabel("Accel cmd 3");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 14:	// Force Command 3
        			// Label
        			label = new JLabel("Force cmd 3");
        		    label.setBounds(30,30+(i*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(160,30+(i*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(220,30+(i*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(5,30+(i*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 15:	// Force Derivative Command 3
        			// Label
        			label = new JLabel("Force Deriv cmd 3");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;        		
        		case 16:	// Displacement Command 4
        			// Label
        			label = new JLabel("Displacement cmd 4");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 17:	// Velocity Command 4
        			// Label
        			label = new JLabel("Velocity cmd 4");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 18:	// Acceleration Command 4
        			// Label
        			label = new JLabel("Accel cmd 4");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 19:	// Force Command 4
        			// Label
        			label = new JLabel("Force cmd 4");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 20:	// Force Derivative Command 4
        			// Label
        			label = new JLabel("Force Deriv cmd 4");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 21:	// Displacement Command 5
        			// Label
        			label = new JLabel("Displacement cmd 5");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 22:	// Velocity Command 5
        			// Label
        			label = new JLabel("Velocity cmd 5");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 23:	// Acceleration Command 5
        			// Label
        			label = new JLabel("Accel cmd 5");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 24:	// Force Command 5
        			// Label
        			label = new JLabel("Force cmd 5");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 25:	// Force Derivative Command 5
        			// Label
        			label = new JLabel("Force Deriv cmd 5");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(false);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 26:	// Custom 1
        			// Label
        			label = new JLabel("Custom 1");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(true);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 27:	// Custom 2
        			// Label
        			label = new JLabel("Custom 2");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(true);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 28:	// Custom 3
        			// Label
        			label = new JLabel("Custom 3");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(true);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		case 29:	// Custom 4
        			// Label
        			label = new JLabel("Custom 4");
        		    label.setBounds(330,30+((i-15)*25),125,25);        		    
        		    this.getContentPane().add(label);
        		    // Address TextField        		    
        		    addressTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    addressTextField[i].setBounds(460,30+((i-15)*25),55,25);
        		    addressTextField[i].setEditable(true);
        		    this.getContentPane().add(addressTextField[i]);
        		    // Value TextField        		    
        		    valueTextField[i].setHorizontalAlignment(JTextField.RIGHT);
        		    valueTextField[i].setBounds(520,30+((i-15)*25),55,25);
        		    valueTextField[i].setEditable(false);
        		    this.getContentPane().add(valueTextField[i]); 
        		    // Block CheckBox        		    
	                blockCheckBox[i].setBounds(305,30+((i-15)*25),20,20);
        		    this.getContentPane().add(blockCheckBox[i]); 
        			break;
        		default:
        	}
        }
        	
    	// Select All Button
    	selectAllButton = new JButton("Select All");
    	selectAllButton.setBounds(180,410,95,25);
    	selectAllButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {	   
	        	selectAllButtonEvent();
	        }});
    	this.getContentPane().add(selectAllButton);
    	
    	// Unselect All Button
    	unselectAllButton = new JButton("Unselect All");
    	unselectAllButton.setBounds(180,440,95,25);
    	unselectAllButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {	   
	        	unselectAllButtonEvent();
	        }});
    	this.getContentPane().add(unselectAllButton);
    	
    	// Rate TextField
    	// Label
		label = new JLabel("Ramping Rate");
	    label.setBounds(350,410,125,25);       		    
	    this.getContentPane().add(label);
	    // Label
		label = new JLabel("ms (Ticks)");
	    label.setBounds(400,440,125,25);       		    
	    this.getContentPane().add(label);
	    // Rate TextField   		    
	    rateTextField = new JTextField("1024");
	    rateTextField.setHorizontalAlignment(JTextField.RIGHT);
	    rateTextField.setBounds(350,440,45,25);
	    rateTextField.setEditable(true);		    
	    this.getContentPane().add(rateTextField);		    
	    // Zero Button
    	zeroButton = new JButton("Zero Ramp");
    	zeroButton.setBounds(480,410,95,60);
    	zeroButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {	   
	        	zeroButtonEvent();
	        }});
    	this.getContentPane().add(zeroButton);
    	// Counter Alert
    	alertLabel = new JLabel("Set Pulsar SCRAMNet On");
    	alertLabel.setHorizontalAlignment(JTextField.LEFT);
    	alertLabel.setBounds(15,440,200,25); 
    	alertLabel.setForeground(Color.RED);
    	alertLabel.setVisible(false);
	    this.getContentPane().add(alertLabel);
           
	    // Create a periodic Timer to update the Button state
	    timer = new Timer(250,this);
	    timer.start();
	  }  
	
	  /** Select All Button Event */
	  public void selectAllButtonEvent() {
		  for (int i = 0; i < 30; i++) {
			  blockCheckBox[i].setSelected(true);
		  }
	  }
	  
	  /** Unselect All Button Event */
	  public void unselectAllButtonEvent() {
		  for (int i = 0; i < 30; i++) {
			  blockCheckBox[i].setSelected(false);
		  }
	  }
	  
	  /** Zero Button Event */
	  public void zeroButtonEvent() {
		  zeroButton.setEnabled(false);
		  long baseTick;
		  float[] currentCommand = new float[30];
		  
		  // Create To Zero Ramp generators
		  ToZeroRampGenerator[] toZero = new ToZeroRampGenerator[30];		  		 		  
		  
		  // Get Rate
		  int lengthOfRamp = 1024;	// default value
		  // Check if it has a value that is a number		 
		  if (!rateTextField.getText().equals(""))	// get updated value 		  
			  lengthOfRamp = Integer.parseInt(rateTextField.getText().trim());
		  else
			  rateTextField.setText("1024");
		  // Can't be less than 1 second
		  if (lengthOfRamp < 1024) {
			  lengthOfRamp = 1024;
			  rateTextField.setText("1024");
		  }
          
          // Read the current SCRAMNet command data            
          for (int i = 0; i < 30; i++) {
        	  if (blockCheckBox[i].isSelected() == true) {
        		  toZero[i] = new ToZeroRampGenerator();
        		  if (!addressTextField[i].getText().equals("")) {
	        		  valueTextField[i].setText(format.format(scr.readFloat(Integer.parseInt(addressTextField[i].getText().trim()))));
	        		  currentCommand[i] = scr.readFloat(Integer.parseInt(addressTextField[i].getText().trim()));
        		  }
        	  }
          }
		  
          baseTick = scr.readGlobalCounter();
          long nextTick = baseTick;
          long count = 1;          
          System.out.println("Basetick: " + baseTick);          
          // Ramp Selected down to 0
          while (count <= lengthOfRamp) {            
        	  for (int i = 0; i < 30; i++) {
        		  if (blockCheckBox[i].isSelected() == true) {
        			  if (!addressTextField[i].getText().equals(""))
        				  scr.writeFloat(Integer.parseInt(addressTextField[i].getText().trim()), (float)toZero[i].generate(currentCommand[i], lengthOfRamp, count));        			  
        		  }        		  
        	  }
              // Wait for next tick
              nextTick = scr.readGlobalCounter();
              while(nextTick == scr.readGlobalCounter()) {}              
        	  //Increment the ramp to the next tick value to try to maintain speed
              count = 1 + nextTick - baseTick;                                
          }
          zeroButton.setEnabled(true);  
	  }
  
	  /** Updates the values */
	  public void actionPerformed(ActionEvent arg0) {
		  // Update values		  
		  for (int i = 0; i < 30; i++) {
			  if (!addressTextField[i].getText().equals(""))
				  valueTextField[i].setText(format.format(scr.readFloat(Integer.parseInt(addressTextField[i].getText().trim()))));
		  }
		  
		  // Check if the SCRAMNet Global Counter is on
		  currentTickCount = scr.readGlobalCounter();	
		  if (currentTickCount == previousTickCount) {
			  zeroButton.setEnabled(false);
			  alertLabel.setVisible(true);
		  }
		  else {
			  zeroButton.setEnabled(true);
			  alertLabel.setVisible(false);
		  }
		  
		  // Save the tick count for next check
		  previousTickCount = currentTickCount;
	  } 

	  /** Main Process */
	  public static void main(String[] args) {
		  try {
			  CommandZero cz = new CommandZero();
			  cz.setVisible(true);
		  } catch (Exception e) {e.printStackTrace();}
	  }
  
 
  
	  // Variables
	  private static final long serialVersionUID = -4520876572241672062L;	
	  // Textboxes
	  JLabel label;
	  JTextField[] addressTextField;
	  JTextField[] valueTextField; 
	  JTextField rateTextField;
	  JLabel alertLabel;
	  // Checkboxes
	  JCheckBox[] blockCheckBox;	 
	  // Buttons
	  JButton zeroButton;
	  JButton selectAllButton;
	  JButton unselectAllButton;
	  // Timer
	  protected Timer timer;	
	  // Timer data objects
	  long previousTickCount;
	  long currentTickCount;
	  // SCRAMNet
	  ScramNetIO scr;
	  // Formatter
	  DecimalFormat format = new DecimalFormat();   	  
	 

} 
