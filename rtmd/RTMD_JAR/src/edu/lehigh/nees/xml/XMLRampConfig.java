package edu.lehigh.nees.xml;

/********************************* 
 * XMLRampConfig
 * <p>
 * Ramp Generator XML configuration utility
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 
 ********************************/
public class XMLRampConfig {
    private boolean isOn = false;
    private int numActuators;
    private String[] rampType;
    private int ticks;
    private boolean[] useFeedback;
    
    
    /** Creates a new instance of XMLRampConfig */
    public XMLRampConfig() {
    }
    
    // Getters and Setters
    /** Set Object On */
    public void setIsOn(boolean bool) {
        isOn = bool;
    }
    /** Get isOn Flag */
    public boolean getIsOn() {
        return isOn;
    }
    /** Set number of Actuators */
    public void setnumActuators(int i) {
        numActuators = i;
        rampType = new String[numActuators];   
        useFeedback = new boolean[numActuators];
    }
    /** Get number of Actuators */
    public int getnumActuators() {
        return numActuators;
    }
    /** Set Ramp Type */    
    public void setrampType(int actID, String type) {
        rampType[actID] = type;
    }
    /** Get Ramp Type */
    public String getrampType(int actID) {
        return rampType[actID];
    }
    /** Set number of ticks to ramp */
    public void setticks(int i) {
        ticks = i;
    }
    /** Get number of ticks to ramp */
    public int getticks() {
        return ticks;
    }
    /** Set useFeedback Flag */
    public void setUseFeedback(int actID, String bool) {
    	if (bool.equals("true"))
    		useFeedback[actID] = true;
    	else
    		useFeedback[actID] = false;
    }
    /** Get useFeedback Flag */
    public boolean getUseFeedback(int actID) {
        return useFeedback[actID];
    }
    
    /** Print Configuration */
    public void print() {
        System.out.println("Ramp Generators On? " + isOn);
        if (isOn) {
            System.out.println("Ramp Generators");
            for (int i = 0; i < numActuators; i++) {
                System.out.println("Actuator " + i + " = " + rampType[i]);   
                System.out.println("Ticks = " + ticks);
                System.out.println("Use Feedback? " + String.valueOf(useFeedback[i]));
            }
        }
    }
    
}
