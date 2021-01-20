package ca.qc.cvm.dba.scoutlog.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import ca.qc.cvm.dba.scoutlog.app.Facade;
import ca.qc.cvm.dba.scoutlog.entity.LogEntry;
import ca.qc.cvm.dba.scoutlog.event.AddLogEvent;
import ca.qc.cvm.dba.scoutlog.view.util.BackgroundPanel;

public class PanelLogAdd extends CommonPanel {
	private static final long serialVersionUID = 1L;
	private JLabel dateLabel;
	private JTextField dateField;
	
	private JLabel nameLabel;
	private JTextField nameField;
	
	private JLabel statusLabel;
	private JComboBox<String> statusField;
	
	private JLabel planetsLabel;
	private JList<String> planetsField;
	
	private JLabel reasonsLabel;
	private JTextPane reasonsField;
	
	private JLabel planetNameLabel;
	private JTextField planetNameField;
	
	private JLabel galaxyNameLabel;
	private JTextField galaxyNameField;
	
	private JLabel habitableLabel;
	private JCheckBox habitableField;

	private JButton photoButton;
	private JLabel photoLabel;
	private ImageIcon photoImg;
	private JLabel photoImage;

	public PanelLogAdd(int width, int height) throws Exception {
		super(width, height, true, "assets/images/background-log-add.jpg");
	}
	
	@Override
	public void jbInit() throws Exception {
		// Ajout de la date
		dateLabel = super.addLabel("Date : ", 20, 20, 150, 30);
		dateField = new JTextField();
		super.addField(dateField, 190, 20, 250, 30);
		dateField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (dateField.getText().equals("YYYY-MM-DD")) {
					dateField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (dateField.getText().equals("")) {
					dateField.setText("YYYY-MM-DD");
				}
			}
			
		});
		
		// Ajout du nom
		nameLabel = super.addLabel("Commandant: ", 20, 70, 150, 30);
		nameField = new JTextField();
		super.addField(nameField, 190, 70, 250, 30);

		// Ajout du statut		
		Vector<String> choices = new Vector<String>();
		choices.add("--");
		choices.add("Normal");
		choices.add("Anormal");
		choices.add("Exploration");
		statusField = new JComboBox<String>(choices);
		statusLabel = super.addLabel("Statut : ", 20, 120, 150, 30);
		super.addField(statusField, 190, 120, 250, 30);
		statusField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reasonsField.setVisible(false);
				reasonsLabel.setVisible(false);
				planetsField.setVisible(false);
				planetsLabel.setVisible(false);
				planetNameLabel.setVisible(false);
				galaxyNameLabel.setVisible(false);
				planetNameField.setVisible(false);
				galaxyNameField.setVisible(false);
				photoLabel.setVisible(false);
				photoImage.setVisible(false);
				photoButton.setVisible(false);
				habitableLabel.setVisible(false);
				habitableField.setVisible(false);
				
				if (statusField.getSelectedIndex() == 1) {
					// Statut normal
				}
				else if (statusField.getSelectedIndex() == 2) {
					reasonsField.setVisible(true);
					reasonsLabel.setVisible(true);
				}
				else if (statusField.getSelectedIndex() == 3) {
					planetsField.setVisible(true);
					planetsLabel.setVisible(true);
					planetNameLabel.setVisible(true);
					galaxyNameLabel.setVisible(true);
					planetNameField.setVisible(true);
					galaxyNameField.setVisible(true);
					photoLabel.setVisible(true);
					photoImage.setVisible(true);
					photoButton.setVisible(true);
					habitableLabel.setVisible(true);
					habitableField.setVisible(true);
				}
				else {
					// Pas choisi encore...
				}
			}			
		});
		
		// Pour le statut anormal : Raisons
		reasonsField = new JTextPane();
		reasonsLabel= super.addLabel("Raison(s) :", 20, 170, 300, 30);
		super.addField(reasonsField, 190, 170, 250, 150);		
		reasonsField.setVisible(false);
		reasonsLabel.setVisible(false);

		planetNameLabel = super.addLabel("Nom planète : ", 20, 170, 150, 30);
		planetNameField = new JTextField();
		super.addField(planetNameField, 190, 170, 250, 30);
		planetNameLabel.setVisible(false);
		planetNameField.setVisible(false);
		
		galaxyNameLabel = super.addLabel("Nom galaxie : ", 20, 220, 150, 30);
		galaxyNameField = new JTextField();
		super.addField(galaxyNameField, 190, 220, 250, 30);
		galaxyNameLabel.setVisible(false);
		galaxyNameField.setVisible(false);
		
		habitableLabel = super.addLabel("Habitable : ", 20, 270, 150, 30);
		habitableField = new JCheckBox();
		super.addField(habitableField, 190, 270, 30, 30);
		habitableField.setOpaque(false);
		habitableLabel.setVisible(false);
		habitableField.setVisible(false);
		
		photoLabel = super.addLabel("Photo (png) : ", 20, 320, 150, 30);
		photoImage = super.addLabel("", 500, 220, 250, 250);
		photoImage.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		photoButton = super.addButton("Choisir", 190, 320, 100, 30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        fileChooser.setFileFilter(new FileFilter(){

					@Override
					public boolean accept(File f) {
						return f != null && (f.isDirectory() || f.toString().endsWith(".png"));
					}

					@Override
					public String getDescription() {
						return "PNG";
					}
		        	
		        });
		        int resultat = fileChooser.showSaveDialog(PanelLogAdd.this);

	            if (resultat != JFileChooser.CANCEL_OPTION) {
	            	try {
	            		BufferedImage inputImage = ImageIO.read(fileChooser.getSelectedFile());
	            		BufferedImage outputImage = new BufferedImage(250, 250, inputImage.getType());
		                Graphics2D g2d = outputImage.createGraphics();
		                g2d.drawImage(inputImage, 0, 0, 250, 250, null);
		                g2d.dispose();
		                
		                
		            	photoImg = new ImageIcon(outputImage);		            	
		            	photoImage.setIcon(photoImg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            }
			}
		});
		
		// Liste de planètes
		planetsField = new JList<String>();
		planetsField.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		planetsLabel= super.addLabel("Les planètes adjacentes (ctrl+clic)", 500, 20, 300, 30);
		super.addField(planetsField, 500, 50, 250, 150);
		
		this.addButton("Ajouter", 50, 450, 150, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LogEntry log = null;
				String errMsg = "";	
				
				if (nameField.getText().trim().length() == 0) {
					errMsg += "\n - Le nom ne peut pas être vide";
				}
				
				if (dateField.getText().equals("YYYY-MM-DD") || dateField.getText().trim().length() != 10) {
					errMsg += "\n - La date est invalide";
				}
				
				if (statusField.getSelectedIndex() == 0) {
					errMsg += "\n - Il faut choisir un statut";
				}
				
				if (statusField.getSelectedIndex() == 2 && reasonsField.getText().length() == 0) {
					errMsg += "\n - La raison ne peut pas être vide";
				}
				
				if (statusField.getSelectedIndex() == 3 && planetNameField.getText().length() == 0) {
					errMsg += "\n - le nom de la planète ne peut pas être vide";
				}
				
				if (statusField.getSelectedIndex() == 3 && galaxyNameField.getText().length() == 0) {
					errMsg += "\n - le nom de la galaxie ne peut pas être vide";
				}

				if (statusField.getSelectedIndex() == 3 && photoImg == null) {
					errMsg += "\n - La photo ne peut pas être vide";
				}
				
				if (errMsg.length() > 0) {
					JOptionPane.showMessageDialog(PanelLogAdd.this, "Veuillez vérifier vos données :" + errMsg);
				}
				else {
					if (statusField.getSelectedIndex() == 1) {
						// Statut normal
						log = new LogEntry(dateField.getText(), nameField.getText(), statusField.getSelectedItem().toString());
					}
					else if (statusField.getSelectedIndex() == 2) {
						// Statut anormal
						log = new LogEntry(dateField.getText(), nameField.getText(), statusField.getSelectedItem().toString(), reasonsField.getText());
					}
					else if (statusField.getSelectedIndex() == 3) {
						// Statut exploration
						List<String> selectedPlanets = planetsField.getSelectedValuesList();					
						
						BufferedImage bi = new BufferedImage(photoImg.getIconWidth(),photoImg.getIconHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = bi.createGraphics();
						photoImg.paintIcon(null, g, 0,0);
						g.dispose();
						
						ByteArrayOutputStream baos = null;
						
					    try {
					    	baos = new ByteArrayOutputStream();
					    	ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
					        ImageIO.write(bi, "png", ios);
					        ios.close();
					    } catch (Exception ee) {
					    	ee.printStackTrace();
					    }
					    
						log = new LogEntry(dateField.getText(), nameField.getText(), statusField.getSelectedItem().toString(), null, selectedPlanets,
										   planetNameField.getText(), galaxyNameField.getText(), baos.toByteArray(), habitableField.isSelected());
					}
				}
				
				if (errMsg.length() == 0 && log != null) {
					// Démarre un événement, qui permet de sauvegarder l'entrée dans le journal.
					// En passant par la facade, un thread est démarré et la méthode MngApplication.processEvent est appelé
					// Dans la méthode MngApplication.processEvent, celle-ci comprend que l'événement est une insertion du log et 
					// appelle la méthode MngApplication.addLog
					Facade.getInstance().processEvent(new AddLogEvent(log));
				}
			}
		});		
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page).
	 * Elle peut donc servir à préparer l'interface graphique (vider les champs, remplir les combobox, etc)
	 */
	@Override
	public void resetUI() {
		nameField.setText("");
		dateField.setText("YYYY-MM-DD");
		statusField.setSelectedItem("--");
		reasonsField.setText("");
		planetNameField.setText("");
		galaxyNameField.setText("");
		photoImg = null;
		photoImage.setIcon(null);
		
		planetsField.removeAll();
		List<String> planets = Facade.getInstance().getPlanetList();

		DefaultListModel<String> model = new DefaultListModel<String>();
		
		for (String planet : planets) {
			model.addElement(planet);
		}
		
		planetsField.setModel(model);
	}

}
