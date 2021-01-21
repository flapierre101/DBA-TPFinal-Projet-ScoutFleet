package ca.qc.cvm.dba.scoutlog.dao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

// Mongo
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

//Barkeley
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import org.bson.Document;
import ca.qc.cvm.dba.scoutlog.entity.LogEntry;

public class LogDAO {
	/**
	 * M�thode permettant d'ajouter une entr�e
	 * 
	 * Note : Ne changer pas la structure de la m�thode! Elle permet de faire
	 * fonctionner l'ajout d'une entr�e du journal. Il faut donc que la compl�ter.
	 * 
	 * @param l'objet avec toutes les donn�es de la nouvelle entr�e
	 * @return si la sauvegarde a fonctionn�e
	 */
	public static boolean addLog(LogEntry log) {
		boolean success = false;
		try {
			MongoDatabase connectionMongo = MongoConnection.getConnection();
			MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
			Document doc = new Document();

			doc.append("date", log.getDate());
			doc.append("name", log.getName());
			doc.append("status", log.getStatus());

			switch (log.getStatus()) {
			case "Anormal":
				doc.append("reasons", log.getReasons());
				break;

			case "Exploration":
				if (log.getImage() != null) {
					Database connectionBK = BerkeleyConnection.getConnection();

					String key = log.getPlanetName();
					byte[] data = log.getImage();

					try {
						DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
						DatabaseEntry theData = new DatabaseEntry(data);
						connectionBK.put(null, theKey, theData);
					} catch (Exception e) {
						e.printStackTrace();
					}
					doc.append("imageKey", key);
					doc.append("planets", log.getNearPlanets());
					doc.append("planetName", log.getPlanetName());
					doc.append("galaxyName", log.getGalaxyName());
					doc.append("habitable", log.isHabitable());
				}
				break;
			}
			collection.insertOne(doc);
			success = true;
//			System.out.println(log.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Permet de retourner la liste de plan�tes d�j� explor�es
	 * 
	 * Note : Ne changer pas la structure de la m�thode! Elle permet de faire
	 * fonctionner l'ajout d'une entr�e du journal. Il faut donc que la compl�ter.
	 * 
	 * @return le nom des plan�tes d�j� explor�es
	 */
	public static List<String> getPlanetList() {
		final List<String> planets = new ArrayList<String>();
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		
		FindIterable<Document> iterator = collection.find();
		try {
			iterator.forEach(new Block<Document>() {
				@Override
				public void apply(final Document document) {
					String planet = document.getString("planetName");
					planets.add(planet);
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// Exemple...
		planets.add("Terre");
		planets.add("Solaria");
		planets.add("Dune");

		return planets;
	}

	/**
	 * Retourne l'entr�e selon sa position dans le temps. La derni�re entr�e est 0,
	 * l'avant derni�re est 1, l'avant avant derni�re est 2, etc.
	 * 
	 * Toutes les informations li�es � l'entr�e doivent �tre affect�es � l'objet
	 * retourn�.
	 * 
	 * 
	 * @param position (d�marre � 0)
	 * @return
	 */
	public static LogEntry getLogEntryByPosition(int position) {
		final List <LogEntry> resultLogs = new ArrayList<LogEntry>();
		
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");

		Document query = new Document();
		Document orderBy = new Document("date", -1);
		FindIterable<Document> iterator = collection.find(query).sort(orderBy);
		try {
			iterator.forEach(new Block<Document>() {
				@Override
				public void apply(final Document document) {
					String status = document.getString("status");
					
					if (status != null) {
						switch (status) {
						case "Normal":
							resultLogs.add(new LogEntry(document.getString("date"), document.getString("name"),
									document.getString("status")));
							break;
						case "Anormal":
							resultLogs.add(new LogEntry(document.getString("date"), document.getString("name"),
									document.getString("status"), document.getString("reasons")));
							break;
						case "Exploration":
							
							System.out.println(document.get("planets").toString()); 
							List <String> test = new ArrayList<>();	
							test.add(document.get("planets").toString());
							resultLogs.add(new LogEntry(document.getString("date"), document.getString("name"),
									document.getString("status"), document.getString("reasons"), test,
									document.getString("planetName"), document.getString("galaxyName"), 
									getPlanetImage(document.getString("imageKey")), document.getBoolean("habitable")));
								// changer true/false de habitable par des mots plus significatifs
							break;
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultLogs.get(position);
	}

	/**
	 * Permet de supprimer une entr�e, selon sa position
	 * 
	 * @param position de l'entr�e, identique � getLogEntryByPosition
	 * @return
	 */
	public static boolean deleteLog(int position) {
		boolean success = false;
		
		// MongoDB
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		LogEntry toDelete = getLogEntryByPosition(position);
		Document docToDelete = new Document ("date", toDelete.getDate());
		
		// BerkeleyDB
		Database connectionBK = BerkeleyConnection.getConnection();
		String keyToDelete = toDelete.getPlanetName();
		
		try {
			// Mongo suite
			collection.deleteOne( docToDelete );
			
			// Berkeley suite
			DatabaseEntry theKey = new DatabaseEntry(keyToDelete.getBytes("UTF-8"));
			connectionBK.delete(null, theKey);
			
			//Sucess si pas d'exeption
			success = true;
		}
		catch(Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	/**
	 * Doit retourner le nombre d'entr�es dans le journal de bord
	 * 
	 * Note : Ne changer pas la structure de la m�thode! Elle permet de faire
	 * fonctionner l'affichage de la liste des entr�es du journal. Il faut donc que
	 * la compl�ter.
	 * 
	 * @return nombre total
	 */
	public static long getNumberOfEntries() {
		long count = 0;
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");

		Document where = new Document();
		count = collection.count(where);
		return count;
	}

	/**
	 * Retourne le nombre de plan�tes habitables
	 * 
	 * @return nombre total
	 */
	public static int getNumberOfHabitablePlanets() {
		int habPlanet = 0;
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		Document where = new Document("habitable", true);
		where.append("status", "Exploration");
		habPlanet = (int)collection.count(where);
		
		return habPlanet;
	}

	/**
	 * Retourne entre 0 et 100 la moyenne d'entr�es de type exploration sur le
	 * nombre total d'entr�es
	 * 
	 * @return moyenne, entre 0 et 100
	 */
	public static int getExplorationAverage() {
		int tempoExplo = 0;
		int avg = 0;
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");

		Document where = new Document("status", "Exploration");
		tempoExplo = (int)collection.count(where);
		avg = tempoExplo * 100 / (int)getNumberOfEntries();
		return avg;
	}

	/**
	 * Retourne le nombre de photos sauvegard�es
	 * 
	 * @return nombre total
	 */
	public static int getPhotoCount() {
		Database connectionBK = BerkeleyConnection.getConnection();
		Cursor c = null;
		int counter = 0;
		try {
			c = connectionBK.openCursor(null, null);
			
		    DatabaseEntry foundKey = new DatabaseEntry();
		    DatabaseEntry foundData = new DatabaseEntry();
		    while (c.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
		    	counter ++;
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
		return counter;
	}

	/**
	 * Retourne le nom des derni�res plan�tes explor�es
	 * 
	 * @param limit nombre � retourner
	 * @return
	 */
	public static List<String> getLastVisitedPlanets(int limit) {
		final List<String> planetList = new ArrayList<String>();
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		
		Document query = new Document();
		Document orderBy = new Document ("date", -1);
		FindIterable<Document> iterator = collection.find(query).sort(orderBy).limit(limit);
		
		try {
			iterator.forEach(new Block<Document>() {
				@Override
				public void apply(final Document document) {
					if (document.getString("status").equals("Exploration")){
						planetList.add(document.getString("planetName"));
					}
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//jamais 5 seulement 4??
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
	 * @return Liste du nom des plan�tes � parcourir, incluant "fromPlanet" et
	 *         "toPlanet", ou null si aucun chemin trouv�
	 */
	public static List<String> getTrajectory(String fromPlanet, String toPlanet) {
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		long planetCount = 0;
		//String tempoString = null;
		
		Document where = new Document("planets", fromPlanet);
		
		planetCount = collection.count(where);
		System.out.println(planetCount);
		
		
		return null;
	}

	/**
	 * La liste des galaxies ayant le plus de plan�tes explor�es (en ordre
	 * d�croissant)
	 * 
	 * @param limit Nombre � retourner
	 * @return List de nom des galaxies + le nombre de plan�tes visit�es, par
	 *         exemple : Androm�de (7 plan�tes visit�es), ...
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
		Database connectionBK = BerkeleyConnection.getConnection();
		Cursor c = null;
		List <String> aDelete = new ArrayList<String>();
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		try {
			// BerkeleyDB
			c = connectionBK.openCursor(null, null);
			
		    DatabaseEntry foundKey = new DatabaseEntry();
		    DatabaseEntry foundData = new DatabaseEntry();
		    while (c.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
		    	aDelete.add(new String(foundKey.getData(), "UTF-8"));
		    }
		    
		    // MongoDB
		    
		    collection.drop();
		}
		catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		finally {
			if (c != null) {
				c.close();
				// Berkeley suite
				for (String delKey : aDelete) {
					try {
						DatabaseEntry foundKey = new DatabaseEntry(delKey.getBytes("UTF-8"));
						connectionBK.delete(null, foundKey);
					}
					catch(UnsupportedEncodingException e) {
						e.printStackTrace();
						success = false;
					}
				}
			}
			success = true;
		}
		
		return success;
	}
	
	public static byte[] getPlanetImage (String key) {
		byte[] image = null;
		Database connectionBK = BerkeleyConnection.getConnection();
		
		try {
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry();
			
			if (connectionBK.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) { 
		        image = theData.getData();
			}
			else {
			        System.out.println("Element inexistant");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
}
