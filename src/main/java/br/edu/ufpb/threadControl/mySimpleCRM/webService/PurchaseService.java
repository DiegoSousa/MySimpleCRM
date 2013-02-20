package br.edu.ufpb.threadControl.mySimpleCRM.webService;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Purchase;
import br.edu.ufpb.threadControl.mySimpleCRM.managerFactory.ManagerDAOFactoryJPA;

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
	@Path(value = "/addpurchase")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addPurchase(Purchase purchase) {
		facade.addPurchase(purchase);
	}

	@DELETE
	@Path(value = "/removepurchase")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removePurchase(Purchase purchase) {
		facade.removePurchase(purchase);
	}

	@PUT
	@Path(value = "/editpurchase")
	@Consumes(MediaType.APPLICATION_JSON)
	public void editPurchase(Purchase purchase) {
		facade.editPurchase(purchase);
	}

	@GET
	@Path("/getlistofpurchase")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Purchase> getListOfPurchase() {
		BlockingQueue<List<Purchase>> list = new LinkedBlockingQueue<List<Purchase>>();
		List<Purchase> listAux = null;
		facade.getListOfPurchase(list);
		try {
			listAux = list.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return listAux;
	}
}
