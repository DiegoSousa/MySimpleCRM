package br.edu.ufpb.threadControl.messengerConcurrent.manager;

import java.util.List;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion;

/**
 * Interface Promotion Manager
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 
 * 
 * Copyright (C) 2012 Diego Sousa de Azevedo
 */

public interface IPromotionManager {

	/**
	 * @param promotion
	 * @return
	 */
	Promotion addPromotion(Promotion promotion);

	/**
	 * @param promotion
	 * @return
	 */
	Promotion removePromotion(Promotion promotion);

	/**
	 * @param namePromotion
	 * @return
	 */
	Promotion restorePromotion(String namePromotion);

	/**
	 * @param promotion
	 * @return
	 */
	Promotion editPromotion(Promotion promotion);

	/**
	 * @param id
	 * @return
	 */
	Promotion searchPromotionById(long id);

	/**
	 * @param name
	 * @return
	 */
	Promotion searchPromotionByName(String name);

	/**
	 * @param product
	 * @return
	 */
	List<Promotion> searchPromotionByProduct(Product product);

	/**
	 * @return
	 */
	List<Promotion> getListPromotion();


}