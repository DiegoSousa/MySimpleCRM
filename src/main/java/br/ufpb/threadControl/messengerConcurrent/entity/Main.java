package br.ufpb.threadControl.messengerConcurrent.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufpb.threadControl.messengerConcurrent.dao.CustomerDAOJPA;
import br.ufpb.threadControl.messengerConcurrent.dao.ProductDAOJPA;
import br.ufpb.threadControl.messengerConcurrent.dao.PurchaseDAOJPA;
import br.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;
import br.ufpb.threadControl.messengerConcurrent.manager.IProductManager;
import br.ufpb.threadControl.messengerConcurrent.manager.IPurchaseManager;
import br.ufpb.threadControl.messengerConcurrent.util.HibernateUtil;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */
public class Main {

	public static void main(String... args) throws Exception {

		HibernateUtil.getInstance().getFactory().createEntityManager();

		IProductManager a = new ProductDAOJPA();
		IPurchaseManager b = new PurchaseDAOJPA();
		ICustomerManager c = new CustomerDAOJPA();
		
		 Product product = new Product("Iphone", 1.500, 100);
		 a.addProduct(product);
		
		 Map<Product, Integer> map = new HashMap<Product, Integer>();
		
		 map.put(product, 2);
		
		 Customer customer = new Customer("Diego", "01234", "12345678",
		 "jenwfjn", "wefiwe", 1, 1, 1);
		
		 c.addCustomer(customer);
		
		 Purchase purchase = new Purchase(customer, map, null);
		
		 b.addPurchase(purchase);

		List<Purchase> list = b.getListPurchase();		

		// List<Purchase> list = b.searchPurchaseByProduct(product);
		Set<Product> d = list.get(0).getListProducts().keySet();
		
		System.out.println(list.get(0).getFinalPrice() + "Final Price ");
		System.out.println(list.get(0).getCustomer() + "Customer");
		System.out.println(list.get(0).getListPromotions() + "Promotions");
		System.out.println(list.get(0).getId() + "id");		
		
		for (Product e : d) {
			System.out.println("Quantidade: "
					+ list.get(0).getListProducts().get(e));
			System.out.println(e);
		}
	}

}
