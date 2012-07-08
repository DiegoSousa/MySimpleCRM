/**
 * 
 */
package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;

/**
 * Runnable Remove customer
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 
 * 
 * Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableRemoveCustomer implements Runnable {
	private ICustomerManager iCustomerManager;
	private Customer customer;

	public RunnableRemoveCustomer(Customer customer, ICustomerManager iCustomerManager) {
		this.iCustomerManager = iCustomerManager;
		this.customer = customer;
	}

	@Override
	public void run() {
		try {
			iCustomerManager.removeCustomer(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
