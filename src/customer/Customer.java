package customer;

/**
 * Student ID: 21360548
 * Represents a singular customer in the system
 * The {@code Customer} acts as a superclass for different customer types, Individual, Business and Educational
 * Stores common attributes, id, name, email, password, address and type
 */
public class Customer {
	
	private int id; // unique identifier (PK)
	private String name;
	private String email;
	private String password;
	private String address;
	private final String type;
	
	/**
	 * @param id	unique identifier
	 * @param name
	 * @param email
	 * @param password
	 * @param address
	 * @param type
	 */
	public Customer(int id, String name, String email, String password, String address, String type) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getType() {
		return type;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Returns a formatted string representation of the customer
	 * Used for displaying customer details in lists and menus
	 */
	@Override
	public String toString() {
	    return "ID: " + id + 
	           " | Name: " + name + 
	           " | Email: " + email + 
	           " | Address: " + address;
	} // Simple override for listing customers in a neat way
}
