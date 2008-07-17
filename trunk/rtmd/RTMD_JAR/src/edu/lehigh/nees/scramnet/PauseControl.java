package edu.lehigh.nees.scramnet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


 /********************************* 
 * Pause Bit Control
 * <p>
 * Toggle the Pause Bit (SCRAMNet address 62)
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  3 Nov 05  T. Marullo  Initial
 *  3 Nov 05  T. Marullo  Added periodic timer to check memory state
 * 
 ********************************/
public class PauseControl extends JFrame implements ActionListener  {
  private static final long serialVersionUID = -5150944384853480893L;

  // SCRAMNet Object
  protected ScramNetIO scr = new ScramNetIO();
  
  // Images for Button states
  protected JButton button;
  protected ImageIcon off = new ImageIcon("/RTMD/res/off.png");
  protected ImageIcon on = new ImageIcon("/RTMD/res/on.png");
  
  // Timer
  protected Timer timer;
 
  /** Initialize the Button Graphics */
  public void init() {
	
	// Set form layout
	Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    // Window parameters
    this.setSize(200,200);
    this.setTitle("Pause Control Bit");
    this.setLocationRelativeTo(null);

    // Initialize the SCRAMNet
    scr.initScramnet();

    // Create a new Button instance
    button = new JButton();
    
    // Check the status of the bit first
    if (scr.readFloat(62) == 0)
		button.setIcon(off);
    else
		button.setIcon(on);

    // Add the button to the contentPane.
    contentPane.add(button, BorderLayout.CENTER);

    // Create a listener for the button
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
		if (scr.readFloat(62) == 0) {         /* Bit is 0, set to 1 */
            scr.writeFloat(62,1);
			System.out.println("On");
            button.setIcon(on);
		}
		else {                             /* Bit is 1, set to 0 */
            scr.writeFloat(62,0);
			System.out.println("Off");
			button.setIcon(off);
		}
      }
    });

    addWindowListener(new java.awt.event.WindowAdapter() {
           public void windowClosing(java.awt.event.WindowEvent evt) {
               System.exit(0);
           }
    });
    
    // Create a periodic Timer to update the Button state
    timer = new Timer(1000,this);
    timer.start();
  }  
  
  /** Updates the Button state */
  public void actionPerformed(ActionEvent arg0) {
	  if (scr.readFloat(62) == 0)
		  button.setIcon(off);
	  else
		  button.setIcon(on);	  
  } 

  /** Main Process */
  public static void main(String[] args) {
    PauseControl sr = new PauseControl();
    sr.init();
    sr.setVisible(true);
  } 
} 
