package edu.lehigh.nees.scramnet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JCheckBox ;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import org.nfunk.jep.JEP;

/********************************* 
 * SCRAMNet Spy
 * <p>
 * View any channel on the SCRAMNet and manipulate it too
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  8 Nov 05  T. Marullo   Initial
 * 10 Nov 05  T. Marullo   Added DAQ Template 
 *  						
 ********************************/
public class Spy extends JFrame implements ActionListener {
 
	/** New Spy object */
	public Spy() {
		// Initialize the expression parsers
		expression = new JEP();
		expression.addStandardConstants();
		expression.addStandardFunctions();		
		format.setMaximumFractionDigits(6);
				
		// SCRAMNet
		scr = new ScramNetIO();
		scr.initScramnet();
		
		init();		
	}
  
	/** Initialize the Button Graphics */
	public void init() {	
		// Set form layout
		Container contentPane = getContentPane();
	    contentPane.setLayout(new BorderLayout());
	
	    // Window parameters
	    this.setSize(300,160);
	    this.setTitle("SCRAMNet Spy");
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setResizable(false);                
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
        // Set up UI Look and Feel
        try {            
            UIManager.setLookAndFeel(new com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel());
        } catch(Exception e) {System.out.println("Error setting Look and Feel: " + e);}

	
	    // Create Textfields
	    // Address
	    label = new JLabel("Address");
	    label.setBounds(10,10,85,25);
	    this.getContentPane().add(label);    
	    addressTextField = new JTextField("0");
	    addressTextField.setHorizontalAlignment(JTextField.RIGHT);
	    addressTextField.setBounds(80,10,80,25);
	    addressTextField.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	address = Integer.parseInt(evt.getActionCommand());	        	
	        }});
	    this.getContentPane().add(addressTextField);
	    // Value
	    valueTextField = new JTextField("0");
	    valueTextField.setHorizontalAlignment(JTextField.RIGHT);
	    valueTextField.setBounds(165,10,120,25);
	    valueTextField.setEditable(false);	         
	    this.getContentPane().add(valueTextField);
	    // Expression For Value
	    label = new JLabel("Expression");
	    label.setBounds(10,50,85,25);
	    this.getContentPane().add(label); 
	    exprTextField = new JTextField("value");
	    exprTextField.setHorizontalAlignment(JTextField.RIGHT);
	    exprTextField.setBounds(80,50,205,25);
	    exprTextField.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	expr = evt.getActionCommand();	        	
	        }});
	    this.getContentPane().add(exprTextField);
	    // Results for Value
	    label = new JLabel("(ie:  value * 3)");
	    label.setBounds(10,90,120,25);
	    this.getContentPane().add(label); 
	    euvalueTextField = new JTextField();
	    euvalueTextField.setHorizontalAlignment(JTextField.RIGHT);
	    euvalueTextField.setBounds(165,90,120,25);
	    euvalueTextField.setEditable(false);	         
	    this.getContentPane().add(euvalueTextField);       
	    // Check box for DAQ template
	    daqCheckBox = new JCheckBox();
	    daqCheckBox.setText("DAQ");
	    daqCheckBox.setBounds(100,90,120,25);
	    daqCheckBox.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {	   
	        	if (daqCheckBox.isSelected()) 
	        		exprTextField.setText("((((((10000.0 / gain)/32768.0) * value) * Vslope) +Voffset) * EUslope) + EUoffset");
	        	else
	        		exprTextField.setText("value");
	        }});
	    this.getContentPane().add(daqCheckBox);
	    	    
	    // Create a periodic Timer to update the Button state
	    timer = new Timer(1000,this);
	    timer.start();
	  }  
  
	  /** Updates the values */
	  public void actionPerformed(ActionEvent arg0) {		
		  // Get value from SCRAMNet
		  if (daqCheckBox.isSelected())
			  value = scr.readDAQcounts(String.valueOf(address));
		  else
			  value = scr.readFloat(address);		  
		  expression.addVariable("value",value);
		  expression.parseExpression(expr);
		  euvalue = expression.getValue();
		  
		  // Update GUI
		  valueTextField.setText(String.valueOf(format.format(value)));
		  euvalueTextField.setText(String.valueOf(format.format(euvalue)));
	  } 

	  /** Main Process */
	  public static void main(String[] args) {
		  Spy spy = new Spy();
		  spy.setVisible(true);
	  }
  
 
  
	  // Variables
	  private static final long serialVersionUID = -4520876575241672062L;	
	  private int address = 0;
	  private float value = 0;
	  private String expr = "value";
	  private double euvalue = 0;
	  // Textboxes
	  JLabel label;
	  JTextField addressTextField;
	  JTextField valueTextField;
	  JTextField exprTextField;
	  JTextField euvalueTextField;	  
	  // Checkbox
	  JCheckBox daqCheckBox;	  
	  // Timer
	  protected Timer timer;	  
	  // Expression parser
	  JEP expression;
	  // SCRAMNet
	  ScramNetIO scr;
	  // Formatter
	  DecimalFormat format = new DecimalFormat();        
         
	  
	 

} 
