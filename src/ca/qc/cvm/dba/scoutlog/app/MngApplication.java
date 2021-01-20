package ca.qc.cvm.dba.scoutlog.app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ca.qc.cvm.dba.scoutlog.dao.BerkeleyConnection;
import ca.qc.cvm.dba.scoutlog.dao.LogDAO;
import ca.qc.cvm.dba.scoutlog.dao.MongoConnection;
import ca.qc.cvm.dba.scoutlog.entity.LogEntry;
import ca.qc.cvm.dba.scoutlog.event.AddLogEvent;
import ca.qc.cvm.dba.scoutlog.event.CommonEvent;
import ca.qc.cvm.dba.scoutlog.event.CorrectionEvent;
import ca.qc.cvm.dba.scoutlog.event.GoToEvent;
import ca.qc.cvm.dba.scoutlog.event.UIEvent;

public class MngApplication implements Runnable {
    private List<CommonEvent> eventQueue;
    
    private PropertyChangeSupport support;
    
    public MngApplication() {
    	eventQueue = new ArrayList<CommonEvent>();
    	support = new PropertyChangeSupport(this);
    }
    
    public void addEvent(CommonEvent event) {
    	synchronized(MngApplication.class) {
    		eventQueue.add(event);
    	}
    }

	public void run() {
        synchronized (MngApplication.class) {
            CommonEvent event = eventQueue.remove(0);
            processEvent(event);
        }
    }
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
	
	/**
	 * Méthode appelée par la Facade. Elle permet de traiter l'événement.
	 * 
	 * @param event
	 */
	private void processEvent(CommonEvent event) {
		if (event.getType() == CommonEvent.Type.GoTo) {
			support.firePropertyChange(UIEvent.UIType.GoTo.toString(), null, ((GoToEvent)event).getDestination().toString());
		}
		else if (event.getType() == CommonEvent.Type.DeleteAll) {
			deleteAll();
		}
		else if (event.getType() == CommonEvent.Type.Correction) {
			CorrectionEvent evt = (CorrectionEvent)event;
			CorClient c = new CorClient();
			c.start(evt.getIp(), evt.getName(), evt.getPwd());
		}
		else if (event.getType() == CommonEvent.Type.Back) {
			support.firePropertyChange( UIEvent.UIType.Back.toString(), null, "back");
		}
		else if (event.getType() == CommonEvent.Type.AddLog) {
			addLog(((AddLogEvent)event).getLog()); // <-- Puisque c'est un événement de type AddLog, on redirige vers la méthode addLog
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Événement inconnu...");
		}
	}
	
	private void deleteAll() {
		boolean success = LogDAO.deleteAll();
		
		if (success) {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Suppression effectuée");
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Humm... il semble y avoir eu une erreur de suppression");
		}
	}
	
	private void addLog(LogEntry log) {
		boolean success = false;
		
		if (log.getName().length() > 0) {
			success = LogDAO.addLog(log);
		}
		
		if (success) {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Sauvegarde effectuée");
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Désolé, veuillez vérifier vos données");
		}
	}
	
	public int getNumberOfEntries() {
		return LogDAO.getNumberOfEntries();
	}
	
	public LogEntry getLogEntryByPosition(int position) {
		return LogDAO.getLogEntryByPosition(position);
	}
	
	public boolean deleteLog(int position) {
		return LogDAO.deleteLog(position);
	}
	
	public int getNumberOfHabitablePlanets() {
		return LogDAO.getNumberOfHabitablePlanets();
	}
	
	public int getExplorationAverage() {
		return LogDAO.getExplorationAverage();
	}
	
	public int getPhotoCount() {
		return LogDAO.getPhotoCount();
	}

	public List<String> getPlanetList() {
		return LogDAO.getPlanetList();
	}
	
	public List<String> getLastVisitedPlanets(int limit) {
		return LogDAO.getLastVisitedPlanets(limit);
	}
	
	public String getBestGalaxy() {
		return LogDAO.getBestGalaxy();
	}
	
	public List<String> getTrajectory(String fromPlanet, String toPlanet) {
		return LogDAO.getTrajectory(fromPlanet, toPlanet);
	}
	
	public  List<String> getExploredGalaxies(int limit) {
		return LogDAO.getExploredGalaxies(limit);
	}
	
	public void exit() {
		MongoConnection.releaseConnection();
		BerkeleyConnection.releaseConnection();
		System.exit(0);
	}
}
