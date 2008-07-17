package edu.lehigh.nees.xml;

/********************************* 
 * XMLKinematicsConfig
 * <p>
 * Kinematics XML configuration utility
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 
 ********************************/
public class XMLKinematicsConfig {
    private boolean isOn = false;    
    private SPNconfig spn = new SPNconfig();
    private String type;

    
    /** Creates a new instance of XMLKinematicsConfig */
    public XMLKinematicsConfig() {        
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
    /** Set type */
    public void setType(String s) {
        type = s;
    }
    /** Get type */
    public String getType() {
        return type;
    }
    /** Set SPNx */
    public void setSPNx(double spnx) {
        spn.SPNx = spnx;        
    }
    /** Get SPNx */
    public double getSPNx() {
        return spn.SPNx;
    }
    /** Set SPNy */
    public void setSPNy(double spny) {
        spn.SPNy = spny;        
    }
    /** Get SPNy */
    public double getSPNy() {
        return spn.SPNy;
    }
    /** Set SPNz */
    public void setSPNz(double spnz) {
        spn.SPNz = spnz;        
    }
    /** Get SPNz */
    public double getSPNz() {
        return spn.SPNz;
    }   
    /** Set Length */
    public void setLength(double l) {
        spn.length = l;        
    }
    /** Get Length */
    public double getLength() {
        return spn.length;
    }
    /** Set Height */
    public void setHeight(double h) {
        spn.height = h;        
    }
    /** Get Height */
    public double getHeight() {
        return spn.height;
    }
    /** Set Number of Actuators which creates all the actuator devices */
    public void setnumActuators(int i) {
        spn.numActuators = i;
        spn.ASNx = new double[spn.numActuators];
        spn.ASNy = new double[spn.numActuators];
        spn.ASNz = new double[spn.numActuators];
        spn.AFNx = new double[spn.numActuators];
        spn.AFNy = new double[spn.numActuators];
        spn.AFNz = new double[spn.numActuators];
        spn.actlengths = new double[spn.numActuators];
    }  
    /** Get Number of Actuators */
    public int getnumActuators() {
        return spn.numActuators;
    }
    /** Set Number of Devices which creates all the measurement devices */
    public void setnumDevices(int i) {
        spn.numDevices = i;
        spn.MSNx = new double[spn.numDevices];
        spn.MSNy = new double[spn.numDevices];
        spn.MSNz = new double[spn.numDevices];
        spn.MFNax = new double[spn.numDevices];
        spn.MFNay = new double[spn.numDevices];
        spn.MFNaz = new double[spn.numDevices];
        spn.MFNbx = new double[spn.numDevices];
        spn.MFNby = new double[spn.numDevices];
        spn.MFNbz = new double[spn.numDevices];
        spn.devlengthsA = new double[spn.numDevices];
        spn.devlengthsB = new double[spn.numDevices];        
    }  
    /** Get number of Devices */
    public int getnumDevices() {
        return spn.numDevices;
    }
    /** Set ASNx */
    public void setASNx(int actID, double x) {
        spn.ASNx[actID] = x;
    }
    /** Get ASNx */
    public double getASNx(int actID) {
        return spn.ASNx[actID];
    }
    /** Set ASNy */
    public void setASNy(int actID, double y) {
        spn.ASNy[actID] = y;
    }
    /** Get ASNy */
    public double getASNy(int actID) {
        return spn.ASNy[actID];
    }
    /** Set ASNz */
    public void setASNz(int actID, double z) {
        spn.ASNz[actID] = z;
    }
    /** Get ASNz */
    public double getASNz(int actID) {
        return spn.ASNz[actID];
    }
    /** Set AFNx */
    public void setAFNx(int actID, double x) {
        spn.AFNx[actID] = x;
    }
    /** Get AFNx */
    public double getAFNx(int actID) {
        return spn.AFNx[actID];
    }
    /** Set AFNy */
    public void setAFNy(int actID, double y) {
        spn.AFNy[actID] = y;
    }
    /** Get AFNy */
    public double getAFNy(int actID) {
        return spn.AFNy[actID];
    }
    /** Set AFNz */
    public void setAFNz(int actID, double z) {
        spn.AFNz[actID] = z;
    }
    /** Get AFNz */
    public double getAFNz(int actID) {
        return spn.AFNz[actID];
    }
    /** Set MSNx */
    public void setMSNx(int devID, double x) {
        spn.MSNx[devID] = x;
    }
    /** Get MSNx */public double[] MFNlocaly = new double[3];
    public double getMSNx(int devID) {
        return spn.MSNx[devID];
    }
    /** Set MSNy */
    public void setMSNy(int devID, double y) {
        spn.MSNy[devID] = y;
    }
    /** Get MSNy */
    public double getMSNy(int devID) {
        return spn.MSNy[devID];
    }
    /** Set MSNz */
    public void setMSNz(int devID, double z) {
        spn.MSNz[devID] = z;
    }
    /** Get MSNz */
    public double getMSNz(int devID) {
        return spn.MSNz[devID];
    }
    /** Set MFNx */
    public void setMFNx(int devID, double x) {
        spn.MFNx[devID] = x;
    }
    /** Get MFNx */
    public double getMFNx(int devID) {
        return spn.MFNx[devID];
    }
    /** Set MFNy */
    public void setMFNy(int devID, double y) {
        spn.MFNy[devID] = y;
    }
    /** Get MFNy */
    public double getMFNy(int devID) {
        return spn.MFNy[devID];
    }
    /** Set MFNz */
    public void setMFNz(int devID, double z) {
        spn.MFNz[devID] = z;
    }
    /** Get MFNz */
    public double getMFNz(int devID) {
        return spn.MFNz[devID];
    }
    /** Set MFNax */
    public void setMFNax(int devID, double x) {
        spn.MFNax[devID] = x;
    }
    /** Get MFNax */
    public double getMFNax(int devID) {
        return spn.MFNax[devID];
    }
    /** Set MFNay */    
    public void setMFNay(int devID, double y) {
        spn.MFNay[devID] = y;
    }
    /** Get MFNay */ 
    public double getMFNay(int devID) {
        return spn.MFNay[devID];
    }
    /** Set MFNaz */    
    public void setMFNaz(int devID, double z) {
        spn.MFNaz[devID] = z;
    }
    /** Get MFNaz */ 
    public double getMFNaz(int devID) {
        return spn.MFNaz[devID];
    }
    /** Set MFNbx */    
    public void setMFNbx(int devID, double x) {
        spn.MFNbx[devID] = x;
    }
    /** Get MFNbx */
    public double getMFNbx(int devID) {
        return spn.MFNbx[devID];
    }
    /** Set MFNby */
    public void setMFNby(int devID, double y) {
        spn.MFNby[devID] = y;
    }
    /** Get MFNby */
    public double getMFNby(int devID) {
        return spn.MFNby[devID];
    }
    /** Set MFNbz */
    public void setMFNbz(int devID, double z) {
        spn.MFNbz[devID] = z;
    }
    /** Get MFNbz */
    public double getMFNbz(int devID) {
        return spn.MFNbz[devID];
    }
    /** Set MFNlocalx */
    public void setMFNlocalx(int devID, double x) {
        spn.MFNlocalx[devID] = x;
    }
    /** Get MFNlocalx */
    public double getMFNlocalx(int devID) {
        return spn.MFNlocalx[devID];
    }
    /** Set MFNlocaly */
    public void setMFNlocaly(int devID, double y) {
        spn.MFNlocaly[devID] = y;
    }
    /** Get MFNlocaly */
    public double getMFNlocaly(int devID) {
        return spn.MFNlocaly[devID];
    }
    /** Set MFNlocalz */
    public void setMFNlocalz(int devID, double z) {
        spn.MFNlocalz[devID] = z;
    }
    /** Get MFNlocalz */
    public double getMFNlocalz(int devID) {
        return spn.MFNlocalz[devID];
    }
    /** Set Actuator Length */
    public void setActLength(int actID, double l) {
        spn.actlengths[actID] = l;
    }
    /** Get Actuator Length */
    public double getActLength(int actID) {
        return spn.actlengths[actID];
    }
    /** Set Device Length */
    public void setDevLength(int devID, double l) {
        spn.devlengths[devID] = l;
    }
    /** Get Device Length */
    public double getDevLength(int devID) {
        return spn.devlengths[devID];
    }
    /** Set Device A Length */
    public void setDevLengthA(int devID, double l) {
        spn.devlengthsA[devID] = l;
    }
    /** Get Device A Length */
    public double getDevLengthA(int devID) {
        return spn.devlengthsA[devID];
    }
    /** Set Device B Length */    
    public void setDevLengthB(int devID, double l) {
        spn.devlengthsB[devID] = l;
    }
    /** Get Device B Length */
    public double getDevLengthB(int devID) {
        return spn.devlengthsB[devID];
    }
       
    /** Print the configuration */
    public void print() {
        System.out.println("Kinematics On? " + isOn);
        if (isOn) {                            
            if (type.equals("Incremental")) {
                System.out.println(type);
                System.out.println("SPNx = " + spn.SPNx);
                System.out.println("SPNy = " + spn.SPNy);
                System.out.println("SPNz = " + spn.SPNz);
                System.out.println("Length = " + spn.length);
                System.out.println("Height = " + spn.height);
                for (int j = 0; j < spn.numActuators; j++) {
                    System.out.println("Actuator " + j);
                    System.out.println("ASNx = " + spn.ASNx[j]);
                    System.out.println("ASNy = " + spn.ASNy[j]);
                    System.out.println("ASNz = " + spn.ASNz[j]);
                    System.out.println("AFNx = " + spn.AFNx[j]);
                    System.out.println("AFNy = " + spn.AFNy[j]);
                    System.out.println("AFNz = " + spn.AFNz[j]);
                    System.out.println("Length = " + spn.actlengths[j]);
                }
                for (int j = 0; j < spn.numDevices; j++) {
                    System.out.println("Device " + j);                
                    System.out.println("MSNx = " + spn.MSNx[j]);
                    System.out.println("MSNy = " + spn.MSNy[j]);
                    System.out.println("MSNz = " + spn.MSNz[j]);
                    System.out.println("MFNx = " + spn.MFNx[j]);
                    System.out.println("MFNy = " + spn.MFNy[j]);
                    System.out.println("MFNz = " + spn.MFNz[j]);
                    System.out.println("Length = " + spn.devlengths[j]);                    
                }            
            }
            else if (type.equals("Total")) {
                System.out.println(type);
                System.out.println("SPNx = " + spn.SPNx);
                System.out.println("SPNy = " + spn.SPNy);
                System.out.println("SPNz = " + spn.SPNz);
                System.out.println("Length = " + spn.length);
                System.out.println("Height = " + spn.height);
                for (int j = 0; j < spn.numActuators; j++) {
                    System.out.println("Actuator " + j);
                    System.out.println("ASNx = " + spn.ASNx[j]);
                    System.out.println("ASNy = " + spn.ASNy[j]);
                    System.out.println("ASNz = " + spn.ASNz[j]);
                    System.out.println("AFNx = " + spn.AFNx[j]);
                    System.out.println("AFNy = " + spn.AFNy[j]);
                    System.out.println("AFNz = " + spn.AFNz[j]);
                    System.out.println("Length = " + spn.actlengths[j]);
                }
                for (int j = 0; j < spn.numDevices; j++) {
                    System.out.println("Device " + j);                
                    System.out.println("MSNx = " + spn.MSNx[j]);
                    System.out.println("MSNy = " + spn.MSNy[j]);
                    System.out.println("MSNz = " + spn.MSNz[j]);
                    System.out.println("MFNax = " + spn.MFNax[j]);
                    System.out.println("MFNay = " + spn.MFNay[j]);
                    System.out.println("MFNaz = " + spn.MFNaz[j]);
                    System.out.println("MFNbx = " + spn.MFNbx[j]);
                    System.out.println("MFNby = " + spn.MFNby[j]);
                    System.out.println("MFNbz = " + spn.MFNbz[j]);
                    System.out.println("MFNlocalx = " + spn.MFNlocalx[j]);
                    System.out.println("MFNlocaly = " + spn.MFNlocaly[j]);
                    System.out.println("MFNlocalz = " + spn.MFNlocalz[j]);
                    System.out.println("A Length = " + spn.devlengthsA[j]);
                    System.out.println("B Length = " + spn.devlengthsB[j]);
                }            
            }
            
        }
    }
}

class SPNconfig{
    public double SPNx, SPNy, SPNz;
    public double length, height;
    public int numActuators;
    public int numDevices;
    public double[] ASNx = new double[3];
    public double[] ASNy = new double[3];
    public double[] ASNz = new double[3];
    public double[] AFNx = new double[3];
    public double[] AFNy = new double[3];
    public double[] AFNz = new double[3];
    public double[] MSNx = new double[3];
    public double[] MSNy = new double[3];
    public double[] MSNz = new double[3];
    public double[] MFNx = new double[3];
    public double[] MFNy = new double[3];
    public double[] MFNz = new double[3];
    public double[] MFNax = new double[3];
    public double[] MFNay = new double[3];
    public double[] MFNaz = new double[3];
    public double[] MFNbx = new double[3];
    public double[] MFNby = new double[3];
    public double[] MFNbz = new double[3];
    public double[] MFNlocalx = new double[3];
    public double[] MFNlocaly = new double[3];
    public double[] MFNlocalz = new double[3];
    public double[] actlengths = new double[3];
    public double[] devlengths = new double[3];
    public double[] devlengthsA = new double[3];
    public double[] devlengthsB = new double[3];    
    public double[] actangles = new double[3];
    public double[] actsntospns = new double[3];
    public double[] devangles = new double[3];
    public double[] devsntospns = new double[3];

    public SPNconfig() {
    }
}
    
    


