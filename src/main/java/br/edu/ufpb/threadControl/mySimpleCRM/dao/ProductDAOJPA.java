package br.edu.ufpb.threadControl.mySimpleCRM.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IProductManager;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPromotionManager;
import br.edu.ufpb.threadControl.mySimpleCRM.util.HibernateUtil;

/**
 * Implementation of Product DAO with JPA
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class ProductDAOJPA implements IProductManager {

	private Logger logger;
	private EntityManager entityManager;
	private IPromotionManager promotionManager;

	public ProductDAOJPA() {

		entityManager = HibernateUtil.getInstance().getFactory()
				.createEntityManager();
		this.logger = Logger.getLogger(ProductDAOJPA.class.getName());
		this.promotionManager = new PromotionDAOJPA();

	}

	@Override
	public synchronized Product addProduct(Product product) {
		try {
			if (product.getPrice() > 0 && product.getQuantity() > 0) {
				product.setName(product.getName().toUpperCase());
				entityManager.getTransaction().begin();
				entityManager.persist(product);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Product: " + product.getName()
						+ ",successfully added!");
				return product;
			}
			logger.log(Level.SEVERE, "Error. " + product.getName()
					+ " price less or equals the 0 and/or " + product.getName()
					+ " Quantity less or equal the 0");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error adding product: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Product removeProduct(Product product) {

		try {
			entityManager.getTransaction().begin();
			Product productAux = entityManager.find(Product.class,
					product.getId());
			productAux.setIsActive(false);
			entityManager.merge(productAux);
			List<Promotion> list = promotionManager
					.searchPromotionByProduct(productAux);
			if (list != null) {
				for (Promotion promotion : list) {
					promotionManager.removePromotion(promotion);
				}
			}
			entityManager.getTransaction().commit();
			logger.log(Level.INFO, "Product: " + product.getName()
					+ " ,successfully removed!");
			return product;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error removing product", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Product restoreProduct(String nameProduct) {
		Product productAux = searchProductDetachedByName(nameProduct
				.toUpperCase());
		try {
			if (productAux != null) {
				productAux.setIsActive(true);
				entityManager.getTransaction().begin();
				entityManager.merge(productAux);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Product: " + productAux.getName()
						+ " successfully activaded!");
				return productAux;
			}
			logger.log(Level.SEVERE,
					"Product not Found or Product already active!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error restoring Product");
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Product editProduct(Product product) {
		double sumOfThePriceOfProducts = 0;
		try {
			if (product.getPrice() > 0 && product.getQuantity() > 0) {
				Product productAux = searchProductById(product.getId());

				// 1° Condition - Be changing the price? Find if you promote!
				if (productAux != null
						&& product.getPrice() != productAux.getPrice()) {
					List<Promotion> list = promotionManager
							.searchPromotionByProduct(productAux);
					if (list != null) {
						for (Promotion promotion : list) {
							// 2° Condition - verifies that the price offer is
							// less than the sum of the prices of the products.
							for (Product productAuxOne : promotion
									.getListProducts()) {
								sumOfThePriceOfProducts += productAuxOne
										.getPrice();
							}
							if (promotion.getPromotionalPrice() >= sumOfThePriceOfProducts) {
								logger.log(Level.SEVERE,
										"Products price can not be less that the Promotion Price");
								return null;
							}
						}
					}
				}
				productAux.setName(product.getName().toUpperCase());
				productAux.setQuantity(product.getQuantity());
				productAux.setIsActive(product.getIsActive());
				productAux.setPrice(product.getPrice());
				entityManager.getTransaction().begin();
				entityManager.merge(productAux);
				entityManager.getTransaction().commit();
				logger.log(Level.INFO, "Product: " + productAux.getName()
						+ " Code: " + productAux.getId() + " updated!");
				return product;
			}
			logger.log(Level.SEVERE,
					"Error. Probably the quantity and/or price of the product"
							+ product.getName() + " is below 0");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error editing product: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Product searchProductById(long id) {
		try {
			Product product = entityManager.find(Product.class, id);
			if (product.getIsActive() == true) {
				logger.log(Level.INFO, "product: " + product.getName()
						+ " found!");
				return product;
			}
			logger.log(Level.SEVERE, "product not found!");
			return null;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching product, probably the Product does not exist:  ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	public synchronized Product searchProductByName(String name) {
		try {
			Product product = (Product) entityManager
					.createQuery(
							"select p from Product p where p.name = :name and p.isActive=true")
					.setParameter("name", name.toUpperCase()).getSingleResult();
			logger.log(Level.INFO, "Product: " + product.getName() + " found!");
			return product;
		} catch (Exception exception) {
			logger.log(Level.SEVERE,
					"Error searching product, probably the product: ''" + name
							+ "'' does not exist: ", exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	private synchronized Product searchProductDetachedByName(String name) {
		try {
			Product product = (Product) entityManager
					.createQuery(
							"select p from Product p where p.name = :name and p.isActive=false")
					.setParameter("name", name.toUpperCase()).getSingleResult();
			logger.log(Level.INFO, "Product: " + product.getName() + " found!");
			return product;
		} catch (Exception exception) {
			logger.log(
					Level.SEVERE,
					"Error searching product, probably the product does not exist: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized List<Product> getListProduct() {
		try {
			List<Product> list = entityManager
					.createQuery(
							"select p from Product p where p.isActive = true order by p.name")
					.getResultList();
			logger.log(Level.INFO, "List Product found!");
			return list;
		} catch (Exception exception) {
			logger.log(Level.SEVERE,
					"Error listing product, probably the list is empty: ",
					exception);
			if (entityManager.getTransaction().isActive() == true) {
				entityManager.getTransaction().rollback();
			}
			return null;
		}
	}

}
