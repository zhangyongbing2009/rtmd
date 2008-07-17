
package edu.lehigh.nees.IntegratedControl.Ramp;

/********************************* 
 * Converger
 * <p>
 * Converges a signal to within a tolerance of a target
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  2 Nov 04  T. Marullo  Initial
 * 
 ********************************/
public class Converger {
    private int j;              // Converger try counter
    private int jmax;           // Max convergence tries
    private double tolmin;      // Min tolerance
    private double tolmax;      // Max tolerance
    private double target;      // Target Value
    private double measured;    // Measured Value
    private boolean isConverged;// Flag for convergence done
    private boolean stopTest;   // Stop test flad
    private String name;        // name of signal
    private String units;       // units of signal
            
    /** Creates a new instance of Converger <br>
     *  Takes a signal name, the units of the signal, a j_max value for the number of convergence steps <br>
     * and minumum and maximum tolerance values  
     */
    public Converger(String sig_name, String sig_units, int j_max, double tol_min, double tol_max) {
        j = 0;          		// Reset counter
        jmax = j_max;     		
        tolmin = tol_min;		
        tolmax = tol_max;        
        isConverged = false;
        stopTest = false;
		name = sig_name;
		units = sig_units;
    }
    
    /** Set the converger with a new target value to converge to */
    public void setTarget(double newTarget) {
        target = newTarget;         
        isConverged = false;
        stopTest = false;
    }    
    
    /** return a bias to add to the total command */
    public double converge(double newMeasured) {
        // Return values for pop up windows
        int response;   
        int response2;
        // Get current value
        measured = newMeasured;
        
        // Max tolerance test
        // Check if the absolute value of the difference is greater than the tolerance
        // If it is, try to converge        
        if (Math.abs(target - measured) > tolmax) {     // Didn't Converge
            String s = "Target= "+target+"\nMeasured= "+measured+"\nDifference= "+Math.abs(target-measured)+"\nDifference exceeds max tolerance, "+tolmax+" "+units+"\nStop Test?";
            response = javax.swing.JOptionPane.showOptionDialog(null,s,"Convergence for " + name,javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE,null, null, null);
            switch (response) {
                case 0: // YES, STOP TEST
                    stopTest = true;                    // Set flag true
                    return 0.0;
                case 1: // NO, DONT STOP TEST
                    // Ask if user wants to redefine the max tolerance
                    String s2 = "Redefine max tolerance of " + tolmax + " "+units+"?";
                    response2 = javax.swing.JOptionPane.showOptionDialog(null,s2,"Convergence for " + name,javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE,null, null, null);
                    switch (response2) {    // YES, Redefine
                        case 0: // Redefine max tolerance
                        tolmax = Double.parseDouble(javax.swing.JOptionPane.showInputDialog(null,s2));
                    }
            };
        }
        
        // Min tolerance test
        // Check if the absolute value of the difference is less than the tolerance
        // If it isn't
        if (Math.abs(target - measured) < tolmin) {     // Converged
            isConverged = true;                         // Set flag true
            j = 0;                                      // Reset Counter
            return 0.0;
        }
        else {                                          // Didn't converge
            if (j <= jmax) {                            // Check if there are tries left
                j++;                                    // Try again
                return 0.8*(target - measured);         // Return 80% of the difference
            }
            else {                                      // End of tries
                // Ask user to stop trying
                String s = "Number of convergence steps reached limit.  Stop test?";
                response = javax.swing.JOptionPane.showOptionDialog(null,s,"Convergence for " + name,javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE,null, null, null);
                switch (response) {
                    case 0: // YES, STOP                        
                        stopTest = true;
                        return 0.0;                            
                    case 1: // NO, DON'T STOP      
                        // Ask if user wants to redefine max convergence steps
                        String s2 = "Redefine max number of convergence steps of " + jmax + "?";
                        response2 = javax.swing.JOptionPane.showOptionDialog(null,s2,"Convergence for " + name,javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE,null, null, null);
                        switch (response2) {
                           case 0: // Redefine max convergence steps
                           jmax = Integer.parseInt(javax.swing.JOptionPane.showInputDialog(null,s2));
                        }
                        // Ask if user wants to redefine min tolerance
                        s2 = "Target= "+target+"\nMeasured= "+measured+"\nDifference= "+Math.abs(target-measured)+"\nDifference exceeds min tolerance, "+tolmin+" "+units+"\nRedefine min tolerance of " + tolmin+ " "+units+"?";
                        response2 = javax.swing.JOptionPane.showOptionDialog(null,s2,"Convergence for " + name,javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE,null, null, null);
                        switch (response2) {
                           case 0: // Redefine min tolerance
                               tolmin = Double.parseDouble(javax.swing.JOptionPane.showInputDialog(null,s2));
                        }
                    j = 0;                              // Try again
                    return 0.8*(target - measured);     // return 80% of difference                             
                };
            }
        }
        // Everything failed, stop test
        stopTest = true;
        // Alert user that Convergence failed and the test should be stopped
        javax.swing.JOptionPane.showMessageDialog(null,"Unrecoverable error in Converger.  Cancel Test", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE); 
        return 0.0;
    }
    
    /** Get the target value to converge to */
    public double getTarget() {
        return target;
    }
    
    /** Did the value converge within limits? */
    public boolean isConverged() {
        return isConverged;
    }
    
    /** Get the stopTest flag */
    public boolean stopTest() {
        return stopTest;
    }
         
    /** Tester */
    public static void main(String[] args) {    
        double target = 0.0;
        double meas = 0.0;
        double converge_offset = 0.0;
        double tolmax = 10;
        double tolmin = 5;
        Converger c = new Converger("test","units",5, tolmin, tolmax);
                
        // pretend this is a cycle
        // set a displacement target
        target = 30;
        System.out.println("Target set = " + target);

        // start a converger object for that displacement
        c.setTarget(target);   
        
        // read measurements
        meas = 25;
        System.out.println("Measurements received = " + meas);        
        
        // Only impose 80% of that displacement
        target *= 0.8;
        System.out.println("80% of Target received = " + target);
              
        // check if the measurments were already converged   
        converge_offset = c.converge(meas);
        while (!c.isConverged() && !c.stopTest()) {     /* Didn't converge */                    
            System.out.println("Convergence was not met\nOffset of "+converge_offset+" added");
            target += converge_offset;                    
            System.out.println("New Target of "+target+ " imposed to structure");    
            //read measurements
            meas+=1;
            System.out.println("Measurements received = " + meas);
            converge_offset = c.converge(meas);
        }
        if (c.stopTest())
            System.out.println("Test Stopped");
        else 
            System.out.println("Original Target = "+c.getTarget()+"\nMeasurement = "+meas+"\nConverged!");
      
              
        System.out.println("Done");
        
        System.exit(0);
    }
    
    
    
}
