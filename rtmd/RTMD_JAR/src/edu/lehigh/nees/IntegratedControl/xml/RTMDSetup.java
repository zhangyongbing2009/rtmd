package edu.lehigh.nees.IntegratedControl.xml;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import edu.lehigh.nees.util.FileHandler;
import edu.lehigh.nees.util.filefilter.*;
import edu.lehigh.nees.xml.ReadXMLConfig;
import edu.lehigh.nees.xml.XMLScramnetConfig;
import edu.lehigh.nees.scramnet.ScramNetIO;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import org.jdom.*;
import org.jdom.output.*;

/*******************************************************************************
 * RTMDSetup
 * <p>
 * XML configuration utility
 * <p>
 * 
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 * 18 Sep 09 T. Marullo Initial 
 * 
 * 
 ******************************************************************************/
public class RTMDSetup extends JFrame {

	/** Creates a new instance of XMLGenerator */
	public RTMDSetup() {
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
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		

		// Create "FILE" menu bar
		generateMenuBar();

		// Create SCRAMNet Panel
		generateScramnetPanel();

		// Create Metadata Include Panel
		generateMetadataPanel();

		// Create Tabbed Panes
		generateTabbedPanes();
	}

	/** Generate the Menu Bar */
	private void generateMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		JMenuItem saveitem = new JMenuItem("Save");
		JMenuItem saveasitem = new JMenuItem("Save As");
		JMenuItem openitem = new JMenuItem("Open");
		JMenuItem exititem = new JMenuItem("Exit");
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
				exitApp();
			}});
		// Organize the Menu Bar
		filemenu.add(openitem);
		filemenu.add(saveitem);
		filemenu.add(saveasitem);
		filemenu.add(exititem);
		menubar.add(filemenu);
		this.setJMenuBar(menubar);
	}

	/** Generate the SCRAMNet Mapping Panel */
	private void generateScramnetPanel() {
		scramnetPanel = new JPanel(new BorderLayout());
		setScramnetTable();
		simBlocksTextArea = new JTextArea("Add any Simulation Channel Names below (These start at address 129)\n");
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
		private static final long serialVersionUID = -4871823215758218042L;

		private String[] columnNames = { "Offset", 
										 "Stream",
										 "Record",
										 "Description",
										 "Units", 
										 "Scale", 
										 "Lower Limit", 
										 "Upper Limit" };

		private Object[][] data = {
				{ "0", new Boolean(false), new Boolean(false), "Simulation Running", "n_a", "1","0", "1" },
				{ "1", new Boolean(false), new Boolean(false), "Displacement Command 1", "mm","500", "-500", "500" },
				{ "2", new Boolean(false), new Boolean(false), "Velocity Command 1", "m_s", "2","-2", "2" },
				{ "3", new Boolean(false), new Boolean(false), "Acceleration Command 1", "m_s2","20", "-20", "20" },
				{ "4", new Boolean(false), new Boolean(false), "Force Command 1", "kN", "2000","-2000", "2000" },
				{ "5", new Boolean(false), new Boolean(false), "Force Derivative Command 1","kN_s", "120000", "-120000", "120000" },
				{ "6", new Boolean(false), new Boolean(false), "Displacement Command 2", "mm","500", "-500", "500" },
				{ "7", new Boolean(false), new Boolean(false), "Velocity Command 2", "m_s", "2","-2", "2" },
				{ "8", new Boolean(false), new Boolean(false), "Acceleration Command 2", "m_s2","20", "-20", "20" },
				{ "9", new Boolean(false), new Boolean(false), "Force Command 2", "kN", "2000","-2000", "2000" },
				{ "10", new Boolean(false), new Boolean(false), "Force Derivative Command 2","kN_s", "120000", "-120000", "120000" },
				{ "11", new Boolean(false), new Boolean(false), "Displacement Command 3", "mm","500", "-500", "500" },
				{ "12", new Boolean(false), new Boolean(false), "Velocity Command 3", "m_s", "2","-2", "2" },
				{ "13", new Boolean(false), new Boolean(false), "Acceleration Command 3", "m_s2","20", "-20", "20" },
				{ "14", new Boolean(false), new Boolean(false), "Force Command 3", "kN", "2000","-2000", "2000" },
				{ "15", new Boolean(false), new Boolean(false), "Force Derivative Command 3","kN_s", "120000", "-120000", "120000" },
				{ "16", new Boolean(false), new Boolean(false), "Displacement Command 4", "mm","500", "-500", "500" },
				{ "17", new Boolean(false), new Boolean(false), "Velocity Command 4", "m_s", "2","-2", "2" },
				{ "18", new Boolean(false), new Boolean(false), "Acceleration Command 4", "m_s2","20", "-20", "20" },
				{ "19", new Boolean(false), new Boolean(false), "Force Command 4", "kN", "2500","-2500", "2500" },
				{ "20", new Boolean(false), new Boolean(false), "Force Derivative Command 4","kN_s", "150000", "-150000", "150000" },
				{ "21", new Boolean(false), new Boolean(false), "Displacement Command 5", "mm","500", "-500", "500" },
				{ "22", new Boolean(false), new Boolean(false), "Velocity Command 5", "m_s", "2","-2", "2" },
				{ "23", new Boolean(false), new Boolean(false), "Acceleration Command 5", "m_s2","20", "-20", "20" },
				{ "24", new Boolean(false), new Boolean(false), "Force Command 5", "kN", "2500","-2500", "-2500" },
				{ "25", new Boolean(false), new Boolean(false), "Force Derivative Command 5","kN_s", "150000", "-150000", "150000" },
				{ "26", new Boolean(false), new Boolean(false), "Displacement Command 6", "mm","1", "0", "0" },
				{ "27", new Boolean(false), new Boolean(false), "Velocity Command 6", "m_s", "1","0", "0" },
				{ "28", new Boolean(false), new Boolean(false), "Acceleration Command 6", "m_s2","1", "0", "0" },
				{ "29", new Boolean(false), new Boolean(false), "Force Command 6", "kN", "1", "0","0" },
				{ "30", new Boolean(false), new Boolean(false), "Force Derivative Command 6","kN_s", "1", "0", "0" },
				{ "31", new Boolean(false), new Boolean(false), "Displacement Command 7", "mm","1", "0", "0" },
				{ "32", new Boolean(false), new Boolean(false), "Velocity Command 7", "m_s", "1","0", "0" },
				{ "33", new Boolean(false), new Boolean(false), "Acceleration Command 7", "m_s2","1", "0", "0" },
				{ "34", new Boolean(false), new Boolean(false), "Force Command 7", "kN", "1", "0","0" },
				{ "35", new Boolean(false), new Boolean(false), "Force Derivative Command 7","kN_s", "1", "0", "0" },
				{ "36", new Boolean(false), new Boolean(false), "Displacement Command 8", "mm","1", "0", "0" },
				{ "37", new Boolean(false), new Boolean(false), "Velocity Command 8", "m_s", "1","0", "0" },
				{ "38", new Boolean(false), new Boolean(false), "Acceleration Command 8", "m_s2","1", "0", "0" },
				{ "39", new Boolean(false), new Boolean(false), "Force Command 8", "kN", "1", "0","0" },
				{ "40", new Boolean(false), new Boolean(false), "Force Derivative Command 8","kN_s", "1", "0", "0" },
				{ "41", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "42", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "43", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "44", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "45", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "46", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "47", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "48", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "49", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "50", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "51", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "52", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "53", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "54", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "55", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "56", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "57", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "58", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "59", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "60", new Boolean(false), new Boolean(false), "Stop Bit", "n_a", "1", "0", "0" },
				{ "61", new Boolean(false), new Boolean(false), "Camera Trigger", "n_a", "1", "0", "0" },
				{ "62", new Boolean(false), new Boolean(false), "Pause Bit", "n_a", "1", "0", "0" },
				{ "63", new Boolean(false), new Boolean(false), "Pulse Trigger", "n_a", "1", "0","0" },
				{ "64", new Boolean(false), new Boolean(false), "Global Counter", "n_a", "1", "0","0" },
				{ "65", new Boolean(false), new Boolean(false), "Supply Pressure 1", "Bar", "400","0", "0" },
				{ "66", new Boolean(false), new Boolean(false), "Supply Pressure 2", "Bar", "400", "0","0" },
				{ "67", new Boolean(false), new Boolean(false), "Displacement 1", "mm", "500", "0", "0" },
				{ "68", new Boolean(false), new Boolean(false), "Load 1", "kN","2000", "0", "0" },
				{ "69", new Boolean(false), new Boolean(false), "Differential Pressure 1", "Bar", "400","0", "0" },
				{ "70", new Boolean(false), new Boolean(false), "Actuator Pressure 1", "DSP", "1","0", "0" },
				{ "71", new Boolean(false), new Boolean(false), "Velocity 1", "m_s", "2", "0","0" },
				{ "72", new Boolean(false), new Boolean(false), "Displacement Ctrl 1 OP", "DSP", "1", "0", "0" },
				{ "73", new Boolean(false), new Boolean(false), "Valve Lin 1 OP", "DSP","1", "0", "0" },
				{ "74", new Boolean(false), new Boolean(false), "Displacement 2", "mm", "500", "0", "0" },
				{ "75", new Boolean(false), new Boolean(false), "Load 2", "kN","2000", "0", "0" },
				{ "76", new Boolean(false), new Boolean(false), "Differential Pressure 2", "Bar", "400","0", "0" },
				{ "77", new Boolean(false), new Boolean(false), "Actuator Pressure 2", "DSP", "1","0", "0" },
				{ "78", new Boolean(false), new Boolean(false), "Velocity 2", "m_s", "2", "0","0" },
				{ "79", new Boolean(false), new Boolean(false), "Displacement Ctrl 2 OP", "DSP", "1", "0", "0" },
				{ "80", new Boolean(false), new Boolean(false), "Valve Lin 2 OP", "DSP","1", "0", "0" },
				{ "81", new Boolean(false), new Boolean(false), "Displacement 3", "mm", "500", "0", "0" },
				{ "82", new Boolean(false), new Boolean(false), "Load 3", "kN","2000", "0", "0" },
				{ "83", new Boolean(false), new Boolean(false), "Differential Pressure 3", "Bar", "400","0", "0" },
				{ "84", new Boolean(false), new Boolean(false), "Actuator Pressure 3", "DSP", "1","0", "0" },
				{ "85", new Boolean(false), new Boolean(false), "Velocity 3", "m_s", "2", "0","0" },
				{ "86", new Boolean(false), new Boolean(false), "Displacement Ctrl 3 OP", "DSP", "1", "0", "0" },
				{ "87", new Boolean(false), new Boolean(false), "Valve Lin 3 OP", "DSP","1", "0", "0" },
				{ "88", new Boolean(false), new Boolean(false), "Displacement 4", "mm", "500", "0", "0" },
				{ "89", new Boolean(false), new Boolean(false), "Load 4", "kN","2500", "0", "0" },
				{ "90", new Boolean(false), new Boolean(false), "Differential Pressure 4", "Bar", "400","0", "0" },
				{ "91", new Boolean(false), new Boolean(false), "Actuator Pressure 4", "DSP", "1","0", "0" },
				{ "92", new Boolean(false), new Boolean(false), "Velocity 4", "m_s", "2", "0","0" },
				{ "93", new Boolean(false), new Boolean(false), "Displacement Ctrl 4 OP", "DSP", "1", "0", "0" },
				{ "94", new Boolean(false), new Boolean(false), "Valve Lin 4 OP", "DSP","1", "0", "0" },
				{ "95", new Boolean(false), new Boolean(false), "Displacement 5", "mm", "500", "0", "0" },
				{ "96", new Boolean(false), new Boolean(false), "Load 5", "kN","2500", "0", "0" },
				{ "97", new Boolean(false), new Boolean(false), "Differential Pressure 5", "Bar", "400","0", "0" },
				{ "98", new Boolean(false), new Boolean(false), "Actuator Pressure 5", "DSP", "1","0", "0" },
				{ "99", new Boolean(false), new Boolean(false), "Velocity 5", "m_s", "2", "0","0" },
				{ "100", new Boolean(false), new Boolean(false), "Displacement Ctrl 5 OP", "DSP", "1", "0", "0" },
				{ "101", new Boolean(false), new Boolean(false), "Valve Lin 5 OP", "DSP","1", "0", "0" },
				{ "102", new Boolean(false), new Boolean(false), "Valve A Spool", "%", "100", "0", "0" },
				{ "103", new Boolean(false), new Boolean(false), "Valve B Spool", "%", "100", "0", "0" },
				{ "104", new Boolean(false), new Boolean(false), "Valve C Spool", "%", "100", "0", "0" },
				{ "105", new Boolean(false), new Boolean(false), "Valve D Spool", "%", "100", "0", "0" },
				{ "106", new Boolean(false), new Boolean(false), "Valve E Spool", "%", "100", "0", "0" },
				{ "107", new Boolean(false), new Boolean(false), "Valve F Spool", "%", "100", "0", "0" },
				{ "108", new Boolean(false), new Boolean(false), "Valve G Spool", "%", "100", "0", "0" },
				{ "109", new Boolean(false), new Boolean(false), "Valve H Spool", "%", "100", "0", "0" },
				{ "110", new Boolean(false), new Boolean(false), "Valve J Spool", "%", "100", "0", "0" },
				{ "111", new Boolean(false), new Boolean(false), "Valve K Spool", "%", "100", "0", "0" },
				{ "112", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "113", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "114", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "115", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "116", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "117", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "118", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "119", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "120", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "121", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "122", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "123", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "124", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "125", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "126", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" },
				{ "127", new Boolean(false), new Boolean(false), "Spare", "n_a", "1", "0", "0" } };

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
		daqxmlCheckBox = new JCheckBox("Include DAQ Scan List?");
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
		daqscramnetCheckBox = new JCheckBox("Export DAQ SCRAMNet Parameters?");
		daqscramnetCheckBox.setBounds(10, 30, 300, 30);
		daqscramnetCheckBox.setEnabled(false);
		metadataPanel.add(daqscramnetCheckBox);
		// xPC Configuration Export
		xPCMetadataCheckBox = new JCheckBox("Export xPC XML and Model files?");
		xPCMetadataCheckBox.setBounds(10, 60, 300, 30);
		metadataPanel.add(xPCMetadataCheckBox);
		// Data Turbine Configuration Export
		turbineMetadataCheckBox = new JCheckBox("Export Data Turbine Configuration?");
		turbineMetadataCheckBox.setBounds(10, 90, 300, 30);
		metadataPanel.add(turbineMetadataCheckBox);
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
		// DAQ Tab Pane
		configTabbedPane.addTab("Metadata", metadataPanel);
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

	
	/** Save the Generator Configuration */
	private boolean saveConfiguration() {
		// Make sure all SCRAMNet table values are valid
		for (int i = 0; i < scramnetTable.getRowCount(); i++) {
			checkIfDouble("Scale " + String.valueOf(i), scramnetTable.getValueAt(i, 5).toString());
			checkIfDouble("LowerLimit " + String.valueOf(i), scramnetTable.getValueAt(i, 6).toString());
			checkIfDouble("UpperLimit " + String.valueOf(i), scramnetTable.getValueAt(i, 7).toString());
		}
		
		// If no configuration was set, make one
		if (configFilename == null) {
			// Create new file
			configFilename = FileHandler.getFilePath("Save Configuration", new XMLFileFilter());			
		}
		
		// If the previous dialog box was canceled, do not continue with save
		if (configFilename == null) {
			return false;
		}
		
		// Create the XML part for the SCRAMNet/Controller
		try {			
			// Create the XML File
			xml = new XMLOutputter(org.jdom.output.Format.getPrettyFormat());
			configdoc = new Document(new Element("NEESsim"));			

			// SCRAMNet
			scramnet = new Element("Scramnet");
			configdoc.getRootElement().addContent(scramnet);
			scramnet.setAttribute("ON", "true");
			for (int i = 0; i < scramnetTable.getRowCount(); i++) {
				Element ctrlBlock = new Element("CtrlBlock");
				ctrlBlock.setAttribute("Offset", scramnetTable.getValueAt(i, 0).toString());
				ctrlBlock.setAttribute("Record", scramnetTable.getValueAt(i, 1).toString());
				ctrlBlock.setAttribute("Stream", scramnetTable.getValueAt(i, 2).toString());
				ctrlBlock.setAttribute("Description", scramnetTable.getValueAt(i, 3).toString());
				ctrlBlock.setAttribute("Units", scramnetTable.getValueAt(i, 4).toString());
				ctrlBlock.setAttribute("Scale", scramnetTable.getValueAt(i, 5).toString());
				ctrlBlock.setAttribute("LowerLimit", scramnetTable.getValueAt(i, 6).toString());
				ctrlBlock.setAttribute("UpperLimit", scramnetTable.getValueAt(i, 7).toString());
				scramnet.addContent(ctrlBlock);
			}

			// Add Simulation Blocks
			String[] simBlockRowText = simBlocksTextArea.getText().split("\n");
			for (int i = 0; i < simBlockRowText.length - 1; i++) {
				Element simBlock = new Element("SimBlock");
				simBlock.setAttribute("Offset", Integer.toString(129 + i));
				simBlock.setAttribute("Name", simBlockRowText[i + 1]);
				scramnet.addContent(simBlock);
			}
					
			// Check if import DAQ SCRAMNet Scan List in Metadata tab was checked
			if (daqxmlCheckBox.isSelected()) {
				// Get SCRAMNet Scan List for DAQ
				// This gets the DAQ PI parameters into an XML DOM object if they are available
				getPIParams();
			}						
			
			// Write XML config object to file
			xml.output(configdoc, new FileWriter(new File(configFilename)));			
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
	
	/** Exit App */
	private void exitApp() {
		this.dispose();
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
		simBlocksTextArea.setText("Add any Simulation Channel Names below (These start at address 129)\n");
		for (int i = 0; i < scrxml.getnumSimBlocks(); i++) {
			simBlocksTextArea.append(scrxml.getSimName(i) + "\n");
		}			
	}
		


/*
		// Check if xPC Configuration in Metadata tab was checked
		if (xPCMetadataCheckBox.isSelected()) {
			// Create xPC XML File						
			String xPCPath = FileHandler.getFilePath("Save xPC XML File",new XMLFileFilter());
			if (xPCPath == null)
				return;
			/ MAKE XPC XML FILE /

			// Create xPC Model files
			ReadxPCXMLConfig xpcxml = new ReadxPCXMLConfig(new File(xPCPath));
			XMLToxPC xmltoxpc = new XMLToxPC();
			xmltoxpc.convertToxPCFiles(JOptionPane.showInputDialog(null,"Enter xPC Model name"), xpcxml.getxPCConfig(), new File(xPCPath).getParent());
			JOptionPane.showMessageDialog(this, "xPC Configuration Generated");
		}
	*/
	
	private boolean getPIParams() {
		try {
			// Create a SCRAMNet Object
			ScramNetIO scr = new ScramNetIO();
			scr.initScramnet();
			scr.setTransMode(2);  // Must be in Byte read mode
	
			// Set base address based on PI660 parameters
			int baseaddr = 65536;  // 0x10000 in byte mode (1 byte per address)
			int scrAddrBase = 192; // Decimal base for SCRAMNet mapping per each channel
			
			// See if SCRAMNet Parameters are set, return out if not
			int dec = scr.readByte(baseaddr);
			if (dec != 80) {     
				JOptionPane.showMessageDialog(this,"SCRAMNet Parameters not enabled on PI660");
			    return false;    
			}				
											
		    // Find available channels
		    int channelCount = 1;
		    int maxChannels = 768; // 3 racks * 16 cards per rack * 16 CVT
		    
		    // Go through every channels
		    while (channelCount <= maxChannels) {
		        // Get next channel offset in SCRAMNet
		        int byteOffset = 88*channelCount;
		        
		        // Read the first byte to see if it is in the test or not
		        dec = scr.readByte(baseaddr + byteOffset);		        
		        boolean inTest;
		        if (dec == 0)		            
		            inTest = false;
		        else
		            inTest = true;
		        
		        // If the channel is in the test, scan the channel block
		        if (inTest) {
		        	// Create an XML element
		        	Element channel = new Element("DAQBlock");					
					//channel.setAttribute("type", "int");
		        	
		        	// Get the SCRAMNet Address
		            int scraddr = scrAddrBase + channelCount-1;		            
		            channel.setAttribute("location", Integer.toString(scraddr));
		        	
		        	// Streaming and Recording
		        	channel.setAttribute("Stream", "false");
		        	channel.setAttribute("Record", "true");
		        	
		            // Get the Name		            
		            String name = "";		            
		            for (int byteIndex = (baseaddr+byteOffset); byteIndex <= (baseaddr+byteOffset+31); byteIndex++) {
		                dec = scr.readByte(byteIndex);               
		                name = name + (char)dec;            
		            }
		            channel.setAttribute("name", name.trim());		            
		            
		            // Get the Units		            
		            String units = "";		            
		            for (int byteIndex = (baseaddr+byteOffset+32); byteIndex <= (baseaddr+byteOffset+39); byteIndex++) {
		                dec = scr.readByte(byteIndex);               
		                units = units + (char)dec;                
		            }
		            channel.setAttribute("units", units.trim());
		            
		            // Get the Gain            		            
		            int byteIndex = (baseaddr+byteOffset+40);            
		            // Swap the 4 bytes words to put in the proper double format
		            float dec1 = scr.readFloat(byteIndex/4);
		            float dec2 = scr.readFloat((byteIndex+4)/4);
		            scr.writeFloat(200000,dec2);    // write to a temp memory location
		            scr.writeFloat(200001,dec1);
		            scr.setTransMode(0);  // Long word mode
		            double gain = scr.readDouble(100000);
		            scr.setTransMode(2);  // Byte mode      
		            channel.setAttribute("Gain", Double.toString(gain));

		            // Get the VSlope            
		            byteIndex = (baseaddr+byteOffset+48);            
		            // Swap the 4 bytes words to put in the proper double format
		            dec1 = scr.readFloat(byteIndex/4);
		            dec2 = scr.readFloat((byteIndex+4)/4);
		            scr.writeFloat(200000,dec2);    // write to a temp memory location
		            scr.writeFloat(200001,dec1);
		            scr.setTransMode(0);  // Long word mode
		            double vslope = scr.readDouble(100000);
		            scr.setTransMode(2);  // Byte mode		  
		            channel.setAttribute("VoltageSlope", Double.toString(vslope));
		            
		            // Get the VOffset            		            
		            byteIndex = (baseaddr+byteOffset+56);            
		            // Swap the 4 bytes words to put in the proper double format
		            dec1 = scr.readFloat(byteIndex/4);
		            dec2 = scr.readFloat((byteIndex+4)/4);
		            scr.writeFloat(200000,dec2);    // write to a temp memory location
		            scr.writeFloat(200001,dec1);
		            scr.setTransMode(0);  // Long word mode
		            double voffset = scr.readDouble(100000);
		            scr.setTransMode(2);  // Byte mode		  
		            channel.setAttribute("VoltageOffset", Double.toString(voffset));
		            
		            // Get the EUSlope            		            
		            byteIndex = (baseaddr+byteOffset+64);            
		            // Swap the 4 bytes words to put in the proper double format
		            dec1 = scr.readFloat(byteIndex/4);
		            dec2 = scr.readFloat((byteIndex+4)/4);
		            scr.writeFloat(200000,dec2);    // write to a temp memory location
		            scr.writeFloat(200001,dec1);
		            scr.setTransMode(0);  // Long word mode
		            double euslope = scr.readDouble(100000);
		            scr.setTransMode(2);  // Byte mode		
		            channel.setAttribute("EUSlope", Double.toString(euslope));
		            
		            // Get the EUOffset            		            
		            byteIndex = (baseaddr+byteOffset+72);            
		            // Swap the 4 bytes words to put in the proper double format
		            dec1 = scr.readFloat(byteIndex/4);
		            dec2 = scr.readFloat((byteIndex+4)/4);
		            scr.writeFloat(200000,dec2);    // write to a temp memory location
		            scr.writeFloat(200001,dec1);
		            scr.setTransMode(0);  // Long word mode
		            double euoffset = scr.readDouble(100000);
		            scr.setTransMode(2);  // Byte mode		 
		            channel.setAttribute("EUOffset", Double.toString(euoffset));
		            
		            // Get the Gage Type            		            
		            byteIndex = (baseaddr+byteOffset+80);     
		            scr.setTransMode(0);  // Long word mode
		            dec = scr.readInt(byteIndex/4);   
		            scr.setTransMode(2);  // Byte mode
		            String gagetype = getGageType(dec);		
		            channel.setAttribute("GageType", gagetype);		            		            		            
		            
		            // Add to node
		            //daqdoc.getRootElement().addContent(channel);
		            scramnet.addContent(channel);
		        }     
		        		        
		        // Go to next channel
		        channelCount = channelCount + 1;        
		    }
		    
		    return true;
		} catch (Exception e) {e.printStackTrace(); return false;}
	}
	
	private String getGageType(int type) {
		String name = "Unknown";
		switch (type) {
			case 0:
	            name = "Bridge";
	            break;
	        case 1 :
	            name = "ICP";
	            break;
	        case 2 :
	            name = "Voltage";
	            break;
	        case 3 :
	            name = "DCLVDT";
	            break;
	        case 4 :
	            name = "RTD";
	            break;
	        case 5 :
	            name = "Frequency";
	            break;
	        case 6 :
	            name = "Period";
	            break;
	        case 7 :
	            name = "Counter";
	            break;
	        case 8 :
	            name = "Digital";
	            break;
	        case 9 :
	            name = "Irig";
	            break;
	        case 10 :
	            name = "EDAC";
	            break;
	        case 11 :
	            name = "5500";
	            break;
	        case 12 :
	            name = "Digital Out";
	            break;
	        case 13 :
	            name = "TCB";
	            break;
	        case 14 :
	            name = "TCC";
	            break;
	        case 15 :
	            name = "TCE";
	            break;
	        case 16 :
	            name = "TCJ";
	            break;
	        case 17 :
	            name = "TCK";
	            break;
	        case 18 :
	            name = "TCN";
	            break;
	        case 19 :
	            name = "TCR";
	            break;
	        case 20 :
	            name = "TCS";
	            break;
	        case 21 :
	            name = "TCT";
	            break;
	        case 22 :
	            name = "TCREF";
	            break;
	        case 23 :
	            name = "1/4 Bridge 120";
	            break;
	        case 24 :
	            name = "1/4 Bridge 350";
	            break;
	        case 25 :
	            name = "1/2 Bridge";
	            break;
	        case 26 :
	            name = "Full Bridge";
	            break;
	        case 27 :
	            name = "Frequency 32";
	            break;
	        case 28 :
	            name = "Period 32";
	            break;
	        case 29 :
	            name = "Counter 32";
	            break;
	        case 30 :
	            name = "FTOV";
	            break;
	        case 31 :
	            name = "DSP";
	            break;
	        case 32 :
	            name = "Digital In";
	            break;
	        case 33 :
	            name = "8400";
	            break;
	        default  :
	            name = "Unknown";	        
		}
        return name;
	}

	public static void main(String[] args) {
		RTMDSetup xml = new RTMDSetup();
		xml.setVisible(true);	
	}

	/** Variables */
	private static final long serialVersionUID = -4871825295758218048L;
	private JPanel configPanel;
	private JTabbedPane configTabbedPane;
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
	private Document configdoc;
	private Element scramnet;
	private Document daqdoc;
	private Element daqelements;
	private Document xpcdoc;
	private Document turbinedoc;
	
}

