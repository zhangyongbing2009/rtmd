package edu.lehigh.nees.xml;

import Jama.*;

/********************************* 
 * XMLIntegratorConfig
 * <p>
 * Integrator XML configuration utility
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05  T. Marullo  Initial
 * 
 ********************************/
public class XMLIntegratorConfig {
    private boolean isOn = false;
    private String testType;
    private String historyFile;
    private int numDOF;    
    private int steps;
    private int iterations;
    private String control;
    private String units;
    private double delta_t;
    private double alpha;    
    private Matrix M, K, C, KA;    
    private boolean hybridOn;
    private boolean[] hybridA, hybridE;
    
    /** Creates a new instance of XMLIntegratorConfig */
    public XMLIntegratorConfig() {        
    }
   
    // Getters and Setters
    /** Set Object on */
    public void setIsOn(boolean bool) {
        isOn = bool;
    }
    /** Get isOn flag */
    public boolean getIsOn() {
        return isOn;
    }
    
    /** Set test type */
    public void settestType(String s) {
        testType = s;
    }
    /** Get test type */
    public String gettestType() {
        return testType;
    }   
    /** Set history file */
    public void sethistoryFile(String s) {
        historyFile = s;
    }
    /** Get history file */
    public String gethistoryFile() {
        return historyFile;
    }   
    /** Set number of DOF which defines new Matrice sizes */
    public void setnumDOF(int i) {
        numDOF = i;        
        M = new Matrix(numDOF, numDOF);
        K = new Matrix(numDOF, numDOF);
        C = new Matrix(numDOF, numDOF);
        hybridA = new boolean[numDOF];
        hybridE = new boolean[numDOF];
    }
    /** Get the number of DOF */
    public int getnumDOF() {
        return numDOF;
    }       
    /** Set number of steps */
    public void setsteps(int i) {
        steps = i;              
    }
    /** Get number of steps */
    public int getsteps() {
        return steps;
    }
    /** Set number of iteration */
    public void setiterations(int i) {
        iterations = i;               
    }
    /** Get number of iterations */
    public int getiterations() {
        return iterations;
    }
    /** Set Delta T */
    public void setdeltaT(double d) {
        delta_t = d;               
    }
    /** Get Delta T */
    public double getdeltaT() {
        return delta_t;
    }
    /** Set Alpha constant */
    public void setalpha(double d) {
        alpha = d;               
    }
    /** Get Alpha constant */
    public double getalpha() {
        return alpha;
    }
    /** Set Mass Matrix */
    public void setM(Matrix m) {
        M = m;
    }
    /** Get Mass Matrix */
    public Matrix getM() {
        return M;
    }
    /** Set Stiffness Matrix */
    public void setK(Matrix m) {
        K = m;
    }
    /** Get Stiffness Matrix */
    public Matrix getK() {
        return K;
    }
    /** Set Dampening Matrix */
    public void setC(Matrix m) {
        C = m;
    }
    /** Get Dampening Matrix */
    public Matrix getC() {
        return C;
    }
    /** Set Control type <br>
     * Index 1 = Displacement <br>
     * Index 2 = Load
     */
    public void setControl(String s) {
        control = s;              
    }
    /** Get Control type */
    public String getControl() {
        return control;
    }
    /** Set Units <br>
     * Index 1 = English <br>
     * Index 2 = Metric
     */
    public void setUnits(String s) {
        units = s;              
    }
    /** Get Units */
    public String getUnits() {
        return units;
    }
    /** Set Hybrid on flag */
    public void setHybridOn(boolean bool) {
        hybridOn = bool;
    }
    /** Get Hybrid flag */
    public boolean getHybridOn() {
        return hybridOn;
    }
    /** Set Analytical Stiffness Matrix for use in Hybrid test */
    public void setKA(Matrix m) {
        KA = m;
    }
    /** Get Analytical Stiffness Matrix for use in Hybrid test */
    public Matrix getKA() {
        return KA;
    }
    /** Set Hybrid Experimental DOF on flags */
    public void setHybridE(boolean[] flags) {
        hybridE = flags;
    }
    /** Get Hybrid Experimental DOF flags */
    public boolean[] getHybridE() {
        return hybridE;
    }
    /** Set Hybrid Analytical DOF on flags */
    public void setHybridA(boolean[] flags) {
        hybridA = flags;
    }
    /** Get Hybrid Analytical DOF flags */
    public boolean[] getHybridA() {
        return hybridA;
    }
    
    /** Print Configuration */                    
    public void print() {
        System.out.println("Integrator On? " + isOn);
        if (isOn) {
            if (testType.equals("Alpha")) {
                System.out.println("Integration Method = " + testType);
                System.out.println("History File = " + historyFile);                
                System.out.println("DOF = " + numDOF);                
                System.out.println("Steps = " + steps);
                System.out.println("Iterations = " + iterations);
                System.out.println("Control = " + control);
                System.out.println("Units = " + units);
                System.out.println("Delta T = " + delta_t);
                System.out.println("Alpha Constant = " + alpha);
                System.out.println("Mass Matrix");
                M.print(4,4);
                System.out.println("Stiffness Matrix");
                K.print(4,4);
                System.out.println("Dampening Matrix");
                C.print(4,4);
                if (hybridOn) {
                    System.out.println("Hybrid On");
                    System.out.print(" Experimental DOF: ");
                    for (int i = 0; i < numDOF; i++)
                        System.out.print(i+1 + ":" + hybridE[i] + " ");
                    System.out.println();
                    System.out.print(" Analytical DOF: ");
                    for (int i = 0; i < numDOF; i++)
                        System.out.print(i+1 + ":" + hybridA[i] + " ");
                    System.out.println();
                    System.out.println("Analytical Stiffness Matrix");
                    KA.print(4,4);
                }
            }       
            if (testType.equals("Newmark")) {
                System.out.println("Integration Method = " + testType);
                System.out.println("History File = " + historyFile);                
                System.out.println("DOF = " + numDOF);                
                System.out.println("Steps = " + steps);
                System.out.println("Iterations = " + iterations);
                System.out.println("Control = " + control);
                System.out.println("Units = " + units);
                System.out.println("Delta T = " + delta_t);
                System.out.println("Alpha Constant = " + alpha);
                System.out.println("Mass Matrix");
                M.print(4,4);
                System.out.println("Stiffness Matrix");
                K.print(4,4);
                System.out.println("Dampening Matrix");
                C.print(4,4);
                if (hybridOn) {
                    System.out.println("Hybrid On");
                    System.out.print(" Experimental DOF: ");
                    for (int i = 0; i < numDOF; i++)
                        System.out.print(i+1 + ":" + hybridE[i]);
                    System.out.println();
                    System.out.print(" Analytical DOF: ");
                    for (int i = 0; i < numDOF; i++)
                        System.out.print(i+1 + ":" + hybridA[i]);
                    System.out.println();
                    System.out.println("Analytical Stiffness Matrix");
                    KA.print(4,4);
                }
            }   
            if (testType.equals("EFF")) {
                System.out.println("Integration Method = " + testType);
                System.out.println("History File = " + historyFile);                
                System.out.println("Steps = " + steps);
                System.out.println("Control = " + control);
                System.out.println("Units = " + units);
            }
            if (testType.equals("Predefined")) {
                System.out.println("Integration Method = " + testType);
                System.out.println("History File = " + historyFile);                
                System.out.println("Steps = " + steps);
                System.out.println("Control = " + control);
                System.out.println("Units = " + units);
            }
        }
    }
        

    
}
