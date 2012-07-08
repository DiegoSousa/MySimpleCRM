package br.edu.ufpb.threadControl.messengerConcurrent.dao;

import java.util.List;

import br.edu.ufpb.threadControl.messengerConcurrent.entity.Customer;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;

/**
 * Implementation of Customer DAO with List
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */
public class CustomerDAOList implements ICustomerManager{

	@Override
	public Customer addCustomer(Customer customer) {
		return null;
	}

	@Override
	public Customer removeCustomer(Customer customer) {
		return null;
	}

	@Override
	public Customer restoreCustomer(String cpf){
		return null;
	}

	@Override
	public Customer editCustomer(Customer customer) {
		return null;
	}

	@Override
	public Customer searchCustomerById(long id) {
		return null;
	}

	@Override
	public Customer searchCustomerByLogin(String login) {
		return null;
	}

	@Override
	public Customer searchCustomerByCpf(String cpf) {
		return null;
	}

	@Override
	public List<Customer> getListOfCustomers() {
		return null;
	}

	

}
