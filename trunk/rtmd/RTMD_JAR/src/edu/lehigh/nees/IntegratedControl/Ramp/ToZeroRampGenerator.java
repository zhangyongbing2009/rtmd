package edu.lehigh.nees.IntegratedControl.Ramp;

/********************************* 
 * To Zero Ramp Generator
 * <p>
 * This ramps a command from its current position to 0
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 
 ********************************/
public class ToZeroRampGenerator{
    private boolean isDone = false;
    private double initVal = 0.0;
 
    /** Creates a new instance of ToZeroRampGenerator     
     */
    public ToZeroRampGenerator() {        
    }
    
    /** Generate the proper interpolated value depending on step count
     * @param currentVal Current value (not used if useFeedback flag is not set)
     * @param totalSteps Total number of steps to ramp over
     * @param currentStep Current step in ramp
     * @return Generated value of ramp
     */
    public double generate(double currentVal, long totalSteps, long currentStep) {
        double nextVal = 0.0;
        double slope = 0.0; 
             
        // Initial step for ramp
        if (currentStep == 1)
        	// Store the current value to adjust the starting point of the ramp
        	initVal = currentVal;        
        
        // Linear
        // Determine the slope of the equation
        slope = (0 - initVal)/totalSteps;

        // Generate the next value
        nextVal = slope * currentStep + initVal;
        
        // At the end of the ramp so set the isDone flag true
        if (currentStep >= totalSteps)
            isDone = true;
         
        return nextVal;        
    }
            
    /** Check if the ramp generator is done */
    public boolean isDone() {
        return isDone;
    }
    /** Set the isDone flag */
    public void setisDone(boolean b) {
        isDone = b;
    }        
    
    /** Test case */
    public static void main(String[] args) {
        ToZeroRampGenerator tzrg = new ToZeroRampGenerator();
               
        int steps = 5120;
        
        System.out.println("Step");        
	    double c = 15.0;
        for (int i = 1; i <= steps; i++) {
            c = tzrg.generate(c, steps, i);
  	      System.out.println(c);
        }
    }
}
