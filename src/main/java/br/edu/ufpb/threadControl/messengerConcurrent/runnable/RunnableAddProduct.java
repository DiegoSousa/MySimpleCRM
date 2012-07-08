/**
 * 
 */
package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IProductManager;

/**
 * Runnable Adder Product
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableAddProduct implements Runnable {	
	private Product product;
	private IProductManager productCrud;

	public RunnableAddProduct(Product product, IProductManager iProductManager) {
		productCrud = iProductManager;
		this.product = product;		
	}

	@Override
	public void run() {
		try {
			productCrud.addProduct(product);
		} catch (Exception e) {			
			e.printStackTrace();	
		}
	}
}
