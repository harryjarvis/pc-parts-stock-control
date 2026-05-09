package part;

/**
 * Student ID: 21360548
 * Represents a stock item within the system, in this case a hardware component
 * {@code Part} class acts as a superclass for hardware components stored in the system,
 * which in my system are CPUs, GPUs, Motherboards and Cases
 * It stores common attributes shared by all components, including ID, name, manufacturer, price and type
 */
public class Part {
	
	private int id; // unique identifier (PK)
	private String name;
	private String manufacturer;
	private double price;
	private final String type;
	
	/**
	 * Constructs a new part object
	 * @param id	unique identifier
	 * @param name
	 * @param manufacturer
	 * @param price
	 * @param type
	 */
	public Part(int id, String name, String manufacturer, double price, String type) {
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
		this.price = price;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public double getPrice() {
		return price;
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
	
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

}
