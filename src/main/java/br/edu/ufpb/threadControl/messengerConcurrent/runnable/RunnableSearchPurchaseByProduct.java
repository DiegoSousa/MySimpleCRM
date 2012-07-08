package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
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
public class RunnableSearchPurchaseByProduct implements Runnable {

	private Product product;
	private BlockingQueue<List<Purchase>> list;
	private IPurchaseManager iPurchaseManager;
	
	
	public RunnableSearchPurchaseByProduct(Product product, BlockingQueue<List<Purchase>> list, IPurchaseManager iPurchaseManager){
		this.product = product;
		this.list = list;
		this.iPurchaseManager = iPurchaseManager;
	}
	
	@Override
	public void run() {
		try {
			list.put(iPurchaseManager.searchPurchaseByProduct(product));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
