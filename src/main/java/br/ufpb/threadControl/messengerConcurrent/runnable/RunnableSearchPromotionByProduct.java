package br.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;

/**
 * Runnable Search Promotion By Product
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableSearchPromotionByProduct implements Runnable {

	private IPromotionManager iPromotionManager;
	private Product product;
	private BlockingQueue<List<Promotion>> list;

	public RunnableSearchPromotionByProduct(Product product,
			BlockingQueue<List<Promotion>> list,
			IPromotionManager iPromotionManager) {
		this.product = product;
		this.iPromotionManager = iPromotionManager;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			list.put(iPromotionManager.searchPromotionByProduct(product));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
