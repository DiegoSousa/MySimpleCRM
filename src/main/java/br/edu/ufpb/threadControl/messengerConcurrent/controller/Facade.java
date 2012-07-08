package br.edu.ufpb.threadControl.messengerConcurrent.controller;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Purchase;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IProductManager;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPurchaseManager;
import br.edu.ufpb.threadControl.messengerConcurrent.managerFactory.ManagerDAOFactory;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddCustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddPromotion;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddPurchase;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableEditCustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableEditProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableEditPromotion;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableEditPurchase;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableGetListCustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableGetListProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableGetListPromotion;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableGetListPurchase;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableRemoveCustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableRemoveProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableRemovePromotion;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableRemovePurchase;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableRestoreCustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableRestoreProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableRestorePromotion;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchCustomerByCpf;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchCustomerById;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchCustomerByLogin;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchProductById;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchProductByName;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchPromotionById;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchPromotionByName;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchPromotionByProduct;

/**
 * Facade
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo.
 */

public class Facade {

	public static Facade facade;
	private ExecutorService executor;
	private static ManagerDAOFactory iManagerFactory;
	private ICustomerManager iCustomerManager;
	private IProductManager iProductManager;
	private IPromotionManager iPromotionManager;
	private IPurchaseManager iPurchaseManager;

	private Facade() {
		this.executor = Executors.newFixedThreadPool(10);
		iCustomerManager = iManagerFactory.getCustomerDao();
		iProductManager = iManagerFactory.getProductDao();
		iPromotionManager = iManagerFactory.getPromotionDao();
		iPurchaseManager = iManagerFactory.getPurchaseDao();
	}

	public synchronized static Facade getInstance(
			ManagerDAOFactory managerDAOFactory) {
		if (facade == null) {
			iManagerFactory = managerDAOFactory;
			facade = new Facade();
		}
		return facade;
	}

	// -------------------------------------------------------------------------
	// methods concurrent CRUD customers

	public void addCustomer(Customer customer) {
		executor.execute(new RunnableAddCustomer(customer, iCustomerManager));
	}

	public void removeCustomer(Customer customer) {
		executor.execute(new RunnableRemoveCustomer(customer, iCustomerManager));
	}

	public void restoreCustomer(String cpf) {
		executor.execute(new RunnableRestoreCustomer(cpf, iCustomerManager));
	}

	public void editCustomer(Customer customer) {
		executor.execute(new RunnableEditCustomer(customer, iCustomerManager));
	}

	public void searchCustomerById(long id, BlockingQueue<Customer> list) {
		executor.execute(new RunnableSearchCustomerById(id, list,
				iCustomerManager));
	}

	public void searchCustomerByLogin(String login, BlockingQueue<Customer> list) {
		executor.execute(new RunnableSearchCustomerByLogin(login, list,
				iCustomerManager));
	}

	public void searchCustomerByCpf(String cpf, BlockingQueue<Customer> list) {
		executor.execute(new RunnableSearchCustomerByCpf(cpf, list,
				iCustomerManager));
	}

	public void getListOfCustomer(BlockingQueue<List<Customer>> list) {
		executor.execute(new RunnableGetListCustomer(list, iCustomerManager));
	}

	// -------------------------------------------------------------------------
	// methods concurrent CRUD Products

	public void addProduct(Product product) {
		executor.execute(new RunnableAddProduct(product, iProductManager));
	}

	public void removeProduct(Product product) {
		executor.execute(new RunnableRemoveProduct(product, iProductManager));
	}

	public void restoreProduct(String nameProduct) {
		executor.execute(new RunnableRestoreProduct(nameProduct,
				iProductManager));
	}

	public void editProduct(Product product) {
		executor.execute(new RunnableEditProduct(product, iProductManager));
	}

	public void searchProductById(long id, BlockingQueue<Product> list) {
		executor.execute(new RunnableSearchProductById(id, list,
				iProductManager));
	}

	public void searchProductByName(String name, BlockingQueue<Product> list) {
		executor.execute(new RunnableSearchProductByName(name, list,
				iProductManager));
	}

	public void getListOfProduct(BlockingQueue<List<Product>> listProduct) {
		executor.execute(new RunnableGetListProduct(listProduct,
				iProductManager));
	}

	// -------------------------------------------------------------------------
	// methods concurrent CRUD Promotion

	public void addPromotion(Promotion promotion) {
		executor.execute(new RunnableAddPromotion(promotion, iPromotionManager));
	}

	public void removePromotion(Promotion promotion) {
		executor.execute(new RunnableRemovePromotion(promotion,
				iPromotionManager));
	}

	public void restorePromotion(String namePromotion) {
		executor.execute(new RunnableRestorePromotion(namePromotion,
				iPromotionManager));
	}

	public void editPromotion(Promotion promotion) {
		executor.execute(new RunnableEditPromotion(promotion, iPromotionManager));
	}

	public void searchPromotionById(long id,
			BlockingQueue<Promotion> listPromotion) {
		executor.execute(new RunnableSearchPromotionById(id, listPromotion,
				iPromotionManager));
	}

	public void searchPromotionByName(String namePromotion,
			BlockingQueue<Promotion> list) {
		executor.execute(new RunnableSearchPromotionByName(namePromotion, list,
				iPromotionManager));
	}

	public void searchPromotionByProduct(Product product,
			BlockingQueue<List<Promotion>> list) {
		executor.execute(new RunnableSearchPromotionByProduct(product, list,
				iPromotionManager));
	}

	public void getListOfPromotion(BlockingQueue<List<Promotion>> listPromotion) {
		executor.execute(new RunnableGetListPromotion(listPromotion,
				iPromotionManager));
	}

	// -------------------------------------------------------------------------
	// methods concurrent of ManagerPurchasesOfProducts

	public void addPurchase(Purchase purchase) {
		executor.execute(new RunnableAddPurchase(purchase, iPurchaseManager));
	}

	public void removePurchase(Purchase purchase) {
		executor.execute(new RunnableRemovePurchase(purchase, iPurchaseManager));
	}
	
	public void editPurchase(Purchase purchase) {
		executor.execute(new RunnableEditPurchase(purchase, iPurchaseManager));
	}	
	
	public void getListOfPurchase(BlockingQueue<List<Purchase>> list) {
		executor.execute(new RunnableGetListPurchase(list, iPurchaseManager));
	}


}