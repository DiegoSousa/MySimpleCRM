/**
 * 
 */
package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;

/**
 * Runnable Edit Promotion
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableEditPromotion implements Runnable {
	private IPromotionManager ipromotionManager;
	private Promotion promotion;

	public RunnableEditPromotion(Promotion promotion, IPromotionManager iPromotionManager) {
		this.ipromotionManager = iPromotionManager;
		this.promotion = promotion;
	}

	@Override
	public void run() {
		try {
			this.ipromotionManager.editPromotion(promotion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}