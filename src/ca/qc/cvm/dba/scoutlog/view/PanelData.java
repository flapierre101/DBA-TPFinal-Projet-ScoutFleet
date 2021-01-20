package ca.qc.cvm.dba.scoutlog.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import ca.qc.cvm.dba.scoutlog.app.Facade;
import ca.qc.cvm.dba.scoutlog.view.FrameMain.Views;
import ca.qc.cvm.dba.scoutlog.view.util.BackgroundPanel;

public class PanelData extends CommonPanel {
	private static final long serialVersionUID = 1L;
	
	private JLabel nbEntry;
	private JLabel avgExploration;
	private JLabel nbHabitablePlanets;
	private JLabel nbPictures;
	private JLabel last5Planets;
	private JLabel bestGalaxy;
	private JLabel trajectory;
	private JTextArea exploredGalaxies;
	
	public PanelData(int width, int height) throws Exception {
		super(width, height, true, "assets/images/background-data.jpg");
	}
	
	@Override
	protected void jbInit() throws Exception {
		super.addLabel("Nombre d'entrées : ", 20, 20, 300, 30);
		nbEntry = super.addLabel("", 320, 20, 150, 30);
		nbEntry.setHorizontalAlignment(JLabel.CENTER);
		nbEntry.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		super.addLabel("Moyenne exploration/total: ", 20, 60, 300, 30);
		avgExploration = super.addLabel("", 320, 60, 150, 30);
		avgExploration.setHorizontalAlignment(JLabel.CENTER);
		avgExploration.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		super.addLabel("Nombre de planètes habitables : ", 20, 100, 300, 30);
		nbHabitablePlanets = super.addLabel("", 320, 100, 150, 30);
		nbHabitablePlanets.setHorizontalAlignment(JLabel.CENTER);
		nbHabitablePlanets.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		super.addLabel("Nombre de photos sauvegardées : ", 20, 140, 300, 30);
		nbPictures = super.addLabel("", 320, 140, 150, 30);
		nbPictures.setHorizontalAlignment(JLabel.CENTER);
		nbPictures.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		super.addLabel("Les 5 dernières planètes visitées : ", 20, 180, 300, 30);
		last5Planets = super.addLabel("", 320, 180, 500, 30);
		last5Planets.setHorizontalAlignment(JLabel.LEFT);
		last5Planets.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		super.addLabel("La galaxie la plus prometteuse : ", 20, 220, 300, 30);
		bestGalaxy = super.addLabel("", 320, 220, 150, 30);
		bestGalaxy.setHorizontalAlignment(JLabel.CENTER);
		bestGalaxy.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		super.addLabel("Trajectoire : ", 20, 260, 150, 30);
		trajectory = super.addLabel("", 320, 260, 500, 30);
		trajectory.setHorizontalAlignment(JLabel.LEFT);
		trajectory.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		JButton trajectoryButton = super.addButton("Calculer", 180, 260, 80, 30, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> planets = Facade.getInstance().getPlanetList();
				
				if (planets.size() > 1) {
					Vector<String> vector = new Vector<String>();
					
					for (String p : planets) {
						vector.add(p);
					}
					
			        JButton btn = new JButton("Ajouter");
			        btn.setSize(new Dimension(100, 20));
			        final JComboBox jcd = new JComboBox(vector);
			        final JComboBox jcd2 = new JComboBox(vector);
			        final JDialog diag = new JDialog();
			        
			        btn.addActionListener(new ActionListener() {
	
						@Override
						public void actionPerformed(ActionEvent e) {
							String p1 = jcd.getSelectedItem().toString();
							String p2 = jcd.getSelectedItem().toString();
							
							List<String> path = Facade.getInstance().getTrajectory(p1, p2);
							
							if (path == null) {
								trajectory.setText("Aucun chemin trouvé");
							}
							else {
								trajectory.setText(path.toString().replace("[", "").replace("]", "") + "");
							}
							
							diag.setVisible(false);
						}
			        });
	
			        Object[] options = new Object[] {};
			        JOptionPane jop = new JOptionPane("",
			                                        JOptionPane.QUESTION_MESSAGE,
			                                        JOptionPane.DEFAULT_OPTION,
			                                        null,options, null);
			        jop.setLayout(new BorderLayout());
			        jop.add(jcd, BorderLayout.NORTH);
			        jop.add(jcd2, BorderLayout.CENTER);	
			        jop.add(btn, BorderLayout.SOUTH);
			        
	 
			        diag.setLocationRelativeTo(PanelData.this);
			        diag.getContentPane().add(jop);
			        diag.pack();
			        diag.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(PanelData.this, "Pas assez de planètes visitées");
				}
			}
		});


		super.addLabel("Les galaxies les plus explorées : ", 20, 300, 300, 30);
		exploredGalaxies = super.addTextArea("", 320, 300, 500, 150);
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page)
	 */
	@Override
	public void resetUI() {		
		nbEntry.setText(Facade.getInstance().getNumberOfEntries() + "");
		avgExploration.setText(Facade.getInstance().getExplorationAverage() + "%");
		nbHabitablePlanets.setText(Facade.getInstance().getNumberOfHabitablePlanets() + "");
		nbPictures.setText(Facade.getInstance().getPhotoCount() + "");
		last5Planets.setText(Facade.getInstance().getLastVisitedPlanets(5).toString().replace("[", "").replace("]", "") + "");
		bestGalaxy.setText(Facade.getInstance().getBestGalaxy());
		trajectory.setText("");
		exploredGalaxies.setText(Facade.getInstance().getExploredGalaxies(6).toString().replace("[", "").replace("]", "").replace(", ", "\n"));
	}

}
