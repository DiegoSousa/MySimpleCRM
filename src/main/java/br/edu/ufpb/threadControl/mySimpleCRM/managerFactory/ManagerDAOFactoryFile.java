package br.edu.ufpb.threadControl.mySimpleCRM.managerFactory;

import br.edu.ufpb.threadControl.mySimpleCRM.dao.CustomerDAOFile;
import br.edu.ufpb.threadControl.mySimpleCRM.dao.ProductDAOFile;
import br.edu.ufpb.threadControl.mySimpleCRM.dao.PromotionDAOFile;
import br.edu.ufpb.threadControl.mySimpleCRM.dao.PurchaseDAOFile;
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
public class ManagerDAOFactoryFile implements ManagerDAOFactory {

	public ICustomerManager getCustomerDao() {
		return new CustomerDAOFile();
	}

	public IPromotionManager getPromotionDao() {
		return new PromotionDAOFile();
	}

	public IProductManager getProductDao() {
		return new ProductDAOFile();
	}

	public IPurchaseManager getPurchaseDao() {
		return new PurchaseDAOFile();
	}

}
