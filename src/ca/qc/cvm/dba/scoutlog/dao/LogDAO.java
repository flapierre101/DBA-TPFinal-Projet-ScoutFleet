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
	 * Méthode permettant d'ajouter une entrée
	 * 
	 * Note : Ne changer pas la structure de la méthode! Elle permet de faire
	 * fonctionner l'ajout d'une entrée du journal. Il faut donc que la compléter.
	 * 
	 * @param l'objet avec toutes les données de la nouvelle entrée
	 * @return si la sauvegarde a fonctionnée
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
	 * Permet de retourner la liste de planètes déjà explorées
	 * 
	 * Note : Ne changer pas la structure de la méthode! Elle permet de faire
	 * fonctionner l'ajout d'une entrée du journal. Il faut donc que la compléter.
	 * 
	 * @return le nom des planètes déjà explorées
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Exemple...
		planets.add("Terre");
		planets.add("Solaria");
		planets.add("Dune");

		return planets;
	}

	/**
	 * Retourne l'entrée selon sa position dans le temps. La dernière entrée est 0,
	 * l'avant dernière est 1, l'avant avant dernière est 2, etc.
	 * 
	 * Toutes les informations liées à l'entrée doivent être affectées à l'objet
	 * retourné.
	 * 
	 * 
	 * @param position (démarre à 0)
	 * @return
	 */
	public static LogEntry getLogEntryByPosition(int position) {
		final List<LogEntry> resultLogs = new ArrayList<LogEntry>();

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

							// System.out.println(document.get("planets").toString());
							List<String> test = new ArrayList<>();
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
	 * Permet de supprimer une entrée, selon sa position
	 * 
	 * @param position de l'entrée, identique à getLogEntryByPosition
	 * @return
	 */
	public static boolean deleteLog(int position) {
		boolean success = false;

		// MongoDB
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		LogEntry toDelete = getLogEntryByPosition(position);
		Document docToDelete = new Document("date", toDelete.getDate());

		// BerkeleyDB
		Database connectionBK = BerkeleyConnection.getConnection();
		String keyToDelete = toDelete.getPlanetName();

		try {
			// Mongo suite
			collection.deleteOne(docToDelete);

			// Berkeley suite
			DatabaseEntry theKey = new DatabaseEntry(keyToDelete.getBytes("UTF-8"));
			connectionBK.delete(null, theKey);

			// Sucess si pas d'exeption
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	/**
	 * Doit retourner le nombre d'entrées dans le journal de bord
	 * 
	 * Note : Ne changer pas la structure de la méthode! Elle permet de faire
	 * fonctionner l'affichage de la liste des entrées du journal. Il faut donc que
	 * la compléter.
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
	 * Retourne le nombre de planètes habitables
	 * 
	 * @return nombre total
	 */
	public static int getNumberOfHabitablePlanets() {
		int habPlanet = 0;
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		Document where = new Document("habitable", true);
		where.append("status", "Exploration");
		habPlanet = (int) collection.count(where);

		return habPlanet;
	}

	/**
	 * Retourne entre 0 et 100 la moyenne d'entrées de type exploration sur le
	 * nombre total d'entrées
	 * 
	 * @return moyenne, entre 0 et 100
	 */
	public static int getExplorationAverage() {
		int tempoExplo = 0;
		int avg = 0;
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");

		Document where = new Document("status", "Exploration");
		tempoExplo = (int) collection.count(where);
		if (getNumberOfEntries() > 0) {
			avg = tempoExplo * 100 / (int) getNumberOfEntries();
		}
		return avg;
	}

	/**
	 * Retourne le nombre de photos sauvegardées
	 * 
	 * @return nombre total
	 */
	public static int getPhotoCount() {
		Cursor c = null;
		Database connectionBK = BerkeleyConnection.getConnection();
		int counter = 0;
		try {
			c = connectionBK.openCursor(null, null);

			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			while (c.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				counter++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return counter;
	}

	/**
	 * Retourne le nom des dernières planètes explorées
	 * 
	 * @param limit nombre à retourner
	 * @return
	 */
	public static List<String> getLastVisitedPlanets(int limit) {
		final List<String> planetList = new ArrayList<String>();
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");

		Document query = new Document();
		Document orderBy = new Document("date", -1);
		FindIterable<Document> iterator = collection.find(query).sort(orderBy).limit(limit);

		try {
			iterator.forEach(new Block<Document>() {
				@Override
				public void apply(final Document document) {
					if (document.getString("status").equals("Exploration")) {
						planetList.add(document.getString("planetName"));
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		// jamais 5 seulement 4??
		return planetList;
	}

	/**
	 * Permet de trouver la galaxie avec le plus grand nombre de planètes habitables
	 * 
	 * @return le nom de la galaxie
	 */
	public static String getBestGalaxy() {
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		final MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		Document where = new Document("status", "Exploration");
		where.append("habitable", true);
		Document orderBy = new Document("galaxyName", 1);
		FindIterable<Document> iterator = collection.find(where).sort(orderBy);
		final List<Long> counterList = new ArrayList<Long>();
		final List<String> bestGalaxyList = new ArrayList<String>();

		try {

			iterator.forEach(new Block<Document>() {
				@Override
				public void apply(final Document document) {
					long counter = 0;
					counter = collection.count(new Document("galaxyName", document.getString("galaxyName")));
					counterList.add(counter);
					for (int i = 1; i < counterList.size(); i++) {
						if (counterList.get(i) > counterList.get(i - 1)) {
							if (bestGalaxyList.size() > 0) {
								bestGalaxyList.set(0, document.getString("galaxyName"));
							} else {
								bestGalaxyList.add(0, document.getString("galaxyName"));
							}
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bestGalaxyList.get(0);
	}

	/**
	 * Permet de trouver une chemin pour se rendre d'une planète à une autre
	 * 
	 * @param fromPlanet
	 * @param toPlanet
	 * @return Liste du nom des planètes à parcourir, incluant "fromPlanet" et
	 *         "toPlanet", ou null si aucun chemin trouvé
	 */
	public static List<String> getTrajectory(String fromPlanet, String toPlanet) {
		// non fonctionnel :(
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		List <String> trajectoryList = new ArrayList<String>();
		Document where = new Document("planetName", fromPlanet);
		where.append("planets", toPlanet);
		
		if (collection.count(where)>0) {
			trajectoryList.add(fromPlanet);
			trajectoryList.add(toPlanet);
			
		}
		else {
			trajectoryList = null;
		}
		return trajectoryList;
	}

	/**
	 * La liste des galaxies ayant le plus de planètes explorées (en ordre
	 * décroissant)
	 * 
	 * @param limit Nombre à retourner
	 * @return List de nom des galaxies + le nombre de planètes visitées, par
	 *         exemple : Andromède (7 planètes visitées), ...
	 */
	public static List<String> getExploredGalaxies(int limit) {
		final List<String> galaxyList = new ArrayList<String>();
		MongoDatabase connectionMongo = MongoConnection.getConnection();
		final MongoCollection<Document> collection = connectionMongo.getCollection("logentry");
		// where statut est explo -> group by galaxies ->
		Document where = new Document("status", "Exploration");
		Document orderBy = new Document("galaxyName", 1);
		FindIterable<Document> iterator = collection.find(where).sort(orderBy);

		try {

			iterator.forEach(new Block<Document>() {
				@Override
				public void apply(final Document document) {
					long counter = 0;
					counter = collection.count(new Document("galaxyName", document.getString("galaxyName")));
					// un if qui peut limité à une présence dans la list...(je n'ait pas trouvé)
					if (!galaxyList.contains(document.getString("galaxyName"))) {
						String result = String.format(
								" " + document.getString("galaxyName") + " ( " + counter + " planètes visitées ) ");
						galaxyList.add(result + "");
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return galaxyList;
	}

	/**
	 * Suppression de toutes les données
	 */
	public static boolean deleteAll() {
		boolean success = false;
		Database connectionBK = BerkeleyConnection.getConnection();
		Cursor c = null;
		List<String> aDelete = new ArrayList<String>();
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
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			if (c != null) {
				c.close();
				// Berkeley suite
				for (String delKey : aDelete) {
					try {
						DatabaseEntry foundKey = new DatabaseEntry(delKey.getBytes("UTF-8"));
						connectionBK.delete(null, foundKey);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						success = false;
					}
				}
			}
			success = true;
		}

		return success;
	}

	public static byte[] getPlanetImage(String key) {
		byte[] image = null;
		Database connectionBK = BerkeleyConnection.getConnection();

		try {
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry();

			if (connectionBK.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				image = theData.getData();
			} else {
				System.out.println("Element inexistant");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
}
