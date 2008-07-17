package edu.lehigh.nees.util;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A wrapper for JFileChooser with accessories for
 * directory history and file preview.
 * (E)nhancedFilechooser.
 *
 * @author	Klaus Berg (http://www.javaworld.com/javaworld/javatips/jw-javatip100.html)
 * <p>
 * <b><u>Revisions</u></b><br>
 * 15 Jun 06  T. Marullo  Modified for RTMD use
 */

public class EFileChooser extends JFileChooser {
	
	private static final long serialVersionUID = 1L;
	private static Vector comboModel;
	private static FileInputStream fis;
	private static ObjectInputStream ois;
	private static String dirFile;	

	private TextPreviewer previewer;
	private PreviewAndHistoryPanel previewAndHistoryPanel;	
	private MyComboBox combo;
	private PreviewAndHistoryPanel.ComboItemListener comboItemListener;

	// --- Helper classes: 

	// --- PreviewAndHistoryPanel ----------------------------------------------

	private final class PreviewAndHistoryPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;

		PreviewAndHistoryPanel() {
			setPreferredSize(new Dimension(250,250));
			setBorder(BorderFactory.createEtchedBorder());
			setLayout(new BorderLayout());

			combo = new MyComboBox(comboModel);
			comboItemListener = new ComboItemListener();
			combo.addItemListener(comboItemListener);
			combo.registerKeyboardAction(new DeleteKeyListener("ONE"),
			KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false),
			JComponent.WHEN_IN_FOCUSED_WINDOW);
			combo.registerKeyboardAction(new DeleteKeyListener("ALL"),
			KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 
			InputEvent.SHIFT_MASK, false),
			JComponent.WHEN_IN_FOCUSED_WINDOW);
			combo.setRenderer(new ComboBoxRendererWithTooltips());
			add(combo, BorderLayout.NORTH);

			previewer = new TextPreviewer();
			add(previewer, BorderLayout.CENTER);
		}      

		private final class DeleteKeyListener implements ActionListener {
			String action_;

			DeleteKeyListener(String action) {
				action_ = action;
			}

			public void actionPerformed(ActionEvent e) {
				if (action_.equals("ONE")) {
					combo.removeItemAt(combo.getSelectedIndex());
				}
				else {
					combo.removeAllItems();
				}
			}
		}

		/**
		 * We use an ItemListener imstead of an ActionListener because an
		 * action event is also generated if the DEL or SHIFT+DEL button
		 * is pressed in order to delete an item resp. all items.
		 */
		private final class ComboItemListener implements ItemListener {
			String dir;

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectNewDirectory();
				}
			}

			private void selectNewDirectory() {
				dir = (String)combo.getSelectedItem();
				EFileChooser.this.setCurrentDirectory(new File(dir));
				JLabel label = new JLabel(dir);
				label.setFont(combo.getFont());
				if (label.getPreferredSize().width > combo.getSize().width) {
					combo.setToolTipText(dir);
				}
				else {
					combo.setToolTipText(null);
				}
			}
		}

		/** 
		 * Display a tooltip for the cell if needed.
		 *
		 * Note :      		 
		 * When JComboBox is located near the border of a Frame, the tooltip 
		 * doesn't display outside the frame due to current Swing limitations.
		*/
		class ComboBoxRendererWithTooltips extends BasicComboBoxRenderer {
			
			private static final long serialVersionUID = 1L;

			public Component getListCellRendererComponent(JList list, 
			Object value,
			int index, 
			boolean isSelected,
			boolean cellHasFocus) {
				setFont(list.getFont());
				setText((value == null) ? "" : value.toString());           
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
					if (index >= 0) {
						if (this.getPreferredSize().width > combo.getSize().width) {
							list.setToolTipText((String)comboModel.elementAt(index));
						}
						else {
							list.setToolTipText(null);
						}
					}
				}
				else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}                 
				return this;
			}  
		}

	}

	// --- TextPreviewer -------------------------------------------------------

	class TextPreviewer extends JComponent {
		
		private static final long serialVersionUID = 1L;
		private JTextArea textArea = new JTextArea();
		private JScrollPane scroller = new JScrollPane(textArea);
		private char[] buff = new char[500];
		private Color bg;

		TextPreviewer() {

			try {
				textArea.setEditable(false);
				if ((bg = UIManager.getColor("TextArea.background")) != null)
					textArea.setBackground(bg);
				else
					textArea.setBackground(Color.white);
				setBorder(BorderFactory.createEtchedBorder());
				setLayout(new BorderLayout());
				add(scroller, BorderLayout.CENTER);
			}
			catch (NullPointerException np) {
				// layout can throw exceptions sometimes: ignore
			}
		}
		public void initTextArea(File file) {
			textArea.setText(contentsOfFile(file));
			textArea.setCaretPosition(0);
		}

		public void clear() {
			textArea.setText("");
		}

		private String contentsOfFile(File file) {
			if (file == null) {
				return "";
			}
			if (file.getName().equals("")) {
				return "";
			}
			String s = new String();      
			FileReader reader = null;
			try {
				reader = new FileReader(file);
				int nch = reader.read(buff, 0, buff.length);
				if (nch != -1) {
					s = new String(buff, 0, nch);
				}
			}
			catch (IOException iox) {
				s = "";
			}
			try {
				reader.close();
			}
			catch (Exception ex) {
				// ignore
			}
			return s;
		}
	}

	// -------------------------------------------------------------------------

	// CONSTRUCTORS //

	public EFileChooser() {
		// look for existing directory history from last session
		dirFile = System.getProperty("user.home")
											  + System.getProperty("file.separator")
											  + ".EFileChooser";
		if (new File(dirFile).exists()) {
			try {
				fis = new FileInputStream(dirFile);         
				ois = new ObjectInputStream(fis);
				comboModel = (Vector)(ois.readObject());						
				ois.close();
				fis.close();         
			}
			catch (Exception e) {
				System.err.println("Trouble reading EFileChooser directories: "
				+ e);
				e.printStackTrace();
			}
		}
		else {
			comboModel = new Vector(5); // we expect about 5 directory entries
		}

		setMultiSelectionEnabled(false);
		setPreferredSize(new Dimension(500,350)); 
		previewAndHistoryPanel = new PreviewAndHistoryPanel();
		JPanel choicePanel = new JPanel(new BorderLayout());
		JTabbedPane choicePane = new JTabbedPane();
		choicePane.addTab("Recent Folders and Preview", previewAndHistoryPanel);
		choicePanel.add(choicePane, BorderLayout.CENTER);
		setAccessory(choicePanel);    
		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals(
				JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {					
					previewer.clear();
					File dir = (File)e.getNewValue();
					if (dir == null) {
						return;
					}
					if (dir.getName().equals("")) {
						return;
					}
					String pathname = dir.getAbsolutePath();
					int i;
					boolean found = false;						
					
					
					for (i = 0; i < comboModel.size(); i++) {
						String dirname = (String)comboModel.elementAt(i);
						if (dirname.equals(pathname)) {
							found = true;
							break;
						}
					}
					if (found) {
						combo.setSelectedIndex(i);
					}
				}
				if (e.getPropertyName().equals(
				JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
					File f = (File)e.getNewValue();
					showFileContents(f);
					insertDirectory(f);
				}
			}
		});
	}


	protected void showFileContents(File f) {
		previewer.initTextArea(f);
	}

	public static void saveDirectoryEntries() {
		try {
			FileOutputStream fos = new FileOutputStream(dirFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			Vector recentListModel = new Vector(5);
			if (comboModel.size() > 5)
				for (int i = comboModel.size()-5; i < comboModel.size(); i++)
					recentListModel.addElement(comboModel.get(i));				
			else
				recentListModel = (Vector)comboModel.clone();
			oos.writeObject(recentListModel);
			oos.flush();
			oos.close();
			fos.close();
		}
		catch (Exception e) {
			System.err.println("Trouble saving EFileChooser directories: " +e);
			e.printStackTrace();
		}

	}

	private void insertDirectory(File file) {
		if (file == null) {
			return;
		}
		if (file.getName().equals("")) {
			return;
		}
		String pathname = file.getAbsolutePath();
		int pos = pathname.lastIndexOf(System.getProperty("file.separator"));
		String dir = pathname.substring(0, pos);
		if (comboModel.contains(dir)) {
			return;
		}
		else {
			comboModel.addElement(dir);
			combo.revalidate(); 
			combo.setSelectedItem(dir);
			saveDirectoryEntries();
		}
	}

	// --- Demo app ------------------------------------------------------------

	public static void main(String[] argv) {
		final EFileChooser fileChooser = new EFileChooser();

		final JFrame frame = new JFrame("EFileChooser Demo");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent A) {
				//fileChooser.saveDirectoryEntries();
				frame.setVisible(false);
				frame.dispose();
				System.exit(0);
			}
		});
		Container c = frame.getContentPane();
		JPanel buttonP = new JPanel();
		JButton openB = new JButton("Open File");
		final JTextField textField = new JTextField(20);
		textField.setEditable(false);
		openB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));				
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					if (f == null) {
						return;
					}
					if (f.getName().equals("")) {
						return;
					}
					textField.setText(f.getAbsolutePath());
				}
			}
		});
		JButton exitB = new JButton("Exit Demo");
		exitB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//fileChooser.saveDirectoryEntries();
				frame.setVisible(false);
				frame.dispose();
				System.exit(0);
			}
		});
		buttonP.add(openB);
		buttonP.add(exitB);
		c.add(buttonP, BorderLayout.NORTH);
		c.add(textField, BorderLayout.SOUTH);
		c.add(new JLabel("Exercising EFileChooser", JLabel.CENTER), BorderLayout.CENTER);
		frame.pack();
		frame.setLocation(300, 300);
		frame.setSize(400, 200);
		frame.setVisible(true);
	}

	// -------------------------------------------------------------------------

	/**
	 * This inner class is used to set the UI to MyComboBoxUI
	 * Unlike JButton, JComboBox has no public setUI() method,
	 * so we have to go this way using the protected setUI()
	 * method of JComponent.
	 */
	class MyComboBox extends JComboBox {
		
		private static final long serialVersionUID = 1L;

		public MyComboBox(Vector items) {
			super(items);
			setUI(new MyComboBoxUI());
		}
	} 

	/**
	 * Modified UI for JComboBox.
	 */
	class MyComboBoxUI extends BasicComboBoxUI {

		private MyComboBoxUI() {
			super();
		}

		/**
		 * Creates an implementation of the ComboPopup interface.
		 * Returns an instance of MyComboPopup.
		 */
		protected ComboPopup createPopup() {
			return new MyComboPopup(comboBox);
		}

		class MyComboPopup extends BasicComboPopup {
			
			private static final long serialVersionUID = 1L;
			protected JList list_;
			protected JComboBox comboBox_;
			protected boolean hasEntered_;

			MyComboPopup(JComboBox combo) {
				super(combo);
				list_ = list;
				comboBox_ = comboBox;
				hasEntered_ = hasEntered;
			}

			/**
			 * Creates the mouse listener that is returned by ComboPopup.getMouseListener().
			 * Returns an instance of MyComboPopup$MyInvocationMouseHandler.
			 */
			protected MouseListener createMouseListener() {
				return new MyInvocationMouseHandler();
			}

			protected MouseListener createListMouseListener() {
				return new MyListMouseHandler();
			}

			protected MouseEvent convertMouseEvent(MouseEvent e) {
				return super.convertMouseEvent(e);
			}

			protected void updateListBoxSelectionForEvent(MouseEvent anEvent,boolean shouldScroll) {
				super.updateListBoxSelectionForEvent(anEvent, shouldScroll);
			}

			protected void stopAutoScrolling() {
				super.stopAutoScrolling();
			}

			protected void delegateFocus(MouseEvent e) {
				super.delegateFocus(e);
			}

			protected void togglePopup() {
				super.togglePopup();
			}

			public void hide() {
				super.hide();
			}

			class MyInvocationMouseHandler extends MouseAdapter {

				public void mousePressed(MouseEvent e) {					

					if ( !SwingUtilities.isLeftMouseButton(e) )
						return;

					if ( !comboBox_.isEnabled() )
						return;

					delegateFocus( e );
					togglePopup();
				}

				public void mouseReleased(MouseEvent e) {
					int oldSelectedIndex = comboBox_.getSelectedIndex();
					Component source = (Component)e.getSource();
					Dimension size = source.getSize();
					Rectangle bounds = new Rectangle( 0, 0, size.width - 1, size.height - 1 );
					if (!bounds.contains( e.getPoint())) {
						MouseEvent newEvent = convertMouseEvent(e);
						Point location = newEvent.getPoint();
						Rectangle r = new Rectangle();
						list_.computeVisibleRect(r);
						if (r.contains(location)) {
							updateListBoxSelectionForEvent(newEvent, false);
							int index = list_.getSelectedIndex();
							comboBox_.setSelectedIndex(index);
							if (index == oldSelectedIndex) {
								comboItemListener.selectNewDirectory();
							}
						}
						hide();
					}
					hasEntered_ = false;
					stopAutoScrolling();
				}

			}

			/**
			 * This listener hides the popup when the mouse is released in the list.
			 */
			protected class MyListMouseHandler extends MouseAdapter {
				
				public void mouseReleased(MouseEvent e) {					
					comboItemListener.selectNewDirectory();
					comboBox_.setSelectedIndex(list_.getSelectedIndex());
					hide();
				}
			} 

		}

	}

}