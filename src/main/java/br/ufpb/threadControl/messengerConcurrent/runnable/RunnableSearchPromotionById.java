package br.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.concurrent.BlockingQueue;

import br.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 *
 * Copyright (C) 2012 
 */
public class RunnableSearchPromotionById implements Runnable{
	
	private long id;
	private IPromotionManager iPromotionManager;	
	private BlockingQueue<Promotion> listPromotion;
	
	public RunnableSearchPromotionById(long id, BlockingQueue<Promotion> listPromotion, IPromotionManager iPromotionManager){		
		this.id=id;		
		this.listPromotion = listPromotion;
		this.iPromotionManager = iPromotionManager;		
	}
			
	public void run(){
		
	try {
		listPromotion.put(iPromotionManager.searchPromotionById(id));
		} catch (InterruptedException e) {
		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
		
		
	}

}
