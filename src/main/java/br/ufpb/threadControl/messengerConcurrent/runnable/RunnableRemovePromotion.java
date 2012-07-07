/**
 * 
 */
package br.ufpb.threadControl.messengerConcurrent.runnable;

import br.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;

/**
 * Runnable Remove Promotion
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableRemovePromotion implements Runnable {
	private IPromotionManager promotionManager;
	private Promotion promotion;

	public RunnableRemovePromotion(Promotion promotion, IPromotionManager iPromotionManager) {
		this.promotionManager = iPromotionManager;
		this.promotion = promotion;
	}

	@Override
	public void run() {
		try {
			promotionManager.removePromotion(promotion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
