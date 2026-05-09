package customer;

import java.util.ArrayList;

/**
 * Student ID: 21360548
 * Provides logic and in-memory management for {@link Customer} objects
 * Responsible for creating, retrieving and deleting customers within the system
 * Maintains an internal list of customers, ensures that each customer is assigned a unique identifier automatically
 */
public class CustomerMethods {

	private ArrayList<Customer> customers;
	
	/**
	 * Constructs a new instance
	 * Initialises the internal customer list
	 */
	public CustomerMethods() {
		customers = new ArrayList<>();
	}
	
	private int idCounter = 1;
	
	/**
	 * Adds a customer that has been loaded from persistent storage
	 * This method preserves the customer's existing ID and updates the internal ID counter to ensure future customers receive a unique ID
	 * @param c customer loaded from file
	 */
	public void addCustomerFromLoad(Customer c) {
		customers.add(c);
		
		if (c.getId() >= idCounter) {
			idCounter = c.getId() + 1; // Increments automatically so new customer will have a unique ID
		}
	}
	
	/**
	 * Adds a new customer to the system
	 * @param c customer to be added
	 */
	public void addCustomer(Customer c) {
		c.setId(idCounter);
		idCounter++;
		customers.add(c);
	}
	
	/**
	 * Retrieves a customer by their unique ID
	 * @param id	customer ID to search for
	 * @return	the matching {@link Customer} or null if not found
	 */
	public Customer viewCustomer(int id) {
		for (Customer c : customers) {
			if (c.getId() == id) {
				return c;
			} 
		} return null;
	}
	
	/**
	 * @return list of all customers
	 */
	public ArrayList<Customer> viewAllCustomers() {
		return new ArrayList<>(customers);
	}
	
	/**
	 * Deletes a customer from the system
	 * @param id 	ID of the customer to be deleted
	 * @return	true if the customer was found and deleted, false otherwise
	 */
	public boolean deleteCustomer(int id) {
		Customer c = viewCustomer(id);
		if (c != null) {
			customers.remove(c);
			return true;
		} return false;
	}
}
