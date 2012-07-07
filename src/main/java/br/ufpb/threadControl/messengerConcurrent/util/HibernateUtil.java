package br.ufpb.threadControl.messengerConcurrent.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class responsible for operations with the database. Example: Create Database and
 * liberate access to EntityManagerFactory
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 1.0 
 * @since 8/04/2012 Copyright (C) 2012 Diego Sousa de Azevedo
 * 
 */

public class HibernateUtil {

	private static HibernateUtil util;
	private static EntityManagerFactory emf;

	/**
	 * Creates the connection with the bank.
	 */

	private HibernateUtil() {
		emf = Persistence.createEntityManagerFactory("messengerConcurrentPU");
	}

	/**
	 * Singleton.
	 * 
	 * @return Instance of HibernateUtil
	 */

	public static HibernateUtil getInstance() {
		if (util == null) {
			util = new HibernateUtil();
		}
		return util;
	}

	/**
	 * @return Instance of HibernateUtil,
	 */

	public EntityManagerFactory getFactory() {
		return emf;
	}
}
