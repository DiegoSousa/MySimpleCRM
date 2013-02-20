package br.edu.ufpb.threadControl.mySimpleCRM.dao;

/**
 * Implementation of Promotion DAO with JPA
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPromotionManager;
import br.edu.ufpb.threadControl.mySimpleCRM.util.HibernateUtil;

public class PromotionDAOJPA implements IPromotionManager {

	private Logger logger;
	private EntityManager entityManager;
	private double sumOfThePricesOfTheProducts = 0;

	public PromotionDAOJPA() {

		this.entityManager = HibernateUtil.getInstance().getFactory()
				.createEntityManager();
		this.logger = Logger.getLogger(PromotionDAOJPA.class.getName());
	}

	@Override
	public synchronized Promotion addPromotion(Promotion promotion) {
		try {
			if (promotion.getListProducts().size() > 0
					&& promotion.getQuantityProductPromotion() > 0
					&& promotion.getPromotionalPrice() > 0
					&& promotion.getName() != "") {
				Promotion promotionAux = decrementListProductInStock(promotion);				
				if (promotionAux == null) {
					return null;
				}				

				promotion.setName(promotion.getName().toUpperCase());
				entityManager.getTransaction().begin();
				entityManager.persist(promotion);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Promotion created with successfully!");
				return promotion;
			}
			logger.log(
					Level.SEVERE,
					"Error adding promotion, probably the quantity and/or price "
							+ "promotion is 0 or product does not have sufficient or the promotion "
							+ "price is greater than the of the product.");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error creating promotion: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Promotion removePromotion(Promotion promotion) {
		try {

			Promotion promotionAux = incrementListProductInStock(promotion);
			Promotion promotionAuxTwo = searchPromotionById(promotion.getId());

			if (promotionAuxTwo != null && promotionAux != null) {
				promotionAuxTwo.setIsActive(false);
				entityManager.merge(promotionAuxTwo);
				entityManager.getTransaction().begin();
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Promotion removed with successfully!");
				return promotion;
			}
			logger.log(Level.SEVERE, "Error removing Promotion");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error removing promotion: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Promotion restorePromotion(String namePromotion) {
		try {
			Promotion promotionAux = searchPromotionDetached(namePromotion
					.toUpperCase());
			if (promotionAux != null) {
				promotionAux.setIsActive(true);
				for (Product product : promotionAux.getListProducts()) {
					if (product.getIsActive() != true) {
						Product productAux = entityManager.find(Product.class,
								product.getId());
						productAux.setIsActive(true);
						entityManager.getTransaction().begin();
						entityManager.merge(productAux);
						entityManager.getTransaction().commit();
					}
					Promotion promotionAuxTwo = decrementProductInStock(
							promotionAux, product);
					if (promotionAuxTwo == null) {
						return null;
					}
				}
				entityManager.getTransaction().begin();
				entityManager.merge(promotionAux);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Promotion: " + promotionAux.getName()
						+ " successfully activaded!");
				return promotionAux;
			}
			logger.log(Level.SEVERE,
					"Promotion not Found or Promotion already active!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error restoring Promotion");
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * Only you can edit quantity and price.
	 * 
	 * 
	 */

	@Override
	public synchronized Promotion editPromotion(Promotion promotion) {
		try {
			Promotion promotionAux = searchPromotionById(promotion.getId());

			if (promotion.getQuantityProductPromotion() > 0
					&& promotion.getPromotionalPrice() > 0
					&& promotion.getListProducts().size() > 0
					&& promotion.getName() != "" 
					&& promotionAux != null) {
				
				Promotion promotionAuxTwo = incrementListProductInStock(promotionAux);
				Promotion promotionAuxThree = decrementListProductInStock(promotion);
				if (promotionAuxTwo == null || promotionAuxThree == null) {
					return null;
				}
				
				promotionAux.setName(promotion.getName().toUpperCase());
				promotionAux.setListProducts(promotion.getListProducts());
				promotionAux.setPromotionalPrice(promotion
						.getPromotionalPrice());
				promotionAux.setQuantityProductPromotion(promotion
						.getQuantityProductPromotion());
				entityManager.getTransaction().begin();
				entityManager.merge(promotionAux);
				entityManager.getTransaction().commit();
				return promotionAux;
			}
			logger.log(
					Level.SEVERE,
					"Error editing promotion, probably the quantity and/or price "
							+ "promotion is 0 or product does not have sufficient or the promotion "
							+ "price is greater than the of the product.");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error creating promotion: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Promotion searchPromotionById(long id) {
		try {
			Promotion promotion = entityManager.find(Promotion.class, id);
			if (promotion.getIsActive() == true) {
				logger.log(Level.INFO, "Promotion: " + promotion.getName()
						+ " Found!");
				return promotion;
			}
			logger.log(Level.SEVERE, "Promotion not found!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error searching promotion: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Promotion searchPromotionByName(String name) {
		try {
			Promotion promotion = (Promotion) entityManager
					.createQuery(
							"select pr from Promotion pr where pr.isActive = true and pr.name = :name")
					.setParameter("name", name.toUpperCase()).getSingleResult();
			logger.log(Level.INFO, "Promotion: " + promotion.getName()
					+ " Found!");
			return promotion;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching promotion, promoting probably does not exist: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	private synchronized Promotion searchPromotionDetached(String name) {
		try {
			Promotion promotion = (Promotion) entityManager
					.createQuery(
							"select pr from Promotion pr where pr.isActive = false and pr.name = :name")
					.setParameter("name", name.toUpperCase()).getSingleResult();
			logger.log(Level.INFO, "Promotion: " + promotion.getName()
					+ " Found!");
			return promotion;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching promotion, promoting probably does not exist: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<Promotion> searchPromotionByProduct(Product product) {
		try {
			List<Promotion> list = entityManager
					.createQuery(
							"select pr from Promotion pr where pr.isActive = true and :product member of pr.listProducts")
					.setParameter("product", product).getResultList();
			logger.log(Level.INFO, "Product in Promotion: " + product.getName()
					+ " Found!");
			return list;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching promotion, promoting probably does not exist: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<Promotion> getListPromotion() {
		try {
			List<Promotion> list = entityManager
					.createQuery(
							"select pr from Promotion pr where pr.isActive=true order by pr.name")
					.getResultList();
			logger.log(Level.INFO, "List Promotion found!");
			return list;
		} catch (Exception exception) {
			logger.log(Level.SEVERE,
					"Error listing promotion, probably the list is empty: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	private synchronized Promotion decrementProductInStock(Promotion promotion,
			Product product) {
		try {
			if (product.getQuantity() > 0
					&& promotion.getQuantityProductPromotion() <= product
							.getQuantity()) {
				Product productAux = entityManager.find(Product.class,
						product.getId());
				productAux.setQuantity(productAux.getQuantity()
						- promotion.getQuantityProductPromotion());
				entityManager.merge(productAux);
				logger.log(Level.INFO, "change in the stock completed");
				return promotion;
			}
			logger.log(Level.SEVERE, "Problem in changing the stock");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE,
					"Problem in the entityManager decrementProductInStock");
			return null;
		}
	}
	
	private synchronized Promotion decrementListProductInStock(
			Promotion promotion) {
		for (Product product : promotion.getListProducts()) {
			sumOfThePricesOfTheProducts += product.getPrice();
			Promotion promotionAux = decrementProductInStock(promotion, product);
			if (promotionAux == null) {
				return null;
			}			
		}

		if (promotion.getPromotionalPrice() >= sumOfThePricesOfTheProducts) {
			logger.log(
					Level.SEVERE,
					"Price of the product is less than the price of promoting and/or quantity of items in stock is less than the quantity of product promotion and/or Promotion can only have a product or a promotion list.");
			return null;
		}		
		sumOfThePricesOfTheProducts = 0;
		return promotion;
	}

	private synchronized Promotion incrementProductInStock(Promotion promotion,
			Product product) {
		try {
			Product productAux = entityManager.find(Product.class,
					product.getId());
			if (productAux != null) {
				productAux.setQuantity(productAux.getQuantity()
						+ promotion.getQuantityProductPromotion());
				entityManager.merge(productAux);
				logger.log(Level.INFO, "change in the stock completed");
				return promotion;
			}
			logger.log(Level.INFO, "Problem in changing the stock");
			return null;
		} catch (Exception exception) {
			logger.log(Level.INFO, "Problem in changing the stock");
			return null;
		}
	}

	private synchronized Promotion incrementListProductInStock(
			Promotion promotion) {
		for (Product product : promotion.getListProducts()) {
			Promotion promotionAux = incrementProductInStock(promotion, product);
			if (promotionAux == null) {
				return null;
			}
		}
		return promotion;
	}
}
