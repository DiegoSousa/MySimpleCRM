package br.edu.ufpb.threadControl.mySimpleCRM.dao;

import java.util.List;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IProductManager;

/**
 * Implementation of Product DAO with List
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */
public class ProductDAOFile implements IProductManager {

	@Override
	public Product addProduct(Product product) {
		return null;
	}
	
	@Override
	public Product removeProduct(Product product) {
		return null;
	}

	@Override
	public Product restoreProduct(String nameProduct) {
		return null;
	}

	@Override
	public Product editProduct(Product product) {
		return null;
	}

	@Override
	public Product searchProductById(long id) {
		return null;
	}

	@Override
	public Product searchProductByName(String name) {
		return null;
	}

	@Override
	public List<Product> getListProduct() {
		return null;
	}

	

}
