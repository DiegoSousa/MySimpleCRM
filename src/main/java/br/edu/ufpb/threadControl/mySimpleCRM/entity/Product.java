package br.edu.ufpb.threadControl.mySimpleCRM.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity Product
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0
 * 
 *          Copyright (C) 2012 Diego Sousa de Azevedo
 */

@Entity
@Table(name = "products")
@XmlRootElement
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_product;
	@Column(nullable = false, unique = true)
	private String name;
	@Column(nullable = false)
	private double price;
	@Column(nullable = false)
	private int quantity;
	@Column(nullable = false)
	private boolean isActive = true;

	public Product(String name, double price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public Product() {
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id_product;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id_product = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 * @throws Exception
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 * @throws Exception
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id_product ^ (id_product >>> 32));
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
		Product other = (Product) obj;
		if (id_product != other.id_product)
			return false;
		return true;
	}

	public String toString() {

		return "\nProduct Name: " + this.name + "\nProduct Code: "
				+ this.id_product + "\nPrice: " + this.price + "\nQuantity: "
				+ this.quantity;
	}

}
