/**
 * 
 */
package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IProductManager;

/**
 * Runnable Get List Product
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableGetListProduct implements Runnable {
	private IProductManager productCrud;
	private BlockingQueue<List<Product>> list;

	public RunnableGetListProduct(BlockingQueue<List<Product>> listProduct, IProductManager iProductManager) {
		this.productCrud = iProductManager;
		this.list = listProduct;
	}

	@Override
	public void run() {
		try {
			list.put(this.productCrud.getListProduct());
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
}