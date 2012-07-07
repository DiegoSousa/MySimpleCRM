/**
 * 
 */
package br.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.concurrent.BlockingQueue;

import br.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;

/**
 * Runnable Search customer
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableSearchCustomerById implements Runnable {
	private ICustomerManager customerCrud;
	private BlockingQueue<Customer> list;
	private Long id;

	public RunnableSearchCustomerById(Long id, BlockingQueue<Customer> list, ICustomerManager iCustomerManager) {
		this.customerCrud = iCustomerManager;
		this.list = list;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			list.put(customerCrud.searchCustomerById(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
