package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPurchaseManager;

/**
 * Runnable Get Historical Of Products Purchased Of Customers.
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableSearchPurchasesOfACustomer implements Runnable {

	private IPurchaseManager iPurchaseManager;
	private BlockingQueue<List<Purchase>> list;
	private Customer customer;

	public RunnableSearchPurchasesOfACustomer(Customer customer,
			BlockingQueue<List<Purchase>> list, IPurchaseManager iPurchaseManager) {
		this.iPurchaseManager = iPurchaseManager;
		this.customer = customer;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			list.put(iPurchaseManager.searchPurchasesOfACustomer(customer));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
