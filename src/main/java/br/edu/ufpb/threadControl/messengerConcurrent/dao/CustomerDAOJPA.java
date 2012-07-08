package br.edu.ufpb.threadControl.messengerConcurrent.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;
import br.edu.ufpb.threadControl.messengerConcurrent.util.HibernateUtil;

/**
 * Implementation of Customer DAO with JPA
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class CustomerDAOJPA implements ICustomerManager {

	private Logger logger;
	private EntityManager entityManager;

	public CustomerDAOJPA() {
		this.entityManager = HibernateUtil.getInstance().getFactory()
				.createEntityManager();
		this.logger = Logger.getLogger(CustomerDAOJPA.class.getName());
	}

	/**
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer
	 */

	@Override
	public synchronized Customer addCustomer(Customer customer) {
		try {
			Customer customerAux = searchCustomerByCpf(customer.getCpf());
			if (customerAux != null) {
				editCustomer(customer);
				restoreCustomer(customer.getCpf());
				return customer;
			}
			customer.setName(customer.getName().toUpperCase());
			entityManager.getTransaction().begin();
			entityManager.persist(customer);
			entityManager.getTransaction().commit();
			logger.log(Level.INFO, "customer: " + customer.getName()
					+ " successfully added!");
			return customer;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error creating customer: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * 
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer
	 */

	@Override
	public synchronized Customer removeCustomer(Customer customer) {

		try {
			entityManager.getTransaction().begin();
			Customer customerAux = entityManager.find(Customer.class,
					customer.getId());
			customerAux.setIsActive(false);
			entityManager.merge(customerAux);
			entityManager.getTransaction().commit();
			logger.log(Level.INFO, customer.getLogin()
					+ " successfully removed!");
			return customer;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error removing customer: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Customer restoreCustomer(String cpf) {
		Customer customerAux = searchCustomerDetachedByCpf(cpf);
		try {
			if (customerAux != null && customerAux.getIsActive() == false) {
				customerAux.setIsActive(true);
				entityManager.getTransaction().begin();
				entityManager.merge(customerAux);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "customer: " + customerAux.getName()
						+ " successfully activaded!");
				return customerAux;
			}
			logger.log(Level.SEVERE,
					"Customer not Found or Customer already active!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error restoring Customer");
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * 
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer
	 */

	@Override
	public synchronized Customer editCustomer(Customer customer) {

		/**
		 * # The searchCustomerByLogin is used to pass the object state detached
		 * to state Manager. # The merge is used to edit the object.
		 */

		try {
			entityManager.getTransaction().begin();
			Customer customerAux = searchCustomerByCpf(customer.getCpf());
			customerAux.setName(customer.getName().toUpperCase());
			customerAux.setCpf(customer.getCpf());
			customerAux.setPhone(customer.getPhone());
			customerAux.setLogin(customer.getLogin());
			customerAux.setPassword(customer.getPassword());
			customerAux.setBirthday(customer.getBirthday());
			entityManager.merge(customerAux);
			entityManager.getTransaction().commit();
			logger.log(Level.INFO, "customer: " + customer.getName()
					+ " edited!");
			return customer;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error editing customer: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	private synchronized Customer searchCustomerDetachedByCpf(String cpf) {
		try {
			Customer customer = (Customer) entityManager
					.createQuery(
							"select c from Customer c where c.cpf = :cpf and c.isActive=false")
					.setParameter("cpf", cpf).getSingleResult();
			logger.log(Level.INFO, "Customer: " + customer.getName()
					+ " found!");
			return customer;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching customer, probably the customer does not exist: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * 
	 * @param id
	 *            belonging to the customer that will be searched
	 * 
	 * @return Object customer
	 */

	@Override
	public synchronized Customer searchCustomerById(long id) {
		try {
			Customer customer = entityManager.find(Customer.class, id);
			if (customer.getIsActive() == true) {
				logger.log(Level.INFO, "customer: " + customer.getName()
						+ " found!");
				return customer;
			}
			logger.log(Level.SEVERE, "customer: " + customer.getName()
					+ " not found!");
			return null;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching customer, probably the Customer does not exist:  ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * 
	 * @param login
	 *            belonging to the customer that will be searched
	 * 
	 * @return Object customer
	 */

	@Override
	public synchronized Customer searchCustomerByLogin(String login) {
		try {
			Customer customer = (Customer) entityManager
					.createQuery(
							"select c from Customer c where c.login = :login and c.isActive=true")
					.setParameter("login", login).getSingleResult();
			logger.log(Level.INFO, "customer: " + customer.getName()
					+ " found!");
			return customer;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching customer, probably the Customer does not exist:  ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Customer searchCustomerByCpf(String cpf) {
		try {
			Customer customer = (Customer) entityManager
					.createQuery(
							"select c from Customer c where c.cpf = :cpf and c.isActive=true")
					.setParameter("cpf", cpf).getSingleResult();
			logger.log(Level.INFO, "customer: " + customer.getName()
					+ " found!");
			return customer;
		} catch (Exception exception) {
			logger.log(Level.WARNING,
					"Error: If you are trying to add a customer, "
							+ "this may just be a routine operation, "
							+ "which should not worry.\nIf you are not "
							+ "adding a customer Probably the Customer "
							+ "does not exist.  ", exception.getMessage());
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * 
	 * @return List of all customer
	 */

	@Override
	@SuppressWarnings("unchecked")
	public synchronized List<Customer> getListOfCustomers() {
		try {
			List<Customer> list = entityManager
					.createQuery(
							"select c from Customer c where c.isActive=true order by c.name")
					.getResultList();
			logger.log(Level.INFO, "List of customer found!");
			return list;
		} catch (Exception exception) {
			logger.log(Level.SEVERE,
					"Error listing customer, probably the list is empty: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}
}
