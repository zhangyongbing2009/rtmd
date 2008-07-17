package edu.lehigh.nees.scramnet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/********************************* 
  * Clear SCRAMNet Button
  * 
  * Graphical Button that allows user to clear the entire SCRAMNet memory block
  *
  * @author Thomas Marullo
  *
  * <b><u>Revisions</u></b><br>
  * 16 Aug 05  T. Marullo   Initial Version
  * 14 Feb 08  T. Marullo   Changed to write explicitly to all memory locations
  * 
  *********************************
 */
public class ClearSCR extends JFrame {
  private static final long serialVersionUID = 5473027905061892950L;
  protected ScramNetIO scr = new ScramNetIO();
  protected JButton button;
  protected ImageIcon clear = new ImageIcon("/RTMD/res/clear.png");
 
  /** Initializes the JFrame for the button graphic */
  public void init() {
    
    // Get the content pane to set the layout
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    // Set window parameters
    this.setSize(200,200);
    this.setTitle("Clear SCRAMNet");
    this.setLocationRelativeTo(null);

    // Initialize the SCRAMNet
    scr.initScramnet();

    // Create a new button instance
    button = new JButton(clear);

    // Add the button to the contentPane.
    contentPane.add(button, BorderLayout.CENTER);

    // Create a listener for the button
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
	   for (int i = 0 ; i <= 2097151; i++)
		   scr.writeInt(i,0);
      }
    });
      
    addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
    });
  }

  /** Main process for running the button */
  public static void main(String[] args) {
    // Create a new Clear SCRAMNet object
    ClearSCR sr = new ClearSCR();
    
    // Initialize and Show the button
    sr.init();
    sr.setVisible(true);
  } 
} 
