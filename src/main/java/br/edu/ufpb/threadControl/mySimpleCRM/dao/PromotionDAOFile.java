package br.edu.ufpb.threadControl.mySimpleCRM.dao;

import java.util.List;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPromotionManager;

/**
 * Implementation of Promotion DAO with List
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class PromotionDAOFile implements IPromotionManager {

	@Override
	public Promotion addPromotion(Promotion promotion) {
		return null;
	}

	@Override
	public Promotion removePromotion(Promotion promotion) {
		return null;
	}

	@Override
	public Promotion restorePromotion(String namePromotion) {
		return null;
	}

	@Override
	public Promotion editPromotion(Promotion promotion) {
		return null;
	}

	@Override
	public Promotion searchPromotionById(long id) {
		return null;
	}

	@Override
	public Promotion searchPromotionByName(String name) {
		return null;
	}

	@Override
	public List<Promotion> searchPromotionByProduct(Product product) {
		return null;
	}

	@Override
	public List<Promotion> getListPromotion() {
		return null;
	}

	



}
