package br.edu.ufpb.threadControl.mySimpleCRM.entity;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity Purchase
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0
 * 
 *          Copyright (C) 2012 Diego Sousa de Azevedo
 */

@Entity
@Table(name = "purchases")
@XmlRootElement
public class Purchase implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_purchase;
	@ManyToOne
	@JoinColumn(name = "id_customer")
	private Customer customer;
	@ElementCollection
	@CollectionTable(name = "purchased_products", joinColumns = { @JoinColumn(name = "id_purchaseFK", referencedColumnName = "id_purchase") })
	@Column(name = "quantity_product")	
	@MapKeyJoinColumn(name = "id_productsFK", referencedColumnName = "id_product", table = "Products", nullable = false)
	// Key (Object Product) - Product Purchase and Value (Integer) - is quantity
	// of product.
	private Map<Product,Integer > listProducts = null;
	@ElementCollection
	@CollectionTable(name = "purchased_promotions", joinColumns = { @JoinColumn(name = "id_purchaseFK", referencedColumnName = "id_purchase") })
	@Column(name = "quantity_promotion")
	@MapKeyJoinColumn(name = "id_promotionsFK", referencedColumnName = "id_promotion", table = "Promotions", nullable = false)
	// Key (Object Promotion) - Promotion Purchase and Value (Integer) - is
	// quantity of promotion.
	private Map<Promotion, Integer> listPromotions = null;
	@Column(nullable = false)
	private double finalPrice;

	public Purchase() {
	}

	public Purchase(Customer customer, Map<Product, Integer> listProducts,
			Map<Promotion, Integer> listPromotions) {
		this.customer = customer;
		this.listProducts = listProducts;
		this.listPromotions = listPromotions;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id_purchase;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id_purchase = id;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the listProducts
	 */
	public Map<Product, Integer> getListProducts() {
		return listProducts;
	}

	/**
	 * @param listProducts
	 *            the listProducts to set
	 */
	public void setListProducts(Map<Product, Integer> listProducts) {
		this.listProducts = listProducts;
	}

	/**
	 * @return the listPromotions
	 */
	public Map<Promotion, Integer> getListPromotions() {
		return listPromotions;
	}

	/**
	 * @param listPromotions
	 *            the listPromotions to set
	 */
	public void setListPromotions(Map<Promotion, Integer> listPromotions) {
		this.listPromotions = listPromotions;
	}

	/**
	 * @return the finalPrice
	 */
	public double getFinalPrice() {
		return finalPrice;
	}

	/**
	 * @param finalPrice
	 *            the finalPrice to set
	 */
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id_purchase ^ (id_purchase >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Purchase other = (Purchase) obj;
		if (id_purchase != other.id_purchase)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Purchase\nCode Purchase: "
				+ id_purchase
				+ "\nCustomer\n"
				+ customer
				+ (listProducts != null ? "\nProduct: " + listProducts : "")
				+ (listPromotions != null ? "\nPromotion: " + listPromotions
						: "") + "\nFinal Price: " + finalPrice;
	}

}
