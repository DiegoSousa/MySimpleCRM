package br.edu.ufpb.threadControl.messengerConcurrent.test;

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

import br.edu.ufcg.threadcontrol.ListOfThreadConfigurations;
import br.edu.ufcg.threadcontrol.SystemConfiguration;
import br.edu.ufcg.threadcontrol.ThreadConfiguration;
import br.edu.ufcg.threadcontrol.ThreadControl;
import br.edu.ufcg.threadcontrol.ThreadState;
import br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Purchase;
import br.edu.ufpb.threadControl.messengerConcurrent.managerFactory.ManagerDAOFactoryJPA;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddCustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddPromotion;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableAddPurchase;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableEditCustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableEditProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableEditPromotion;
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
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchPurchaseByProduct;
import br.edu.ufpb.threadControl.messengerConcurrent.runnable.RunnableSearchPurchasesOfACustomer;
import br.edu.ufpb.threadControl.messengerConcurrent.util.HibernateUtil;

/**
 * Class used to test the facade, which contains all the application methods.
 *
 * Note: This class uses the framework JPA / Hibernate for persistence and
 * constant "ALL_THREADS_TO_BE_IN_STATE" Attribute that has the function to
 * count the active threads.
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class FacadeTestJpaWithConstant {

	private static Facade facade;
	private ThreadControl threadControl;
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
		threadControl = new ThreadControl();
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
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#addCustomer(br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer)}
	 * .
	 */
	@Test
	public void testAddCustomer() {
		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;
		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());
		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#removeCustomer(br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer)}
	 * .
	 */

	@Test
	public void testRemoveCustomer() {
		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// AddCustomer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());
		// Removing Customer
		threadControl.prepare(getRemoveCustomerFinishedState());
		threadControl.proceed();

		facade.removeCustomer(customerAuxOne);
		facade.removeCustomer(customerAuxTwo);

		copyListOfAllCustomer.clear();
		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertFalse(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(1, currentObjectsList.size());
		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#restoreCustomer(java.lang.String)}
	 * .
	 */
	@Test
	public void testRestoreCustomer() {
		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);

		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());
		// removing the list of customer
		threadControl.prepare(getRemoveCustomerFinishedState());
		threadControl.proceed();

		facade.removeCustomer(customerAuxOne);
		facade.removeCustomer(customerAuxTwo);

		copyListOfAllCustomer.clear();
		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertFalse(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(1, currentObjectsList.size());
		// Restoring Customer
		threadControl.prepare(getRestoreCustomerFinishedState());
		threadControl.proceed();
		facade.restoreCustomer("111");
		copyListOfAllCustomer.clear();
		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertEquals(2, currentObjectsList.size());
		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#editCustomer(br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer)}
	 * .
	 */
	@Test
	public void testEditCustomer() {
		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertEquals(3, currentObjectsList.size());
		// Editing customer
		threadControl.prepare(getEditCustomerFinishedState());
		threadControl.proceed();

		customerAuxOne.setName("Ewerton");
		customerAuxTwo.setCpf("555");
		customerAuxThree.setPhone("2222-2222");

		facade.editCustomer(customerAuxOne);
		facade.editCustomer(customerAuxTwo);
		facade.editCustomer(customerAuxThree);

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("555", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertEquals("Ewerton".toUpperCase(), customerAuxOne.getName());
		assertEquals("555", customerAuxTwo.getCpf());
		assertEquals("2222-2222", customerAuxThree.getPhone());
		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchCustomerById(java.lang.Long, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchCustomerById() {
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		// add customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();
		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		assertEquals("diego.sousa@dce.ufpb.br", customerAuxOne.getLogin());
		// getting customer
		threadControl.prepare(getSearchCustomerByIdFinishedState());
		threadControl.proceed();

		facade.searchCustomerById(customerAuxOne.getId(), takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertEquals("diego.sousa@dce.ufpb.br", customerAuxTwo.getLogin());
		assertEquals("Diego".toUpperCase(), customerAuxTwo.getName());
		assertEquals("111", customerAuxTwo.getCpf());
		assertFalse(customerAuxTwo.getLogin() == "diego@diegosousa.com");

		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchCustomerByLogin(java.lang.String, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchCustomerByLogin() {

		Customer customerAuxOne = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		// add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByLoginFinishedState());
		threadControl.proceed();
		facade.searchCustomerByLogin("diego.sousa@dce.ufpb.br", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		assertEquals("diego.sousa@dce.ufpb.br", customerAuxOne.getLogin());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchCustomerByCpf(java.lang.String, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testSearchCustomerByCpf() {

		Customer customerAuxOne = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		// add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();
		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		assertEquals("diego.sousa@dce.ufpb.br", customerAuxOne.getLogin());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#getListOfCustomer(java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testGetListOfCustomer() {

		List<Customer> currentObjectsList = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;
		Customer customerAuxFour = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsList = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(customerAuxOne));
		assertTrue(currentObjectsList.contains(customerAuxTwo));
		assertTrue(currentObjectsList.contains(customerAuxThree));
		assertFalse(currentObjectsList.contains(customerAuxFour));
		assertEquals(3, currentObjectsList.size());

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#addProduct(br.edu.ufpb.threadControl.messengerConcurrent.entity.Product)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#removeProduct(br.edu.ufpb.threadControl.messengerConcurrent.entity.Product)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

		threadControl.prepare(getRemoveProductFinishedState());
		threadControl.proceed();

		facade.removeProduct(productAuxOne);
		facade.removeProduct(productAuxTwo);

		copyListOfAllProduct.clear();
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertFalse(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(1, currentObjectsList.size());
		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#restoreProduct(java.lang.String)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

		threadControl.prepare(getRemoveProductFinishedState());
		threadControl.proceed();

		facade.removeProduct(productAuxOne);
		facade.removeProduct(productAuxTwo);

		copyListOfAllProduct.clear();
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertFalse(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(1, currentObjectsList.size());

		threadControl.prepare(getRestoreProductFinishedState());
		threadControl.proceed();
		facade.restoreProduct("Ipod");
		copyListOfAllProduct.clear();
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(productAuxOne));
		assertEquals(2, currentObjectsList.size());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#editProduct(br.edu.ufpb.threadControl.messengerConcurrent.entity.Product)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());

		threadControl.prepare(getEditProductFinishedState());
		threadControl.proceed();

		productAuxOne.setName("Sony Vaio");
		productAuxTwo.setPrice(1500.00);
		productAuxThree.setQuantity(100);

		facade.editProduct(productAuxOne);
		facade.editProduct(productAuxTwo);
		facade.editProduct(productAuxThree);

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("Sony Vaio", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertEquals("Sony Vaio".toUpperCase(), productAuxOne.getName());
		assertTrue(productAuxTwo.getPrice() == 1500.00);
		assertEquals(100, productAuxThree.getQuantity());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchProductById(java.lang.Long, java.util.concurrent.BlockingQueue)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByIdFinishedState());

		facade.searchProductById(productAuxOne.getId(), takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByIdFinishedState());

		facade.searchProductById(productAuxTwo.getId(), takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByIdFinishedState());

		facade.searchProductById(productAuxThree.getId(), takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchProductByName(java.lang.String, java.util.concurrent.BlockingQueue)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsList.contains(productAuxOne));
		assertTrue(currentObjectsList.contains(productAuxTwo));
		assertTrue(currentObjectsList.contains(productAuxThree));
		assertEquals(3, currentObjectsList.size());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#getListOfProduct(java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testGetListProduct() {

		List<Product> currentObjectsList = null;
		Product productAuxOne = null;

		Product product1 = new Product("IPod", 1200.00, 100);
		Product product2 = new Product("IPhone", 2200.00, 200);
		Product product3 = new Product("IPad", 3200.00, 300);

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsList = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertEquals(3, currentObjectsList.size());
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsList.contains(productAuxOne));
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#addPromotion(br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProductOne, 3000, 30);

		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());
		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#removePromotion(br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);
		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);
		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());
		// Remove Promotion
		threadControl.prepare(getRemovePromotionFinishedState());
		threadControl.proceed();
		facade.removePromotion(promotionAuxTwo);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);
		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxTwo));

		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#restorePromotion(java.lang.String)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);
		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);
		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());
		// removePromotion
		threadControl.prepare(getRemovePromotionFinishedState());
		threadControl.proceed();
		facade.removePromotion(promotionAuxTwo);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);
		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxTwo));

		threadControl.prepare(getRestorePromotionFinishedState());
		threadControl.proceed();
		// restore Promotion
		facade.restorePromotion("Promotion Apple Combo");

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);
		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#editPromotion(br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);
		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);
		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		assertTrue(currentObjectsListPromotion.contains(promotionAuxOne));
		assertTrue(currentObjectsListPromotion.contains(promotionAuxTwo));
		assertFalse(currentObjectsListPromotion.contains(promotionAuxThree));
		assertEquals(3, currentObjectsListProduct.size());
		// Edit Promotion
		threadControl.prepare(getEditPromotionFinishedState());
		threadControl.proceed();

		promotionAuxTwo.setName("Promotion lightning Apple");
		facade.editPromotion(promotionAuxTwo);

		threadControl.waitUntilStateIsReached();
		// Search Promotion By Name
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion lightning Apple",
				takerPromotionList);
		try {
			promotionAuxThree = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();

		assertEquals("Promotion lightning Apple".toUpperCase(),
				promotionAuxThree.getName());
		assertEquals(30, promotionAuxThree.getQuantityProductPromotion());
		assertTrue(promotionAuxThree.getPromotionalPrice() == 3000);

		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchPromotionById(long, java.util.concurrent.BlockingQueue)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// Add Promotion
		Promotion promotionAuxOne = null;
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);
		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();
		// Getting Promotion By Id
		threadControl.prepare(getSearchPromotionByIdFinishedState());
		threadControl.proceed();
		facade.searchPromotionById(promotion1.getId(), takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getPromotionalPrice() == 50);
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchPromotionByName(java.lang.String, java.util.concurrent.BlockingQueue)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// Add Promotion
		Promotion promotionAuxOne = null;
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);
		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();
		// Getting Promotion By Name
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();
		facade.searchPromotionByName(promotion1.getName(), takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertEquals("Promoção Apple".toUpperCase(), promotionAuxOne.getName());
		assertTrue(promotionAuxOne.getPromotionalPrice() == 50);
		threadControl.proceed();
	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#searchPromotionByProduct(br.edu.ufpb.threadControl.messengerConcurrent.entity.Product, java.util.concurrent.BlockingQueue)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// Add Promotion
		List<Promotion> listPromotions = null;
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(productAuxOne);
		listProduct.add(productAuxThree);

		List<Product> listProductTwo = new ArrayList<Product>();
		listProductTwo.add(productAuxTwo);
		listProductTwo.add(productAuxThree);

		Promotion promotion1 = new Promotion("Promoção Apple", listProductTwo,
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);
		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();
		// Getting Promotion By Product
		threadControl.prepare(getSearchPromotionByProductFinishedState());
		threadControl.proceed();
		facade.searchPromotionByProduct(productAuxThree, copyListOfAllPromotion);

		try {
			listPromotions = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertEquals(2, listPromotions.size());
		assertEquals("Promoção Apple".toUpperCase(), listPromotions.get(0)
				.getName());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#getListOfPromotion(java.util.concurrent.BlockingQueue)}
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

		threadControl.prepare(getAddProductFinishedState());
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);
		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);
		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();
		// Getting Promotion By Product
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();
		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			listPromotions = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		assertEquals(2, listPromotions.size());
		assertTrue(listPromotions.contains(promotionAuxOne));
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#addPurchase(br.edu.ufpb.threadControl.messengerConcurrent.entity.Purchase)}
	 * .
	 */
	@Test
	public void testAddPurchaseProduct() {

		List<Customer> currentObjectsListCustomer = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddProductFinishedState());
		threadControl.proceed();
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddPurchaseFinishedState());
		threadControl.proceed();

		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPurchaseFinishedState());
		threadControl.proceed();
		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#removePurchase(br.edu.ufpb.threadControl.messengerConcurrent.entity.Purchase)}
	 * .
	 */
	@Test
	public void testRemovePurchase() {

		List<Customer> currentObjectsListCustomer = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddProductFinishedState());
		threadControl.proceed();
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddPurchaseFinishedState());
		threadControl.proceed();

		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPurchaseFinishedState());
		threadControl.proceed();
		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertEquals(3, listPurchase.size());
		Purchase purchaseAux = listPurchase.get(0);

		threadControl.prepare(getRemovePurchaseFinishedState());
		threadControl.proceed();
		facade.removePurchase(purchaseAux);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPurchaseFinishedState());
		threadControl.proceed();
		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		assertEquals(2, listPurchase.size());
		threadControl.proceed();

	}

	/**
	 * Test method for
	 * {@link br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade#editPurchase(br.edu.ufpb.threadControl.messengerConcurrent.entity.Product, java.util.concurrent.BlockingQueue)}
	 * .
	 */
	@Test
	public void testEditPurchase() {

		List<Customer> currentObjectsListCustomer = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddProductFinishedState());
		threadControl.proceed();
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddPurchaseFinishedState());
		threadControl.proceed();

		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPurchaseFinishedState());
		threadControl.proceed();
		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
		threadControl.proceed();

	}

	@Test
	public void testGetListPurchase() {

		List<Customer> currentObjectsListCustomer = null;
		Customer customerAuxOne = null;
		Customer customerAuxTwo = null;
		Customer customerAuxThree = null;

		Customer customer1 = new Customer("Diego", "111", "3422-1048",
				"diego.sousa@dce.ufpb.br", "S3cr3t", 18, 11, 1988);
		Customer customer2 = new Customer("Ayla", "222", "3422-1049",
				"ayla@dce.ufpb.br", "S3cr3t2", 18, 11, 1988);
		Customer customer3 = new Customer("Kawe", "333", "3422-1050",
				"kawe.ramon@dce.ufpb.br", "S3cr3t3", 18, 11, 1988);
		// Add Customer
		threadControl.prepare(getAddCustomerFinishedState());
		facade.addCustomer(customer1);
		facade.addCustomer(customer2);
		facade.addCustomer(customer3);

		threadControl.waitUntilStateIsReached();
		// getting the list of customer
		threadControl.prepare(getListOfCustomerFinishedState());
		threadControl.proceed();

		facade.getListOfCustomer(copyListOfAllCustomer);

		try {
			currentObjectsListCustomer = copyListOfAllCustomer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("111", takerClientList);

		try {
			customerAuxOne = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("222", takerClientList);

		try {
			customerAuxTwo = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		// getting customer
		threadControl.prepare(getSearchCustomerByCpfFinishedState());
		threadControl.proceed();

		facade.searchCustomerByCpf("333", takerClientList);

		try {
			customerAuxThree = takerClientList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddProductFinishedState());
		threadControl.proceed();
		facade.addProduct(product1);
		facade.addProduct(product2);
		facade.addProduct(product3);

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfProductFinishedState());
		threadControl.proceed();

		facade.getListOfProduct(copyListOfAllProduct);

		try {
			currentObjectsListProduct = copyListOfAllProduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPod", takerProductList);

		try {
			productAuxOne = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPad", takerProductList);

		try {
			productAuxTwo = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchProductByNameFinishedState());
		threadControl.proceed();

		facade.searchProductByName("IPhone", takerProductList);

		try {
			productAuxThree = takerProductList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

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
				50, 30);
		Promotion promotion2 = new Promotion("Promotion Apple Combo",
				listProduct, 3000, 30);

		threadControl.prepare(getAddPromotionFinishedState());
		threadControl.proceed();

		facade.addPromotion(promotion1);
		facade.addPromotion(promotion2);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPromotionFinishedState());
		threadControl.proceed();

		facade.getListOfPromotion(copyListOfAllPromotion);

		try {
			currentObjectsListPromotion = copyListOfAllPromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promoção Apple", takerPromotionList);

		try {
			promotionAuxOne = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
		threadControl.prepare(getSearchPromotionByNameFinishedState());
		threadControl.proceed();

		facade.searchPromotionByName("Promotion Apple Combo",
				takerPromotionList);

		try {
			promotionAuxTwo = takerPromotionList.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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

		threadControl.prepare(getAddPurchaseFinishedState());
		threadControl.proceed();

		facade.addPurchase(purchaseOne);
		facade.addPurchase(purchaseTwo);
		facade.addPurchase(purchaseThree);
		threadControl.waitUntilStateIsReached();

		threadControl.prepare(getListOfPurchaseFinishedState());
		threadControl.proceed();
		facade.getListOfPurchase(copyListOfAllPurchase);

		try {
			listPurchase = copyListOfAllPurchase.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		threadControl.waitUntilStateIsReached();
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
		threadControl.proceed();

	}

	// -------SystemConfiguration Generic--------

	public SystemConfiguration getGenericSystemConfiguration(
			String threadClassName, ThreadState expectedState) {
		ThreadConfiguration config = new ThreadConfiguration(threadClassName,
				expectedState, ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	// -------------- SystemConfiguration Customer-----------------------

	public SystemConfiguration getAddCustomerFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableAddCustomer.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getRemoveCustomerFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableRemoveCustomer.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getEditCustomerFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableEditCustomer.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getRestoreCustomerFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableRestoreCustomer.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchCustomerByIdFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchCustomerById.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchCustomerByLoginFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchCustomerByLogin.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchCustomerByCpfFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchCustomerByCpf.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getListOfCustomerFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableGetListCustomer.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	// ---------SystemConfiguration Product--------------

	public SystemConfiguration getAddProductFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableAddProduct.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getRemoveProductFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableRemoveProduct.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getRestoreProductFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableRestoreProduct.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getEditProductFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableEditProduct.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchProductByIdFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchProductById.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchProductByNameFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchProductByName.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getListOfProductFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableGetListProduct.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	// ---------SystemConfiguration Promotion--------------

	public SystemConfiguration getAddPromotionFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableAddPromotion.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getRemovePromotionFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableRemovePromotion.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getRestorePromotionFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableRestorePromotion.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getEditPromotionFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableEditPromotion.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchPromotionByIdFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchPromotionById.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchPromotionByNameFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchPromotionByName.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchPromotionByProductFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchPromotionByProduct.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getListOfPromotionFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableGetListPromotion.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	// ---------SystemConfiguration Purchases Of Products--------------

	public SystemConfiguration getAddPurchaseFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableAddPurchase.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getRemovePurchaseFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableRemovePurchase.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchPurchaseByProductFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchPurchaseByProduct.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getSearchPurchasesOfACustomerFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableSearchPurchasesOfACustomer.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}

	public SystemConfiguration getListOfPurchaseFinishedState() {
		ThreadConfiguration config = new ThreadConfiguration(
				RunnableGetListPurchase.class.getCanonicalName(),
				ThreadState.FINISHED,
				ThreadConfiguration.ALL_THREADS_TO_BE_IN_STATE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config);
		return sysConfig;
	}
}
