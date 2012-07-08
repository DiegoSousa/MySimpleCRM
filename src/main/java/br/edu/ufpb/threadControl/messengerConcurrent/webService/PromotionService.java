package br.edu.ufpb.threadControl.messengerConcurrent.webService;

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

import org.codehaus.jettison.json.JSONObject;

import br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Promotion;
import br.edu.ufpb.threadControl.messengerConcurrent.managerFactory.ManagerDAOFactoryJPA;

/**
 * Description Class
 * 
 * @author Diego Sousa, diego[at]diegosousa[dot]com
 * @version 0.0.1
 * @since
 * 
 *        Copyright (C) 2012
 */

@Path(value = "/promotion")
public class PromotionService {

	Facade facade = Facade.getInstance(new ManagerDAOFactoryJPA());

	@POST
	@Path("/addpromotion")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addPromotion(Promotion promotion) {		
		facade.addPromotion(promotion);
	}

	@DELETE
	@Path("/removerpromotion")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removerPromotion(Promotion promotion) {
		facade.removePromotion(promotion);
	}

	@PUT
	@Path("/editpromotion")
	@Consumes(MediaType.APPLICATION_JSON)
	public void editPromotion(Promotion promotion) {
		facade.editPromotion(promotion);
	}

	@GET
	@Path("/searchpromotionbyid")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Promotion searchPromotionById(String id) throws Exception {
		BlockingQueue<Promotion> listPromotion = new LinkedBlockingQueue<Promotion>();

		JSONObject jsonObject = new JSONObject(id);

		facade.searchPromotionById(jsonObject.getLong("id"), listPromotion);
		return listPromotion.take();
	}	

	@GET
	@Path("/getlistofpromotion")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Promotion> getListPromotions() {
		BlockingQueue<List<Promotion>> listpromotion = new LinkedBlockingQueue<List<Promotion>>();
		List<Promotion> listAux = null;

		facade.getListOfPromotion(listpromotion);
		try {
			listAux = listpromotion.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return listAux;
	}

}
