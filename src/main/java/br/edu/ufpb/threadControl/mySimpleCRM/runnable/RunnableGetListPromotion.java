/**
 * 
 */
package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPromotionManager;

/**
 * Runnable Get List Promotion
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableGetListPromotion implements Runnable {
	private IPromotionManager iPromotionManager;
	private BlockingQueue<List<Promotion>> list;

	public RunnableGetListPromotion(BlockingQueue<List<Promotion>> listPromotion, IPromotionManager iPromotionManager) {
		this.iPromotionManager = iPromotionManager;
		this.list = listPromotion;
	}

	@Override
	public void run() {
		try {
			list.put(iPromotionManager.getListPromotion());			
		} catch (Exception e) {
			e.getMessage();
		}
	}
}