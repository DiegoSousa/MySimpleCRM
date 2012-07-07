package br.ufpb.threadControl.messengerConcurrent.manager;

import java.util.List;

import br.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.ufpb.threadControl.messengerConcurrent.entity.Purchase;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */
public interface IPurchaseManager {

	/**
	 * @param purchase
	 * @return
	 */
	public abstract Purchase addPurchase(Purchase purchase);

	/**
	 * @param purchase
	 * @return
	 */
	public abstract Purchase removePurchase(Purchase purchase);

	/**
	 * @param purchase
	 * @return
	 */
	public abstract Purchase editPurchase(Purchase purchase);

	/**
	 * @param product
	 * @return
	 */

	public abstract List<Purchase> searchPurchaseByProduct(Product product);

	/**
	 * @param customer
	 * @return
	 */
	public abstract List<Purchase> searchPurchasesOfACustomer(Customer customer);

	/**
	 * @return
	 */
	public abstract List<Purchase> getListPurchase();

}