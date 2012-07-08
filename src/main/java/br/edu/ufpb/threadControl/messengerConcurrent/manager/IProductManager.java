package br.edu.ufpb.threadControl.messengerConcurrent.manager;

import java.util.List;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;

/**
 * Interface Product Manager
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 
 * 
 * Copyright (C) 2012 Diego Sousa de Azevedo
 */

public interface IProductManager {

	/**
	 * @param product
	 * @return
	 */
	Product addProduct(Product product);

	/**
	 * @param nameProduct
	 * @return
	 */
	Product restoreProduct(String nameProduct);

	/**
	 * @param product
	 * @return
	 */
	Product removeProduct(Product product);

	/**
	 * @param product
	 * @return
	 */
	Product editProduct(Product product);

	/**
	 * @param id
	 * @return
	 */
	Product searchProductById(long id);

	/**
	 * @param name
	 * @return
	 */
	Product searchProductByName(String name);	

	/**
	 * @return
	 */
	List<Product> getListProduct();
		


}