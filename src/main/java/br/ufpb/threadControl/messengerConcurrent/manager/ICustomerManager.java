package br.ufpb.threadControl.messengerConcurrent.manager;

import java.util.List;

import br.ufpb.threadControl.messengerConcurrent.entity.Customer;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */
public interface ICustomerManager {

	/**
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer
	 */

	public abstract Customer addCustomer(Customer customer);

	/**
	 * 
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer
	 */

	public abstract Customer removeCustomer(Customer customer);

	/**
	 * @param cpf
	 * @throws Exception
	 * @return Object customer
	 * 
	 */
	public abstract Customer restoreCustomer(String cpf);

	/**
	 * 
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer
	 */

	public abstract Customer editCustomer(Customer customer);

	/**
	 * 
	 * @param id
	 *            belonging to the customer that will be searched
	 * 
	 * @return Object customer
	 */

	public abstract Customer searchCustomerById(long id);

	/**
	 * 
	 * @param login
	 *            belonging to the customer that will be searched
	 * 
	 * @return Object customer
	 */

	public abstract Customer searchCustomerByLogin(String login);

	/**
	 * @param cpf
	 * @return
	 * 
	 */
	public abstract Customer searchCustomerByCpf(String cpf);

	/**
	 * 
	 * @return List of all customer
	 */

	public abstract List<Customer> getListOfCustomers();

}