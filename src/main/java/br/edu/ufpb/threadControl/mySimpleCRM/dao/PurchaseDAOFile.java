package br.edu.ufpb.threadControl.mySimpleCRM.dao;

import java.util.List;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPurchaseManager;

/**
 * Implementation of Purchase DAO with List
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */
public class PurchaseDAOFile implements IPurchaseManager {

	@Override
	public Purchase addPurchase(Purchase purchase) {
		return null;
	}

	@Override
	public Purchase removePurchase(Purchase purchase) {
		return null;
	}

	@Override
	public List<Purchase> searchPurchaseByProduct(Product product) {
		return null;
	}

	@Override
	public List<Purchase> getListPurchase() {
		return null;
	}

	@Override
	public List<Purchase> searchPurchasesOfACustomer(Customer customer) {
		return null;
	}

	@Override
	public Purchase editPurchase(Purchase purchase) {
		return null;
	}

	

}
