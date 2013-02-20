package br.edu.ufpb.threadControl.mySimpleCRM.managerFactory;

import br.edu.ufpb.threadControl.mySimpleCRM.dao.CustomerDAOJPA;
import br.edu.ufpb.threadControl.mySimpleCRM.dao.ProductDAOJPA;
import br.edu.ufpb.threadControl.mySimpleCRM.dao.PromotionDAOJPA;
import br.edu.ufpb.threadControl.mySimpleCRM.dao.PurchaseDAOJPA;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.ICustomerManager;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IProductManager;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPromotionManager;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.IPurchaseManager;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */
public class ManagerDAOFactoryJPA implements ManagerDAOFactory {

	public ICustomerManager getCustomerDao() {
		return new CustomerDAOJPA();
	}

	public IPromotionManager getPromotionDao() {
		return new PromotionDAOJPA();
	}

	public IProductManager getProductDao() {
		return new ProductDAOJPA();
	}

	public IPurchaseManager getPurchaseDao() {
		return new PurchaseDAOJPA();
	}

}
