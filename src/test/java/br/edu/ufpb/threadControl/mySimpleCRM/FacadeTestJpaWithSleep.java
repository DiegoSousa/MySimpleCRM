package br.edu.ufpb.threadControl.mySimpleCRM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase;
import br.edu.ufpb.threadControl.mySimpleCRM.managerFactory.ManagerDAOFactoryJPA;
import br.edu.ufpb.threadControl.mySimpleCRM.util.HibernateUtil;

/**
 * Class used to test the facade, which contains all the application methods.
 * Note: This class uses the framework JPA / Hibernate for persistence
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class FacadeTestJpaWithSleep {

	private static Facade facade;
	private static EntityManager entityManager;

	public BlockingQueue<List<Customer>> copyListOfAllCustomer;
	public BlockingQueue<List<Product>> copyListOfAllProduct;
	public BlockingQueue<List<Promotion>> copyListOfAllPromotion;
	public BlockingQueue<List<Purchase>> copyListOfAllPurchase;

	public BlockingQueue<Customer> takerClientList;
	public BlockingQueue<Product> takerProductList;
	public BlockingQueue<Promotion> takerPromotionList;
	public BlockingQueue<Purchase> takerPurchaseList;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		entityManager = HibernateUtil.getInstance().getFactory()
				.createEntityManager();
		System.out.println("Starting the test facade class...");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Finished the test facade class!");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		/**
		 * Erases data at the end of each test
		 */
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("delete from purchased_promotions;")
				.executeUpdate();
		entityManager.createNativeQuery("delete from purchased_products;")
				.executeUpdate();
		entityManager.createNativeQuery("delete from purchases;")
				.executeUpdate();
		entityManager.createNativeQuery("delete from products_in_promotion;")
				.executeUpdate();
		entityManager.createNativeQuery("delete from promotions;")
				.executeUpdate();
		entityManager.createNativeQuery("delete from products;")
				.executeUpdate();
		entityManager.createNativeQuery("delete from customers;")
				.executeUpdate();
		entityManager.getTransaction().commit();

		/*---------------------------------------------------------------------------------*/

		facade = Facade.getInstance(new ManagerDAOFactoryJPA());
		copyListOfAllCustomer = new LinkedBlockingQueue<List<Customer>>();
		copyListOfAllProduct = new LinkedBlockingQueue<List<Product>>();
		copyListOfAllPromotion = new LinkedBlockingQueue<List<Promotion>>();
		copyListOfAllPurchase = new LinkedBlockingQueue<List<Purchase>>();

		takerClientList = new LinkedBlockingQueue<Customer>();
		takerProductList = new LinkedBlockingQueue<Product>();
		takerPromotionList = new LinkedBlockingQueue<Promotion>();
		takerPurchaseList = new LinkedBlockingQueue<Purchase>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#addCustomer(br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer)}
	 * .
	 */
	@Test
	public void testAddCustomer() {

		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);

		// Add Customer
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, currentObjectsList.size());
		assertTrue(currentObjectsList.contains(customer1));
		assertTrue(currentObjectsList.contains(customer2));
		assertTrue(currentObjectsList.contains(customer3));

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("diego.sousa@dce.ufpb.br", customerAuxOne.getLogin());
		assertEquals("S3cr3t", customerAuxOne.getPassword());

		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#removeCustomer(br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer)}
	 * .
	 */

	@Test
	public void testRemoveCustomer() {
		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);

		// AddCustomer
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, currentObjectsList.size());
		assertTrue(currentObjectsList.contains(customer1));
		assertTrue(currentObjectsList.contains(customer2));
		assertTrue(currentObjectsList.contains(customer3));

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());

		// Removing Customer
		facade.removeCustomer(customerAuxOne);
		facade.removeCustomer(customerAuxTwo);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		copyListOfAllCustomer.clear();
		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(1, currentObjectsList.size());
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#restoreCustomer(java.lang.String)}
	 * .
	 */
	@Test
	public void testRestoreCustomer() {
		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);

		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, currentObjectsList.size());
		assertTrue(currentObjectsList.contains(customer1));
		assertTrue(currentObjectsList.contains(customer2));
		assertTrue(currentObjectsList.contains(customer3));

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());

		// removing the list of customer
		facade.removeCustomer(customerAuxOne);
		facade.removeCustomer(customerAuxTwo);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		copyListOfAllCustomer.clear();

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(currentObjectsList.contains(customerAuxOne));
		assertFalse(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(1, currentObjectsList.size());

		// Restoring Customer
		facade.restoreCustomer("07278910112");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		copyListOfAllCustomer.clear();
		// getting the list of customer

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertFalse(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(2, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#editCustomer(br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer)}
	 * .
	 */

	@Test
	public void testEditCustomer() {
		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);

		// Add customer
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, currentObjectsList.size());
		assertTrue(currentObjectsList.contains(customer1));
		assertTrue(currentObjectsList.contains(customer2));
		assertTrue(currentObjectsList.contains(customer3));

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());

		// Editing customer
		customerAuxOne.setName("Ewerton");
		customerAuxTwo.setCpf("07278910115");
		customerAuxThree.setPhone("3422-1013");

		facade.editCustomer(customerAuxOne);
		facade.editCustomer(customerAuxTwo);
		facade.editCustomer(customerAuxThree);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910115", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("Ewerton".toUpperCase(), customerAuxOne.getName());
		assertEquals("07278910115", customerAuxTwo.getCpf());
		assertEquals("3422-1013", customerAuxThree.getPhone());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchCustomerById(java.lang.Long, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchCustomerById() {
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);

		// add customer
		facade.addCustomer(customer1);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());
		assertEquals("diego.sousa@dce.ufpb.br", customerAuxOne.getLogin());

		// getting customer
		facade.searchCustomerById(customerAuxOne.getId(), takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxTwo.getPassword());
		assertEquals("3422-1010", customerAuxTwo.getPhone());
		assertEquals("diego.sousa@dce.ufpb.br", customerAuxTwo.getLogin());
		assertEquals("Diego".toUpperCase(), customerAuxTwo.getName());
		assertEquals("07278910112", customerAuxTwo.getCpf());
		assertFalse(customerAuxTwo.getLogin() == "diego@diegosousa.com");

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchCustomerByLogin(java.lang.String, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchCustomerByLogin() {

		Customer customerAuxOne = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		
		// add Customer
		facade.addCustomer(customer1);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting customer
		facade.searchCustomerByLogin("diego.sousa@dce.ufpb.br", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());
		assertEquals("diego.sousa@dce.ufpb.br", customerAuxOne.getLogin());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchCustomerByCpf(java.lang.String, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchCustomerByCpf() {

		Customer customerAuxOne = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);

		// add Customer
		facade.addCustomer(customer1);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());
		assertEquals("diego.sousa@dce.ufpb.br", customerAuxOne.getLogin());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#getListOfCustomer(java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testGetListOfCustomer() {

		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;
		Customer customerAuxFour = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		
		// Add Customer
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, currentObjectsList.size());
		assertTrue(currentObjectsList.contains(customer1));
		assertTrue(currentObjectsList.contains(customer2));
		assertTrue(currentObjectsList.contains(customer3));

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertFalse(currentObjectsList.contains(customerAuxFour));
		assertEquals(3, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#addProduct(br.edu.ufpb.threadControl.mySimpleCRM.entity.Product)}
	 * .
	 */
	@Test
	public void testAddProduct() {

		List<Product> currentObjectsList = null;
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(product1));
		assertTrue(currentObjectsList.contains(product2));
		assertTrue(currentObjectsList.contains(product3));
		assertEquals(3, currentObjectsList.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#removeProduct(br.edu.ufpb.threadControl.mySimpleCRM.entity.Product)}
	 * .
	 */
	@Test
	public void testRemoveProduct() {

		List<Product> currentObjectsList = null;
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(product1));
		assertTrue(currentObjectsList.contains(product2));
		assertTrue(currentObjectsList.contains(product3));
		assertEquals(3, currentObjectsList.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

		facade.removeProduct(productAuxOne);
		facade.removeProduct(productAuxTwo);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		copyListOfAllProduct.clear();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(1, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#restoreProduct(java.lang.String)}
	 * .
	 */
	@Test
	public void testRestoreProduct() {

		List<Product> currentObjectsList = null;
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(product1));
		assertTrue(currentObjectsList.contains(product2));
		assertTrue(currentObjectsList.contains(product3));
		assertEquals(3, currentObjectsList.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

		facade.removeProduct(productAuxOne);
		facade.removeProduct(productAuxTwo);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		copyListOfAllProduct.clear();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(1, currentObjectsList.size());

		facade.restoreProduct("Ipod");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		copyListOfAllProduct.clear();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(2, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#editProduct(br.edu.ufpb.threadControl.mySimpleCRM.entity.Product)}
	 * .
	 */
	@Test
	public void testEditProduct() {

		List<Product> currentObjectsList = null;
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(product1));
		assertTrue(currentObjectsList.contains(product2));
		assertTrue(currentObjectsList.contains(product3));
		assertEquals(3, currentObjectsList.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

		productAuxOne.setName("Sony Vaio");
		productAuxTwo.setPrice(150000.00);
		productAuxThree.setQuantity(100);

		facade.editProduct(productAuxOne);
		facade.editProduct(productAuxTwo);
		facade.editProduct(productAuxThree);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.searchProductByName("Sony Vaio", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Sony Vaio".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 150000.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertEquals("Sony Vaio".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxTwo.getPrice() == 150000.00);
		assertEquals(100, productAuxThree.getQuantity());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchProductById(java.lang.Long, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchProductById() {

		List<Product> currentObjectsList = null;
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(product1));
		assertTrue(currentObjectsList.contains(product2));
		assertTrue(currentObjectsList.contains(product3));
		assertEquals(3, currentObjectsList.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		facade.searchProductById(productAuxOne.getId(), takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductById(productAuxTwo.getId(), takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductById(productAuxThree.getId(), takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchProductByName(java.lang.String, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchProductByName() {

		List<Product> currentObjectsList = null;
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(product1));
		assertTrue(currentObjectsList.contains(product2));
		assertTrue(currentObjectsList.contains(product3));
		assertEquals(3, currentObjectsList.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#getListOfProduct(java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testGetListProduct() {

		List<Product> currentObjectsList = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsList.contains(product1));
		assertTrue(currentObjectsList.contains(product2));
		assertTrue(currentObjectsList.contains(product3));
		assertEquals(3, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#addPromotion(br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion)}
	 * .
	 */
	@Test
	public void testAddPromotion() {
		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));
		assertEquals(3, currentObjectsListProduct.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProductOne = new ArrayList<Product>();
		listProductOne.add(productAuxOne);
		listProductOne.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProductOne, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertTrue(promotionAuxTwo.getQuantityProductPromotion() == 30);
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#removePromotion(br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion)}
	 * .
	 */
	@Test
	public void testRemovePromotion() {
		
		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));
		assertEquals(3, currentObjectsListProduct.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);
		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertTrue(promotionAuxTwo.getQuantityProductPromotion() == 30);
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());

		// Remove Promotion
		facade.removePromotion(promotionAuxTwo);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertEquals(1, currentObjectsListPromotion.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#restorePromotion(java.lang.String)}
	 * .
	 */
	@Test
	public void testRestorePromotion() {

		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));
		assertEquals(3, currentObjectsListProduct.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);
		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertTrue(promotionAuxTwo.getQuantityProductPromotion() == 30);
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());

		// removePromotion
		facade.removePromotion(promotionAuxTwo);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);
		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertEquals(1, currentObjectsListPromotion.size());

		// restore Promotion
		facade.restorePromotion("Promotion Apple Combo");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);
		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertEquals(2, currentObjectsListPromotion.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#editPromotion(br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion)}
	 * .
	 */
	@Test
	public void testEditPromotion() {

		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));
		assertEquals(3, currentObjectsListProduct.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);
		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertTrue(promotionAuxTwo.getQuantityProductPromotion() == 30);
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());
		// Edit Promotion

		promotionAuxTwo.setName("Promotion lightning Apple");
		facade.editPromotion(promotionAuxTwo);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Search Promotion By Name
		facade.searchPromotionByName("Promotion lightning Apple",
				takerPromotionList);
		try {
			promotionAuxThree = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion lightning Apple".toUpperCase(),
				promotionAuxThree.getName());
		assertEquals(30, promotionAuxThree.getQuantityProductPromotion());
		assertTrue(promotionAuxThree.getPromotionalPrice() == 3000);

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchPromotionById(long, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchPromotionById() {

		// Add Products
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		Promotion promotionAuxOne = null;
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting Promotion By Id
		facade.searchPromotionById(promotion1.getId(), takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getPromotionalPrice() == 5000);

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchPromotionByName(java.lang.String, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchPromotionByName() {

		// Add Products
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		Promotion promotionAuxOne = null;
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting Promotion By Name
		facade.searchPromotionByName(promotion1.getName(), takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getPromotionalPrice() == 5000);

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#searchPromotionByProduct(br.edu.ufpb.threadControl.mySimpleCRM.entity.Product, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchPromotionByProduct() {

		// Add Products
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		List<Promotion> listPromotions = null;
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting Promotion By Product
		facade.searchPromotionByProduct(productAuxThree, copyListOfAllPromotion);

		try {
			listPromotions = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(2, listPromotions.size());
		assertTrue(listPromotions.contains(promotion1));
		assertTrue(listPromotions.contains(promotion2));

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#getListOfPromotion(java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testGetListPromotion() {

		// Add Products
		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		// Add Promotion
		Promotion promotionAuxOne = null;
		List<Promotion> listPromotions = null;
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting Promotion By Product
		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			listPromotions = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(2, listPromotions.size());
		assertTrue(listPromotions.contains(promotionAuxOne));

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#addPurchase(br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase)}
	 * .
	 */
	@Test
	public void testAddPurchaseProduct() {

		/*------------Starting operations of the customer------------*/

		List<Customer> currentObjectsListCustomer = null;

		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 19, 12, 1989);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 20, 01, 1990);

		// Add Customer
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting the list of customers
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, currentObjectsListCustomer.size());
		assertTrue(currentObjectsListCustomer.contains(customer1));
		assertTrue(currentObjectsListCustomer.contains(customer2));
		assertTrue(currentObjectsListCustomer.contains(customer3));

		// getting customer by CPF
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Diego".toUpperCase(), customerAuxOne.getName());
		assertFalse(customerAuxOne.getLogin() == "ayla@dce.ufpb.br");

		// getting customer by CPF
		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());
		assertEquals("Ayla".toUpperCase(), customerAuxTwo.getName());
		assertFalse(customerAuxTwo.getLogin() == "diego.sousa@dce.ufpb.br");

		// getting customer by CPF
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());
		assertEquals("Kawe".toUpperCase(), customerAuxThree.getName());
		assertFalse(customerAuxThree.getLogin() == "ayla@dce.ufpb.br");

		assertTrue(currentObjectsListCustomer.contains(customerAuxOne));
		assertTrue(currentObjectsListCustomer.contains(customerAuxTwo));
		assertTrue(currentObjectsListCustomer.contains(customerAuxThree));
		assertEquals(3, currentObjectsListCustomer.size());

		/*----------------End of customer operations-------------------*/

		/*------------Starting operations of the product---------------*/

		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting the List of Products
		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, currentObjectsListProduct.size());
		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));

		// Searching by product name
		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(productAuxOne));
		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertFalse(productAuxOne.getPrice() == 3200);

		// Searching by product name
		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(productAuxTwo));
		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertFalse(productAuxTwo.getPrice() == 1200);

		// Searching by product name
		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(productAuxThree));
		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertFalse(productAuxThree.getPrice() == 1200);

		assertTrue(currentObjectsListProduct.contains(productAuxOne));
		assertTrue(currentObjectsListProduct.contains(productAuxTwo));
		assertTrue(currentObjectsListProduct.contains(productAuxThree));
		assertEquals(3, currentObjectsListProduct.size());

		/*-----------------End of Product operations-------------------*/

		/*------------Starting operations of the Promotions---------------*/

		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting the list of Promotions
		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		// getting promotions by Name
		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertEquals(listProductTwo, promotionAuxOne.getListProducts());

		// getting promotions by Name
		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertEquals(listProduct, promotionAuxTwo.getListProducts());

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(2, currentObjectsListPromotion.size());

		/*-----------------End of Promotion operations-----------------*/

		/*------------Starting operations of the Purchases-------------*/

		// Creating list of Products
		List<Purchase> listPurchase = null;
		Map<Product, Integer> listProductAux = new HashMap<Product, Integer>();
		listProductAux.put(productAuxOne, 5);

		// Creating list of Promotions
		Map<Promotion, Integer> listPromotionAux = new HashMap<Promotion, Integer>();
		listPromotionAux.put(promotionAuxOne, 5);
		listPromotionAux.put(promotionAuxTwo, 10);

		Purchase purchaseOne = new Purchase(customerAuxOne, listProductAux,
				null);
		Purchase purchaseTwo = new Purchase(customerAuxTwo, null,
				listPromotionAux);
		Purchase purchaseThree = new Purchase(customerAuxThree, listProductAux,
				listPromotionAux);

		// Adding Purchases
		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// Getting the list of Purchases
		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, listPurchase.size());
		assertTrue(listPurchase.contains(purchaseOne));
		assertTrue(listPurchase.contains(purchaseTwo));
		assertTrue(listPurchase.contains(purchaseThree));

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#removePurchase(br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase)}
	 * 
	 */
	@Test
	public void testRemovePurchase() {

		List<Customer> currentObjectsListCustomer = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		
		// Add Customer
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListCustomer.contains(customer1));
		assertTrue(currentObjectsListCustomer.contains(customer2));
		assertTrue(currentObjectsListCustomer.contains(customer3));
		assertEquals(3, currentObjectsListCustomer.size());

		// Searching Customer By CPF
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		// Searching Customer By CPF
		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// Searching Customer By CPF
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());
		assertTrue(currentObjectsListCustomer.contains(customerAuxOne));
		assertTrue(currentObjectsListCustomer.contains(customerAuxTwo));
		assertTrue(currentObjectsListCustomer.contains(customerAuxThree));
		assertEquals(3, currentObjectsListCustomer.size());

		/*-----------------End of client operations-----------------*/

		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));
		assertEquals(3, currentObjectsListProduct.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);
		assertTrue(currentObjectsListProduct.contains(productAuxOne));
		assertTrue(currentObjectsListProduct.contains(productAuxTwo));
		assertTrue(currentObjectsListProduct.contains(productAuxThree));
		assertEquals(3, currentObjectsListProduct.size());

		/*-----------------End of Product operations-----------------*/

		// Add Promotion
		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertTrue(promotionAuxTwo.getQuantityProductPromotion() == 30);

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(2, currentObjectsListPromotion.size());

		/*-----------------End of Promotion operations-----------------*/
		List<Purchase> listPurchase = null;
		Map<Product, Integer> listProductAux = new HashMap<Product, Integer>();
		listProductAux.put(productAuxOne, 5);

		Map<Promotion, Integer> listPromotionAux = new HashMap<Promotion, Integer>();
		listPromotionAux.put(promotionAuxOne, 5);
		listPromotionAux.put(promotionAuxTwo, 10);

		Purchase purchaseOne = new Purchase(customerAuxOne, listProductAux,
				null);
		Purchase purchaseTwo = new Purchase(customerAuxTwo, null,
				listPromotionAux);
		Purchase purchaseThree = new Purchase(customerAuxThree, listProductAux,
				listPromotionAux);

		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, listPurchase.size());

		facade.removePurchase(listPurchase.get(0));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(2, listPurchase.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade#editPurchase(br.edu.ufpb.threadControl.mySimpleCRM.entity.Product, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testEditPurchase() {

		List<Customer> currentObjectsListCustomer = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		
		// Add Customer
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListCustomer.contains(customer1));
		assertTrue(currentObjectsListCustomer.contains(customer2));
		assertTrue(currentObjectsListCustomer.contains(customer3));
		assertEquals(3, currentObjectsListCustomer.size());

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());

		assertTrue(currentObjectsListCustomer.contains(customerAuxOne));
		assertTrue(currentObjectsListCustomer.contains(customerAuxTwo));
		assertTrue(currentObjectsListCustomer.contains(customerAuxThree));
		assertEquals(3, currentObjectsListCustomer.size());

		/*-----------------End of client operations-----------------*/

		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));
		assertEquals(3, currentObjectsListProduct.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		assertTrue(currentObjectsListProduct.contains(productAuxOne));
		assertTrue(currentObjectsListProduct.contains(productAuxTwo));
		assertTrue(currentObjectsListProduct.contains(productAuxThree));
		assertEquals(3, currentObjectsListProduct.size());

		/*-----------------End of Product operations-----------------*/

		// Add Promotion
		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertTrue(promotionAuxTwo.getQuantityProductPromotion() == 30);

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(2, currentObjectsListPromotion.size());

		/*-----------------End of Promotion operations-----------------*/
		List<Purchase> listPurchase = null;
		Map<Product, Integer> listProductAux = new HashMap<Product, Integer>();
		listProductAux.put(productAuxOne, 5);

		Map<Promotion, Integer> listPromotionAux = new HashMap<Promotion, Integer>();
		listPromotionAux.put(promotionAuxOne, 5);
		listPromotionAux.put(promotionAuxTwo, 10);

		Purchase purchaseOne = new Purchase(customerAuxOne, listProductAux,
				null);
		Purchase purchaseTwo = new Purchase(customerAuxTwo, null,
				listPromotionAux);
		Purchase purchaseThree = new Purchase(customerAuxThree, listProductAux,
				listPromotionAux);

		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, listPurchase.size());

		Purchase purchaseAux = null;

		for (Purchase purchase : listPurchase) {
			if (purchase.getListProducts() != null) {
				if (purchase.getListProducts() == purchaseOne.getListProducts()) {
					purchaseAux = purchase;
				}
			}
		}

		assertEquals(customerAuxOne, purchaseAux.getCustomer());

	}

	@Test
	public void testGetListPurchase() {

		List<Customer> currentObjectsListCustomer = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "07278910112", "3422-1010",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "07278910113", "3422-1011",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "07278910114", "3422-1012",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add Customer

		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		// getting the list of customer
		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListCustomer.contains(customer1));
		assertTrue(currentObjectsListCustomer.contains(customer2));
		assertTrue(currentObjectsListCustomer.contains(customer3));
		assertEquals(3, currentObjectsListCustomer.size());

		// getting customer
		facade.searchCustomerByCpf("07278910112", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t", customerAuxOne.getPassword());
		assertEquals("3422-1010", customerAuxOne.getPhone());

		facade.searchCustomerByCpf("07278910113", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t2", customerAuxTwo.getPassword());
		assertEquals("3422-1011", customerAuxTwo.getPhone());

		// getting customer
		facade.searchCustomerByCpf("07278910114", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("S3cr3t3", customerAuxThree.getPassword());
		assertEquals("3422-1012", customerAuxThree.getPhone());

		assertTrue(currentObjectsListCustomer.contains(customerAuxOne));
		assertTrue(currentObjectsListCustomer.contains(customerAuxTwo));
		assertTrue(currentObjectsListCustomer.contains(customerAuxThree));
		assertEquals(3, currentObjectsListCustomer.size());

		/*-----------------End of client operations-----------------*/

		// Add Products
		List<Product> currentObjectsListProduct = null;

		Product productAuxOne = null;
		Product productAuxTwo = null;
		Product productAuxThree = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListProduct.contains(product1));
		assertTrue(currentObjectsListProduct.contains(product2));
		assertTrue(currentObjectsListProduct.contains(product3));
		assertEquals(3, currentObjectsListProduct.size());

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPod".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxOne.getPrice() == 1200.00);

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPad".toUpperCase(), productAuxTwo.getName());
		assertTrue(productAuxTwo.getPrice() == 3200.00);

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("IPhone".toUpperCase(), productAuxThree.getName());
		assertTrue(productAuxThree.getPrice() == 2200.00);

		assertTrue(currentObjectsListProduct.contains(productAuxOne));
		assertTrue(currentObjectsListProduct.contains(productAuxTwo));
		assertTrue(currentObjectsListProduct.contains(productAuxThree));
		assertEquals(3, currentObjectsListProduct.size());

		/*-----------------End of Product operations-----------------*/

		// Add Promotion
		List<Promotion> currentObjectsListPromotion = null;
		Promotion promotionAuxOne = null;
		Promotion promotionAuxTwo = null;
		Promotion promotionAuxThree = null;

		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				5000, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(currentObjectsListPromotion.contains(promotion1));
		assertTrue(currentObjectsListPromotion.contains(promotion2));
		assertEquals(2, currentObjectsListPromotion.size());

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getQuantityProductPromotion() == 30);

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals("Promotion Apple Combo".toUpperCase(),
				promotionAuxTwo.getName());
		assertTrue(promotionAuxTwo.getQuantityProductPromotion() == 30);

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(2, currentObjectsListPromotion.size());

		/*-----------------End of Promotion operations-----------------*/
		List<Purchase> listPurchase = null;
		Map<Product, Integer> listProductAux = new HashMap<Product, Integer>();
		listProductAux.put(productAuxOne, 5);

		Map<Promotion, Integer> listPromotionAux = new HashMap<Promotion, Integer>();
		listPromotionAux.put(promotionAuxOne, 5);
		listPromotionAux.put(promotionAuxTwo, 10);

		Purchase purchaseOne = new Purchase(customerAuxOne, listProductAux,
				null);
		Purchase purchaseTwo = new Purchase(customerAuxTwo, null,
				listPromotionAux);
		Purchase purchaseThree = new Purchase(customerAuxThree, listProductAux,
				listPromotionAux);

		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(3, listPurchase.size());

		Purchase purchaseAux = null;

		for (Purchase purchase : listPurchase) {
			if (purchase.getListProducts() != null) {
				if (purchase.getListProducts() == purchaseOne.getListProducts()) {
					purchaseAux = purchase;
				}
			}
		}
		assertEquals(customerAuxOne, purchaseAux.getCustomer());
	}
}
