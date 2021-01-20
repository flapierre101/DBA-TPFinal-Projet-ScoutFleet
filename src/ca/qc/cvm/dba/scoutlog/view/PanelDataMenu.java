package ca.qc.cvm.dba.scoutlog.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import ca.qc.cvm.dba.scoutlog.app.Facade;
import ca.qc.cvm.dba.scoutlog.view.util.BackgroundPanel;

public class PanelDataMenu extends CommonPanel {
	private static final long serialVersionUID = 1L;

	private JLabel nbHabitablePlanets;
	
	public PanelDataMenu(int width, int height) throws Exception {
		super(width, height, true, "assets/images/background-data-menu.jpg");
	}
	
	@Override
	protected void jbInit() throws Exception {
		super.addLabel("Nombre de plan�tes habitables : ", 20, 20, 250, 30);
		nbHabitablePlanets = super.addLabel("", 300, 20, 80, 30);
		nbHabitablePlanets.setHorizontalAlignment(JLabel.CENTER);
		nbHabitablePlanets.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		// Utilisez super.addField et super.addButton et etc pour cr�er votre interface graphique
	}
	
	/**
	 * Cette m�thode est appel�e automatiquement � chaque fois qu'un panel est affich� (lorsqu'on arrive sur la page)
	 */
	@Override
	public void resetUI() {
		nbHabitablePlanets.setText(Facade.getInstance().getNumberOfHabitablePlanets() + "");
		
		// Aller chercher les r�sultats et afficher ici..
	}

}
