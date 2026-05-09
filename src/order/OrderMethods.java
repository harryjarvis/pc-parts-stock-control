package order;

import java.util.ArrayList;
import java.time.LocalDate;
import customer.Customer;
import part.Part;

/**
 * Student ID: 21360548
 * Provides logic and in-memory management for {@link Order} objects
 * Responsible for creating, retrieving, editing and deleting orders within the system
 * Maintains an internal list of orders and automatically assigns unique IDs to new orders
 */
public class OrderMethods {
	
	private ArrayList<Order> orders;
	
	public OrderMethods() {
		orders = new ArrayList<>();
	}
	
	private int idCounter = 1; // Again for orders, IDs are set automatically and are incrementing
	
	/**
	 * Adds an order that has been loaded from persistent storage
	 * This method preserves the existing order ID and updates the internal ID counter to ensure newly created orders retrieve unique identifiers
	 * @param o	order loaded from file
	 */
	public void addOrderFromLoad(Order o) {
		orders.add(o);
		
		if (o.getId() >= idCounter) {
			idCounter = o.getId() + 1;
		}
	}
	
	/**
	 * Adds a new order to the system
	 * A unique identifier is assigned before the order is stored in memory
	 * @param o	order to be added
	 */
	public void addOrder(Order o) {
		o.setId(idCounter);
		idCounter++;
		orders.add(o);
	}

	/**
	 * Retrieves an order by its unique ID
	 * @param id	order ID to search for
	 * @return	the matching {@link Order}, or null if not found
	 */
	public Order viewOrder(int id) {
		for (Order o : orders) {
			if (o.getId() == id) {
				return o;
			}
		} return null;
	}

	/**
	 * Returns a list of all orders currently stored in the system
	 * @return	list of all orders
	 */
	public ArrayList<Order> viewAllOrders() {
		return new ArrayList<>(orders);
	}
	
	/**
	 * Edits an existing order, updating the customer, status and date associated, as well as items
	 * @param id
	 * @param customer
	 * @param items
	 * @param status
	 * @param date
	 * @return
	 */
	public boolean editOrder(int id, Customer customer, ArrayList<OrderItem> items, String status, LocalDate date) {
		Order o = viewOrder(id);
		if (o != null) {
			o.setCustomer(customer);
			o.setStatus(status);
			o.setDate(date);
			return true;
		} return false;
	}
	
	/**
	 * Deletes an order from the system using its ID
	 * @param id	unique identifier
	 * @return	true if order was found and deleted, false otherwise
	 */
	public boolean deleteOrder(int id) {
		Order o = viewOrder(id);
		if (o != null) {
			orders.remove(o);
			return true;
		} return false;
	}
}
