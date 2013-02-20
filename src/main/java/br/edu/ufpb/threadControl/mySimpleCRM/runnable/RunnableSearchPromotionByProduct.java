package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPromotionManager;

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
