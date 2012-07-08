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
 * Copyright (C) 2012 
 */
public class RunnableRemovePurchase implements Runnable {

	private Purchase purchase;	
	private IPurchaseManager purchaseCrud;
	
	public RunnableRemovePurchase(Purchase purchase, IPurchaseManager iPurchaseManager){
		purchaseCrud = iPurchaseManager;
		this.purchase = purchase;
	}
	
	
	@Override
	public void run() {
		
		try {
			purchaseCrud.removePurchase(purchase);
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
}
