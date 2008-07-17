package edu.lehigh.nees.IntegratedControl.Ramp;

/********************************* 
 * Ramp Generator
 * <p>
 * Ramp Generator for command signals
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 
 ********************************/
public class RampGenerator {
    private int type;    
    private boolean isDone = false;
    private boolean useFeedback = false;
    double initVal = 0.0;   
    double prevVal = 0.0;
 
    /** Creates a new instance of RampGenerator 
     * @param rampType "none" "Linear" "Sine" "Haversine"
     * @param initial_value Initial Value of the ramp
     */
    public RampGenerator(String rampType, double initial_value) {
        if (rampType.equals("none"))
            type = 0;
        else if (rampType.equals("Linear")) {
            type = 1;
            initVal = prevVal = initial_value;
        }
        else if (rampType.equals("Sine")) {
            type = 2;
            initVal = prevVal = initial_value;
        }
        else if (rampType.equals("Haversine")) {
            type = 3;
            initVal = prevVal = initial_value;
        }
        else
            type = -1;            
    }
    
    /** Generate the proper interpolated value depending on ramp type and step count
     * @param finalVal Final value to achieve
     * @param currentVal Current value (not used if useFeedback flag is not set)
     * @param totalSteps Total number of steps to ramp over
     * @param currentStep Current step in ramp
     * @return Generated value of ramp
     */
    public double generate(double finalVal, double currentVal, long totalSteps, long currentStep) {
        double nextVal = 0.0;
        double slope = 0.0; 
        double freq = 0.0;                
        
        // Initial step for ramp
        if (currentStep == 1 && useFeedback)
        	// Store the current value to adjust the starting point of the ramp
        	initVal = currentVal;
        else if (currentStep == 1 && !useFeedback) {
        	// Ignore the real feedback and just assume the previous final value was reached
        	// And, store the final value as the next previous value
        	initVal = prevVal;
        	prevVal = finalVal;
        }
        
        switch (this.type) {
            case 0: // None
                nextVal = finalVal;
                isDone = true;
                break;
            case 1: // Linear
                // Determine the slope of the equation
                slope = (finalVal - initVal)/totalSteps;

                // Generate the next value
                nextVal = slope * currentStep + initVal;
                break;
            case 2: // Sine                
                // Calculate a quarter period
                freq = (java.lang.Math.PI)/totalSteps;
 
                // Generate the next value
                nextVal = (finalVal-initVal) * java.lang.Math.sin(((freq*currentStep)/2)) + initVal;                
                break;
            case 3: // Haversine
                // Calculate a quarter period
                freq = (java.lang.Math.PI)/totalSteps;
           
                // Generate the next value
                nextVal = (finalVal-initVal) * java.lang.Math.pow(java.lang.Math.sin(((freq*currentStep)/2)),2) + initVal;
                break;
            default:
                System.err.println("Not a valid ramp type");
        }

        // At the end of the ramp so set the isDone flag true
        if (currentStep >= totalSteps)
            isDone = true;
         
        return nextVal;        
    }
    
    /** Set type of ramp generator <br>
     *  1 = Linear <br>
     *  2 = Sine <br>
     *  3 = Haversine
     * @param t Ramp Type
     */
    public void setType(int t) {
        if (type < 1 || type > 3) {
            System.err.println("Not a valid ramp type");
            System.exit(1);
        }
        else
            type = t;
    }
    
    /** Get type of ramp generator <br>
     *  0 = None <br>
     *  1 = Linear <br>
     *  2 = Sine <br>
     *  3 = Haversine    
     */
    public int getType() {
        return type;
    }
    
    /** Check if the ramp generator is done */
    public boolean isDone() {
        return isDone;
    }
    /** Set the isDone flag */
    public void setisDone(boolean b) {
        isDone = b;
    }
    
    /** Get the useFeedback flag */    
    public boolean getUseFeedback() {
        return useFeedback;
    }
    
    /** Allow the use of location feedback to adjust the start of the ramp */
    public void setUseFeedback(boolean b) {
    	useFeedback = b;
    }
    
    /** Test case */
    public static void main(String[] args) {
        RampGenerator rg = new RampGenerator("Haversine", 5.0);
        rg.setUseFeedback(false);
               
        int steps = 10;
        
        System.out.println("Step");        
	double c = 5.0;
        double f = 10.0;
        for (int i = 1; i <= steps; i++) {
            c = rg.generate(f, c, steps, i);
  	    System.out.println(c);
        }

        System.out.println("Step");
	c = 13.0;
        f = 25.0;
	    for (int i = 1; i <= steps; i++) {
            c = rg.generate(f, c, steps, i);
  	    System.out.println(c);
        }

        System.out.println("Step");
        c = 29.0;
        f = -10.0;
        for (int i = 1; i <= steps; i++) {
            c = rg.generate(f, c, steps, i);
  	    System.out.println(c);  	    
        }
    }
}
