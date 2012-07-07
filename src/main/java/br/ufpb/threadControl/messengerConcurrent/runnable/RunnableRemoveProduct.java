/**
 * 
 */
package br.ufpb.threadControl.messengerConcurrent.runnable;

import br.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.ufpb.threadControl.messengerConcurrent.manager.IProductManager;

/**
 * Runnable Remove Product
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */
public class RunnableRemoveProduct implements Runnable {
	private IProductManager iProductManager;
	private Product product;

	public RunnableRemoveProduct(Product product, IProductManager iProductManager) {
		this.iProductManager = iProductManager;
		this.product = product;
	}

	@Override
	public void run() {
		try {			
			this.iProductManager.removeProduct(product);
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
}
