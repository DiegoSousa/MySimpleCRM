/**
 * 
 */
package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.ICustomerManager;

/**
 * Runnable Edit customer
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableEditCustomer implements Runnable {
	private ICustomerManager customerManager;
	private Customer customer;

	public RunnableEditCustomer(Customer customer, ICustomerManager iCustomerManager) {
		this.customerManager = iCustomerManager;
		this.customer = customer;
	}

	@Override
	public void run() {
		try {
			this.customerManager.editCustomer(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}