package order;

import customer.Customer;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Student ID: 21360548
 * Represents a customers order within the system
 * An order contains a reference to the customer who placed it, a list of items ({@link OrderItem} representing the ordered parts
 * As well as the order status and the date the order was created
 */
public class Order {

	private int id; // unique identifier (PK)
	private Customer customer;
	private ArrayList<OrderItem> items;
	private String status;
	private LocalDate date;
	
	/**
	 * 
	 * @param id	unique identifier
	 * @param customer
	 * @param items
	 * @param status
	 * @param date
	 */
	public Order(int id, Customer customer, ArrayList<OrderItem> items, String status, LocalDate date) {
		this.id = id;
		this.customer = customer;
		this.items = items;
		this.status = status;
		this.date = date;
	}
	
	/**
	 * Adds a new item to the order
	 * @param item	order item to be added
	 */
	public void addItem(OrderItem item) {
		items.add(item);
	}

	public int getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public ArrayList<OrderItem> getItems() {
		return items;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setItems(ArrayList<OrderItem> items) {
		this.items = items;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	/**
	 * Returns a vertically formatted string of order items
	 * Used for console display, showing each item on a new line with quantity info
	 * @return formatted list of order items (vertical layout)
	 */
	public String getItemsVertical() {
	    StringBuilder sbv = new StringBuilder();
	    
	    for (OrderItem item : items) {
	        sbv.append("\n")
	          .append("\033[36m• \033[0m")   // cyan bullet
	          .append("\033[37m")            // start gray text
	          .append(item.getPart().getName())
	          .append(" x")
	          .append(item.getQuantity())
	          .append("\033[0m");            // reset colour
	    } return sbv.toString();
	}

	/**
	 * Returns a single-line string representation of the order items
	 * @return formatted list of order items (inline layout)
	 */
	public String getItemsInline() {
		StringBuilder sbi = new StringBuilder();
		ArrayList<OrderItem> items = this.items;
		
		for (int i = 0; i < items.size(); i++) {
			sbi.append(items.get(i).toString());
			if (i < items.size() - 1)
				sbi.append(", ");
		} return sbi.toString();
	}
	
	/**
	 * Returns a vertically arranged list of items with part IDs included
	 * @return formatted list of order items with part IDs
	 */
	public String getItemsVerticalId() {
		StringBuilder sbvid = new StringBuilder();
		
		for (OrderItem item : items) {
			sbvid.append("\n")
				.append("\033[36m• \033[0m")
				.append("\033[37mID: ")
				.append(item.getPart().getId())
				.append(" - ")
		        .append(item.getPart().getName())
		        .append(" x")
		        .append(item.getQuantity())
		        .append("\033[0m");  
		} return sbvid.toString();
	}
	
	/**
	 * Calculates the total price of the order by summing
	 * the price of each part multiplied by its quantity.
	 *
	 * @return total order cost
	 */
	public double getTotal() {
	    double total = 0.0;

	    for (OrderItem item : items) {
	        total += item.getPart().getPrice() * item.getQuantity();
	    }

	    return total;
	}
}
