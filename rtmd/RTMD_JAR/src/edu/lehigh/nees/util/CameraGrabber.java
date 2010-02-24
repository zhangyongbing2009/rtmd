package edu.lehigh.nees.util;

import edu.lehigh.nees.scramnet.ScramNetIO;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;

/********************************* 
 * CameraGrabber
 * <p>
 * Captures images from a webcam running<br>
 * a webserver where the image is at<br>
 * [ip]/jpg/image.jpg
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 29 Jun 06  T. Marullo  Initial import
 * 24 Oct 06  T. Marullo  Added timer support.
 * 07 Nov 06  T. Marullo  Changed the source to FlexTPS jpeg stream
 * 						  Changed the resulting path to Expericam[N]
 * 13 Nov 06  T. Marullo  Changed the source to any JPEG webstream
 *                        Added user,password support for wget
 * 04 Jan 08  T. Marullo  Revised with URL classes and removed wget       
 * 03 Jul 08  T. Marullo  Added a name property to the camera arguments       
 * 11 Jul 08  T. Marullo  Made cross platform compatible
 * 						  Added threading capability for downloading images          
 * 
 ********************************/
public class CameraGrabber {	
	  
  private String[] cameraIP;
  private String[] cameraPath;
  private URL[] cameraURL;
  private URLConnection[] cameraConnection;
  private GetImages[] imageThreads;
	
  /** Main that runs the program */ 
  public static void main(String[] args) {
	
	// Check args
    if (args.length == 0) {
    	System.out.println("\nSCRAMNet triggered: (typically address scramnetAddress)");
    	System.out.println("Usage: CameraGrabber --trigger [SCRAMNet address] \"name@jpegstream1\" \"name@jpegstream2\" ... \"name@jpegstream[n]\"");
    	System.out.println("\nTime triggered:");
    	System.out.println("Usage: CameraGrabber --timer [milliseconds] \"name@jpegstream1\" \"name@jpegstream2\" ... \"name@jpegstream[n]\"");
    	System.out.println("\n       For password protected streams, enter: name@jpegstream,user,password"); 
    	System.exit(1);
    }
    
    // Start ne process
    new CameraGrabber(args);
  }
  
  /** Main class */
  public CameraGrabber(String[] args) {
	      
    // Get the IPs for the cameras
    cameraIP = new String[args.length-2];
    cameraPath = new String[args.length-2];
    cameraURL = new URL[args.length-2];    
    cameraConnection = new URLConnection[args.length-2];        
    for (int i = 0; i < args.length-2; i++) {    	
    	String[] camarg = args[i+2].split("@");
    	cameraPath[i] = new String(camarg[0]);
    	cameraIP[i] = camarg[1];  	    	
    	// Create directory
        boolean success = (new File(camarg[0])).mkdir();
        if (success) 
        	System.out.println("Camera: " + cameraIP[i] + " logging to " + camarg[0]);
        else
        	System.out.println("Error creating directory " + camarg[0] + ", may already exist");    	       	
    }           
        
    // If the argument is scramnet trigger,
    if (args[0].equals("--trigger")) {
    	int scramnetAddress = 61; // default address 61
    	scramnetAddress = Integer.parseInt(args[1]);
    	
    	// Create scramnet object
    	ScramNetIO scr = new ScramNetIO();
        scr.initScramnet();   

        // set a counter equal to the current number on scramnet
        int count = scr.readInt(scramnetAddress);        

        System.out.println("Camera Capture Started\n Hit CTRL-C to exit");  
     
        // Wait for the next trigger on the scramnet        
        while(count == scr.readInt(scramnetAddress));
        count = scr.readInt(scramnetAddress);
     
        // Go into loop and wait for change in count
        while(true) {
        	System.out.println("Capture " + count); 
     
        	// Create new image threads
        	imageThreads = new GetImages[cameraIP.length];
        	
        	// Start the image download process
        	for (int i = 0; i < cameraIP.length; i++) {        		
        		imageThreads[i] = new GetImages(cameraIP[i], cameraPath[i], cameraURL[i], cameraConnection[i], count);
        		imageThreads[i].start();
        	}
        	
        	// Wait for all downloads to complete
        	for (int i = 0; i < cameraIP.length; i++) {        		
        		try {
        			imageThreads[i].join();
        		} catch (Exception e) {}
        	}
        	
        	// Wait for next scramnet trigger
        	while(count == scr.readInt(scramnetAddress));
        	count = scr.readInt(scramnetAddress);
    	}
    }  
        
    // If it is time triggered,
    if (args[0].equals("--timer")) {
    	int timestep = 1000; // default 1 second
    	timestep = Integer.parseInt(args[1]);
    	
    	// Minimum time = 10 ms
    	if (timestep < 10) timestep = 10;
    	
        // set a counter equal to the current number on scramnet
        int count = 0; 
        System.out.println("Camera Capture Started\n Hit CTRL-C to exit");
        
        // Go into loop and wait for change in count
        while(true) {
        	System.out.println("Capture " + count++); 
     
        	// Create new image threads
        	imageThreads = new GetImages[cameraIP.length];
        	
        	// Start the image download process
        	for (int i = 0; i < cameraIP.length; i++) {        		
        		imageThreads[i] = new GetImages(cameraIP[i], cameraPath[i], cameraURL[i], cameraConnection[i], count);
        		imageThreads[i].start();
        	}
        	
        	// Wait for all downloads to complete
        	for (int i = 0; i < cameraIP.length; i++) {        		
        		try {
        			imageThreads[i].join();
        		} catch (Exception e) {}
        	}

        	// Sleep
        	try {Thread.sleep(timestep);} catch (Exception e) {e.printStackTrace();}
    	}
    }

    // bad option
    else {
    	System.out.println("Bad Option: " + args[0]);
    }
  } 
}

class GetImages extends Thread 
{          
	final long RETRY_INTERVAL = 5000;
	final int HTTP_TIMEOUT = 10000;
	private String cameraIP;
	private String cameraPath;
	private URL cameraURL;
	private URLConnection cameraConnection;
	private int count;
	  
	public GetImages(String _cameraIP, String _cameraPath, URL _cameraURL, URLConnection _cameraConnection, int _count) {
		cameraIP = _cameraIP;
		cameraPath = _cameraPath;
		cameraURL = _cameraURL;
		cameraConnection = _cameraConnection;
		count = _count;
	}

    public void run()                       
    {              
    	try {    		
    		cameraURL = new URL(cameraIP);
    	    cameraConnection = cameraURL.openConnection();
    	    cameraConnection.setReadTimeout(HTTP_TIMEOUT);   
    	    cameraConnection.connect();
    	} catch (IOException e) {
    		  System.out.println("Error: Failed to connect to webcam host " + cameraURL);
    		  return;
    	}
    	try {           				    				
    		  String contentType = cameraConnection.getContentType();
    		  int contentLength = cameraConnection.getContentLength();        		
    		  if (contentType == null || contentLength < 0) { 
    			  System.out.println("Failed to find content type in stream " + cameraURL);
    			  return;
    		  }		  
    		  else {
    			  DataInputStream dis = new DataInputStream(cameraConnection.getInputStream());
    			  byte[] imageData = new byte[contentLength];
    			  dis.readFully(imageData);
    			  File file = new File(cameraPath + "/image" + zeroPad(count) + ".jpg");
    			  DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
    			  dos.write(imageData,0,imageData.length);
    			  dos.close();
    		  }
    	} catch (IOException e) {e.printStackTrace();}; 
    }
    
    /** Zero padding process */
    private static String zeroPad(int i) {
      int len = 6;
      String s = Integer.toString(i);
      return ("00000000000".substring(0, len - s.length()) + s);
    }       
}
