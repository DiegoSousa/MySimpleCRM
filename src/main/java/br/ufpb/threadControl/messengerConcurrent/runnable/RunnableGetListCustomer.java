package br.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;

/**
 * Runnable Get List Of customer.
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableGetListCustomer implements Runnable {
	private ICustomerManager customerCrud;
	private BlockingQueue<List<Customer>> list;

	public RunnableGetListCustomer(BlockingQueue<List<Customer>> listCustomer, ICustomerManager iCustomerManager) {
		this.customerCrud = iCustomerManager;
		this.list = listCustomer;
	}

	@Override
	public void run() {
		try {
			list.put(this.customerCrud.getListOfCustomers());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}