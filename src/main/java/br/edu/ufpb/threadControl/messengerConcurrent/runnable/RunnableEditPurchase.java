package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Purchase;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPurchaseManager;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */
public class RunnableEditPurchase implements Runnable {

	private IPurchaseManager iPurchaseManager;
	private Purchase purchase;

	public RunnableEditPurchase(Purchase purchase,
			IPurchaseManager iPurchaseManager) {
		this.purchase = purchase;
		this.iPurchaseManager = iPurchaseManager;
	}

	@Override
	public void run() {
		iPurchaseManager.editPurchase(purchase);
	}
}
