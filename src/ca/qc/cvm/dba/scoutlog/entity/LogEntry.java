package ca.qc.cvm.dba.scoutlog.entity;

import java.util.List;

public class LogEntry {
	private String date;
	private String name;
	private String status;
	private List<String> nearPlanets;
	private String reasons;
	private String planetName;
	private String galaxyName;
	private byte[] image;
	private boolean habitable;
	
	// Normal
	public LogEntry(String date, String name, String status) {
		this(date, name, status, null);
	}
	
	// Anormal
	public LogEntry(String date, String name, String status, String reasons) {
		this(date, name, status, reasons, null, null, null, null, false);
	}
	
	// Exploration
	public LogEntry(String date, String name, String status, String reasons, List<String> planets,
					String planetName, String galaxyName, byte[] image, boolean habitable) {
		this.date = date;
		this.name = name;
		this.status = status;
		this.reasons = reasons;
		this.nearPlanets = planets;
		this.planetName = planetName;
		this.galaxyName = galaxyName;
		this.image = image;
		this.habitable = habitable;
	}
	
	public boolean isHabitable() {
		return habitable;
	}
	
	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public List<String> getNearPlanets() {
		return nearPlanets;
	}

	public String getReasons() {
		return reasons;
	}

	public String getPlanetName() {
		return planetName;
	}

	public String getGalaxyName() {
		return galaxyName;
	}

	public byte[] getImage() {
		return image;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNearPlanets(List<String> nearPlanets) {
		this.nearPlanets = nearPlanets;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public void setPlanetName(String planetName) {
		this.planetName = planetName;
	}

	public void setGalaxyName(String galaxyName) {
		this.galaxyName = galaxyName;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setHabitable(boolean habitable) {
		this.habitable = habitable;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Commandant : " + this.name + "\n");
		str.append("Date : " + this.date + "\n");
		str.append("Statut : " + this.status + "\n");
		str.append("Raisons : " + this.reasons + "\n");
		str.append("Planètes proches : " + this.nearPlanets + "\n");
		str.append("Nom planète : " + this.planetName + "\n");
		str.append("Nom galaxie : " + this.galaxyName + "\n");
		str.append("Habitable : " + this.habitable + "\n");
		
		return str.toString();
	}
}
