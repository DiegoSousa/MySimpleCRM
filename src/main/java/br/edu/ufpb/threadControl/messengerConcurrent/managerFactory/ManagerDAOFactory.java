package br.edu.ufpb.threadControl.messengerConcurrent.managerFactory;

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
public interface ManagerDAOFactory {

	public ICustomerManager getCustomerDao();

	public IPromotionManager getPromotionDao();

	public IProductManager getProductDao();

	public IPurchaseManager getPurchaseDao();

}
