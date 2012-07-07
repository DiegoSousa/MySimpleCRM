package br.ufpb.threadControl.messengerConcurrent.runnable;

import br.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;

/**
 * Runnable Restore Customer
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 1.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class RunnableRestoreCustomer implements Runnable {

	private ICustomerManager iCustomerManager;
	private String cpf;

	public RunnableRestoreCustomer(String cpf, ICustomerManager iCustomerManager) {
		this.cpf = cpf;
		this.iCustomerManager = iCustomerManager;
	}

	@Override
	public void run() {
		iCustomerManager.restoreCustomer(cpf);

	}

}
