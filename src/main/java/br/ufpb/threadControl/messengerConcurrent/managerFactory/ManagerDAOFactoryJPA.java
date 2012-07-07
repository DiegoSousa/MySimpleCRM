package br.ufpb.threadControl.messengerConcurrent.managerFactory;

import br.ufpb.threadControl.messengerConcurrent.dao.CustomerDAOJPA;
import br.ufpb.threadControl.messengerConcurrent.dao.ProductDAOJPA;
import br.ufpb.threadControl.messengerConcurrent.dao.PromotionDAOJPA;
import br.ufpb.threadControl.messengerConcurrent.dao.PurchaseDAOJPA;
import br.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;
import br.ufpb.threadControl.messengerConcurrent.manager.IProductManager;
import br.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;
import br.ufpb.threadControl.messengerConcurrent.manager.IPurchaseManager;

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
