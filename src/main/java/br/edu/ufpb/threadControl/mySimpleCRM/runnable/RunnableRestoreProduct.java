package br.edu.ufpb.threadControl.mySimpleCRM.runnable;

import br.edu.ufpb.threadControl.mySimpleCRM.manager.IProductManager;

/**
 * Runnable Restore Product
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableRestoreProduct implements Runnable {

	private String name;
	private IProductManager iProductManager;

	public RunnableRestoreProduct(String name, IProductManager iProductManager) {
		this.name = name;
		this.iProductManager = iProductManager;
	}

	@Override
	public void run() {
		iProductManager.restoreProduct(name);
	}

}
