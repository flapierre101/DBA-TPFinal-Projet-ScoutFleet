package ca.qc.cvm.dba.scoutlog.dao;

import java.util.ArrayList;
import java.util.List;

import ca.qc.cvm.dba.scoutlog.entity.LogEntry;

public class LogDAO {
	/**
	 * M�thode permettant d'ajouter une entr�e
	 * 
	 * Note : Ne changer pas la structure de la m�thode! Elle
	 * permet de faire fonctionner l'ajout d'une entr�e du journal.
	 * Il faut donc que la compl�ter.
	 * 
	 * @param l'objet avec toutes les donn�es de la nouvelle entr�e
	 * @return si la sauvegarde a fonctionn�e
	 */
	public static boolean addLog(LogEntry log) {
		boolean success = false;
		
		System.out.println(log.toString());
		
		if (log.getImage() != null) {
			// l'entr�e poss�de une image!
		}
		
		return success;
	}
	
	/**
	 * Permet de retourner la liste de plan�tes d�j� explor�es
	 * 
	 * Note : Ne changer pas la structure de la m�thode! Elle
	 * permet de faire fonctionner l'ajout d'une entr�e du journal.
	 * Il faut donc que la compl�ter.
	 * 
	 * @return le nom des plan�tes d�j� explor�es
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
	 * Retourne l'entr�e selon sa position dans le temps.
	 * La derni�re entr�e est 0,
	 * l'avant derni�re est 1,
	 * l'avant avant derni�re est 2, etc.
	 * 
	 * Toutes les informations li�es � l'entr�e doivent �tre affect�es � 
	 * l'objet retourn�. 
	 * 
	 * 
	 * @param position (d�marre � 0)
	 * @return
	 */
	public static LogEntry getLogEntryByPosition(int position) {
		return null;
	}
	
	/**
	 * Permet de supprimer une entr�e, selon sa position 
	 *  
	 * @param position de l'entr�e, identique � getLogEntryByPosition
	 * @return
	 */
	public static boolean deleteLog(int position) {
		boolean success = false;
		
		return success;
	}
	
	/**
	 * Doit retourner le nombre d'entr�es dans le journal de bord
	 * 
	 * Note : Ne changer pas la structure de la m�thode! Elle
	 * permet de faire fonctionner l'affichage de la liste des entr�es 
	 * du journal. Il faut donc que la compl�ter.
	 * 
	 * @return nombre total
	 */
	public static int getNumberOfEntries() {
		return 0;
	}
	
	/**
	 * Retourne le nombre de plan�tes habitables
	 * 
	 * @return nombre total
	 */
	public static int getNumberOfHabitablePlanets() {
		return 0;
	}
	
	/**
	 * Retourne entre 0 et 100 la moyenne d'entr�es de type exploration sur le
	 * nombre total d'entr�es
	 * 
	 * @return moyenne, entre 0 et 100
	 */
	public static int getExplorationAverage() {
		return 0;
	}

	
	/**
	 * Retourne le nombre de photos sauvegard�es
	 * 
	 * @return nombre total
	 */
	public static int getPhotoCount() {
		return 0;
	}
	

	/**
	 * Retourne le nom des derni�res plan�tes explor�es
	 * 
	 * @param limit nombre � retourner
	 * @return
	 */
	public static List<String> getLastVisitedPlanets(int limit) {
		List<String> planetList = new ArrayList<String>();
				
		return planetList;
	}
	
	/**
	 * Permet de trouver la galaxie avec le plus grand nombre de plan�tes habitables
	 * 
	 * @return le nom de la galaxie
	 */
	public static String getBestGalaxy() {
		return "";
	}
	
	/**
	 * Permet de trouver une chemin pour se rendre d'une plan�te � une autre 
	 * 
	 * @param fromPlanet
	 * @param toPlanet
	 * @return Liste du nom des plan�tes � parcourir, incluant "fromPlanet" et "toPlanet", ou null si aucun chemin trouv�
	 */
	public static List<String> getTrajectory(String fromPlanet, String toPlanet) {
		
		return null;
	}

	/**
	 * La liste des galaxies ayant le plus de plan�tes explor�es (en ordre d�croissant) 
	 * 
	 * @param limit Nombre � retourner
	 * @return List de nom des galaxies + le nombre de plan�tes visit�es, par exemple : Androm�de (7 plan�tes visit�es), ...
	 */	
	public static List<String> getExploredGalaxies(int limit) {
		List<String> galaxyList = new ArrayList<String>();

		return galaxyList;
	}
	
	/**
	 * Suppression de toutes les donn�es
	 */
	public static boolean deleteAll() {
		boolean success = false;
		
		return success;
	}
}
