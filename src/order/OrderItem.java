package order;

import part.Part;

/**
 * Student ID: 21360548
 * Represents a single item within an order
 * An OrderItem links a {@link Part} with a quantity, allowing an {@link Order} to contain multiple parts and track how many of each parts have been ordered
 */
public class OrderItem {
	
	private Part part;
	private int quantity;
	
	public OrderItem(Part part, int quantity) {
		this.part = part;
		this.quantity = quantity;
	}

	public Part getPart() {
		return part;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override // Simple override to show quantity
	public String toString() {
		return part.getName() + " (x" + quantity + ")";
	}
	
	/**
	 * Calculates the total cost for this line item (part price × quantity).
	 *
	 * @return line total cost
	 */
	public double getLineTotal() {
	    return part.getPrice() * quantity;
	}
}
