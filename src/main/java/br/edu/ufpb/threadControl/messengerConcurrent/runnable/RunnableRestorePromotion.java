package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;

/**
 * Runnable Restore Promotion
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */


public class RunnableRestorePromotion implements Runnable {

	private String namePromotion;
	private IPromotionManager iPromotionManager;

	public RunnableRestorePromotion(String namePromotion,
			IPromotionManager iPromotionManager) {
		this.namePromotion = namePromotion;
		this.iPromotionManager = iPromotionManager;
	}

	@Override
	public void run() {
		iPromotionManager.restorePromotion(namePromotion);
	}

}
