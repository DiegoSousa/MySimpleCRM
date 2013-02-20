package br.edu.ufpb.threadControl.mySimpleCRM.dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.edu.ufpb.threadControl.mySimpleCRM.entity.Customer;
import br.edu.ufpb.threadControl.mySimpleCRM.manager.ICustomerManager;

/**
 * Implementation of Customer DAO with File
 * 
 * @author Diego Sousa - www.diegosousa.com
 * @version 2.0 Copyright (C) 2012 Diego Sousa de Azevedo
 */

public class CustomerDAOFile implements ICustomerManager {

	private static String pathFile = "src/bd_file/customer.db";
	private Logger logger;
	private File file = null;
	private boolean cont = true;

	public CustomerDAOFile() {
		this.logger = Logger.getLogger(CustomerDAOFile.class.getName());
	}

	public synchronized static String getPath() {
		return pathFile;
	}

	private synchronized File getInstaceFile() {
//		try {
//			Thread.currentThread().join();
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		 if (cont != true) {
		 try {
		 wait();
		 } catch (InterruptedException e) {
		 e.printStackTrace();
		 }
		 }
		cont = false;
		return this.file = new File(pathFile);
	}

	/**
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer if the operation was successful or null if the
	 *         operation not successful.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful.
	 * 
	 *                Add Customer Object.
	 */

	@Override
	public synchronized Customer addCustomer(Customer customer) {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		File file = getInstaceFile();

		try {
			if (searchCustomerDetachedByCpf(customer.getCpf()) != null) {
				List<Customer> listOne = getListOfCustomers();
				for (Customer customerOne : listOne) {
					if (customerOne.getCpf() == customer.getCpf()) {
						listOne.remove(customerOne);
						for (Customer customerTwo : listOne) {
							if (customerTwo.getCpf() == customer.getCpf()
									|| customerTwo.getLogin() == customer
											.getLogin()
									|| customerTwo.getPhone() == customer
											.getPhone()) {
								logger.log(Level.SEVERE,
										"Cpf, Login or Phone already registered!");
								notify();
								return null;
							}
						}
						listOne.add(customer);
						file.delete();
						for (Customer customerThree : listOne) {
							fileOutputStream = new FileOutputStream(file, true);
							objectOutputStream = new ObjectOutputStream(
									fileOutputStream);
							objectOutputStream.writeObject(customerThree);
							try {
								objectOutputStream.close();
								fileOutputStream.close();
							} catch (IOException exceptionTwo) {
								logger.log(Level.SEVERE,
										"Error closing file: ", exceptionTwo);
							}
						}
						logger.log(Level.INFO,
								"Customer: " + customer.getName()
										+ " successfully added!");
						notify();
						return customer;
					}
				}
				logger.log(Level.SEVERE, "Unexpected error!");
				notify();
				return null;
			} else {
				for (Customer customerThree : getListOfCustomers()) {
					if (customerThree.getCpf() == customer.getCpf()
							|| customerThree.getLogin() == customer.getLogin()
							|| customerThree.getPhone() == customer.getPhone()) {
						logger.log(Level.SEVERE,
								"Cpf, Login or Phone already registered!");
						notify();
						return null;
					}
				}
				fileOutputStream = new FileOutputStream(file, true);
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(customer);
				try {
					objectOutputStream.close();
					fileOutputStream.close();
				} catch (IOException exceptionTwo) {
					logger.log(Level.SEVERE, "Error closing file: ",
							exceptionTwo);
				}
				logger.log(Level.INFO, "customer: " + customer.getName()
						+ " successfully added!");
				notify();
				return customer;
			}
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error creating customer: ", exception);
			notify();
			return null;
		}
	}

	/**
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer if the operation was successful or null if the
	 *         operation not successful.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                Remove Customer Object.
	 */

	@Override
	public synchronized Customer removeCustomer(Customer customer) {
		if (customer != null) {
			FileOutputStream fileOutputStream = null;
			ObjectOutputStream objectOutputStream = null;
			// File file = null;
			try {
				// file = new File(pathFile);
				List<Customer> list = getListOfCustomers();
				for (Customer customerAux : list) {
					if (customerAux.getId() == customer.getId()) {
						list.remove(customerAux);
						customerAux.setIsActive(false);
						list.add(customerAux);
					}
				}
				file.delete();// To clear.
				for (Customer customerAuxTwo : list) {
					try {
						// file = new File(pathFile);
						fileOutputStream = new FileOutputStream(
								getInstaceFile(), true);
						objectOutputStream = new ObjectOutputStream(
								fileOutputStream);
						objectOutputStream.writeObject(customerAuxTwo);
					} catch (Exception exception) {
						logger.log(Level.SEVERE, "Error removing customer: ",
								exception);
						notify();
						return null;
					} finally {
						try {
							objectOutputStream.close();
							fileOutputStream.close();
						} catch (IOException exceptionTwo) {
							logger.log(Level.SEVERE, "Error closing file: ",
									exceptionTwo);
						}
					}
				}
				logger.log(Level.INFO, "customer: " + customer.getName()
						+ " successfully removed!");
				notify();
				return customer;
			} catch (Exception exception) {
				logger.log(Level.SEVERE, "Error removing customer: ", exception);
				notify();
				return null;
			}
		}
		logger.log(Level.SEVERE, "Customer not found!");
		notify();
		return null;
	}

	/**
	 * @param String
	 *            cpf
	 * 
	 * @return Object customer if the operation was successful or null if the
	 *         operation not successful.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                Restore Customer Object.
	 */

	@Override
	public synchronized Customer restoreCustomer(String cpf) {
		Customer customerAuxDetached = searchCustomerDetachedByCpf(cpf);

		if (customerAuxDetached != null) {
			FileOutputStream fileOutputStream = null;
			ObjectOutputStream objectOutputStream = null;
			// File file = null;
			try {
				// file = new File(pathFile);
				List<Customer> list = getListOfCustomers();
				for (Customer customerAux : list) {
					if (customerAux.getId() == customerAuxDetached.getId()) {
						list.remove(customerAux);
						customerAux.setIsActive(true);
						list.add(customerAux);
					}
				}
				file.delete();// To clear.
				for (Customer customerAuxTwo : list) {
					try {
						// file = new File(pathFile);
						fileOutputStream = new FileOutputStream(
								getInstaceFile(), true);
						objectOutputStream = new ObjectOutputStream(
								fileOutputStream);
						objectOutputStream.writeObject(customerAuxTwo);
					} catch (Exception exception) {
						logger.log(Level.SEVERE, "Error restoring customer: ",
								exception);
						notify();
						return null;
					} finally {
						try {
							objectOutputStream.close();
							fileOutputStream.close();
						} catch (IOException exceptionTwo) {
							logger.log(Level.SEVERE, "Error closing file: ",
									exceptionTwo);
						}
					}
				}
				customerAuxDetached.setIsActive(true);
				logger.log(Level.INFO,
						"customer: " + customerAuxDetached.getName()
								+ " successfully restored!");
				notify();
				return customerAuxDetached;
			} catch (Exception exception) {
				logger.log(Level.SEVERE, "Error restoring customer: ",
						exception);
				notify();
				return null;
			}
		}
		logger.log(Level.SEVERE, "Customer not found!");
		notify();
		return null;
	}

	/**
	 * @param customer
	 *            Object customer
	 * 
	 * @return Object customer if the operation was successful or null if the
	 *         operation not successful.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                edit Customer Object.
	 */

	@Override
	public synchronized Customer editCustomer(Customer customer) {
		if (customer != null) {
			FileOutputStream fileOutputStream = null;
			ObjectOutputStream objectOutputStream = null;
			// File file = null;
			try {
				// file = new File(pathFile);
				List<Customer> list = getListOfCustomers();
				for (Customer customerAux : list) {
					if (customerAux.getId() == customer.getId()
							&& customerAux.getIsActive() == true) {
						list.remove(customerAux);
						customerAux.setName(customer.getName().toUpperCase());
						customerAux.setCpf(customer.getCpf());
						customerAux.setPhone(customer.getPhone());
						customerAux.setLogin(customer.getLogin());
						customerAux.setPassword(customer.getPassword());
						customerAux.setBirthday(customer.getBirthday());
						list.add(customerAux);
					}
				}
				file.delete();// To clear.
				for (Customer customerAuxTwo : list) {
					try {
						// file = new File(pathFile);
						fileOutputStream = new FileOutputStream(
								getInstaceFile(), true);
						objectOutputStream = new ObjectOutputStream(
								fileOutputStream);
						objectOutputStream.writeObject(customerAuxTwo);
					} catch (Exception exception) {
						logger.log(Level.SEVERE, "Error editing customer: ",
								exception);
						notify();
						return null;
					} finally {
						try {
							objectOutputStream.close();
							fileOutputStream.close();
						} catch (IOException exceptionTwo) {
							logger.log(Level.SEVERE, "Error closing file: ",
									exceptionTwo);
						}
					}
				}
				logger.log(Level.INFO, "customer: " + customer.getName()
						+ " successfully adited!");
				notify();
				return customer;
			} catch (Exception exception) {
				logger.log(Level.SEVERE, "Error editing customer: ", exception);
				notify();
				return null;
			}
		}
		logger.log(Level.SEVERE, "Customer not found!");
		notify();
		return null;
	}

	/**
	 * @param String
	 *            cpf
	 * 
	 * @return Object customer if customer exists or null if customer not
	 *         exists.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                Search Customer Detached By Cpf. The application can not
	 *                remove a customer, which is an attribute isActive is set
	 *                to false.
	 */

	private synchronized Customer searchCustomerDetachedByCpf(String cpf) {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		// File file = null;
		Customer customer = null;
		try {
			// file = new File(pathFile);
			fileInputStream = new FileInputStream(getInstaceFile());
			objectInputStream = new ObjectInputStream(fileInputStream);

			while ((customer = (Customer) objectInputStream.readObject()) != null) {
				if (customer.getCpf() == cpf && customer.getIsActive() == false) {
					logger.log(Level.INFO, "customer: " + customer.getName()
							+ " found!");
					return customer;
				}
				objectInputStream = new ObjectInputStream(fileInputStream);
			}
			return customer;
		} catch (EOFException ex) {
			logger.log(Level.SEVERE, "Customer not found!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.WARNING,
					"Error: If you are trying to add a customer, "
							+ "this may just be a routine operation, "
							+ "which should not worry.\nIf you are not "
							+ "adding a customer Probably the Customer "
							+ "does not exist.  ", exception.getMessage());
			return null;
		} finally {
			try {
				objectInputStream.close();
				fileInputStream.close();
			} catch (IOException exceptionTwo) {
				logger.log(Level.SEVERE, "Error closing file: ", exceptionTwo);
			}
		}
	}

	/**
	 * @param long id. References the id of the registry in database.
	 * 
	 * @return Object customer if customer exists or null if customer not
	 *         exists.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                Search Customer by Id.
	 */

	@Override
	public synchronized Customer searchCustomerById(long id) {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		// File file = null;
		Customer customer;
		try {
			// file = new File(pathFile);
			fileInputStream = new FileInputStream(getInstaceFile());
			objectInputStream = new ObjectInputStream(fileInputStream);

			while ((customer = (Customer) objectInputStream.readObject()) != null) {
				if (customer.getId() == id && customer.getIsActive() == true) {
					logger.log(Level.INFO, "customer: " + customer.getName()
							+ " found!");
					return customer;
				}
				objectInputStream = new ObjectInputStream(fileInputStream);
			}
			return customer;
		} catch (EOFException ex) {
			logger.log(Level.SEVERE, "Customer not found!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error searching customer: ", exception);
			return null;
		} finally {
			try {
				objectInputStream.close();
				fileInputStream.close();
			} catch (IOException exceptionTwo) {
				logger.log(Level.SEVERE, "Error closing file: ", exceptionTwo);
			}
		}
	}

	/**
	 * @param String
	 *            login. References the customer login.
	 * 
	 * @return Object customer if customer exists or null if customer not
	 *         exists.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                Search Customer by Login.
	 */

	@Override
	public synchronized Customer searchCustomerByLogin(String login) {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		// File file = null;
		Customer customer;
		try {
			// file = new File(pathFile);
			fileInputStream = new FileInputStream(getInstaceFile());
			objectInputStream = new ObjectInputStream(fileInputStream);

			while ((customer = (Customer) objectInputStream.readObject()) != null) {
				if (customer.getLogin() == login
						&& customer.getIsActive() == true) {
					logger.log(Level.INFO, "customer: " + customer.getName()
							+ " found!");
					return customer;
				}
				objectInputStream = new ObjectInputStream(fileInputStream);
			}
			return customer;
		} catch (EOFException ex) {
			logger.log(Level.SEVERE, "Customer not found!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Error searching customer: ", exception);
			return null;
		} finally {
			try {
				objectInputStream.close();
				fileInputStream.close();
			} catch (IOException exceptionTwo) {
				logger.log(Level.SEVERE, "Error closing file: ", exceptionTwo);
			}
		}
	}

	/**
	 * @param String
	 *            cpf. References the customer cpf.
	 * 
	 * @return Object customer if customer exists or null if customer not
	 *         exists.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                Search Customer by Cpf.
	 */

	@Override
	public synchronized Customer searchCustomerByCpf(String cpf) {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		// File file = null;
		Customer customer = null;
		try {
			// file = new File(pathFile);
			fileInputStream = new FileInputStream(getInstaceFile());
			objectInputStream = new ObjectInputStream(fileInputStream);

			while ((customer = (Customer) objectInputStream.readObject()) != null) {
				if (customer.getCpf() == cpf && customer.getIsActive() == true) {
					logger.log(Level.INFO, "customer: " + customer.getName()
							+ " found!");
					return customer;
				}
				objectInputStream = new ObjectInputStream(fileInputStream);
			}
			return customer;
		} catch (EOFException ex) {
			logger.log(Level.SEVERE, "Customer not found!");
			return null;
		} catch (Exception exception) {
			logger.log(Level.WARNING,
					"Error: If you are trying to add a customer, "
							+ "this may just be a routine operation, "
							+ "which should not worry.\nIf you are not "
							+ "adding a customer Probably the Customer "
							+ "does not exist.  ", exception.getMessage());
			return null;
		} finally {
			try {
				objectInputStream.close();
				fileInputStream.close();
			} catch (IOException exceptionTwo) {
				logger.log(Level.SEVERE, "Error closing file: ", exceptionTwo);
			}
		}
	}

	/**
	 * @return List<Customer>. If List<Customer> > 0 or return null if
	 *         List<Customer> is empty.
	 * 
	 * @exception Exception. If
	 *                the transaction is not successful or if the customer not
	 *                exists.
	 * 
	 *                Get list of Customers.
	 */

	@Override
	public synchronized List<Customer> getListOfCustomers() {
		List<Customer> list = new ArrayList<Customer>();
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		// File file = null;
		Customer customer;
		try {
			// file = new File(pathFile);
			fileInputStream = new FileInputStream(getInstaceFile());
			objectInputStream = new ObjectInputStream(fileInputStream);

			while ((customer = (Customer) objectInputStream.readObject()) != null) {
				list.add(customer);
				objectInputStream = new ObjectInputStream(fileInputStream);
			}
			return list;
		} catch (EOFException ex) {
			logger.log(Level.INFO, "List of customer found!");
			return list;
		} catch (Exception exception) {
			logger.log(Level.SEVERE,
					"Error listing customer, probably the list is empty: ",
					exception);
			return null;
		} finally {
			try {
				objectInputStream.close();
				fileInputStream.close();
			} catch (IOException exceptionTwo) {
				logger.log(Level.SEVERE, "Error closing file: ", exceptionTwo);
			}
		}
	}
}
