package edu.lehigh.nees.IntegratedControl.Selector;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JCheckBox ;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import org.nfunk.jep.JEP;
import edu.lehigh.nees.scramnet.*;

/********************************* 
 * SCRAMNet Spy
 * <p>
 * View any channel on the SCRAMNet and manipulate it too
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 24 Apr 09  T. Marullo   Initial
 *  						
 ********************************/
public class SCRAMNetSpy extends JPanel implements ActionListener {
	/** Variables */
	protected static final long serialVersionUID = -4520876575241672062L;	
	protected int address = 0;
	protected float value = 0;
	protected String expr = "value";
	protected double euvalue = 0;
	// Textboxes
	protected JLabel label;
	protected JTextField addressTextField;
	protected JTextField valueTextField;
	protected JTextField exprTextField;	  
	// Checkboxes
	protected JCheckBox daqCheckBox;
	protected JCheckBox valueCheckBox;
	// Timer
	protected Timer timer;	  
	// Expression parser
	protected JEP expression;
	// SCRAMNet
	protected ScramNetIO scr;
	// Formatter
	protected DecimalFormat format = new DecimalFormat();        

	/** New Spy object */
	public SCRAMNetSpy(ScramNetIO _scr) {
		// Initialize the expression parsers
		expression = new JEP();
		expression.addStandardConstants();
		expression.addStandardFunctions();		
		format.setMaximumFractionDigits(6);

		// SCRAMNet hook
		scr = _scr;		

		// Initialize the panel
		init();		
	}

	/** Initialize the Button Graphics */
	public void init() {	
		// Set form layout
		this.setLayout(null);

		// Window parameters
		this.setPreferredSize(new Dimension(200,48));
		
		// Create Textfields
		// Address
		label = new JLabel("Addr");
		label.setBounds(0,0,35,20);
		this.add(label);    
		addressTextField = new JTextField("0");
		addressTextField.setHorizontalAlignment(JTextField.RIGHT);
		addressTextField.setBounds(35,0,45,20);		
		addressTextField.setToolTipText("SCRAMNet Spy: Enter a SCRAMNet address");
		addressTextField.addActionListener(this);
		this.add(addressTextField);
		// Value
		valueTextField = new JTextField("0");
		valueTextField.setHorizontalAlignment(JTextField.RIGHT);
		valueTextField.setBounds(80,0,70,20);
		valueTextField.setBackground(null);
		valueTextField.setToolTipText("SCRAMNet Spy: The value at that address");
		valueTextField.setEditable(false);	         
		this.add(valueTextField);
		// Check box for showing raw value or expr value
		valueCheckBox = new JCheckBox();
		valueCheckBox.setText("Raw");
		valueCheckBox.setBounds(150,0,55,20);
		valueCheckBox.setBackground(null);
		valueCheckBox.setToolTipText("SCRAMNet Spy: Checked shows the raw value, unchecked shows the calculated expression");
		valueCheckBox.addActionListener(this);
		this.add(valueCheckBox);
		// Expression For Value
		label = new JLabel("Expr");
		label.setBounds(0,25,35,20);
		this.add(label); 
		exprTextField = new JTextField("value");
		exprTextField.setHorizontalAlignment(JTextField.RIGHT);
		exprTextField.setBounds(35,25,115,20);		
		exprTextField.setToolTipText("SCRAMNet Spy: Enter an expression to calculate a value, 'value' is the raw value at the SCRAMNet location");
		exprTextField.addActionListener(this);
		this.add(exprTextField);
		// Check box for DAQ template
		daqCheckBox = new JCheckBox();
		daqCheckBox.setText("DAQ");
		daqCheckBox.setBounds(150,25,70,20);
		daqCheckBox.setBackground(null);
		daqCheckBox.setToolTipText("SCRAMNet Spy: Provide the template for showing a DAQ value");
		daqCheckBox.addActionListener(this);
		this.add(daqCheckBox);
			
	}  
	
	/** Perform actions */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		
		// Address field
		if (source == addressTextField) {
			address = Integer.parseInt(evt.getActionCommand());
		}		
		// Expression field
		if (source == exprTextField) {
			expr = evt.getActionCommand();
		}		
		// DAQ Check Box
		if (source == daqCheckBox) {
			if (daqCheckBox.isSelected()) 
				exprTextField.setText("((((((10000.0 / gain)/32768.0) * value) * Vslope) + Voffset) * EUslope) + EUoffset");
			else
				exprTextField.setText("value");
		}		
	}

	/** Updates the values */
	public void updateValues() {		
		// Get value from SCRAMNet
		if (daqCheckBox.isSelected())
			value = scr.readDAQcounts(String.valueOf(address));
		else
			value = scr.readFloat(address);
		
		// Check whether to show the raw value or the expression value
		if (valueCheckBox.isSelected()) {
			// Show Raw value
			valueTextField.setText(String.valueOf(format.format(value)));
		}
		else {			
			// Show expression
			expression.addVariable("value",value);
			expression.parseExpression(expr);
			euvalue = expression.getValue();
			valueTextField.setText(String.valueOf(format.format(euvalue)));
		}		
	}	
} 
