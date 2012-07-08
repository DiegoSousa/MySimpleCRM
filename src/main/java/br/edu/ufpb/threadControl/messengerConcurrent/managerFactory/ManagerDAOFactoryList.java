package br.edu.ufpb.threadControl.messengerConcurrent.managerFactory;

import br.edu.ufpb.threadControl.messengerConcurrent.dao.CustomerDAOList;
import br.edu.ufpb.threadControl.messengerConcurrent.dao.ProductDAOList;
import br.edu.ufpb.threadControl.messengerConcurrent.dao.PromotionDAOList;
import br.edu.ufpb.threadControl.messengerConcurrent.dao.PurchaseDAOList;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.ICustomerManager;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IProductManager;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPromotionManager;
import br.edu.ufpb.threadControl.messengerConcurrent.manager.IPurchaseManager;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */
public class ManagerDAOFactoryList implements ManagerDAOFactory {

	public ICustomerManager getCustomerDao() {
		return new CustomerDAOList();
	}

	public IPromotionManager getPromotionDao() {
		return new PromotionDAOList();
	}

	public IProductManager getProductDao() {
		return new ProductDAOList();
	}

	public IPurchaseManager getPurchaseDao() {
		return new PurchaseDAOList();
	}

}
