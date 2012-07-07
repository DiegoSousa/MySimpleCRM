/**
 * 
 */
package br.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.concurrent.BlockingQueue;

import br.ufpb.threadControl.messengerConcurrent.entity.Product;
import br.ufpb.threadControl.messengerConcurrent.manager.IProductManager;

/**
 * Runnable Search Product By name.
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableSearchProductById implements Runnable {
	private IProductManager productCrud;
	private Long id;
	private BlockingQueue<Product> list;

	public RunnableSearchProductById(Long id, BlockingQueue<Product> list, IProductManager iProductManager) {
		this.productCrud = iProductManager;
		this.list = list;
		this.id = id;

	}

	@Override
	public void run() {
		try {
			list.put(productCrud.searchProductById(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
