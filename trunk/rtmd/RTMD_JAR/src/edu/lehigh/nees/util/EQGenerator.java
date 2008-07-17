package edu.lehigh.nees.util;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class EQGenerator extends Applet implements Runnable {

	private static final long serialVersionUID = -36702952807440169L;
   int width, height;
   int x, y;    // the coordinates of the upper-left corner of the box
   int mx, my;  // the most recently recorded mouse coordinates
   boolean isMouseDraggingBox = false;
   //TrendPlot plot;
   DragPanel dbp;
   
   private int xval;
   private int yval;
   private int yval_old;

   boolean stop;
   
   Image backbuffer;
   Graphics backg;
   

   public void init() {
      setBackground( Color.black );
      this.setLayout(null);
      
      // Initial offset
      xval = 30;
      yval = 135;
      yval_old = 135;
      backbuffer = createImage( 530, 300 );
      backg = backbuffer.getGraphics();
      backg.setColor( Color.black );
      backg.fillRect( 0, 0, 530, 100 );
      backg.setColor( Color.yellow );
      
      dbp = new DragPanel(this);
      dbp.setBounds(0,0,30,300);
      this.add(dbp);      
      
      stop = false;
      
   }
   
   public void updateVal(int y) {   	
   	yval = y;    	
   }
   
   
   public void update( Graphics g ) {	 	   
	   System.out.println(yval);
	  backg.drawLine(xval,yval_old+15,++xval,yval+15);
	  yval_old = yval;
	  //System.out.println("x=" + xval);
	  //System.out.println("y=" + yval_old + "," + yval);
	  g.drawImage( backbuffer, 0, 0, this );
	  g.setColor(Color.white);
	  g.drawLine(0,150,530,150);
	  //System.out.println("updated");
	  if (xval >= 530)
		  stop = true;
   }
   
   public void paint( Graphics g ) {
	  
      update( g );
   }
   
   public void run() {		
		while (!stop) {
			try {
				this.repaint();
				Thread.sleep(20);						
			} catch (Exception e) {e.printStackTrace();}
		}
	}
   
 }

class DragPanel extends Panel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -36702952807643169L;
    private static final int BALL_DIAMETER = 30; // Diameter of ball
    //--- instance variables
    /** Ball coords.  Changed by mouse listeners.  Used by paintComponent. */    
    private int _ballY     = 135;   // y coord - set from drag          
    private int _dragFromY = 0;    // bounding box.
    private boolean _canDrag  = false;
    private EQGenerator plot;
    private boolean PlotStarted;
    Thread t;
    
    //============================================================= constructor
    /** Constructor sets size, colors, and adds mouse listeners.*/
    public DragPanel(EQGenerator p) {    	
    	plot = p;
        setSize(new Dimension(30, 300));
        setBackground(Color.blue);
        setForeground(Color.yellow);
        //--- Add the mouse listeners.
        this.addMouseListener(this); 
        this.addMouseMotionListener(this);
        PlotStarted = false;
    }//endconstructor
    
    //=================================================== method paintComponent
    /** Ball is drawn at the last recorded mouse listener coordinates. */
    public void paint(Graphics g) {
        super.paint(g);   // Required for background.
        g.fillRect(0, _ballY, BALL_DIAMETER, BALL_DIAMETER);
        plot.updateVal(_ballY);
        g.drawLine(0,150,30,150);
    }//end paintComponent
    

    public void mousePressed(MouseEvent e) {
    	// start plot
    	if (!PlotStarted) {
    		t = new Thread(plot);
    		t.start();
    		PlotStarted = true;
    	}
    
    	        
        int y = e.getY();   // Save the y coord of the click
        

            _canDrag = true;            
            _dragFromY = y - _ballY;  // how far from top

    }//end mousePressed
    
    //============================================================ mouseDragged
    /** Set x,y  to mouse position and repaint. */
    public void mouseDragged(MouseEvent e) {
        if (_canDrag) {   // True only if button was pressed inside ball.
            //--- Ball pos from mouse and original click displacement            
            _ballY = e.getY() - _dragFromY;
                       
            //--- Don't move the ball off top or bottom
            _ballY = Math.max(_ballY, 0);
            _ballY = Math.min(_ballY, getHeight() - BALL_DIAMETER);
            
            this.repaint(); // Repaint because position changed.
        }
    }//end mouseDragged

    //====================================================== method mouseExited
    /** Turn off dragging if mouse exits panel. */
    public void mouseExited(MouseEvent e) {}
    

    //=============================================== Ignore other mouse events.
    public void mouseMoved   (MouseEvent e) {}  // ignore these events
    public void mouseEntered (MouseEvent e) {}  // ignore these events
    public void mouseClicked (MouseEvent e) {}  // ignore these events
    public void mouseReleased(MouseEvent e) {}  // ignore these events
}