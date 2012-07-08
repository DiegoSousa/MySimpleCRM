package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;

/**
 * Runnable Search Promotion By Name
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableSearchPromotionByName implements Runnable {

	private BlockingQueue<Promotion> list;
	private IPromotionManager iPromotionManager;
	private String name;

	public RunnableSearchPromotionByName(String name,
			BlockingQueue<Promotion> list,
			IPromotionManager iPromotionManager) {
		this.name = name;
		this.list = list;
		this.iPromotionManager = iPromotionManager;
	}

	@Override
	public void run() {

		try {
			list.put(iPromotionManager.searchPromotionByName(name));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
