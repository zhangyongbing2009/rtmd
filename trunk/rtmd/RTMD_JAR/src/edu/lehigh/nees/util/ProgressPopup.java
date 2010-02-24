/**
 * 
 */
package edu.lehigh.nees.util;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * @author Tommy
 *
 */
public class ProgressPopup implements Runnable {
	/** Variables **/
	private static final long serialVersionUID = 1L;
	// progress bar
	private JProgressBar progressBar;	
	private JDialog popup;		
	// flag
	private boolean isDone;
	
	/** Constructor */
	public ProgressPopup(String _title, String _label) {
		progressBar = new JProgressBar();
		progressBar.setBounds(0,0,100,25);						
		popup = new JDialog();
		popup.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		popup.setTitle(_title);
		popup.setResizable(false);
		popup.setBounds(0,0,300,70);
		popup.setLocationRelativeTo(null);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.setAlwaysOnTop(true);		
		JLabel label = new JLabel(_label);
		label.setHorizontalAlignment(JLabel.CENTER);
		popup.getContentPane().add(label,BorderLayout.NORTH);    	
		popup.getContentPane().add(progressBar,BorderLayout.CENTER);
		popup.pack();
		
		isDone = false;
			
	}
	
	/** isDone setter */
	public void setIsDone(boolean b) {
		isDone = b;
	}
	
	/** isDone getter */
	public boolean getIsDone() {
		return isDone;
	}
	
	public void setIndeterminate(boolean b) {
		progressBar.setIndeterminate(b);
	}
	
	public void setMaximum(int i) {
		progressBar.setMaximum(i);
		progressBar.setIndeterminate(false);
	}
	
	public void setMinimum(int i) {
		progressBar.setMinimum(i);
		progressBar.setIndeterminate(false);
	}
	
	public void setValue(int i) {
		progressBar.setValue(i);
	}

	/** Load Data */
	public void run() {		
		popup.setVisible(true);	    		
		
		// Show popup until 		
		while (!isDone) {			
			try{Thread.sleep(100);}catch(Exception e){}			
		}
						
		// Close progress bar
		popup.setVisible(false);				
	}
}