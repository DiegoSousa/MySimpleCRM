/**
 * 
 */
package br.ufpb.threadControl.messengerConcurrent.runnable;

import br.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;

/**
 * Runnable adder Promotion
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableAddPromotion implements Runnable {
	private IPromotionManager iPromotionManager;
	private Promotion promotion;

	public RunnableAddPromotion(Promotion promotion,
			IPromotionManager iPromotionManager) {
		this.iPromotionManager = iPromotionManager;
		this.promotion = promotion;
	}

	@Override
	public void run() {
		this.iPromotionManager.addPromotion(promotion);
	}
}
