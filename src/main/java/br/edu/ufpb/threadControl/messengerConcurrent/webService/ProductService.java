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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.edu.ufpb.threadControl.messengerConcurrent.controller.Facade;
import br.edu.ufpb.threadControl.messengerConcurrent.entity.Product;
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

@Path(value = "/product")
public class ProductService {

	Facade facade = Facade.getInstance(new ManagerDAOFactoryJPA());

	@POST
	@Path("/addproduct")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addProduct(Product product) {
		facade.addProduct(product);
	}

	@DELETE
	@Path("/removerproduct")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removerProduct(Product product) {
		facade.removeProduct(product);
	}

	@PUT
	@Path("/restoreproduct")
	@Consumes(MediaType.APPLICATION_JSON)
	public void restoreProduct(String nameProduct) {
		facade.restoreProduct(nameProduct);
	}

	@PUT
	@Path("/editproduct")
	@Consumes(MediaType.APPLICATION_JSON)
	public void editProduct(Product product) {
		facade.editProduct(product);
	}

	@GET
	@Path("/searchproductbyid")
	@Produces(MediaType.APPLICATION_JSON)
	public Product searchProductById(String id) {
		BlockingQueue<Product> listproduct = new LinkedBlockingQueue<Product>();
		Product productAux = null;

		try {
			JSONObject jsonObject = new JSONObject(id);
			facade.searchProductById(jsonObject.getLong("id"), listproduct);
			productAux = listproduct.take();
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		return productAux;
	}

	@GET
	@Path("/searchproductbyname")
	@Produces(MediaType.APPLICATION_JSON)
	public Product searchProductByName(String name) {
		BlockingQueue<Product> listproduct = new LinkedBlockingQueue<Product>();
		Product productAux = null;

		try {
			JSONObject jsonObject = new JSONObject(name);
			facade.searchProductByName(jsonObject.getString("name"),
					listproduct);
			productAux = listproduct.take();
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		return productAux;
	}

	@GET
	@Path("/getlistofproduct")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getListProducts() {
		BlockingQueue<List<Product>> listproduct = new LinkedBlockingQueue<List<Product>>();

		facade.getListOfProduct(listproduct);

		List<Product> listAux = null;
		try {
			listAux = listproduct.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return listAux;
	}
}
