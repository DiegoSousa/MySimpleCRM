package br.edu.ufpb.threadControl.messengerConcurrent.runnable;

import java.util.concurrent.BlockingQueue;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 *
 * Copyright (C) 2012 
 */
public class RunnableSearchCustomerByCpf implements Runnable {


	private String cpf;
	private BlockingQueue<Customer> list;
	private ICustomerManager iCustomerManager;

	public RunnableSearchCustomerByCpf(String cpf,
			BlockingQueue<Customer> list, ICustomerManager iCustomerManager) {
		this.cpf = cpf;
		this.list = list;
		this.iCustomerManager = iCustomerManager;
	}

	@Override
	public void run() {
		try {
			list.put(iCustomerManager.searchCustomerByCpf(cpf));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
