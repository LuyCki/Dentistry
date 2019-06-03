package util;

import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.User;


public class DatabaseUtil {
	
	private static DatabaseUtil db = new DatabaseUtil();
	
	public static DatabaseUtil getDb() {
		return db;
	}

	public static void setDb(DatabaseUtil db) {
		DatabaseUtil.db = db;
	}

	/**
	 * member for connection database
	 */
	public static EntityManagerFactory entityManagerFactory;
	
	/**
	 * operations in database
	 */
	public static EntityManager entityManager;
	private static DatabaseUtil instance;
	
	public static DatabaseUtil getInstance() {
		if (instance == null) {
			instance = new DatabaseUtil();
			instance.setUp();
		}
		
		return instance;
	}
	
	/**
	 *  connection database
	 */
	public void setUp(){
		try {
		entityManagerFactory = Persistence.createEntityManagerFactory("Dentistry");
		entityManager = entityManagerFactory.createEntityManager();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * methods for process the transactions in database
	 */

	public void startTransaction() {
		entityManager.getTransaction().begin();
	}
	public void commitTransaction() {
		entityManager.getTransaction().commit();
	}
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void closeEntityManager() {
		entityManager.close();
	}
	
	public void executeTransaction(Consumer<EntityManager> action) {
		try {
			startTransaction();
			action.accept(entityManager);
			commitTransaction();
		} catch (RuntimeException e) {
			System.err.println("Transaction error: " + e.getLocalizedMessage());
			closeEntityManager();
		}
	}
}
