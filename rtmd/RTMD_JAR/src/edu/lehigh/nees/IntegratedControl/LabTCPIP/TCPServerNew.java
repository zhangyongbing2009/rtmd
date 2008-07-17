package edu.lehigh.nees.IntegratedControl.LabTCPIP;

import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.awt.BorderLayout;
import java.io.*;   

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// for IOException and Input/OutputStream

public class TCPServerNew extends Thread {    
  private int port;
  private int numcommands;
  private float[] commands;  
  private boolean running;
  private boolean newcommands;
  private boolean responseready;
  private String processedData;
  private ServerSocket servSock;
  private Socket clntSock;
  private static final int BUFSIZE = 100;   // Size of receive buffer
  private byte[] recvBuffer;
  private byte[] sendBuffer;
  private InputStream in;
  private OutputStream out;  
  private int recvMsgSize;   // Size of received message
  private int count;
  private int DEBUG;
  private JFrame buttonFrame;
  private JButton button; 
  private boolean killServer = false;
    
	
  public TCPServerNew(int _port, int _numcommands, String name, int debug) {
	  port = _port;
	  numcommands = _numcommands;
	  DEBUG = debug;
	  count = 0;
	  try {
		  servSock = new ServerSocket(port);
		  System.out.println("Waiting for connection...");		  
		  clntSock = servSock.accept();     // Get client connection
		  System.out.println("Handling client at " +
	        clntSock.getInetAddress().getHostAddress() + " on port " +
	             clntSock.getPort());
	      in = clntSock.getInputStream();      
	      out = clntSock.getOutputStream(); 		  
	  } catch (Exception e) {e.printStackTrace();System.exit(1);}
	  	  
	  recvBuffer = new byte[BUFSIZE];  // Receive buffer
	  sendBuffer = new byte[BUFSIZE];  // Send buffer
	  
	  buttonFrame = new JFrame();
	  buttonFrame.setLayout(new BorderLayout());
	  buttonFrame.setSize(200,100);
	  buttonFrame.setTitle(name);
	  button = new JButton("Shutdown Server");
	  button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				killServer = true;
				running = false;
			}
	  });
	  buttonFrame.getContentPane().add(button, BorderLayout.CENTER);
	  buttonFrame.setVisible(true);	  
  }
  
  public boolean isRunning() {
	  return running;
  }
  
  public float[] getNewCommands() {
	  while (!newcommands && !killServer) {try{TCPServerNew.sleep(10);}catch(Exception e){}}
	  if (DEBUG == 1)
		  System.out.println("Matlab got the command string...");	  
	  newcommands = false;
	  return commands;
  }
  
  public void sendResponse(float[] response) {
	  processedData = "";
	  for (int i = 0; i < response.length-1; i++)
		  processedData = processedData + String.valueOf(response[i]) + ",";
	  processedData = processedData + String.valueOf(response[response.length-1]);
	  responseready = true;
  }

  public void run() {		  	  
	  running = true;
	  newcommands = false;
	  responseready = false;
	  String fromClient = "";
	  String toClient = "";
	  
	  while(running) {
		  try {
			if (DEBUG == 1)
				System.out.println("Waiting for data from Client...");			
			while ((recvMsgSize = in.read(recvBuffer)) != -1) { 				
				fromClient = new String(recvBuffer);
				fromClient = fromClient.substring(0,recvMsgSize);	
				count++;
				if (DEBUG == 1)
					System.out.println("Command " + count + " received");
				if (DEBUG == 1)
					System.out.println("Recv " + recvMsgSize + " bytes: " + fromClient);
		    	String[] ss = fromClient.split(",");
		    	
		    	if (ss[0].equals("execute")) {
		    		if (DEBUG == 1)
		    			System.out.println("Execute command received...");
					if (ss.length != numcommands+1) {
						toClient = "invalid";
						sendBuffer = toClient.getBytes();
				        out.write(sendBuffer, 0, sendBuffer.length);
				        if (DEBUG == 1)
				        	System.out.println("Wrong size command string: " + fromClient);
					}
					else {
						commands = new float[numcommands];
						
						try {
							for (int i = 0; i < numcommands; i++)
								commands[i] = Float.parseFloat(ss[i+1]);
						} catch (NumberFormatException nfe) {
							toClient = "invalid";
							sendBuffer = toClient.getBytes();
					        out.write(sendBuffer, 0, sendBuffer.length);
					        if (DEBUG == 1)
					        	System.out.println("Command string number format error: " + toClient);
					        continue;
						}										
						if (DEBUG == 1)					
							System.out.println("Accepted command string: " + toClient);
						newcommands = true;
						if (DEBUG == 1)
							System.out.println("Waiting for command string to be processed by Matlab...");					
						while (!responseready) Thread.sleep(10);
						if (DEBUG == 1)
							System.out.println("Matlab provided a response string...");
						toClient = processedData;
						sendBuffer = toClient.getBytes();
				        out.write(sendBuffer, 0, sendBuffer.length);	
				        if (DEBUG == 1)
				        	System.out.println("Response sent");
				        if (DEBUG == 1)
				        	System.out.println("Response string sent to Client: " + processedData);
						responseready = false;
					}
				}	
				else if (ss[0].equals("close")) {
					if (DEBUG == 1)
						System.out.println("Close command received...");
					toClient = "closed";
					sendBuffer = toClient.getBytes();
			        out.write(sendBuffer, 0, sendBuffer.length);
					newcommands = true;
					running = false;					
				}		    	
				else if (ss[0].equals("offset")) {
					if (DEBUG == 1)
						System.out.println("Offset command received...");
					commands = new float[1];
					commands[0] = (float)99999;
					newcommands = true;
					if (DEBUG == 1)
						System.out.println("Waiting for command string to be processed by Matlab...");					
					while (!responseready) Thread.sleep(10);
					if (DEBUG == 1)
						System.out.println("Matlab provided a response string...");					
					toClient = processedData;										
					sendBuffer = toClient.getBytes();
			        out.write(sendBuffer, 0, sendBuffer.length);
			        if (DEBUG == 1)
			        	System.out.println("Response sent");
			        if (DEBUG == 1)
			        	System.out.println("Response string sent to Client: " + processedData);
					responseready = false;				
				}
				else {
					if (DEBUG == 1)
						System.out.println("Invalid command string received: " + toClient);
					
					toClient = "invalid";
					sendBuffer = toClient.getBytes();
			        out.write(sendBuffer, 0, sendBuffer.length);
					newcommands = true;
				}			    			    			    	
	    	}						
		  } catch (Exception e) {System.out.println("Server stopping...\n");}
	  }	
	  close();	 
	  
  }
  
  public void close() {
	  running = false;
	  try {
		  clntSock.close();
		  servSock.close();
	  } catch (Exception e) {e.printStackTrace();}
	  System.out.println("Server stopped");
	  buttonFrame.setVisible(false);
  }
  
}