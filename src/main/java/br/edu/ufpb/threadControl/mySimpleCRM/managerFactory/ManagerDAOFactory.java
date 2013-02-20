package br.edu.ufpb.threadControl.mySimpleCRM.managerFactory;

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
public interface ManagerDAOFactory {

	public ICustomerManager getCustomerDao();

	public IPromotionManager getPromotionDao();

	public IProductManager getProductDao();

	public IPurchaseManager getPurchaseDao();

}
