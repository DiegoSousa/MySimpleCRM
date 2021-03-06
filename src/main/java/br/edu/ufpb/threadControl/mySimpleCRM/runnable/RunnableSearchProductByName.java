package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IProductManager;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */
public class RunnableSearchProductByName implements Runnable {

	private IProductManager iProductManager;
	private String name;
	private BlockingQueue<Product> list;

	public RunnableSearchProductByName(String name,
			BlockingQueue<Product> list, IProductManager iProductManager) {
		this.iProductManager = iProductManager;
		this.list = list;
		this.name = name;

	}

	@Override
	public void run() {
		try {
			list.put(iProductManager.searchProductByName(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
