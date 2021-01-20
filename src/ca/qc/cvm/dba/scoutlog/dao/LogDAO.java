package ca.qc.cvm.dba.scoutlog.dao;

import java.util.ArrayList;
import java.util.List;

import ca.qc.cvm.dba.scoutlog.entity.LogEntry;

public class LogDAO {
	/**
	 * Méthode permettant d'ajouter une entrée
	 * 
	 * Note : Ne changer pas la structure de la méthode! Elle
	 * permet de faire fonctionner l'ajout d'une entrée du journal.
	 * Il faut donc que la compléter.
	 * 
	 * @param l'objet avec toutes les données de la nouvelle entrée
	 * @return si la sauvegarde a fonctionnée
	 */
	public static boolean addLog(LogEntry log) {
		boolean success = false;
		
		System.out.println(log.toString());
		
		if (log.getImage() != null) {
			// l'entrée possède une image!
		}
		
		return success;
	}
	
	/**
	 * Permet de retourner la liste de planètes déjà explorées
	 * 
	 * Note : Ne changer pas la structure de la méthode! Elle
	 * permet de faire fonctionner l'ajout d'une entrée du journal.
	 * Il faut donc que la compléter.
	 * 
	 * @return le nom des planètes déjà explorées
	 */
	public static List<String> getPlanetList() {
		List<String> planets = new ArrayList<String>();
		
		// Exemple...
		planets.add("Terre");
		planets.add("Solaria");
		planets.add("Dune");
		
		return planets;
	}
	
	/**
	 * Retourne l'entrée selon sa position dans le temps.
	 * La dernière entrée est 0,
	 * l'avant dernière est 1,
	 * l'avant avant dernière est 2, etc.
	 * 
	 * Toutes les informations liées à l'entrée doivent être affectées à 
	 * l'objet retourné. 
	 * 
	 * 
	 * @param position (démarre à 0)
	 * @return
	 */
	public static LogEntry getLogEntryByPosition(int position) {
		return null;
	}
	
	/**
	 * Permet de supprimer une entrée, selon sa position 
	 *  
	 * @param position de l'entrée, identique à getLogEntryByPosition
	 * @return
	 */
	public static boolean deleteLog(int position) {
		boolean success = false;
		
		return success;
	}
	
	/**
	 * Doit retourner le nombre d'entrées dans le journal de bord
	 * 
	 * Note : Ne changer pas la structure de la méthode! Elle
	 * permet de faire fonctionner l'affichage de la liste des entrées 
	 * du journal. Il faut donc que la compléter.
	 * 
	 * @return nombre total
	 */
	public static int getNumberOfEntries() {
		return 0;
	}
	
	/**
	 * Retourne le nombre de planètes habitables
	 * 
	 * @return nombre total
	 */
	public static int getNumberOfHabitablePlanets() {
		return 0;
	}
	
	/**
	 * Retourne entre 0 et 100 la moyenne d'entrées de type exploration sur le
	 * nombre total d'entrées
	 * 
	 * @return moyenne, entre 0 et 100
	 */
	public static int getExplorationAverage() {
		return 0;
	}

	
	/**
	 * Retourne le nombre de photos sauvegardées
	 * 
	 * @return nombre total
	 */
	public static int getPhotoCount() {
		return 0;
	}
	

	/**
	 * Retourne le nom des dernières planètes explorées
	 * 
	 * @param limit nombre à retourner
	 * @return
	 */
	public static List<String> getLastVisitedPlanets(int limit) {
		List<String> planetList = new ArrayList<String>();
				
		return planetList;
	}
	
	/**
	 * Permet de trouver la galaxie avec le plus grand nombre de planètes habitables
	 * 
	 * @return le nom de la galaxie
	 */
	public static String getBestGalaxy() {
		return "";
	}
	
	/**
	 * Permet de trouver une chemin pour se rendre d'une planète à une autre 
	 * 
	 * @param fromPlanet
	 * @param toPlanet
	 * @return Liste du nom des planètes à parcourir, incluant "fromPlanet" et "toPlanet", ou null si aucun chemin trouvé
	 */
	public static List<String> getTrajectory(String fromPlanet, String toPlanet) {
		
		return null;
	}

	/**
	 * La liste des galaxies ayant le plus de planètes explorées (en ordre décroissant) 
	 * 
	 * @param limit Nombre à retourner
	 * @return List de nom des galaxies + le nombre de planètes visitées, par exemple : Andromède (7 planètes visitées), ...
	 */	
	public static List<String> getExploredGalaxies(int limit) {
		List<String> galaxyList = new ArrayList<String>();

		return galaxyList;
	}
	
	/**
	 * Suppression de toutes les données
	 */
	public static boolean deleteAll() {
		boolean success = false;
		
		return success;
	}
}
