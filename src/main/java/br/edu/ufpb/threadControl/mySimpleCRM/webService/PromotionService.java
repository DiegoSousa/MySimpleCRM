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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.edu.ufpb.threadControl.mySimpleCRM.controller.Facade;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Product;
import br.edu.ufpb.threadControl.mySimpleCRM.entity.Promotion;
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
	@Path("/restorepromotion")
	@Consumes(MediaType.APPLICATION_JSON)
	public void restorePromotion(String namePromotion) {
		facade.restorePromotion(namePromotion);
	}

	@PUT
	@Path("/editpromotion")
	@Consumes(MediaType.APPLICATION_JSON)
	public void editPromotion(Promotion promotion) {
		facade.editPromotion(promotion);
	}

	@GET
	@Path("/searchpromotionbyid")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
	public Promotion searchPromotionById(String id) throws Exception {
		BlockingQueue<Promotion> listPromotion = new LinkedBlockingQueue<Promotion>();
		Promotion promotionAux = null;
		try {
			JSONObject jsonObject = new JSONObject(id);
			facade.searchPromotionById(jsonObject.getLong("id"), listPromotion);
			promotionAux = listPromotion.take();
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return promotionAux;
	}

	@GET
	@Path("/searchpromotionbyname")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
	public Promotion searchPromotionByName(String namePromotion) {

		BlockingQueue<Promotion> listPromotion = new LinkedBlockingQueue<Promotion>();
		Promotion promotionAux = null;
		try {
			JSONObject jsonObject = new JSONObject(namePromotion);
			facade.searchPromotionByName(jsonObject.getString("namePromotion"),
					listPromotion);
			promotionAux = listPromotion.take();
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return promotionAux;
	}
	
	@GET
	@Path("/searchpromotionbyproduct")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
	public List<Promotion> searchPromotionByProduct(Product product) {

		BlockingQueue<List<Promotion>> listPromotion = new LinkedBlockingQueue<List<Promotion>>();
		List<Promotion> promotionAux = null;
		try {
			facade.searchPromotionByProduct(product, listPromotion);
			promotionAux = listPromotion.take();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return promotionAux;
	}

	@GET
	@Path("/getlistofpromotion")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Promotion> getListOfPromotion() {
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
