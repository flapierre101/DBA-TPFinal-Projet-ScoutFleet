package ca.qc.cvm.dba.scoutlog.app;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Observer;

import ca.qc.cvm.dba.scoutlog.dao.LogDAO;
import ca.qc.cvm.dba.scoutlog.entity.LogEntry;
import ca.qc.cvm.dba.scoutlog.event.CommonEvent;

/**
 * Cette classe est l'intermédiaire entre la logique et la vue
 * Entre les panel et le MngApplication. C'est le point d'entrée de la vue
 * vers la logique
 */
public class Facade {
	private static Facade instance;
	
	private MngApplication app;
	
	private Facade() {
		app = new MngApplication();
	}
	
	public static Facade getInstance() {
		if (instance == null) {
			instance = new Facade();
		}
		
		return instance;
	}
	
	public void processEvent(CommonEvent event) {
		app.addEvent(event);
        new Thread(app).start();
	}
	
	public void addObserverClass( PropertyChangeListener pcl) {
		app.addPropertyChangeListener(pcl);
	}
	
	public List<String> getPlanetList() {
		return app.getPlanetList();
	}
	
	public int getNumberOfEntries() {
		return app.getNumberOfEntries();
	}
	
	public LogEntry getLogEntryByPosition(int position) {
		return app.getLogEntryByPosition(position);
	}
		
	public int getExplorationAverage() {
		return app.getExplorationAverage();
	}
	
	public int getNumberOfHabitablePlanets() {
		return app.getNumberOfHabitablePlanets();
	}
	
	public int getPhotoCount() {
		return app.getPhotoCount();
	}
	
	public List<String> getLastVisitedPlanets(int limit) {
		return app.getLastVisitedPlanets(limit);
	}
	
	public String getBestGalaxy() {
		return app.getBestGalaxy();
	}
	
	public List<String> getTrajectory(String fromPlanet, String toPlanet) {
		return app.getTrajectory(fromPlanet, toPlanet);
	}
	
	public List<String> getExploredGalaxies(int limit) {
		return app.getExploredGalaxies(limit);
	}
	
	public boolean deleteLog(int position) {
		return app.deleteLog(position);
	}
	
	public void exit() {
		app.exit();
	}
}
