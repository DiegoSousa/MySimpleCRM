/**
 * 
 */
package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPurchaseManager;

/**
 * Runnable Product Purchase.
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableAddPurchase implements Runnable {

	private IPurchaseManager iPurchaseManager;
	private Purchase purchase;

	public RunnableAddPurchase(Purchase purchase, IPurchaseManager iPurchaseManager) {
		this.iPurchaseManager = iPurchaseManager;
		this.purchase = purchase;
	}

	public void run() {
		try {
			iPurchaseManager.addPurchase(purchase);
		} catch (Exception e) {
			System.out.println("Aquiii");
			e.printStackTrace();
			
		}
	}

}