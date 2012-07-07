package br.ufpb.threadControl.messengerConcurrent.webService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.ufpb.threadControl.messengerConcurrent.controller.Facade;
import br.ufpb.threadControl.messengerConcurrent.entity.Purchase;
import br.ufpb.threadControl.messengerConcurrent.managerFactory.ManagerDAOFactoryJPA;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */

@Path(value = "/purchase")
public class PurchaseService {

	Facade facade = Facade.getInstance(new ManagerDAOFactoryJPA());

	@POST
	@Path(value = "/purchaseproduct")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Purchase purchaseProduct(Purchase purchase) {
		facade.addPurchase(purchase);
		return purchase;
	}

}
