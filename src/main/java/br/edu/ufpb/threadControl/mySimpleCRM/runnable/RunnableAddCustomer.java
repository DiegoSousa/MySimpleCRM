/**
 * 
 */
package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.ICustomerManager;

/**
 * Runnable adder client
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableAddCustomer implements Runnable {

	private ICustomerManager iCustomerManager;
	private Customer customer;

	public RunnableAddCustomer(Customer customer, ICustomerManager iCustomerManager) {
		this.iCustomerManager = iCustomerManager;
		this.customer = customer;
	}

	@Override
	public void run() {
		try {
			this.iCustomerManager.addCustomer(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}