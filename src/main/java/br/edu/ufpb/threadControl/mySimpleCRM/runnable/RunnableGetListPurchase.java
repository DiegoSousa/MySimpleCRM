/**
 * 
 */
package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPurchaseManager;

/**
 * Runnable Get Historical Of Products Purchased Of All Customers.
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableGetListPurchase implements Runnable {

	private BlockingQueue<List<Purchase>> list;
	private IPurchaseManager purchaseCrud;

	public RunnableGetListPurchase(BlockingQueue<List<Purchase>> list, IPurchaseManager iPurchaseManager) {
		this.purchaseCrud = iPurchaseManager;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			list.put(purchaseCrud.getListPurchase());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}