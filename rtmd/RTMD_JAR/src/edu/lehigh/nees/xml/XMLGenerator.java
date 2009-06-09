package edu.lehigh.nees.xml;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import com.zdo.data.MutableTwoDimDataSetImpl;
import com.zdo.data.TwoDimPointImpl;
import com.zdo.plot.Scatter;
import com.zdo.plot.ScatterConstraints;
import edu.lehigh.nees.util.FileHandler;
import edu.lehigh.nees.util.filefilter.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.Format;
import org.jdom.*;
import org.jdom.output.*;

/*******************************************************************************
 * XMLGenerator
 * <p>
 * XML configuration utility
 * <p>
 * 
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 16 Aug 05 T. Marullo Initial 
 *  5 Apr 06 T. Marullo Removed Hybrid 
 *                      Added DAQ XML File Tab to merge both files 
 * 27 Apr 06 T. Marullo Added History Plot to Integrator panel 
 *                      Added Pause Bit to SCRAMNet table at 62 
 *                      Change DAQ tab to Metadata tab 
 *                      Added Metadata features for xPC and Data Turbine exporting 
 * 29 Jun 06 T. Marullo Changed Skin Changed Scan to Stream in SCRAMNet Table 
 *                      Changed true/false to checkbox 
 *                      Bug Fix: Changing history file doesn't crash plot when selecting the first item 
 * 06 Jul 06 T. Marullo Added NodeBlock XML generation 
 *                      Bug Fix: scramnetTable broken when generating XML 
 * 12 Jul 06 T. Marullo Added File Filters to Save dialog boxes
 * 13 Jul 06 T. Marullo Changed Scan to Stream in XML 
 * 28 Jul 06 T. Marullo Modified HybridViz XML generation 
 *  9 Aug 06 T. Marullo Changed HybridViz XML extention to .hvs 
 * 26 Oct 06 T. Marullo Added SimBlock support
 * 27 Oct 06 T. Marullo Modified Save function
 * 						Added SaveAs function
 * 						Added Open function
 * 						Added New function
 * 28 Mar 07 T. Marullo Saving file after opening doesn't ask for confirmation
 * 18 Jul 07 T. Marullo Replace / with _ for units
 * 25 Jul 07 T. Marullo Changed SCRAMNet Feedbacks
 *  7 Aug 07 T. Marullo Change MinSimLimit and MaxSimLimit to LowerLimit and UpperLimit
 * 
 * 
 ******************************************************************************/
public class XMLGenerator extends JFrame {

	/** Creates a new instance of XMLGenerator */
	public XMLGenerator() {
		super("RTMD: Integrated Control Configurator");
		init();
	}

	/** Initialize the GUI interface */
	public void init() {
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		// Set up UI Look and Feel
        try {                        
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {System.out.println("Error setting Look and Feel: " + e);}
		this.getContentPane().setLayout(new BorderLayout());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

		// Create "FILE" menu bar
		generateMenuBar();

		// Create SCRAMNet Panel
		generateScramnetPanel();

		// Create Integrator Panel
		generateIntegratorPanel();

		// Create Kinematics Panel
		generateKinematicsPanel();

		// Create Ramp Panel
		generateRampPanel();

		// Create Metadata Include Panel
		generateMetadataPanel();

		// Create Tabbed Panes
		generateTabbedPanes();
	}

	/** Get History files */
	private String[] getHistoryFiles() {
		// Scan History file directory
		String[] historyFileNames = null;
		File dir = new File("/RTMD/historyFiles");
		String[] histFiles = dir.list();
		if (histFiles == null) {
			// Dir does not exist or is not a directory			
			JOptionPane.showMessageDialog(null,"History File directory, /RTMD/historyFiles, doesn't exist!");
		} else {
			// Get all the file names
			historyFileNames = new String[histFiles.length + 1];
			historyFileNames[0] = "Select History File";
			for (int i = 0; i < histFiles.length; i++) {
				historyFileNames[i + 1] = histFiles[i];
			}
		}
		// Create new ComboBoxModel
		return historyFileNames;
	}

	/** Generate the Menu Bar */
	private void generateMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		JMenuItem newitem = new JMenuItem("New");
		JMenuItem saveitem = new JMenuItem("Save");
		JMenuItem saveasitem = new JMenuItem("Save As");
		JMenuItem openitem = new JMenuItem("Open");
		JMenuItem exititem = new JMenuItem("Exit");
		JMenu toolsmenu = new JMenu("Tools");
		JMenuItem previewitem = new JMenuItem("Preview");
		JMenuItem validateitem = new JMenuItem("Validate");
		JMenu helpmenu = new JMenu("Help");
		JMenuItem helpitem = new JMenuItem("Help Contents");
		JMenuItem aboutitem = new JMenuItem("About");
		newitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					Runtime.getRuntime().exec("javaw.exe -classpath \"C:\\RTMD\\lib\\jdom.jar;c:\\RTMD\\lib\\zdo-plot.jar;c:\\RTMD\\lib\\rtmd.jar;.\" edu.lehigh.nees.xml.XMLGenerator");					
				} catch (Exception e) {e.printStackTrace();}
			}});
		openitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				openConfiguration();
			}});
		saveitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveConfiguration();
			}});
		saveasitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveAs();
			}});
		exititem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}});
		previewitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				preview(true);
			}});
		validateitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				validateXML();
			}});
		helpitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				new XMLHelp().setVisible(true);
			}});
		aboutitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JFrame frame = new JFrame("About");
				frame.setSize(400, 200);
				frame.setLocationRelativeTo(null);
				JEditorPane label = new JEditorPane();
				label.setContentType("text/html");
				label.setEditable(false);
				label.setText("<center><h3>RTMD: Integrated Control Configurator</h3><p><h4>Version 2.0<br>Copyright (c) 2005 ATLSS Lehigh University<br>Author:  Tommy Marullo</h4></center>");
				frame.getContentPane().add(label, BorderLayout.CENTER);
				frame.setVisible(true);
			}});
		// Organize the Menu Bar
		filemenu.add(newitem);
		filemenu.add(openitem);
		filemenu.add(saveitem);
		filemenu.add(saveasitem);
		filemenu.add(exititem);
		menubar.add(filemenu);
		toolsmenu.add(previewitem);
		toolsmenu.add(validateitem);
		menubar.add(toolsmenu);
		helpmenu.add(helpitem);
		helpmenu.add(aboutitem);
		menubar.add(helpmenu);
		this.setJMenuBar(menubar);
	}

	/** Generate the Integrator Panel */
	private void generateIntegratorPanel() {
		integratorPanel = new JPanel(null);
		JLabel label;
		// Integration Method Label and Combo Box
		label = new JLabel("Integration Method");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 10, 150, 30);
		integratorPanel.add(label);
		String[] integrationTypes = { 	"Select Integration Method", 
			  	   						"Pseudo Dynamic",
		  	   							"Effective Force", 
				   						"Predefined History" };
		integrationMethodComboBox = new JComboBox(new DefaultComboBoxModel(integrationTypes));											
		integrationMethodComboBox.setBounds(150, 10, 200, 30);
		integrationMethodComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disableIntegrationPanelOptions(); // Cancel all options
				if (integrationMethodComboBox.getSelectedIndex() == 1) { // PSD
					integratorComboBox.setEnabled(true);
				} else if (integrationMethodComboBox.getSelectedIndex() == 2) { // Effective Force																				
					historyFileComboBox.setEnabled(true);
					timeStepsTextField.setEnabled(true);
					controlComboBox.setSelectedIndex(2);
					numDOFTextField.setText("1");
				} else if (integrationMethodComboBox.getSelectedIndex() == 3) { // Predefined
					historyFileComboBox.setEnabled(true);
					timeStepsTextField.setEnabled(true);
					controlComboBox.setEnabled(true);
					unitsComboBox.setEnabled(true);
				}
			}});
		integratorPanel.add(integrationMethodComboBox);
		// Integrator Label and Combo Box
		label = new JLabel("Integrator");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 50, 150, 30);
		integratorPanel.add(label);
		integratorComboBox = new JComboBox(new DefaultComboBoxModel(
								new String[] { "Select Integrator", 
											   "Alpha Method",	
											   "Newmark Method" }));
		integratorComboBox.setBounds(150, 50, 150, 30);
		integratorComboBox.setEnabled(false);
		integratorComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Alpha Method
				if (integratorComboBox.getSelectedIndex() == 1) { 		
					disableIntegrationPanelOptions(); // First Cancel all options
					integrationMethodComboBox.setEnabled(true);
					integratorComboBox.setEnabled(true);
					historyFileComboBox.setEnabled(true);
					numDOFTextField.setEnabled(true);
					timeStepsTextField.setEnabled(true);
					subStepsTextField.setEnabled(true);					
					controlComboBox.setSelectedIndex(1);
					unitsComboBox.setEnabled(true);
					alphaTextField.setEnabled(true);
					deltaTTextField.setEnabled(true);
					matriciesButton.setEnabled(true);
				// Newmark Method
				} else if (integratorComboBox.getSelectedIndex() == 2) { 
					disableIntegrationPanelOptions(); // First Cancel all options
					integrationMethodComboBox.setEnabled(true);
					integratorComboBox.setEnabled(true);
					historyFileComboBox.setEnabled(true);
					numDOFTextField.setEnabled(true);
					timeStepsTextField.setEnabled(true);
					controlComboBox.setSelectedIndex(1);
					unitsComboBox.setEnabled(true);
					alphaTextField.setEnabled(true);
					deltaTTextField.setEnabled(true);
					matriciesButton.setEnabled(true);
				}
			}});
		integratorPanel.add(integratorComboBox);
		// History File Label and Combo Box
		label = new JLabel("History File");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 90, 150, 30);
		integratorPanel.add(label);
		historyFileComboBox = new JComboBox(
								new DefaultComboBoxModel(getHistoryFiles()));
		historyFileComboBox.setBounds(150, 90, 150, 30);
		historyFileComboBox.setEnabled(false);
		integratorPanel.add(historyFileComboBox);
		historyFileComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/** Create History Plot */
				historyPanel.removeAll();
				// Check if a valid record
				if (historyFileComboBox.getSelectedIndex() > 0) {
					historyPlot = 
						new HLPlot((String) historyFileComboBox.getSelectedItem()); // Create a new history plot
					historyPanel.add(historyPlot.getPlot());
					historyPanel.revalidate();
					historyPanel.repaint();

					/** Read number of steps for any history file */
					int i = 0;
					try {
						BufferedReader input = new BufferedReader(
												new InputStreamReader(
												 new FileInputStream(
												  new File("/RTMD/historyFiles/" + (String) historyFileComboBox.getSelectedItem()))));
						try {
							while (input.readLine() != null)
								i++;
						} catch (Exception e2) {e2.printStackTrace();}						
						try {
							input.close();
						} catch (Exception e3) {e3.printStackTrace();}
					} catch (Exception e4) {e4.printStackTrace();}
					
					timeStepsTextField.setText(String.valueOf(i));

					// If predefined, get number of columns in history file for
					// numDOF
					if (integrationMethodComboBox.getSelectedIndex() == 3) {
						try {
							int columns = 1;

							// Check number of columns for Predefined History
							BufferedReader input = new BufferedReader(
												    new InputStreamReader(
													 new FileInputStream(
													  new File("/RTMD/historyFiles/" + (String) historyFileComboBox.getSelectedItem()))));
							String line = input.readLine();
							if (line.indexOf(",") != -1)
								columns = line.split(",").length;

							// Set text field to be enabled and have the number
							// of colums for DOF
							numDOFTextField.setText(Integer.toString(columns));
							numDOFTextField.setEnabled(true);
						} catch (Exception e2) {e2.printStackTrace();}
					}
				}
			}
			});
		// Delta T Label and Text Box
		label = new JLabel("Delta T");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 130, 150, 30);
		integratorPanel.add(label);
		deltaTTextField = new JTextField();
		deltaTTextField.setFont(new Font("Arial", Font.BOLD, 14));
		deltaTTextField.setBounds(150, 130, 150, 30);
		deltaTTextField.setEnabled(false);
		deltaTTextField.setHorizontalAlignment(JTextField.RIGHT);
		integratorPanel.add(deltaTTextField);
		// Number of DOF Label and Text Box
		label = new JLabel("# DOF");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 170, 150, 30);
		integratorPanel.add(label);
		numDOFTextField = new JTextField();
		numDOFTextField.setFont(new Font("Arial", Font.BOLD, 14));
		numDOFTextField.setBounds(150, 170, 150, 30);
		numDOFTextField.setEnabled(false);
		numDOFTextField.setHorizontalAlignment(JTextField.RIGHT);
		// Set Matrices Button and Sim Data Button on or off depending on num
		// DOF value
		numDOFTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!numDOFTextField.getText().equals("")) {
					matriciesButton.setEnabled(true);
				} else {
					matriciesButton.setEnabled(false);
				}
			}});
		integratorPanel.add(numDOFTextField);
		// Number of Time Steps Label and Text Box
		label = new JLabel("# Time Steps");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 210, 150, 30);
		integratorPanel.add(label);
		timeStepsTextField = new JTextField();
		timeStepsTextField.setFont(new Font("Arial", Font.BOLD, 14));
		timeStepsTextField.setBounds(150, 210, 150, 30);
		timeStepsTextField.setEnabled(false);
		timeStepsTextField.setHorizontalAlignment(JTextField.RIGHT);
		integratorPanel.add(timeStepsTextField);
		// Number of Sub Steps Label and Text Box
		label = new JLabel("# Sub Steps");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 250, 150, 30);
		integratorPanel.add(label);
		subStepsTextField = new JTextField();
		subStepsTextField.setFont(new Font("Arial", Font.BOLD, 14));
		subStepsTextField.setBounds(150, 250, 150, 30);
		subStepsTextField.setEnabled(false);
		subStepsTextField.setHorizontalAlignment(JTextField.RIGHT);
		integratorPanel.add(subStepsTextField);
		// Displacement or Load Control
		label = new JLabel("Control");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 290, 150, 30);
		integratorPanel.add(label);
		controlComboBox = new JComboBox(
						   new DefaultComboBoxModel(
						    new String[] { "Select Control",
						    		       "Displacement", 
										   "Load" }));
		controlComboBox.setBounds(150, 290, 150, 30);
		controlComboBox.setEnabled(false);
		integratorPanel.add(controlComboBox);
		// Units
		label = new JLabel("Units");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 330, 150, 30);
		integratorPanel.add(label);
		unitsComboBox = new JComboBox(
				         new DefaultComboBoxModel(
				          new String[] { "Select Units", 
				        		  		 "English", 
				          				 "Metric" }));
		unitsComboBox.setBounds(150, 330, 150, 30);
		unitsComboBox.setEnabled(false);
		integratorPanel.add(unitsComboBox);
		// Alpha Constant Label and Text Box
		label = new JLabel("Alpha Constant");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBounds(10, 370, 150, 30);
		integratorPanel.add(label);
		alphaTextField = new JTextField();
		alphaTextField.setFont(new Font("Arial", Font.BOLD, 14));
		alphaTextField.setBounds(150, 370, 150, 30);
		alphaTextField.setEnabled(false);
		alphaTextField.setHorizontalAlignment(JTextField.RIGHT);
		integratorPanel.add(alphaTextField);
		// Matrices Button
		matriciesButton = new JButton("Set Matrices");
		matriciesButton.setBounds(350, 370, 150, 30);
		matriciesButton.setEnabled(false);
		matriciesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMatricies();
			}});
		integratorPanel.add(matriciesButton);
		// History Plot Panel
		historyPanel = new JPanel(new BorderLayout());
		historyPanel.setBounds(0, 420, 665, 120);
		historyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "History"));
		integratorPanel.add(historyPanel);
	}

	/** Set the Matrices */
	private void setMatricies() {
		matrixFrame = new JFrame();
		matrixFrame.getContentPane().setLayout(new BorderLayout());
		matrixFrame.setTitle("Matricies");
		matrixFrame.getContentPane().setPreferredSize(new Dimension(200, 165));
		int DOF = checkIfInteger("# DOF", numDOFTextField.getText());
		// Create a new table if the table was never created or the DOF changed
		if (matrixTable == null || matrixTable.getColumnCount() != (DOF+1)) {
			matrixTable = new JTable(DOF * 3, DOF + 1);
			matrixTable.setTableHeader(null);
			matrixTable.setRowHeight(15);
			matrixTable.setValueAt("M", 0, 0);			
			matrixTable.setValueAt("K", DOF * 1, 0);
			matrixTable.setValueAt("C", DOF * 2, 0);			
		}		
		JScrollPane scrollpane = new JScrollPane(matrixTable);
		matrixFrame.getContentPane().add(scrollpane, BorderLayout.CENTER);
		matrixFrame.setVisible(true);
		// Matrices OK Button
		JButton matriciesOKButton = new JButton("Set");				
		matriciesOKButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				matrixFrame.setVisible(false);
			}});
		matrixFrame.getContentPane().add(matriciesOKButton, BorderLayout.PAGE_END);
		matrixFrame.pack();		
	}

	/** Generate the Kinematics Panel */
	private void generateKinematicsPanel() {
		kinematicsPanel = new JPanel(null);
		JLabel label;
		// Kinematics type
		kinematicsMethodComboBox = new JComboBox(
				                    new DefaultComboBoxModel(
				                     new String[] { "Select Kinematics Method", 
				                    		        "Incremental",
						                            "Total" }));
		kinematicsMethodComboBox.setBounds(200, 10, 200, 30);
		kinematicsMethodComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNumberActuatorsButton.setEnabled(true);
				setNumberDevicesButton.setEnabled(true);
			}});
		kinematicsPanel.add(kinematicsMethodComboBox);
		// SPN X
		label = new JLabel("SPN X");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setBounds(0, 70, 50, 30);
		kinematicsPanel.add(label);
		spnxTextField = new JTextField();
		spnxTextField.setFont(new Font("Arial", Font.BOLD, 14));
		spnxTextField.setBounds(60, 70, 70, 30);
		spnxTextField.setHorizontalAlignment(JTextField.RIGHT);
		kinematicsPanel.add(spnxTextField);
		// SPN Y
		label = new JLabel("SPN Y");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setBounds(0, 100, 50, 30);
		kinematicsPanel.add(label);
		spnyTextField = new JTextField();
		spnyTextField.setFont(new Font("Arial", Font.BOLD, 14));
		spnyTextField.setBounds(60, 100, 70, 30);
		spnyTextField.setHorizontalAlignment(JTextField.RIGHT);
		kinematicsPanel.add(spnyTextField);
		// SPN Z
		label = new JLabel("SPN Z");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setBounds(0, 130, 50, 30);
		kinematicsPanel.add(label);
		spnzTextField = new JTextField();
		spnzTextField.setFont(new Font("Arial", Font.BOLD, 14));
		spnzTextField.setBounds(60, 130, 70, 30);
		spnzTextField.setHorizontalAlignment(JTextField.RIGHT);
		kinematicsPanel.add(spnzTextField);
		// Width
		label = new JLabel("SPN Width");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setBounds(150, 70, 100, 30);
		kinematicsPanel.add(label);
		spnwidthTextField = new JTextField();
		spnwidthTextField.setFont(new Font("Arial", Font.BOLD, 14));
		spnwidthTextField.setBounds(260, 70, 70, 30);
		spnwidthTextField.setHorizontalAlignment(JTextField.RIGHT);
		kinematicsPanel.add(spnwidthTextField);
		// Height
		label = new JLabel("SPN Height");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setBounds(150, 100, 100, 30);
		kinematicsPanel.add(label);
		spnheightTextField = new JTextField();
		spnheightTextField.setFont(new Font("Arial", Font.BOLD, 14));
		spnheightTextField.setBounds(260, 100, 70, 30);
		spnheightTextField.setHorizontalAlignment(JTextField.RIGHT);
		kinematicsPanel.add(spnheightTextField);
		// Set Number Actuators Button
		setNumberActuatorsButton = new JButton("# Actuators?");
		setNumberActuatorsButton.setBounds(400, 70, 170, 30);
		setNumberActuatorsButton.setEnabled(false);
		setNumberActuatorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numKinematicActuators = Integer.parseInt(JOptionPane.showInputDialog(null, "Number of Actuators?",0));
				setNumberActuatorsButton.setText("Number of Actuators? " + String.valueOf(numKinematicActuators));
				// Set the Actuator options of the Kinematics Panel and update
				// Scroll length
				generateActuatorKinematicsOptions();
				updateKinematicsScrollLength();
				kinematicsPanel.revalidate();
				kinematicsPanel.repaint();
			}
		});
		kinematicsPanel.add(setNumberActuatorsButton);
		// Set Number Devices Button
		setNumberDevicesButton = new JButton("# Devices?");
		setNumberDevicesButton.setBounds(400, 100, 170, 30);
		setNumberDevicesButton.setEnabled(false);
		setNumberDevicesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numKinematicDevices = Integer.parseInt(JOptionPane.showInputDialog(null, "Number of Devices?",0));
				setNumberDevicesButton.setText("Number of Devices? " + String.valueOf(numKinematicDevices));
				// Set the Device options of the Kinematics Panel and update
				// Scroll length
				generateDeviceKinematicsOptions();
				updateKinematicsScrollLength();
				disableKinematicsPanelOptions(); // Cancel all options
				if (kinematicsMethodComboBox.getSelectedIndex() == 2) { // Total
					for (int i = 0; i < numKinematicDevices; i++) {
						mfnbxTextField[i].setEnabled(true);
						mfnbyTextField[i].setEnabled(true);
						mfnbzTextField[i].setEnabled(true);
						mfnlocalxTextField[i].setEnabled(true);
						mfnlocalyTextField[i].setEnabled(true);
						mfnlocalzTextField[i].setEnabled(true);
						mblengthTextField[i].setEnabled(true);
					}
				}
				kinematicsPanel.revalidate();
				kinematicsPanel.repaint();
			}
		});
		kinematicsPanel.add(setNumberDevicesButton);
	}

	/** Generate the options for the Actuators */
	private void generateActuatorKinematicsOptions() {
		JLabel label;
		// Set the number of Actuators
		asnxTextField = new JTextField[numKinematicActuators];
		asnyTextField = new JTextField[numKinematicActuators];
		asnzTextField = new JTextField[numKinematicActuators];
		afnxTextField = new JTextField[numKinematicActuators];
		afnyTextField = new JTextField[numKinematicActuators];
		afnzTextField = new JTextField[numKinematicActuators];
		alengthTextField = new JTextField[numKinematicActuators];
		// Text Fields
		for (int i = 0; i < numKinematicActuators; i++) {
			label = new JLabel("Actuator " + String.valueOf(i + 1));
			label.setHorizontalAlignment(JLabel.RIGHT);
			label.setBounds(0, 230 + (i * 180), 100, 30);
			kinematicsPanel.add(label);
			// ASN X
			label = new JLabel("ASN X");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(110, 200 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			asnxTextField[i] = new JTextField();
			asnxTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			asnxTextField[i].setBounds(110, 230 + (i * 180), 60, 30);
			asnxTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(asnxTextField[i]);
			// ASN Y
			label = new JLabel("ASN Y");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(170, 200 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			asnyTextField[i] = new JTextField();
			asnyTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			asnyTextField[i].setBounds(170, 230 + (i * 180), 60, 30);
			asnyTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(asnyTextField[i]);
			// ASN Z
			label = new JLabel("ASN Z");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(230, 200 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			asnzTextField[i] = new JTextField();
			asnzTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			asnzTextField[i].setBounds(230, 230 + (i * 180), 60, 30);
			asnzTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(asnzTextField[i]);
			// AFN X
			label = new JLabel("AFN X");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(110, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			afnxTextField[i] = new JTextField();
			afnxTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			afnxTextField[i].setBounds(110, 290 + (i * 180), 60, 30);
			afnxTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(afnxTextField[i]);
			// AFN Y
			label = new JLabel("AFN Y");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(170, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			afnyTextField[i] = new JTextField();
			afnyTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			afnyTextField[i].setBounds(170, 290 + (i * 180), 60, 30);
			afnyTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(afnyTextField[i]);
			// AFN Z
			label = new JLabel("AFN Z");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(230, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			afnzTextField[i] = new JTextField();
			afnzTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			afnzTextField[i].setBounds(230, 290 + (i * 180), 60, 30);
			afnzTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(afnzTextField[i]);
			// Length
			label = new JLabel("Length");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(110, 320 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			alengthTextField[i] = new JTextField();
			alengthTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			alengthTextField[i].setBounds(170, 320 + (i * 180), 60, 30);
			alengthTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(alengthTextField[i]);
		}
	}

	/** Generate the options for the Devices */
	private void generateDeviceKinematicsOptions() {
		JLabel label;
		// Set the number of Devices
		msnxTextField = new JTextField[numKinematicDevices];
		msnyTextField = new JTextField[numKinematicDevices];
		msnzTextField = new JTextField[numKinematicDevices];
		mfnxTextField = new JTextField[numKinematicDevices];
		mfnyTextField = new JTextField[numKinematicDevices];
		mfnzTextField = new JTextField[numKinematicDevices];
		mfnbxTextField = new JTextField[numKinematicDevices];
		mfnbyTextField = new JTextField[numKinematicDevices];
		mfnbzTextField = new JTextField[numKinematicDevices];
		mfnlocalxTextField = new JTextField[numKinematicDevices];
		mfnlocalyTextField = new JTextField[numKinematicDevices];
		mfnlocalzTextField = new JTextField[numKinematicDevices];
		mlengthTextField = new JTextField[numKinematicDevices];
		mblengthTextField = new JTextField[numKinematicDevices];
		// Devices Labels
		for (int i = 0; i < numKinematicDevices; i++) {
			label = new JLabel("Device " + String.valueOf(i + 1));
			label.setHorizontalAlignment(JLabel.RIGHT);
			label.setBounds(300, 230 + (i * 180), 100, 30);
			kinematicsPanel.add(label);
			// ASN X
			label = new JLabel("MSN X");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(410, 200 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			msnxTextField[i] = new JTextField();
			msnxTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			msnxTextField[i].setBounds(410, 230 + (i * 180), 60, 30);
			msnxTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(msnxTextField[i]);
			// MSN Y
			label = new JLabel("MSN Y");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(470, 200 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			msnyTextField[i] = new JTextField();
			msnyTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			msnyTextField[i].setBounds(470, 230 + (i * 180), 60, 30);
			msnyTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(msnyTextField[i]);
			// MSN Z
			label = new JLabel("MSN Z");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(530, 200 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			msnzTextField[i] = new JTextField();
			msnzTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			msnzTextField[i].setBounds(530, 230 + (i * 180), 60, 30);
			msnzTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(msnzTextField[i]);
			// MFN X
			label = new JLabel("MFN X");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(410, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnxTextField[i] = new JTextField();
			mfnxTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnxTextField[i].setBounds(410, 290 + (i * 180), 60, 30);
			mfnxTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnxTextField[i]);
			// MFN Y
			label = new JLabel("MFN Y");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(470, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnyTextField[i] = new JTextField();
			mfnyTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnyTextField[i].setBounds(470, 290 + (i * 180), 60, 30);
			mfnyTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnyTextField[i]);
			// MFN Z
			label = new JLabel("MFN Z");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(530, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnzTextField[i] = new JTextField();
			mfnzTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnzTextField[i].setBounds(530, 290 + (i * 180), 60, 30);
			mfnzTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnzTextField[i]);
			// Device Length
			label = new JLabel("Length");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(410, 320 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mlengthTextField[i] = new JTextField();
			mlengthTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mlengthTextField[i].setBounds(470, 320 + (i * 180), 60, 30);
			mlengthTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mlengthTextField[i]);
			// MFN-B X
			label = new JLabel("MFN-B X");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(590, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnbxTextField[i] = new JTextField();
			mfnbxTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnbxTextField[i].setBounds(590, 290 + (i * 180), 60, 30);
			mfnbxTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnbxTextField[i]);
			// MFN-B Y
			label = new JLabel("MFN-B Y");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(650, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnbyTextField[i] = new JTextField();
			mfnbyTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnbyTextField[i].setBounds(650, 290 + (i * 180), 60, 30);
			mfnbyTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnbyTextField[i]);
			// MFN-B Z
			label = new JLabel("MFN-B Z");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(710, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnbzTextField[i] = new JTextField();
			mfnbzTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnbzTextField[i].setBounds(710, 290 + (i * 180), 60, 30);
			mfnbzTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnbzTextField[i]);
			// Device B Length
			label = new JLabel("Length");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(590, 320 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mblengthTextField[i] = new JTextField();
			mblengthTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mblengthTextField[i].setBounds(650, 320 + (i * 180), 60, 30);
			mblengthTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mblengthTextField[i]);
			// MFN Local X
			label = new JLabel("Local X");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(770, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnlocalxTextField[i] = new JTextField();
			mfnlocalxTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnlocalxTextField[i].setBounds(770, 290 + (i * 180), 60, 30);
			mfnlocalxTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnlocalxTextField[i]);
			// MFN Local Y
			label = new JLabel("Local Y");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(830, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnlocalyTextField[i] = new JTextField();
			mfnlocalyTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnlocalyTextField[i].setBounds(830, 290 + (i * 180), 60, 30);
			mfnlocalyTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnlocalyTextField[i]);
			// MFN Local Z
			label = new JLabel("Local Z");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(890, 260 + (i * 180), 60, 30);
			kinematicsPanel.add(label);
			mfnlocalzTextField[i] = new JTextField();
			mfnlocalzTextField[i].setFont(new Font("Arial", Font.BOLD, 14));
			mfnlocalzTextField[i].setBounds(890, 290 + (i * 180), 60, 30);
			mfnlocalzTextField[i].setHorizontalAlignment(JTextField.RIGHT);
			kinematicsPanel.add(mfnlocalzTextField[i]);

		}
	}

	/** Update Kinematics Panel Scroll length */
	private void updateKinematicsScrollLength() {
		// Dynamically set the new dimension for the Scroll bar
		Dimension d = kinematicsPanel.getSize();
		if (numKinematicDevices == 0) {
			if (numKinematicActuators > numKinematicDevices) {
				d.setSize(d.getWidth(),alengthTextField[alengthTextField.length - 1].getY() + 30);
				kinematicsPanel.setPreferredSize(d);
			} else {
				d.setSize(mfnlocalzTextField[mfnlocalzTextField.length - 1].getX() + 60,
						  mlengthTextField[mlengthTextField.length - 1].getY() + 30);
				kinematicsPanel.setPreferredSize(d);
			}
		} else {
			if (numKinematicActuators > numKinematicDevices) {
				d.setSize(mfnlocalzTextField[mfnlocalzTextField.length - 1].getX() + 60,
						  alengthTextField[alengthTextField.length - 1].getY() + 30);
				kinematicsPanel.setPreferredSize(d);
			} else {
				d.setSize(mfnlocalzTextField[mfnlocalzTextField.length - 1].getX() + 60,
						  mlengthTextField[mlengthTextField.length - 1].getY() + 30);
				kinematicsPanel.setPreferredSize(d);
			}
		}
	}

	/** Generate the Ramp Generators Panel */
	private void generateRampPanel() {
		rampPanel = new JPanel(null);
		// Set Number Actuator Button
		setNumberRampActuatorsButton = new JButton("# Actuators?");
		setNumberRampActuatorsButton.setBounds(10, 10, 170, 30);
		setNumberRampActuatorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numRampActuators = Integer.parseInt(JOptionPane.showInputDialog(null, "Number of Actuators?",0));
				setNumberRampActuatorsButton.setText("Number of Actuators? " + String.valueOf(numRampActuators));
				// Set the Actuator options of the Ramp Panel
				generateRampPanelOptions();
				rampPanel.revalidate();
				rampPanel.repaint();
			}
		});
		rampPanel.add(setNumberRampActuatorsButton);
		// Set Number Ramp Ticks
		setNumberRampTicksButton = new JButton("# Ticks?");
		setNumberRampTicksButton.setBounds(200, 10, 170, 30);
		setNumberRampTicksButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Save the number of ticks for the Ramp
				numRampTicks = Integer.parseInt(JOptionPane.showInputDialog(null, "Number of Ticks?",0));
				setNumberRampTicksButton.setText("Number of Ticks? " + String.valueOf(numRampTicks));
				rampPanel.revalidate();
				rampPanel.repaint();
			}
		});
		rampPanel.add(setNumberRampTicksButton);
		// Ramp Note
		JLabel label = new JLabel("Note: 1024 Ticks = 1 Second");
		label.setBounds(400, 10, 300, 30);
		rampPanel.add(label);
	}

	/** Generate the Ramp Panel Actuator Options */
	private void generateRampPanelOptions() {
		JLabel label;
		rampMethodComboBox = new JComboBox[numRampActuators];
		rampFeedbackCheckBox = new JCheckBox[numRampActuators];
		for (int i = 0; i < numRampActuators; i++) {
			// Ramp Method Label, Combo Box and feedback checkbox
			label = new JLabel("Actuator " + String.valueOf(i + 1));
			label.setHorizontalAlignment(JLabel.LEFT);
			label.setBounds(10, 50 + (40 * i), 100, 30);
			rampPanel.add(label);
			rampMethodComboBox[i] = new JComboBox(
					                 new DefaultComboBoxModel(
					                  new String[] {"Select Ramp Method", 
					                		        "none", 
					                		        "Linear",
					                		        "Sine", 
					                		        "Haversine" }));
			rampMethodComboBox[i].setBounds(100, 50 + (40 * i), 200, 30);
			rampPanel.add(rampMethodComboBox[i]);
			rampFeedbackCheckBox[i] = new JCheckBox("Use Feedback?");
			rampFeedbackCheckBox[i].setBounds(320, 50 + (40 * i), 200, 30);
			rampPanel.add(rampFeedbackCheckBox[i]);
		}

		// Dynamically set the new dimension for the Scroll bar
		Dimension d = rampPanel.getSize();
		d.setSize(d.getWidth(),
				  rampMethodComboBox[rampMethodComboBox.length - 1].getY() + 30);
		rampPanel.setPreferredSize(d);
	}

	/** Generate the SCRAMNet Mapping Panel */
	private void generateScramnetPanel() {
		scramnetPanel = new JPanel(new BorderLayout());
		setScramnetTable();
		simBlocksTextArea = new JTextArea("Add any Simulation Channel Names below (These start at address 2000)\n");
		scramnetPanel.add(simBlocksTextArea, BorderLayout.PAGE_END);
		scramnetPanel.revalidate();
		scramnetPanel.repaint();
	}

	/** Set SCRAMNet Table Sim-Controller data map */
	private void setScramnetTable() {
		// Create a new Table
		scramnetTable = new JTable(new SCRAMNetTableModel());
		scramnetPanel.add(scramnetTable.getTableHeader(),BorderLayout.PAGE_START);
		scramnetPanel.add(scramnetTable, BorderLayout.CENTER);
	}

	/** The Model for the SCRAMNet table */
	class SCRAMNetTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -4871823295758218042L;

		private String[] columnNames = { "Offset", 
										 "In Test", 
										 "Description",
										 "Units", 
										 "Scale", 
										 "Lower Limit", 
										 "Upper Limit" };

		private Object[][] data = {
				{ "0", new Boolean(false), "Simulation Running", "n_a", "1","0", "1" },
				{ "1", new Boolean(false), "Displacement Command 1", "mm","500", "-500", "500" },
				{ "2", new Boolean(false), "Velocity Command 1", "m_s", "2","-2", "2" },
				{ "3", new Boolean(false), "Acceleration Command 1", "m_s2","20", "-20", "20" },
				{ "4", new Boolean(false), "Force Command 1", "kN", "2000","-2000", "2000" },
				{ "5", new Boolean(false), "Force Derivative Command 1","kN_s", "120000", "-120000", "120000" },
				{ "6", new Boolean(false), "Displacement Command 2", "mm","500", "-500", "500" },
				{ "7", new Boolean(false), "Velocity Command 2", "m_s", "2","-2", "2" },
				{ "8", new Boolean(false), "Acceleration Command 2", "m_s2","20", "-20", "20" },
				{ "9", new Boolean(false), "Force Command 2", "kN", "2000","-2000", "2000" },
				{ "10", new Boolean(false), "Force Derivative Command 2","kN_s", "120000", "-120000", "120000" },
				{ "11", new Boolean(false), "Displacement Command 3", "mm","500", "-500", "500" },
				{ "12", new Boolean(false), "Velocity Command 3", "m_s", "2","-2", "2" },
				{ "13", new Boolean(false), "Acceleration Command 3", "m_s2","20", "-20", "20" },
				{ "14", new Boolean(false), "Force Command 3", "kN", "2000","-2000", "2000" },
				{ "15", new Boolean(false), "Force Derivative Command 3","kN_s", "120000", "-120000", "120000" },
				{ "16", new Boolean(false), "Displacement Command 4", "mm","500", "-500", "500" },
				{ "17", new Boolean(false), "Velocity Command 4", "m_s", "2","-2", "2" },
				{ "18", new Boolean(false), "Acceleration Command 4", "m_s2","20", "-20", "20" },
				{ "19", new Boolean(false), "Force Command 4", "kN", "2500","-2500", "2500" },
				{ "20", new Boolean(false), "Force Derivative Command 4","kN_s", "150000", "-150000", "150000" },
				{ "21", new Boolean(false), "Displacement Command 5", "mm","500", "-500", "500" },
				{ "22", new Boolean(false), "Velocity Command 5", "m_s", "2","-2", "2" },
				{ "23", new Boolean(false), "Acceleration Command 5", "m_s2","20", "-20", "20" },
				{ "24", new Boolean(false), "Force Command 5", "kN", "2500","-2500", "-2500" },
				{ "25", new Boolean(false), "Force Derivative Command 5","kN_s", "150000", "-150000", "150000" },
				{ "26", new Boolean(false), "Displacement Command 6", "mm","1", "0", "0" },
				{ "27", new Boolean(false), "Velocity Command 6", "m_s", "1","0", "0" },
				{ "28", new Boolean(false), "Acceleration Command 6", "m_s2","1", "0", "0" },
				{ "29", new Boolean(false), "Force Command 6", "kN", "1", "0","0" },
				{ "30", new Boolean(false), "Force Derivative Command 6","kN_s", "1", "0", "0" },
				{ "31", new Boolean(false), "Displacement Command 7", "mm","1", "0", "0" },
				{ "32", new Boolean(false), "Velocity Command 7", "m_s", "1","0", "0" },
				{ "33", new Boolean(false), "Acceleration Command 7", "m_s2","1", "0", "0" },
				{ "34", new Boolean(false), "Force Command 7", "kN", "1", "0","0" },
				{ "35", new Boolean(false), "Force Derivative Command 7","kN_s", "1", "0", "0" },
				{ "36", new Boolean(false), "Displacement Command 8", "mm","1", "0", "0" },
				{ "37", new Boolean(false), "Velocity Command 8", "m_s", "1","0", "0" },
				{ "38", new Boolean(false), "Acceleration Command 8", "m_s2","1", "0", "0" },
				{ "39", new Boolean(false), "Force Command 8", "kN", "1", "0","0" },
				{ "40", new Boolean(false), "Force Derivative Command 8","kN_s", "1", "0", "0" },
				{ "41", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "42", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "43", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "44", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "45", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "46", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "47", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "48", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "49", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "50", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "51", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "52", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "53", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "54", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "55", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "56", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "57", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "58", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "59", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "60", new Boolean(false), "Stop Bit", "n_a", "1", "0", "0" },
				{ "61", new Boolean(false), "Camera Trigger", "n_a", "1", "0", "0" },
				{ "62", new Boolean(false), "Pause Bit", "n_a", "1", "0", "0" },
				{ "63", new Boolean(false), "Pulse Trigger", "n_a", "1", "0","0" },
				{ "64", new Boolean(false), "Global Counter", "n_a", "1", "0","0" },
				{ "65", new Boolean(false), "Supply Pressure 1", "Bar", "400","0", "0" },
				{ "66", new Boolean(false), "Supply Pressure 2", "Bar", "400", "0","0" },
				{ "67", new Boolean(false), "Displacement 1", "mm", "500", "0", "0" },
				{ "68", new Boolean(false), "Load 1", "kN","2000", "0", "0" },
				{ "69", new Boolean(false), "Differential Pressure 1", "Bar", "400","0", "0" },
				{ "70", new Boolean(false), "Actuator Pressure 1", "DSP", "1","0", "0" },
				{ "71", new Boolean(false), "Velocity 1", "m_s", "2", "0","0" },
				{ "72", new Boolean(false), "Displacement Ctrl 1 OP", "DSP", "1", "0", "0" },
				{ "73", new Boolean(false), "Valve Lin 1 OP", "DSP","1", "0", "0" },
				{ "74", new Boolean(false), "Displacement 2", "mm", "500", "0", "0" },
				{ "75", new Boolean(false), "Load 2", "kN","2000", "0", "0" },
				{ "76", new Boolean(false), "Differential Pressure 2", "Bar", "400","0", "0" },
				{ "77", new Boolean(false), "Actuator Pressure 2", "DSP", "1","0", "0" },
				{ "78", new Boolean(false), "Velocity 2", "m_s", "2", "0","0" },
				{ "79", new Boolean(false), "Displacement Ctrl 2 OP", "DSP", "1", "0", "0" },
				{ "80", new Boolean(false), "Valve Lin 2 OP", "DSP","1", "0", "0" },
				{ "81", new Boolean(false), "Displacement 3", "mm", "500", "0", "0" },
				{ "82", new Boolean(false), "Load 3", "kN","2000", "0", "0" },
				{ "83", new Boolean(false), "Differential Pressure 3", "Bar", "400","0", "0" },
				{ "84", new Boolean(false), "Actuator Pressure 3", "DSP", "1","0", "0" },
				{ "85", new Boolean(false), "Velocity 3", "m_s", "2", "0","0" },
				{ "86", new Boolean(false), "Displacement Ctrl 3 OP", "DSP", "1", "0", "0" },
				{ "87", new Boolean(false), "Valve Lin 3 OP", "DSP","1", "0", "0" },
				{ "88", new Boolean(false), "Displacement 4", "mm", "500", "0", "0" },
				{ "89", new Boolean(false), "Load 4", "kN","2500", "0", "0" },
				{ "90", new Boolean(false), "Differential Pressure 4", "Bar", "400","0", "0" },
				{ "91", new Boolean(false), "Actuator Pressure 4", "DSP", "1","0", "0" },
				{ "92", new Boolean(false), "Velocity 4", "m_s", "2", "0","0" },
				{ "93", new Boolean(false), "Displacement Ctrl 4 OP", "DSP", "1", "0", "0" },
				{ "94", new Boolean(false), "Valve Lin 4 OP", "DSP","1", "0", "0" },
				{ "95", new Boolean(false), "Displacement 5", "mm", "500", "0", "0" },
				{ "96", new Boolean(false), "Load 5", "kN","2500", "0", "0" },
				{ "97", new Boolean(false), "Differential Pressure 5", "Bar", "400","0", "0" },
				{ "98", new Boolean(false), "Actuator Pressure 5", "DSP", "1","0", "0" },
				{ "99", new Boolean(false), "Velocity 5", "m_s", "2", "0","0" },
				{ "100", new Boolean(false), "Displacement Ctrl 5 OP", "DSP", "1", "0", "0" },
				{ "101", new Boolean(false), "Valve Lin 5 OP", "DSP","1", "0", "0" },
				{ "102", new Boolean(false), "Valve A Spool", "%", "100", "0", "0" },
				{ "103", new Boolean(false), "Valve B Spool", "%", "100", "0", "0" },
				{ "104", new Boolean(false), "Valve C Spool", "%", "100", "0", "0" },
				{ "105", new Boolean(false), "Valve D Spool", "%", "100", "0", "0" },
				{ "106", new Boolean(false), "Valve E Spool", "%", "100", "0", "0" },
				{ "107", new Boolean(false), "Valve F Spool", "%", "100", "0", "0" },
				{ "108", new Boolean(false), "Valve G Spool", "%", "100", "0", "0" },
				{ "109", new Boolean(false), "Valve H Spool", "%", "100", "0", "0" },
				{ "110", new Boolean(false), "Valve J Spool", "%", "100", "0", "0" },
				{ "111", new Boolean(false), "Valve K Spool", "%", "100", "0", "0" },
				{ "112", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "113", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "114", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "115", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "116", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "117", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "118", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "119", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "120", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "121", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "122", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "123", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "124", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "125", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "126", new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "127", new Boolean(false), "Spare", "n_a", "1", "0", "0" } };

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. 
		 */
		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			if (col > 0)
				return true;
			else
				return false;
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	/** Generate the Metadata Include Panel */
	private void generateMetadataPanel() {
		metadataPanel = new JPanel(null);
		// DAQ XML Include
		daqxmlCheckBox = new JCheckBox("Include DAQ XML below?");
		daqxmlCheckBox.setBounds(10, 0, 300, 30);
		daqxmlCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (daqxmlCheckBox.isSelected())
					daqscramnetCheckBox.setEnabled(true);
				else {
					daqscramnetCheckBox.setEnabled(false);
					daqscramnetCheckBox.setSelected(false);
				}
			}});
		metadataPanel.add(daqxmlCheckBox);		
		// DAQ SCRAMNet Export
		daqscramnetCheckBox = new JCheckBox("Export SCRAMnet List from DAQ XML?");
		daqscramnetCheckBox.setBounds(10, 30, 300, 30);
		daqscramnetCheckBox.setEnabled(false);
		metadataPanel.add(daqscramnetCheckBox);
		// xPC Configuration Export
		xPCMetadataCheckBox = new JCheckBox("Export xPC XML and Model files?");
		xPCMetadataCheckBox.setBounds(10, 60, 300, 30);
		metadataPanel.add(xPCMetadataCheckBox);
		// Data Turbine Configuration Export
		turbineMetadataCheckBox = new JCheckBox("Export Data Turbine XML?");
		turbineMetadataCheckBox.setBounds(10, 90, 300, 30);
		metadataPanel.add(turbineMetadataCheckBox);
		// Generate Button
		generateButton = new JButton("Generate");
		generateButton.setBounds(10, 120, 100, 30);
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				generateXML();
			}});
		metadataPanel.add(generateButton);
	}

	/** Generate the Tabbed Panes */
	private void generateTabbedPanes() {
		// Create the base tab pane
		configPanel = new JPanel(new BorderLayout());
		this.getContentPane().add(configPanel);
		configTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		configPanel.add(configTabbedPane, BorderLayout.CENTER);

		// SCRAMNet Tab Pane
		scramnetScroll = new JScrollPane();
		scramnetScroll.getViewport().add(scramnetPanel);
		configTabbedPane.addTab("SCRAMNet", scramnetScroll);
		// Integrator Tab Pane
		configTabbedPane.addTab("Integrator", integratorPanel);
		// Kinematics Tab Pane with Scroll
		kinematicsScroll = new JScrollPane();
		kinematicsScroll.getViewport().add(kinematicsPanel);
		configTabbedPane.addTab("Kinematics", kinematicsScroll);
		// Ramp Generator Tab Pane with Scroll
		rampScroll = new JScrollPane();
		rampScroll.getViewport().add(rampPanel);
		configTabbedPane.addTab("Ramp Generators", rampScroll);
		// DAQ Tab Pane
		configTabbedPane.addTab("Metadata", metadataPanel);
	}

	/** Turn off some Integration panel options */
	private void disableIntegrationPanelOptions() {
		integratorComboBox.setEnabled(false);
		historyFileComboBox.setEnabled(false);
		numDOFTextField.setEnabled(false);
		timeStepsTextField.setEnabled(false);
		subStepsTextField.setEnabled(false);
		controlComboBox.setEnabled(false);
		unitsComboBox.setEnabled(false);
		alphaTextField.setEnabled(false);
		deltaTTextField.setEnabled(false);
		matriciesButton.setEnabled(false);
	}

	/** Turn off Total Kinematics panel options */
	private void disableKinematicsPanelOptions() {
		for (int i = 0; i < numKinematicDevices; i++) {
			mfnbxTextField[i].setEnabled(false);
			mfnbyTextField[i].setEnabled(false);
			mfnbzTextField[i].setEnabled(false);
			mfnlocalxTextField[i].setEnabled(false);
			mfnlocalyTextField[i].setEnabled(false);
			mfnlocalzTextField[i].setEnabled(false);
			mblengthTextField[i].setEnabled(false);
		}
	}

	/** Check if the string is an Integer */
	private int checkIfInteger(String itemName, String value) {
		try {
			if (value == null) {
				JOptionPane.showMessageDialog(this, itemName + " is null");
				return -1;
			}
			int retval = Integer.parseInt(value);
			return retval;
		} catch (NumberFormatException numForEx) {
			JOptionPane.showMessageDialog(this, "Bad Value: " + itemName + "= "	+ value);
			return -1;
		}
	}

	/** Check if the string is a Double */
	private double checkIfDouble(String itemName, String value) {
		try {
			if (value == null) {
				JOptionPane.showMessageDialog(this, itemName + " is null");
				return -1;
			}
			double retval = Double.parseDouble(value);
			return retval;
		} catch (NumberFormatException numForEx) {
			JOptionPane.showMessageDialog(this, "Bad Value: " + itemName + "= "	+ value);
			return -1;
		}
	}

	/** Generate the XML */
	private void preview(boolean show) {
		try {
			// First acquire Integration Matrices
			String M = "";
			String K = "";
			String C = "";
			if ((((String) integratorComboBox.getSelectedItem()).equals("Alpha Method"))
			   || 
			   (((String) integratorComboBox.getSelectedItem()).equals("Newmark Method"))) {
				int DOF = checkIfInteger("# DOF", numDOFTextField.getText());
				for (int i = 0; i < DOF; i++) {
					for (int j = 0; j < DOF; j++) {
						if (i == DOF - 1 && j == DOF - 1) {
							M = M + checkIfDouble("M[" + i + "," + j + "]",(String) matrixTable.getValueAt(i,j + 1));
							if (((String) integratorComboBox.getSelectedItem()).equals("Alpha Method"))
								K = K + checkIfDouble("K[" + i + "," + j + "]",(String) matrixTable.getValueAt(DOF	+ i, j + 1));
							C = C + checkIfDouble("C[" + i + "," + j + "]",(String) matrixTable.getValueAt((DOF * 2) + i, j + 1));													
						} else {
							M = M + checkIfDouble("M[" + i + "," + j + "]",(String) matrixTable.getValueAt(i,j + 1)) + ",";
							if (((String) integratorComboBox.getSelectedItem()).equals("Alpha Method")) 
								K = K + checkIfDouble("K[" + i + "," + j + "]",(String) matrixTable.getValueAt(DOF + i, j + 1)) + ",";
							C = C + checkIfDouble("C[" + i + "," + j + "]",(String) matrixTable.getValueAt((DOF * 2) + i, j + 1)) + ",";							
						}
					}
				}
			}

			// Create the XML File
			xml = new XMLOutputter(org.jdom.output.Format.getPrettyFormat());
			doc = new Document(new Element("NEESsim"));
			// Integrator
			// PSD
			if (integrationMethodComboBox.getSelectedIndex() == 1) {
				// Alpha Method
				if (integratorComboBox.getSelectedIndex() == 1) {
					Element integrator = new Element("Integrator");
					doc.getRootElement().addContent(integrator);
					integrator.setAttribute("ON", "true");
					Element alpha = new Element("Alpha");
					integrator.addContent(alpha);
					Element history = new Element("History");
					history.setAttribute("value", (String) historyFileComboBox.getSelectedItem());
					alpha.addContent(history);
					Element dof = new Element("DOF");
					dof.setAttribute("value", numDOFTextField.getText());
					alpha.addContent(dof);
					Element steps = new Element("Steps");
					steps.setAttribute("value", timeStepsTextField.getText());
					alpha.addContent(steps);
					Element iterations = new Element("Iterations");
					iterations.setAttribute("value", subStepsTextField.getText());
					alpha.addContent(iterations);
					Element deltaT = new Element("DeltaT");
					deltaT.setAttribute("value", deltaTTextField.getText());
					alpha.addContent(deltaT);
					Element control = new Element("Control");
					control.setAttribute("value", (String) controlComboBox.getSelectedItem());
					alpha.addContent(control);
					Element units = new Element("Units");
					units.setAttribute("value", (String) unitsComboBox.getSelectedItem());
					alpha.addContent(units);
					Element alphaConstant = new Element("AlphaConstant");
					alphaConstant.setAttribute("value", alphaTextField.getText());
					alpha.addContent(alphaConstant);
					Element massMatrix = new Element("MassMatrix");
					massMatrix.setAttribute("value", M);
					alpha.addContent(massMatrix);
					Element stiffnessMatrix = new Element("StiffnessMatrix");
					stiffnessMatrix.setAttribute("value", K);
					alpha.addContent(stiffnessMatrix);
					Element dampeningMatrix = new Element("DampeningMatrix");
					dampeningMatrix.setAttribute("value", C);
					alpha.addContent(dampeningMatrix);
				}
				// Newmark Method
				if (integratorComboBox.getSelectedIndex() == 2) { 
					Element integrator = new Element("Integrator");
					doc.getRootElement().addContent(integrator);
					integrator.setAttribute("ON", "true");
					Element newmark = new Element("Newmark");
					integrator.addContent(newmark);
					Element history = new Element("History");
					history.setAttribute("value", (String) historyFileComboBox.getSelectedItem());
					newmark.addContent(history);
					Element dof = new Element("DOF");
					dof.setAttribute("value", numDOFTextField.getText());
					newmark.addContent(dof);
					Element steps = new Element("Steps");
					steps.setAttribute("value", timeStepsTextField.getText());
					newmark.addContent(steps);
					Element iterations = new Element("Iterations");
					iterations.setAttribute("value", subStepsTextField.getText());
					newmark.addContent(iterations);
					Element deltaT = new Element("DeltaT");
					deltaT.setAttribute("value", deltaTTextField.getText());
					newmark.addContent(deltaT);
					Element control = new Element("Control");
					control.setAttribute("value", (String) controlComboBox.getSelectedItem());
					newmark.addContent(control);
					Element units = new Element("Units");
					units.setAttribute("value", (String) unitsComboBox.getSelectedItem());
					newmark.addContent(units);
					Element alphaConstant = new Element("AlphaConstant");
					alphaConstant.setAttribute("value", alphaTextField.getText());
					newmark.addContent(alphaConstant);
					Element massMatrix = new Element("MassMatrix");
					massMatrix.setAttribute("value", M);
					newmark.addContent(massMatrix);
					Element dampeningMatrix = new Element("DampeningMatrix");
					dampeningMatrix.setAttribute("value", C);
					newmark.addContent(dampeningMatrix);
				}
			// Effective Force
			} else if (integrationMethodComboBox.getSelectedIndex() == 2) { 
				Element integrator = new Element("Integrator");
				doc.getRootElement().addContent(integrator);
				integrator.setAttribute("ON", "true");
				Element eff = new Element("EFF");
				integrator.addContent(eff);
				Element history = new Element("History");
				history.setAttribute("value", (String) historyFileComboBox.getSelectedItem());
				eff.addContent(history);
				Element dof = new Element("DOF");
				dof.setAttribute("value", numDOFTextField.getText());
				eff.addContent(dof);
				Element steps = new Element("Steps");
				steps.setAttribute("value", timeStepsTextField.getText());
				eff.addContent(steps);
				Element control = new Element("Control");
				control.setAttribute("value", (String) controlComboBox.getSelectedItem());
				eff.addContent(control);
				Element units = new Element("Units");
				units.setAttribute("value", (String) unitsComboBox.getSelectedItem());
				eff.addContent(units);
			} else if (integrationMethodComboBox.getSelectedIndex() == 3) { // Predefined
				Element integrator = new Element("Integrator");
				doc.getRootElement().addContent(integrator);
				integrator.setAttribute("ON", "true");
				Element predefined = new Element("Predefined");
				integrator.addContent(predefined);
				Element history = new Element("History");
				history.setAttribute("value", (String) historyFileComboBox.getSelectedItem());
				predefined.addContent(history);
				Element dof = new Element("DOF");
				dof.setAttribute("value", numDOFTextField.getText());
				predefined.addContent(dof);
				Element steps = new Element("Steps");
				steps.setAttribute("value", timeStepsTextField.getText());
				predefined.addContent(steps);
				Element control = new Element("Control");
				control.setAttribute("value", (String) controlComboBox.getSelectedItem());
				predefined.addContent(control);
				Element units = new Element("Units");
				units.setAttribute("value", (String) unitsComboBox.getSelectedItem());
				predefined.addContent(units);
			} else {
				Element integrator = new Element("Integrator");
				doc.getRootElement().addContent(integrator);
				integrator.setAttribute("ON", "false");
			}

			// Kinematics
			// Incremental
			if (kinematicsMethodComboBox.getSelectedIndex() == 1) { 
				Element kinematics = new Element("Kinematics");
				doc.getRootElement().addContent(kinematics);
				kinematics.setAttribute("ON", "true");
				kinematics.setAttribute("Type", "Incremental");
				Element spn = new Element("SPN");
				spn.setAttribute("X", spnxTextField.getText());
				spn.setAttribute("Y", spnyTextField.getText());
				spn.setAttribute("Z", spnzTextField.getText());
				spn.setAttribute("Length", spnwidthTextField.getText());
				spn.setAttribute("Height", spnheightTextField.getText());
				kinematics.addContent(spn);
				for (int i = 0; i < numKinematicActuators; i++) {
					Element actuator = new Element("Actuator");
					actuator.setAttribute("ID", Integer.toString(i));
					actuator.setAttribute("ASNx", asnxTextField[i].getText());
					actuator.setAttribute("ASNy", asnyTextField[i].getText());
					actuator.setAttribute("ASNz", asnzTextField[i].getText());
					actuator.setAttribute("AFNx", afnxTextField[i].getText());
					actuator.setAttribute("AFNy", afnyTextField[i].getText());
					actuator.setAttribute("AFNz", afnzTextField[i].getText());
					actuator.setAttribute("Length", alengthTextField[i].getText());
					kinematics.addContent(actuator);
				}
				for (int i = 0; i < numKinematicDevices; i++) {
					Element device = new Element("Device");
					device.setAttribute("ID", Integer.toString(i));
					device.setAttribute("MSNx", msnxTextField[i].getText());
					device.setAttribute("MSNy", msnyTextField[i].getText());
					device.setAttribute("MSNz", msnzTextField[i].getText());
					device.setAttribute("MFNx", mfnxTextField[i].getText());
					device.setAttribute("MFNy", mfnyTextField[i].getText());
					device.setAttribute("MFNz", mfnzTextField[i].getText());
					device.setAttribute("Length", mlengthTextField[i].getText());
					kinematics.addContent(device);
				}
			// Total
			} else if (kinematicsMethodComboBox.getSelectedIndex() == 2) { 
				Element kinematics = new Element("Kinematics");
				doc.getRootElement().addContent(kinematics);
				kinematics.setAttribute("ON", "true");
				kinematics.setAttribute("Type", "Total");
				Element spn = new Element("SPN");
				spn.setAttribute("X", spnxTextField.getText());
				spn.setAttribute("Y", spnyTextField.getText());
				spn.setAttribute("Z", spnzTextField.getText());
				spn.setAttribute("Length", spnwidthTextField.getText());
				spn.setAttribute("Height", spnheightTextField.getText());
				kinematics.addContent(spn);
				for (int i = 0; i < numKinematicActuators; i++) {
					Element actuator = new Element("Actuator");
					actuator.setAttribute("ID", Integer.toString(i));
					actuator.setAttribute("ASNx", asnxTextField[i].getText());
					actuator.setAttribute("ASNy", asnyTextField[i].getText());
					actuator.setAttribute("ASNz", asnzTextField[i].getText());
					actuator.setAttribute("AFNx", afnxTextField[i].getText());
					actuator.setAttribute("AFNy", afnyTextField[i].getText());
					actuator.setAttribute("AFNz", afnzTextField[i].getText());
					actuator.setAttribute("Length", alengthTextField[i].getText());
					kinematics.addContent(actuator);
				}
				for (int i = 0; i < numKinematicDevices; i++) {
					Element device = new Element("Device");
					device.setAttribute("ID", Integer.toString(i));
					device.setAttribute("MSNx", msnxTextField[i].getText());
					device.setAttribute("MSNy", msnyTextField[i].getText());
					device.setAttribute("MSNz", msnzTextField[i].getText());
					device.setAttribute("MFNx", mfnxTextField[i].getText());
					device.setAttribute("MFNy", mfnyTextField[i].getText());
					device.setAttribute("MFNz", mfnzTextField[i].getText());
					device.setAttribute("LengthA", mlengthTextField[i].getText());
					device.setAttribute("MFNbx", mfnbxTextField[i].getText());
					device.setAttribute("MFNby", mfnbyTextField[i].getText());
					device.setAttribute("MFNbz", mfnbzTextField[i].getText());
					device.setAttribute("LengthB", mblengthTextField[i].getText());
					device.setAttribute("MFNlocalx", mfnlocalxTextField[i].getText());
					device.setAttribute("MFNlocaly", mfnlocalyTextField[i].getText());
					device.setAttribute("MFNlocalz", mfnlocalzTextField[i].getText());
					kinematics.addContent(device);
				}
			} else {
				Element kinematics = new Element("Kinematics");
				doc.getRootElement().addContent(kinematics);
				kinematics.setAttribute("ON", "false");
			}

			// Ramp Generators
			if (numRampActuators > 0) {
				Element rampgenerator = new Element("RampGenerator");
				doc.getRootElement().addContent(rampgenerator);
				rampgenerator.setAttribute("ON", "true");
				for (int i = 0; i < numRampActuators; i++) {
					Element rampActuator = new Element("Actuator");
					rampActuator.setAttribute("ID", Integer.toString(i));
					rampActuator.setAttribute("Ramp",(String) rampMethodComboBox[i].getSelectedItem());
					rampActuator.setAttribute("UseFeedback", String.valueOf(rampFeedbackCheckBox[i].isSelected()));
					rampgenerator.addContent(rampActuator);
				}
				Element ticks = new Element("Ticks");
				ticks.setAttribute("value", Integer.toString(numRampTicks));
				rampgenerator.addContent(ticks);
			} else {
				Element rampgenerator = new Element("RampGenerator");
				doc.getRootElement().addContent(rampgenerator);
				rampgenerator.setAttribute("ON", "false");
			}

			// SCRAMNet
			Element scramnet = new Element("Scramnet");
			doc.getRootElement().addContent(scramnet);
			scramnet.setAttribute("ON", "true");
			for (int i = 0; i < scramnetTable.getRowCount(); i++) {
				Element ctrlBlock = new Element("CtrlBlock");
				ctrlBlock.setAttribute("Offset", scramnetTable.getValueAt(i, 0).toString());
				ctrlBlock.setAttribute("Stream", scramnetTable.getValueAt(i, 1).toString());
				ctrlBlock.setAttribute("Description", scramnetTable.getValueAt(i, 2).toString());
				ctrlBlock.setAttribute("Units", scramnetTable.getValueAt(i, 3).toString());
				ctrlBlock.setAttribute("Scale", scramnetTable.getValueAt(i, 4).toString());
				ctrlBlock.setAttribute("LowerLimit", scramnetTable.getValueAt(i, 5).toString());
				ctrlBlock.setAttribute("UpperLimit", scramnetTable.getValueAt(i, 6).toString());
				scramnet.addContent(ctrlBlock);
			}

			// Add SimBlocks
			String[] simBlockRowText = simBlocksTextArea.getText().split("\n");
			for (int i = 0; i < simBlockRowText.length - 1; i++) {
				Element simBlock = new Element("SimBlock");
				simBlock.setAttribute("Offset", Integer.toString(2000 + i));
				simBlock.setAttribute("Name", simBlockRowText[i + 1]);
				scramnet.addContent(simBlock);
			}

			if (show) {
				JFrame frame = new JFrame("Preview: XML Configuration");
				frame.getContentPane().setLayout(new BorderLayout());
				frame.setLocation(100, 100);
				frame.setSize(600, 600);
				StringWriter sw = new StringWriter();
				xml.output(doc, sw);
				JTextArea textarea = new JTextArea(sw.toString());
				JScrollPane scrollpane = new JScrollPane(textarea);
				frame.getContentPane().add(scrollpane);
				frame.setDefaultCloseOperation(HIDE_ON_CLOSE);
				frame.setVisible(true);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Preview Error:  Missing Value");
			e.printStackTrace();
		}

	}

	/**
	 * Validate the XML so that all fields required are entered and proper
	 * values
	 */
	private boolean validateXML() {
		// First check Matricies
		if ((((String) integratorComboBox.getSelectedItem()).equals("Alpha Method"))
		 || (((String) integratorComboBox.getSelectedItem()).equals("Newmak Method"))) {
			int DOF = checkIfInteger("# DOF", numDOFTextField.getText());
			if (matrixTable == null)
				JOptionPane.showMessageDialog(this, "Matrix Table not set");
			else {
				for (int i = 0; i < DOF; i++) {
					for (int j = 0; j < DOF; j++) {
						if (i == DOF - 1 && j == DOF - 1) {
							checkIfDouble("M[" + i + "," + j + "]",(String) matrixTable.getValueAt(i, j + 1));
							if ((((String) integratorComboBox.getSelectedItem()).equals("Alpha Method")))
								checkIfDouble("K[" + i + "," + j + "]",(String) matrixTable.getValueAt(DOF + i, j + 1));
							checkIfDouble("C[" + i + "," + j + "]",(String) matrixTable.getValueAt((DOF * 2) + i, j + 1));
						} else {
							checkIfDouble("M[" + i + "," + j + "]",(String) matrixTable.getValueAt(i, j + 1));
							if ((((String) integratorComboBox.getSelectedItem()).equals("Alpha Method")))
								checkIfDouble("K[" + i + "," + j + "]",(String) matrixTable.getValueAt(DOF + i, j + 1));
							checkIfDouble("C[" + i + "," + j + "]",(String) matrixTable.getValueAt((DOF * 2) + i, j + 1));
						}
					}
				}
			}
		}

		// Check the Integrator Parameters
		if (integrationMethodComboBox.getSelectedIndex() == 1) { // PSD
			if (integratorComboBox.getSelectedIndex() == 0) // nothing
				JOptionPane.showMessageDialog(this, "No Integrator Selected");
			if (integratorComboBox.getSelectedIndex() == 1) { // Alpha
				if (historyFileComboBox.getSelectedIndex() == 0)
					JOptionPane.showMessageDialog(this,	"No History File Selected");
				else {
					checkIfInteger("# DOF", numDOFTextField.getText());
					checkIfInteger("# Time Steps", timeStepsTextField.getText());
					checkIfInteger("# Sub Steps", subStepsTextField.getText());
					checkIfDouble("Delta T", deltaTTextField.getText());
					checkIfDouble("Alpha", alphaTextField.getText());
				}
			}
			if (integratorComboBox.getSelectedIndex() == 2) { // Newmark Method
				if (historyFileComboBox.getSelectedIndex() == 0)
					JOptionPane.showMessageDialog(this,"No History File Selected");
				else {
					checkIfInteger("# DOF", numDOFTextField.getText());
					checkIfInteger("# Time Steps", timeStepsTextField.getText());
					checkIfDouble("Delta T", deltaTTextField.getText());
					checkIfDouble("Alpha", alphaTextField.getText());
				}
			}
		} else if (integratorComboBox.getSelectedIndex() == 2) { // Effective Force
			if (historyFileComboBox.getSelectedIndex() == 0)
				JOptionPane.showMessageDialog(this, "No History File Selected");
			else {
				checkIfInteger("# Time Steps", timeStepsTextField.getText());
			}
		} else if (integratorComboBox.getSelectedIndex() == 3) { // Predefined
			if (historyFileComboBox.getSelectedIndex() == 0)
				JOptionPane.showMessageDialog(this, "No History File Selected");
			else {
				checkIfInteger("# Time Steps", timeStepsTextField.getText());
			}
			if (controlComboBox.getSelectedIndex() == 0)
				JOptionPane.showMessageDialog(this, "No Control Selected");
		}

		// Check Kinematics
		if (kinematicsMethodComboBox.getSelectedIndex() != 0) { // Kinematics
			checkIfDouble("SPN X", spnxTextField.getText());
			checkIfDouble("SPN Y", spnyTextField.getText());
			checkIfDouble("SPN Z", spnzTextField.getText());
			checkIfDouble("SPN Width", spnwidthTextField.getText());
			checkIfDouble("SPN Height", spnheightTextField.getText());
			for (int i = 0; i < numKinematicActuators; i++) {
				checkIfDouble("ASN X" + String.valueOf(i + 1), asnxTextField[i].getText());
				checkIfDouble("ASN Y" + String.valueOf(i + 1), asnyTextField[i].getText());
				checkIfDouble("ASN Z" + String.valueOf(i + 1), asnzTextField[i].getText());
				checkIfDouble("AFN X" + String.valueOf(i + 1), afnxTextField[i].getText());
				checkIfDouble("AFN Y" + String.valueOf(i + 1), afnyTextField[i].getText());
				checkIfDouble("AFN Z" + String.valueOf(i + 1), afnzTextField[i].getText());
				checkIfDouble("A" + String.valueOf(i + 1) + " Length",alengthTextField[i].getText());
			}
			for (int i = 0; i < numKinematicDevices; i++) {
				checkIfDouble("MSN X" + String.valueOf(i + 1), msnxTextField[i].getText());
				checkIfDouble("MSN Y" + String.valueOf(i + 1), msnyTextField[i].getText());
				checkIfDouble("MSN Z" + String.valueOf(i + 1), msnzTextField[i].getText());
				checkIfDouble("MFN X" + String.valueOf(i + 1), mfnxTextField[i].getText());
				checkIfDouble("MFN Y" + String.valueOf(i + 1), mfnyTextField[i].getText());
				checkIfDouble("MFN Z" + String.valueOf(i + 1), mfnzTextField[i].getText());
				checkIfDouble("M" + String.valueOf(i + 1) + " Length",mlengthTextField[i].getText());
				if (kinematicsMethodComboBox.getSelectedIndex() == 2) { // Total Kinematics only
					checkIfDouble("MFN-B X" + String.valueOf(i + 1),mfnbxTextField[i].getText());
					checkIfDouble("MFN-B Y" + String.valueOf(i + 1),mfnbyTextField[i].getText());
					checkIfDouble("MFN-B Z" + String.valueOf(i + 1),mfnbzTextField[i].getText());
					checkIfDouble("M-B" + String.valueOf(i + 1) + " Length",mblengthTextField[i].getText());
					checkIfDouble("MFN Local X" + String.valueOf(i + 1),mfnlocalxTextField[i].getText());
					checkIfDouble("MFN Local Y" + String.valueOf(i + 1),mfnlocalyTextField[i].getText());
					checkIfDouble("MFN Local Z" + String.valueOf(i + 1),mfnlocalzTextField[i].getText());
				}
			}
		}

		// Check RAMP selections
		for (int i = 0; i < numRampActuators; i++) {
			if (rampMethodComboBox[i].getSelectedIndex() == 0)
				JOptionPane.showMessageDialog(this,"Ramping Method not selected for Actuator " + String.valueOf(i + 1));
			else if (numRampTicks == 0)
				JOptionPane.showMessageDialog(this, "No Ramp Ticks defined");
		}

		// SCRAMNet
		for (int i = 0; i < scramnetTable.getRowCount(); i++) {
			checkIfDouble("Scale " + String.valueOf(i), scramnetTable.getValueAt(i, 4).toString());
			checkIfDouble("LowerLimit " + String.valueOf(i), scramnetTable.getValueAt(i, 5).toString());
			checkIfDouble("UpperLimit " + String.valueOf(i), scramnetTable.getValueAt(i, 6).toString());
		}

		// All good!
		return true;
	}

	/** Save the Generator Configuration */
	private boolean saveConfiguration() {
		if (!validateXML()) {
			return false;
		}
		
		if (configFilename == null) {
			// Create new file
			configFilename = FileHandler.getFilePath("Save Configuration", new XMLFileFilter());			
		}
		
		if (configFilename == null) {
			return false;
		}
		
		try {
			preview(false); // don't show the config to the user but generate
							// the file
			// Write XML object to file
			xml.output(doc, new FileWriter(new File(configFilename)));
			JOptionPane.showMessageDialog(this, "Configuration File Saved");
		} catch (Exception e) {e.printStackTrace(); return false;}	
		
		// Set the App title to reflect current configuration file opened
		this.setTitle("RTMD: Integrated Control Configurator" + " - " + configFilename);
		return true;
	}
	
	/** Save As */
	private void saveAs() {
		configFilename = null;
		saveConfiguration();
	}

	/** Open the Generator configuration */
	private void openConfiguration() {
		configFilename = FileHandler.getFilePath("Open Configuration File", new XMLFileFilter());
		if (configFilename == null) {
			return;
		}		
		
		// Set the App title to reflect current configuration file opened
		this.setTitle("RTMD: Integrated Control Configurator" + " - " + configFilename);
		// Import the SCRAMNet information
		ReadXMLConfig xmlconfig = new ReadXMLConfig(new File(configFilename));
		XMLScramnetConfig scrxml = xmlconfig.getScramnetConfig();
		for (int i = 0; i < scrxml.getnumCtrlBlocks(); i++) {
			scramnetTable.setValueAt(scrxml.getCtrlOffset(i),i,0);
			scramnetTable.setValueAt(Boolean.parseBoolean(scrxml.getCtrlStream(i)),i,1);
			scramnetTable.setValueAt(scrxml.getCtrlDescription(i),i,2);
			scramnetTable.setValueAt(scrxml.getCtrlUnits(i),i,3);
			scramnetTable.setValueAt(scrxml.getCtrlScale(i),i,4);
			scramnetTable.setValueAt(scrxml.getLowerLimit(i),i,5);
			scramnetTable.setValueAt(scrxml.getUpperLimit(i),i,6);
		}
		simBlocksTextArea.setText("Add any Simulation Channel Names below (These start at address 2000)\n");
		for (int i = 0; i < scrxml.getnumSimBlocks(); i++) {
			simBlocksTextArea.append(scrxml.getSimName(i) + "\n");
		}
		// Import the Integration information
		XMLIntegratorConfig intxml = xmlconfig.getIntegratorConfig();
		if (intxml.getIsOn()) {
			if (intxml.gettestType().equals("Alpha")) {
				integrationMethodComboBox.setSelectedIndex(1);
				integratorComboBox.setSelectedIndex(1);				
			}
			if (intxml.gettestType().equals("Newmark")) {
				integrationMethodComboBox.setSelectedIndex(1);
				integratorComboBox.setSelectedIndex(2);				
			}
			if (intxml.gettestType().equals("EFF")) {
				integrationMethodComboBox.setSelectedIndex(2);
				integratorComboBox.setSelectedIndex(0);				
			}
			if (intxml.gettestType().equals("Predefined")) {
				integrationMethodComboBox.setSelectedIndex(3);
				integratorComboBox.setSelectedIndex(0);				
			}										
			historyFileComboBox.setSelectedItem(intxml.gethistoryFile());
			deltaTTextField.setText(Double.toString(intxml.getdeltaT()));
			numDOFTextField.setText(Integer.toString(intxml.getnumDOF()));
			timeStepsTextField.setText(Integer.toString(intxml.getsteps()));
			subStepsTextField.setText(Integer.toString(intxml.getiterations()));
			controlComboBox.setSelectedItem(intxml.getControl());
			unitsComboBox.setSelectedItem(intxml.getUnits());
			alphaTextField.setText(Double.toString(intxml.getalpha()));
			// Matricies 			
			int DOF = Integer.parseInt(numDOFTextField.getText());
			matrixTable = new JTable(DOF * 3, DOF + 1);
			matrixTable.setTableHeader(null);
			matrixTable.setRowHeight(15);
			matrixTable.setValueAt("M", 0, 0);			
			matrixTable.setValueAt("K", DOF * 1, 0);
			matrixTable.setValueAt("C", DOF * 2, 0);		
			for (int i = 0; i < DOF; i++) {
				for (int j = 0; j < DOF; j++) {					
					matrixTable.setValueAt(Double.toString(intxml.getM().get(i,j)),i,j+1);
					if (intxml.gettestType().equals("Alpha")) 
						matrixTable.setValueAt(Double.toString(intxml.getK().get(i,j)),DOF + i, j + 1);
					matrixTable.setValueAt(Double.toString(intxml.getC().get(i,j)),(DOF * 2) + i, j + 1);					
				}
			}
		}
			
		// Import the Kinematics information
		XMLKinematicsConfig kinxml = xmlconfig.getKinematicsConfig();
		if (kinxml.getIsOn()) {
			kinematicsMethodComboBox.setSelectedItem(kinxml.getType());
			spnxTextField.setText(Double.toString(kinxml.getSPNx()));
			spnyTextField.setText(Double.toString(kinxml.getSPNy()));
			spnzTextField.setText(Double.toString(kinxml.getSPNz()));
			spnheightTextField.setText(Double.toString(kinxml.getHeight()));
			spnwidthTextField.setText(Double.toString(kinxml.getLength()));												
			numKinematicActuators = kinxml.getnumActuators();
			setNumberActuatorsButton.setText("Number of Actuators? " + String.valueOf(numKinematicActuators));
			// Set the Actuator options of the Kinematics Panel and update
			// Scroll length
			generateActuatorKinematicsOptions();
			updateKinematicsScrollLength();			
			for (int i = 0; i < numKinematicActuators; i++) {
				asnxTextField[i].setText(Double.toString(kinxml.getASNx(i)));
				asnyTextField[i].setText(Double.toString(kinxml.getASNy(i)));
				asnzTextField[i].setText(Double.toString(kinxml.getASNz(i)));				
				afnxTextField[i].setText(Double.toString(kinxml.getAFNx(i)));
				afnyTextField[i].setText(Double.toString(kinxml.getAFNy(i)));
				afnzTextField[i].setText(Double.toString(kinxml.getAFNz(i)));
				alengthTextField[i].setText(Double.toString(kinxml.getActLength(i)));
			}
			
			numKinematicDevices = kinxml.getnumDevices();
			setNumberDevicesButton.setText("Number of Devices? " + String.valueOf(numKinematicDevices));
			// Set the Device options of the Kinematics Panel and update
			// Scroll length
			generateDeviceKinematicsOptions();
			updateKinematicsScrollLength();
			disableKinematicsPanelOptions(); // Cancel all options
			for (int i = 0; i < numKinematicDevices; i++) {
				msnxTextField[i].setText(Double.toString(kinxml.getMSNx(i)));
				msnyTextField[i].setText(Double.toString(kinxml.getMSNy(i)));
				msnzTextField[i].setText(Double.toString(kinxml.getMSNz(i)));				
				mfnxTextField[i].setText(Double.toString(kinxml.getMFNx(i)));
				mfnyTextField[i].setText(Double.toString(kinxml.getMFNy(i)));
				mfnzTextField[i].setText(Double.toString(kinxml.getMFNz(i)));
				mlengthTextField[i].setText(Double.toString(kinxml.getDevLength(i)));
				if (kinematicsMethodComboBox.getSelectedIndex() == 2) { // Total
					mfnbxTextField[i].setEnabled(true);
					mfnbyTextField[i].setEnabled(true);
					mfnbzTextField[i].setEnabled(true);
					mfnbxTextField[i].setText(Double.toString(kinxml.getMFNbx(i)));
					mfnbyTextField[i].setText(Double.toString(kinxml.getMFNby(i)));
					mfnbzTextField[i].setText(Double.toString(kinxml.getMFNbz(i)));
					mfnlocalxTextField[i].setEnabled(true);
					mfnlocalyTextField[i].setEnabled(true);
					mfnlocalzTextField[i].setEnabled(true);
					mfnlocalxTextField[i].setText(Double.toString(kinxml.getMFNlocalx(i)));
					mfnlocalyTextField[i].setText(Double.toString(kinxml.getMFNlocaly(i)));
					mfnlocalzTextField[i].setText(Double.toString(kinxml.getMFNlocalz(i)));
					mblengthTextField[i].setEnabled(true);					
					mblengthTextField[i].setText(Double.toString(kinxml.getDevLengthB(i)));
				}
			}							
			
			kinematicsPanel.revalidate();
			kinematicsPanel.repaint();
			
			
		}
		
		// Import the Ramp information
		XMLRampConfig rampxml = xmlconfig.getRampConfig();
		if (rampxml.getIsOn()) {
			numRampActuators = rampxml.getnumActuators();
			setNumberRampActuatorsButton.setText("Number of Actuators? " + String.valueOf(numRampActuators));
			// Set the Actuator options of the Ramp Panel
			generateRampPanelOptions();
			for (int i = 0; i < numRampActuators; i++) {
				rampMethodComboBox[i].setSelectedItem(rampxml.getrampType(i));
				rampFeedbackCheckBox[i].setSelected(rampxml.getUseFeedback(i));
			}
		
			numRampTicks = rampxml.getticks();
			setNumberRampTicksButton.setText("Number of Ticks? " + String.valueOf(numRampTicks));			
	
			rampPanel.revalidate();
			rampPanel.repaint();
		}
	
	}
		

	/** Save the metadata XML files */
	private void generateXML() {
		// You must first save the configuration
		if (!saveConfiguration()) {
			return;
		}			
		
		// Save a copy of the configuration file name
		String tempConfigFilename = configFilename;
		
		// Check if import DAQ XML in Metadata tab was checked
		if (daqxmlCheckBox.isSelected()) {
			// Open DAQ XML file
			BufferedReader daqxmlfile = FileHandler.chooseInputFile("Open DAQ Configuration XML File", new XMLFileFilter());
			if (daqxmlfile == null)
				return;
			// Merge with NEESsim file
			BufferedReader simxmlfile = FileHandler.openInputFile(tempConfigFilename);
			PrintStream tempoutfile = FileHandler.openOutputFile("_temp");
			tempoutfile.println("<root>");
			try {
				String s;
				s = simxmlfile.readLine();
				while ((s = simxmlfile.readLine()) != null)
					tempoutfile.println(s);
			} catch (IOException e) {}			
			try {
				String s;
				while ((s = daqxmlfile.readLine()) != null)
					tempoutfile.println(s);
			} catch (IOException e) {}		
			tempoutfile.println("</root>");
			tempoutfile.close();
			// Base the rest off this filename now
			tempConfigFilename = "_temp";
		}

		// Check if DAQ XML in Metadata tab was checked
		if (daqscramnetCheckBox.isSelected()) {
			// Create DAQ SCRAMNet XML
			JOptionPane.showMessageDialog(this,"Now save DAQ SCRAMNet List XML");
			xslt = new XSLTTransform();
			String daqPath = FileHandler.getFilePath("Set DAQ SCRAMNet XML File", new XMLFileFilter());
			if (daqPath == null)
				return;
			xslt.transform("/RTMD/lib/daq.xsl", tempConfigFilename, daqPath);
			JOptionPane.showMessageDialog(this,"DAQ SCRAMNet List XML Generated");
		}

		// Check if xPC Configuration in Metadata tab was checked
		if (xPCMetadataCheckBox.isSelected()) {
			// Create xPC XML File
			JOptionPane.showMessageDialog(this,"Now save xPC XML Configuration");
			xslt = new XSLTTransform();
			String xPCPath = FileHandler.getFilePath("Set xPC XML File",new XMLFileFilter());
			if (xPCPath == null)
				return;
			xslt.transform("/RTMD/lib/xPC.xsl", tempConfigFilename, xPCPath);

			// Create xPC Model files
			ReadxPCXMLConfig xpcxml = new ReadxPCXMLConfig(new File(xPCPath));
			XMLToxPC xmltoxpc = new XMLToxPC();
			xmltoxpc.convertToxPCFiles(JOptionPane.showInputDialog(null,"Enter xPC Model name"), xpcxml.getxPCConfig(), new File(xPCPath).getParent());
			JOptionPane.showMessageDialog(this, "xPC Configuration Generated");
		}

		// Check if Data Turbine Configuration in Metadata tab was checked
		if (turbineMetadataCheckBox.isSelected()) {
			// Create Data Turbine Configuration file
			JOptionPane.showMessageDialog(this,"Now save Data Turbine XML Configuration");
			xslt = new XSLTTransform();
			String turbinePath = FileHandler.getFilePath("Set Data Turbine XML File", new XMLFileFilter());
			if (turbinePath == null)
				return;
			xslt.transform("/RTMD/lib/turbine.xsl", tempConfigFilename, turbinePath);
			JOptionPane.showMessageDialog(this,"Data Turbine Configuration Generated");
		}

		// Cleanup
		if (daqxmlCheckBox.isSelected()) {
			new File("_temp").delete();
		}

	}

	public static void main(String[] args) {
		//XMLGenerator.setDefaultLookAndFeelDecorated(true);
		XMLGenerator xml = new XMLGenerator();
		xml.setVisible(true);	
	}

	/** Variables */
	private static final long serialVersionUID = -4871825295758218048L;
	private JPanel configPanel;
	private JTabbedPane configTabbedPane;
	// Integrator Panel
	private JPanel integratorPanel;
	private JComboBox integrationMethodComboBox;
	private JComboBox integratorComboBox;
	private JComboBox historyFileComboBox;
	private JTextField numDOFTextField;
	private JTextField timeStepsTextField;
	private JTextField subStepsTextField;
	private JComboBox controlComboBox;
	private JComboBox unitsComboBox;
	private JTextField alphaTextField;
	private JTextField deltaTTextField;
	private JButton matriciesButton;
	private JTable matrixTable;
	private JFrame matrixFrame;
	private HLPlot historyPlot;
	private JPanel historyPanel;
	// Kinematics Panel
	private JPanel kinematicsPanel;
	private JScrollPane kinematicsScroll;
	private JComboBox kinematicsMethodComboBox;
	private JTextField spnxTextField;
	private JTextField spnyTextField;
	private JTextField spnzTextField;
	private JTextField spnheightTextField;
	private JTextField spnwidthTextField;
	private JButton setNumberActuatorsButton;
	private JButton setNumberDevicesButton;
	private int numKinematicActuators;
	private int numKinematicDevices;
	private JTextField[] asnxTextField;
	private JTextField[] asnyTextField;
	private JTextField[] asnzTextField;
	private JTextField[] afnxTextField;
	private JTextField[] afnyTextField;
	private JTextField[] afnzTextField;
	private JTextField[] msnxTextField;
	private JTextField[] msnyTextField;
	private JTextField[] msnzTextField;
	private JTextField[] mfnxTextField;
	private JTextField[] mfnyTextField;
	private JTextField[] mfnzTextField;
	private JTextField[] mfnbxTextField;
	private JTextField[] mfnbyTextField;
	private JTextField[] mfnbzTextField;
	private JTextField[] mfnlocalxTextField;
	private JTextField[] mfnlocalyTextField;
	private JTextField[] mfnlocalzTextField;
	private JTextField[] alengthTextField;
	private JTextField[] mlengthTextField;
	private JTextField[] mblengthTextField;
	// Ramp Variables
	private JPanel rampPanel;
	private JButton setNumberRampActuatorsButton;
	private JButton setNumberRampTicksButton;
	private JScrollPane rampScroll;
	private int numRampActuators;
	private int numRampTicks;
	private JComboBox[] rampMethodComboBox;
	private JCheckBox[] rampFeedbackCheckBox;
	// SCRAMNet Variables
	private JPanel scramnetPanel;
	private JScrollPane scramnetScroll;
	Object[][] scramnetData;
	private JTable scramnetTable;
	private JTextArea simBlocksTextArea;
	// Metadata Variables
	private JPanel metadataPanel;
	private JCheckBox daqscramnetCheckBox;
	private JCheckBox daqxmlCheckBox;
	private JCheckBox xPCMetadataCheckBox;
	private JCheckBox turbineMetadataCheckBox;		
	private JButton generateButton;
	private String configFilename;
	// XML
	private XMLOutputter xml;
	private Document doc;
	private XSLTTransform xslt;
}

/*******************************************************************************
 * XML Help Window
 * <p>
 * Displays a Help Window in the XML configuration tool
 * <p>
 * 
 * @author Thomas Marullo
 *         <p>
 *         <b><u>Revisions</u></b><br>
 *         16 Aug 05 T. Marullo Initial
 * 
 ******************************************************************************/
class XMLHelp extends JFrame implements javax.swing.event.TreeSelectionListener {
	/** Create a new Help Window instance */
	public XMLHelp() {
		super("RTMD: Integrated Control Configurator Help");
		setSize(500, 400);
		init();
	}

	/** Initialize the window components */
	private void init() {
		// Create help tree hierarchy and layout
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Integrated Control Configurator");
		DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Integrator");
		DefaultMutableTreeNode child1a = new DefaultMutableTreeNode("Integration Method");
		DefaultMutableTreeNode child1b = new DefaultMutableTreeNode("Integrator");
		DefaultMutableTreeNode child1c = new DefaultMutableTreeNode("Hybrid Test");
		DefaultMutableTreeNode child1d = new DefaultMutableTreeNode("History File");
		DefaultMutableTreeNode child1e = new DefaultMutableTreeNode("Delta T");
		DefaultMutableTreeNode child1f = new DefaultMutableTreeNode("# DOF");
		DefaultMutableTreeNode child1g = new DefaultMutableTreeNode("# Time Steps");
		DefaultMutableTreeNode child1h = new DefaultMutableTreeNode("# Sub Steps");
		DefaultMutableTreeNode child1i = new DefaultMutableTreeNode("Alpha Constant");
		DefaultMutableTreeNode child1j = new DefaultMutableTreeNode("Matrices");
		DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Kinematics");
		DefaultMutableTreeNode child2a = new DefaultMutableTreeNode("Incremental");
		DefaultMutableTreeNode child2b = new DefaultMutableTreeNode("Total");
		DefaultMutableTreeNode child3 = new DefaultMutableTreeNode("Ramp Generators");
		DefaultMutableTreeNode child4 = new DefaultMutableTreeNode("SCRAMNet");		
		DefaultMutableTreeNode child5 = new DefaultMutableTreeNode("Metadata Generation");
		root.add(child1);
		child1.add(child1a);
		child1.add(child1b);
		child1.add(child1c);
		child1.add(child1d);
		child1.add(child1e);
		child1.add(child1f);
		child1.add(child1g);
		child1.add(child1h);
		child1.add(child1i);
		child1.add(child1j);
		root.add(child2);
		child2.add(child2a);
		child2.add(child2b);
		root.add(child3);
		root.add(child4);
		root.add(child5);	
		helptree = new JTree(root);
		helptree.addTreeSelectionListener(this);
		JScrollPane scrollpaneTree = new JScrollPane(helptree);

		// Create a Help Text Area for viewing HTML formatted text
		helparea = new JEditorPane();
		helparea.setEditable(false);
		helparea.setContentType("text/html");
		helparea.setText("<h3>RTMD: Integrated Control Configurator Help</h3><p>Author: Tommy Marullo<br>Copyright (c) 2006 ATLSS Lehigh University");
		JScrollPane scrollpaneText = new JScrollPane(helparea);

		// Create a split pane layout
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollpaneTree, scrollpaneText);
		splitpane.setOneTouchExpandable(true);
		splitpane.setDividerLocation(150);
		this.getContentPane().add(splitpane, BorderLayout.CENTER);
	}

	/**
	 * Event handler for when the Tree selection changes<br>
	 * Contains the help menu data
	 */
	public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
		String selected = helptree.getLastSelectedPathComponent().toString();
		if (selected.equals("Integrated Control Configurator"))
			helparea.setText("<h3>Integrated Control Configurator</h3><br>RTMD: Integrated Control is used for Simulation and Control for RTMD equipment. This program creates the proper configuration for Integrated Control use.");
		else if (selected.equals("Integrator"))
			helparea.setText("<h3>Integrator</h3><br>Generates the commands that will be imposed to the system along with processing the feedback data.");
		else if (selected.equals("Integration Method"))
			helparea.setText("<h3>Integration Method</h3><br>Available Integration Methods:<br>Pseudo Dynamic for n-DOF<br>Effective Force for 1-DOF<br>Predefined History for 1-DOF");
		else if (selected.equals("Integrator"))
			helparea.setText("<h3>Integrator</h3><br>Integrators available for Pseudo Dynamic Method<br>Alpha Method<br>Newmark (Explicit) Method");
		else if (selected.equals("Hybrid Test"))
			helparea.setText("<h3>Hybrid Test</h3><br>Hybrid test contains both Experimental and Analytical computations.  User must set which DOF (floors) are Experimental and/or Analytical parts.  For the Analytical parts, a stiffness matrix (kA) must be defined");
		else if (selected.equals("History File"))
			helparea.setText("<h3>History File</h3><br>History files for the Integrator are CSV formatted and located in /RTMD/historyFiles");
		else if (selected.equals("Delta T"))
			helparea.setText("<h3>Delta T</h3>Time Interval per Time Step (Iteration)</h3>");
		else if (selected.equals("# DOF"))
			helparea.setText("<h3>Number of Degrees of Freedom</h3>");
		else if (selected.equals("# Time Steps"))
			helparea.setText("<h3>Number of (Big) Time Steps</h3><br>Each time step represents an integration step");
		else if (selected.equals("# Sub Steps"))
			helparea.setText("<h3>Number of (Small) Sub Steps (Iterations)</h3><br>Each sub step represents an iteration step in one integration time step");
		else if (selected.equals("Alpha Constant"))
			helparea.setText("<h3>Alpha Constant</h3><br>Alpha Constant also generates Beta and Gamma constants");
		else if (selected.equals("Matrices"))
			helparea.setText("<h3>Mass, Stiffness and Dampening Matrices</h3><br>Set the Mass (M), Stiffness (K), and Dampening (C) matrices based on the number of degrees of freedom");
		else if (selected.equals("Kinematics"))
			helparea.setText("<h3>Kinematics</h3><br>Kinematics uses a local-global coordinate system for defining where Actuators and Devices are relative to an SPN along with calculating precise commands and restoring forces");
		else if (selected.equals("Incremental"))
			helparea.setText("<h3>Incremental Kinematics</h3><br>Incremental Kinematics keeps track of previous values and generates increments for each command generation and restoring force calculation");
		else if (selected.equals("Total"))
			helparea.setText("<h3>Total Kinematics</h3><br>Total Kinematics keeps generates total commands for each command generation and restoring force calculation");
		else if (selected.equals("Ramp Generators"))
			helparea.setText("<h3>Ramp Generators</h3><br>A Ramp Generator can be specified for an Actuator with the following configuration:<br>None: Total command is always imposed<br>Linear: Linear slope interpolation<br>Sine: Sine wave slope interpolation<br>Haversine: Haversine slope interpolation<p>Ramp Ticks represent how many DSP ticks the ramp will interpolate over");
		else if (selected.equals("SCRAMNet"))
			helparea.setText("<h3>SCRAMNet</h3><br>This shows the SCRAMNet memory map between the Controller and Simulation PCs. These are default values and can be changed here.<p>Set 'Stream' for a data point so Data Turbine can read this data.  Setting 'Stream' also tells the program to generate an xPC node for this SCRAMNet address.<p>Minimum and Maximum SCRAMNet Limits should be changed.<p>At the bottom is a text area for entering Simulation channel names.  Enter each channel name on a new line.  These channels come from calculations done in the xPC or Sim PC that are also pushed onto the SCRAMNet");
		else if (selected.equals("Metadata Generation"))
			helparea.setText("<h3>Metadata Generation</h3><br>This section provides option to create specific metadata files from the configuration.<p><h2>Include DAQ XML below</h2><br>This option will ask the user to open the DAQ XML database file generated on the DAQ PC.  It will then append the configuration with the DAQ configuration.<p><h2>Include Visualiaztion Node List below</h2><br>This option will ask the user to open a HybridViz configuration file which has node information that needs to be linked to SCRAMNet addresses.  The corresponding XML file generated is then used back in the HybridViz application.<p><h2>Export SCRAMNet List from DAQ XML</h2><br>This option will generate a separate XML file specific for the DAQ channels.  This file is used specifically for Matlab and Java based programs that need to read in DAQ channel parameters.  This file is used in the Java class, ReadDAQXMLConfig.<p><h2>Export xPC XML and Model files</h2><br>This option will generate three files.  The first is an XML file relating the xPC nodes to with their corresponding parameters.  This is necessary to convert xPC raw data into engineering units.<p>The second file is a Matlab .m file.  This contains the required SCRAMNet parameters for the Simulink model file generated last.  This model file contains all the Control and Simulation blocks that are Streamed and allows a user to attach their model to the SCRAMNet easily.<p><h2>Export Data Turbine XML</h2><br>This option will ask the user to specify an XML file for Data Turbine.  This file contains all Streamed channels and their parameters to send to Data Turbine.");
	}

	// Variables
	private JTree helptree;
	private JEditorPane helparea;
	private static final long serialVersionUID = -4871825295758218048L;
}

/*******************************************************************************
 * HLPlot
 * <p>
 * Plots the History file in a graphical format and allows a user to step along
 * <p>
 * 
 * @author Thomas Marullo
 *         <p>
 *         <b><u>Revisions</u></b><br>
 *         16 Aug 05 T. Marullo Initial
 * 
 ******************************************************************************/
class HLPlot {
	private double[][] historyData;
	private int steps;
	private int columns;
	private int currentStep;
	MutableTwoDimDataSetImpl[] locDataSet;
	MutableTwoDimDataSetImpl[] trendDataSet;
	private Scatter plot;

	public HLPlot(String filename) {
		historyData = getHistoryData(filename);
		currentStep = 0;
		locDataSet = new MutableTwoDimDataSetImpl[columns];
		trendDataSet = new MutableTwoDimDataSetImpl[columns];
		for (int i = 0; i < columns; i++) {
			locDataSet[i] = new MutableTwoDimDataSetImpl();
			trendDataSet[i] = new MutableTwoDimDataSetImpl();
		}
		initPlot();
	}

	protected void initPlot() {
		DecimalFormat yf = new DecimalFormat(); // create a format
		yf.setMinimumFractionDigits(2); // for y-axis
		yf.setMaximumFractionDigits(2);
		yf.setNegativePrefix("-");
		yf.setPositivePrefix("+");

		DecimalFormat xf = new DecimalFormat(); // create a format
		xf.setMinimumFractionDigits(0); // for y-axis
		xf.setMaximumFractionDigits(0);

		plot = new Scatter(); // create plot
		plot.setBackground(Color.black);
		plot.setXFormat((Format) xf);
		plot.setYFormat((Format) yf);
		plot.setCursorSize(1); // Small cursor
		plot.setMarkerSize(1); // Markets aren't important

		// Set up the Trend plot
		ScatterConstraints[] trendSC = new ScatterConstraints[columns];
		for (int i = 0; i < columns; i++) {
			trendDataSet[i].setMaximumPoints(steps);
			trendSC[i] = new ScatterConstraints();
		}

		for (int i = 0; i < columns; i++) {
			Color color = Color.getHSBColor((float) Math.random(), 1.0F, 1.0F);
			trendSC[i].setDataColor(color); // set data color
			trendSC[i].setDataLineColor(color); // set data line color
			trendSC[i].setShowCursor(false); // don't show the cursor
			plot.addDataSet(trendDataSet[i], trendSC[i]); // add to plot
			// End Trend plot //
		}

		// Set up the Location plot
		ScatterConstraints[] locSC = new ScatterConstraints[columns];
		for (int i = 0; i < columns; i++) {
			locDataSet[i].setMaximumPoints(steps);
			locSC[i] = new ScatterConstraints();
		}

		for (int i = 0; i < columns; i++) {
			Color color = Color.getHSBColor((float) Math.random(), 1.0F, 1.0F);
			locSC[i].setDataColor(color); // set data color
			locSC[i].setDataLineColor(color); // set data line color
			locSC[i].setShowCursor(false); // don't show the cursor
			plot.addDataSet(locDataSet[i], locSC[i]); // add to plot
			// End Location plot //
		}

		// Plot the history
		for (int i = 0; i < steps; i++)
			for (int j = 0; j < columns; j++)
				trendDataSet[j].addPoint( // add point
						new TwoDimPointImpl(new Double(i), new Double(historyData[j][i])));
		plot.repaint();
	}

	/** Add the next point */
	public void nextStep() {
		for (int i = 0; i < columns; i++)
			locDataSet[i].addPoint( // add point
					new TwoDimPointImpl(new Double(currentStep), new Double(historyData[i][currentStep])));
		currentStep++;
	}

	/** Store the history data into an array for plotting */
	protected double[][] getHistoryData(String filename) {
		double[][] data = null;
		try {
			BufferedReader cmds; // File input handler
			File cfile = new File("/RTMD/historyFiles/" + filename);
			FileInputStream fs = new FileInputStream(cfile);
			cmds = new BufferedReader(new InputStreamReader(fs));
			steps = getNumberSteps(filename);
			columns = getNumberColumns(filename);
			data = new double[columns][steps];
			for (int i = 0; i < steps; i++) {
				if (columns == 1)
					data[0][i] = Double.parseDouble(cmds.readLine());
				else {
					String[] values = cmds.readLine().split(",");
					for (int j = 0; j < columns; j++)
						data[j][i] = Double.parseDouble(values[j]);
				}
			}
			cmds.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/** Read number of steps for any history file */
	protected int getNumberSteps(String filename) {
		try {
			BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(
							         new File("/RTMD/historyFiles/" + filename))));
			int i = 0;
			try {
				while (input.readLine() != null)
					i++;
			} catch (Exception e) {e.printStackTrace();}
			try {
				input.close();
			} catch (Exception e) {e.printStackTrace();}
			return i;
		} catch (Exception e) {e.printStackTrace();}
		return -1;
	}

	/** Read number of columns for any history file */
	protected int getNumberColumns(String filename) {
		try {
			BufferedReader input = new BufferedReader(
					new InputStreamReader(new FileInputStream(
							new File("/RTMD/historyFiles/" + filename))));
			int columns = 1;
			try {
				String s = input.readLine();
				if (s.indexOf(",") != -1) {
					columns = s.split(",").length;
				}
			} catch (Exception e) {e.printStackTrace();}		
			try {
				input.close();
			} catch (Exception e) {e.printStackTrace();}
			return columns;
		} catch (Exception e) {e.printStackTrace();}
		return -1;
	}

	/** Return the plot object */
	public Scatter getPlot() {
		return plot;
	}
}