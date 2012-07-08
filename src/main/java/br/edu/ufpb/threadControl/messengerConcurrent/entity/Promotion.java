package br.edu.ufpb.threadControl.messengerConcurrent.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity Promotion
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0
 * 
 *          Copyright (C) 2012 Diego Sousa de Azevedo
 */

@Entity
@Table(name = "promotions")
@XmlRootElement
public class Promotion implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_promotion;
	@Column(unique = true, nullable = false)
	private String name;
	@ManyToMany
	@JoinTable(name = "products_in_Promotion", joinColumns = { @JoinColumn(name = "id_promotion", referencedColumnName = "id_promotion") }, inverseJoinColumns = { @JoinColumn(name = "id_product", referencedColumnName = "id_product") })
	private List<Product> listProducts = null;
	@Column(nullable = false, name = "stock_of_products_in_promotion")
	private int quantityProductPromotion;
	@Column(nullable = false, name = "promotional_price")
	private double promotionalPrice;
	@Column(nullable = false)
	private boolean isActive = true;

	/**
	 * @param id_promotion
	 * @param products
	 * @param quantityProductPromotion
	 * @param promotionalPrice
	 */

	public Promotion(String name, List<Product> listProducts,
			double promotionalPrice, int quantityProductPromotion) {
		this.name = name;
		this.listProducts = listProducts;
		this.quantityProductPromotion = quantityProductPromotion;
		this.promotionalPrice = promotionalPrice;
	}

	public Promotion() {
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id_promotion;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id_promotion = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nome
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the listProducts
	 */
	public List<Product> getListProducts() {
		return listProducts;
	}

	/**
	 * @param listProducts
	 *            the listProducts to set
	 */
	public void setListProducts(List<Product> listProducts) {
		this.listProducts = listProducts;
	}

	/**
	 * @return the quantityProductPromotion
	 */
	public int getQuantityProductPromotion() {
		return quantityProductPromotion;
	}

	/**
	 * @param quantityProductPromotion
	 *            the quantityProductPromotion to set
	 */
	public void setQuantityProductPromotion(int quantityProductPromotion) {
		this.quantityProductPromotion = quantityProductPromotion;
	}

	/**
	 * @return the promotionalPrice
	 */
	public double getPromotionalPrice() {
		return promotionalPrice;
	}

	/**
	 * @param promotionalPrice
	 *            the promotionalPrice to set
	 */
	public void setPromotionalPrice(double promotionalPrice) {
		this.promotionalPrice = promotionalPrice;
	}

	/**
	 * @return the isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {

		return "Promotion Name: " + name + "\nId Promotion: " + id_promotion
				+ "\nQuantity Products in Promotion: "
				+ quantityProductPromotion + "\nPromotional Price: "
				+ promotionalPrice + "\n\nProduct Data:"
				+ listProducts.toString() + "\n\n";
	}
}
