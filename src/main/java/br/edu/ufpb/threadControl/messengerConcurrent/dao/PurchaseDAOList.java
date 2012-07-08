package br.edu.ufpb.threadControl.messengerConcurrent.dao;

import java.util.List;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Purchase;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPurchaseManager;

/**
 * Implementation of Purchase DAO with List
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */
public class PurchaseDAOList implements IPurchaseManager {

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
