/**
 * 
 */
package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IProductManager;

/**
 * Runnable Edit Product
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableEditProduct implements Runnable {
	private IProductManager iproductManager;
	private Product product;

	public RunnableEditProduct(Product product, IProductManager iProductManager) {
		this.iproductManager = iProductManager;
		this.product = product;
	}

	@Override
	public void run() {
		try {
			this.iproductManager.editProduct(product);
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
}