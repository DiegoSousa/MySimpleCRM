package br.edu.ufpb.threadControl.messengerConcurrent.dao;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Purchase;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPurchaseManager;
import br.edu.ufpb.threadControl.messengerConcurrent.util.HibernateUtil;

/**
 * Implementation of Purchase DAO with JPA
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class PurchaseDAOJPA implements IPurchaseManager {

	private Logger logger;
	private EntityManager entityManager;
	private double sumOfThePricesOfProducts = 0;
	private double sumOfThePricesOfPromotions = 0;

	public PurchaseDAOJPA() {
		this.entityManager = HibernateUtil.getInstance().getFactory()
				.createEntityManager();
		this.logger = Logger.getLogger(PurchaseDAOJPA.class.getName());
	}

	@Override
	public synchronized Purchase addPurchase(Purchase purchase) {
		try {
			if (purchase.getCustomer() != null
					&& purchase.getListProducts() != null
					|| purchase.getListPromotions() != null) {

				double soma = 0;

				if (purchase.getListProducts() != null) {
					Purchase purchaseAuxProduct = decrementListProductInStock(purchase);
					if (purchaseAuxProduct == null) {
						return null;
					}
					soma += purchaseAuxProduct.getFinalPrice();
				}

				if (purchase.getListPromotions() != null) {
					Purchase purchaseAuxPromotion = decrementListPromotionInStock(purchase);

					if (purchaseAuxPromotion == null) {
						return null;
					}
					soma += purchaseAuxPromotion.getFinalPrice();
				}

				entityManager.getTransaction().begin();
				purchase.setFinalPrice(soma);
				entityManager.persist(purchase);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Sale was successful");
				return purchase;
			}
			logger.log(
					Level.SEVERE,
					"Error purchasing product, probably the quantity and/or price "
							+ "promotion is 0 or product does not have sufficient or the promotion "
							+ "price is greater than the of the product.");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error to sell the product: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Purchase removePurchase(Purchase purchase) {
		try {
			if (purchase.getListProducts() != null) {
				Purchase purchaseAuxProduct = incrementListProductInStock(purchase);
				if (purchaseAuxProduct == null) {
					return null;
				}
			}
			if (purchase.getListPromotions() != null) {
				Purchase purchaseAuxPromotion = incrementListPromotionInStock(purchase);
				if (purchaseAuxPromotion == null) {
					return null;
				}
			}
			entityManager.getTransaction().begin();
			entityManager.remove(entityManager.find(Purchase.class,
					purchase.getId()));
			entityManager.getTransaction().commit();
			logger.log(Level.INFO, "Purchase: " + purchase.getId()
					+ " successfully removed!");
			return purchase;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error removing purchase: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Purchase editPurchase(Purchase purchase) {
		try {
			if (purchase.getCustomer() != null
					&& purchase.getListProducts() != null
					|| purchase.getListPromotions() != null) {

				Purchase purchaseAux = entityManager.find(Purchase.class,
						purchase.getId());
				double soma = 0;

				if (purchase.getListProducts() != null) {
					if (purchaseAux.getListProducts() != purchase
							.getListProducts()) {
						Purchase purchaseAuxProductOne = incrementListProductInStock(purchaseAux);
						Purchase purchaseAuxProductTwo = decrementListProductInStock(purchase);
						if (purchaseAuxProductOne == null
								|| purchaseAuxProductTwo == null) {
							return null;
						}
						soma += purchaseAuxProductTwo.getFinalPrice();
						purchaseAux.setListProducts(purchase.getListProducts());
					}
				}
				if (purchase.getListPromotions() != null) {
					if (purchaseAux.getListPromotions() != purchase
							.getListPromotions()) {
						Purchase purchaseAuxPromotionOne = incrementListPromotionInStock(purchaseAux);
						Purchase purchaseAuxPromotionTwo = decrementListPromotionInStock(purchase);
						if (purchaseAuxPromotionOne == null
								|| purchaseAuxPromotionTwo == null) {
							return null;
						}
						soma += purchaseAuxPromotionTwo.getFinalPrice();
						purchaseAux.setListPromotions(purchaseAux
								.getListPromotions());
					}
				}

				entityManager.getTransaction().begin();
				purchaseAux.setCustomer(purchase.getCustomer());
				if (purchase.getFinalPrice() != purchaseAux.getFinalPrice()) {
					purchaseAux.setFinalPrice(purchase.getFinalPrice());
				} else {
					purchaseAux.setFinalPrice(soma);
				}
				entityManager.persist(purchaseAux);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Sale was successful");
				return purchase;
			}
			logger.log(
					Level.SEVERE,
					"Error purchasing product, probably the quantity and/or price "
							+ "promotion is 0 or product does not have sufficient or the promotion "
							+ "price is greater than the of the product.");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error to sell the product: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<Purchase> searchPurchaseByProduct(Product product) {
		try {
			List<Purchase> purchase = (List<Purchase>) entityManager
					//.createQuery("select p from Purchase p join p.listProducts map where KEY(map).id_product = :id_product")
					//.createQuery("select p from Purchase p join p.listProducts map where KEY(map).id_product = 1").getResultList();
					//.setParameter("id_product", product.getId()).getResultList();
					.createQuery("select p from Purchase p").getResultList();
			logger.log(Level.INFO, "Historical Product Purchases found!");
			return purchase;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error searching purchase.", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized List<Purchase> searchPurchasesOfACustomer(
			Customer customer) {

		try {
			List<Purchase> list = entityManager
					.createQuery(
							"select p from Purchase p where p.customer = :customer")
					.setParameter("customer", customer.getId()).getResultList();
			logger.log(Level.INFO,
					"Historical Customer Purchases: " + customer.getName()
							+ " found!");
			return list;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching historical purchase of a Customer, probably the list does not exist",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized List<Purchase> getListPurchase() {

		try {
			List<Purchase> list = entityManager.createQuery(
					"SELECT p FROM Purchase p").getResultList();
			logger.log(Level.INFO, "List Purchase found!");
			return list;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching list purchase, probably the list does not exist: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;

		}
	}

	private synchronized Purchase decrementProductInStock(Purchase purchase,
			Product product) {
		try {
			if (product.getQuantity() > 0
					&& purchase.getListProducts().get(product) <= product
							.getQuantity()) {
				Product productAux = entityManager.find(Product.class,
						product.getId());
				productAux.setQuantity(productAux.getQuantity()
						- purchase.getListProducts().get(product));
				entityManager.merge(productAux);
				logger.log(Level.INFO, "change in the stock completed");
				return purchase;
			}
			logger.log(Level.SEVERE, "Problem in changing the stock");
			return null;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Problem in changing the stock of Product: "
							+ product.getName());
			return null;
		}
	}

	private synchronized Purchase decrementListProductInStock(Purchase purchase) {
		Set<Product> products = purchase.getListProducts().keySet();
		for (Product product : products) {
			sumOfThePricesOfProducts += (product.getPrice() * purchase
					.getListProducts().get(product));
			Purchase purchaseAux = decrementProductInStock(purchase, product);
			if (purchaseAux == null) {
				return null;
			}
		}
		purchase.setFinalPrice(sumOfThePricesOfProducts);
		sumOfThePricesOfProducts = 0;
		return purchase;
	}

	private synchronized Purchase decrementPromotionInStock(Purchase purchase,
			Promotion promotion) {
		try {
			if (purchase.getListPromotions().get(promotion) <= promotion
					.getQuantityProductPromotion()) {
				Promotion promotionAux = entityManager.find(Promotion.class,
						promotion.getId());
				promotionAux.setQuantityProductPromotion(promotionAux
						.getQuantityProductPromotion()
						- purchase.getListPromotions().get(promotion));
				entityManager.merge(promotionAux);
				logger.log(Level.INFO, "change in the stock completed");
				return purchase;
			}
			logger.log(Level.SEVERE, "Problem in changing the stock");
			return null;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Problem in changing the stock of Promotion: "
							+ promotion.getName());
			return null;
		}
	}

	private synchronized Purchase decrementListPromotionInStock(
			Purchase purchase) {
		Set<Promotion> promotions = purchase.getListPromotions().keySet();

		for (Promotion promotion : promotions) {
			sumOfThePricesOfPromotions += (promotion.getPromotionalPrice() * purchase
					.getListPromotions().get(promotion));
			Purchase purchaseAux = decrementPromotionInStock(purchase,
					promotion);
			if (purchaseAux == null) {
				return null;
			}
		}
		purchase.setFinalPrice(sumOfThePricesOfPromotions);
		sumOfThePricesOfPromotions = 0;
		return purchase;
	}

	private synchronized Purchase incrementProductInStock(Purchase purchase,
			Product product) {
		try {
			Product productAux = entityManager.find(Product.class,
					product.getId());
			productAux.setQuantity(productAux.getQuantity()
					+ purchase.getListProducts().get(product));
			entityManager.merge(productAux);
			logger.log(Level.INFO, "change in the stock completed");
			return purchase;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Problem in changing the stock of Product: "
							+ product.getName());
			return null;
		}
	}

	private synchronized Purchase incrementListProductInStock(Purchase purchase) {
		Set<Product> products = purchase.getListProducts().keySet();
		for (Product product : products) {
			Purchase purchaseAux = incrementProductInStock(purchase, product);
			if (purchaseAux == null) {
				return null;
			}
		}
		return purchase;
	}

	private synchronized Purchase incrementPromotionInStock(Purchase purchase,
			Promotion promotion) {
		try {
			Promotion promotionAux = entityManager.find(Promotion.class,
					promotion.getId());
			promotionAux.setQuantityProductPromotion(promotionAux
					.getQuantityProductPromotion()
					+ purchase.getListPromotions().get(promotion));
			entityManager.merge(promotionAux);
			logger.log(Level.INFO, "change in the stock of Promotion: "
					+ promotion.getName() + " completed");
			return purchase;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Problem in changing the stock of Promotion: "
							+ promotion.getName());
			return null;
		}
	}

	private synchronized Purchase incrementListPromotionInStock(
			Purchase purchase) {
		Set<Promotion> promotions = purchase.getListPromotions().keySet();
		for (Promotion promotion : promotions) {
			Purchase purchaseAux = incrementPromotionInStock(purchase,
					promotion);
			if (purchaseAux == null) {
				return null;
			}
		}
		return purchase;
	}
}
