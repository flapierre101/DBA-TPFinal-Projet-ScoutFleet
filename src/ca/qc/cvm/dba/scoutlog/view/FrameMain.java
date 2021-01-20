package ca.qc.cvm.dba.scoutlog.view;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ca.qc.cvm.dba.scoutlog.app.Facade;
import ca.qc.cvm.dba.scoutlog.event.GoToEvent;
import ca.qc.cvm.dba.scoutlog.event.UIEvent;

public class FrameMain extends JFrame implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 900;
	private static final int HEIGHT = 600;
	
	// Poura jouter un autre panel (une page/section à l'application), il faut faire ce qui suit:
	// 1- Ajouter le nom ici... ex: ViewStats
	// 2- Créer le fichier PanelViewStats (par exemple), et selon le modèle d'une autre vue
	// 3- Ajouter dans la variable <<panels>>, voir plus bas, le lien entre le nom et le JPanel
	//    EX : panels.put(Views.ViewStats, new PanelViewStats(WIDTH, HEIGHT));
	// 4- Vous pouvez maintenant appeler pour vous rediriger vers ce nouveau panel
	//    EX : Facade.getInstance().processEvent(new GoToEvent(FrameMain.Views.ViewStats));
	public enum Views {MainMenu, LogMenu, Data, LogAdd}
	
	private Views currentView;
	private List<Views> previousViews;
	
	private HashMap<Views, CommonPanel> panels;
	
	public FrameMain() throws Exception {
		Facade.getInstance().addObserverClass(this);
		jbInit();
	}
	
	private void jbInit() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        this.setIconImage(new ImageIcon("assets/images/logo.png").getImage());
        this.setResizable(false);
		this.setSize(WIDTH, HEIGHT);
		
		this.setTitle("ScoutLog");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				Facade.getInstance().exit();
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {	}
        	
        });

		Toolkit g = this.getToolkit();
		int x = (g.getScreenSize().width / 2) - WIDTH/2;
		int y = (g.getScreenSize().height / 2) - HEIGHT/2;
		this.setLocation(x, y);
		
		panels = new HashMap<Views, CommonPanel>();		
		panels.put(Views.MainMenu, new PanelMainMenu(WIDTH, HEIGHT));
		panels.put(Views.LogMenu, new PanelLogMenu(WIDTH, HEIGHT));
		panels.put(Views.LogAdd, new PanelLogAdd(WIDTH, HEIGHT));
		panels.put(Views.Data, new PanelData(WIDTH, HEIGHT));
		
		previousViews = new ArrayList<Views>();
		
		this.setState(Views.MainMenu, true);
	}
	
	/**
	 * Permet d'afficher un autre panel. Vous ne devriez pas avoir à 
	 * appeler cette méthode.
	 * 
	 * @param state
	 * @param addToStack
	 */
	public void setState(Views state, boolean addToStack) {
		if (currentView != state) {
			for (CommonPanel p : panels.values()) {
				this.getContentPane().remove(p);
			}

			panels.get(state).resetUI();
			
			this.getContentPane().add(panels.get(state));
			this.revalidate();
			this.repaint();
			
			if (addToStack) {
				previousViews.add(currentView);
			}
		}

		currentView = state;
	}

	/**
	 * Méthode appelée automatiquement lorsque dans le fichier MngApplication déclenche un événement 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		UIEvent.UIType event = UIEvent.UIType.valueOf(evt.getPropertyName());
		
		if (event == UIEvent.UIType.ShowMessage) {
			JOptionPane.showMessageDialog(this, evt.getNewValue().toString());
		}
		else if (event == UIEvent.UIType.GoTo) {
			this.setState(Views.valueOf(evt.getNewValue().toString()), true);
		}
		else if (event == UIEvent.UIType.Back) {
			if (previousViews.size() > 0) {
				this.setState(previousViews.remove(previousViews.size() - 1), false);
			}
		}
		else if (event == UIEvent.UIType.Refresh) {
			panels.get(currentView).resetUI();
		}
	}
}
