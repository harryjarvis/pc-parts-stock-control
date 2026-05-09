package customer;

public class IndividualCustomer extends Customer {
	
	private String preferredContact;
	private int loyaltyPoints;
	private String favouritePart;
	
	// Constructor
	public IndividualCustomer(int id, String name, String email, String password, String address,
			String preferredContact, int loyaltyPoints, String favouritePart) {
		
		super(id, name, email, password, address, "Individual"); // bypass type with "Individual"
		
		this.preferredContact = preferredContact;
		this.loyaltyPoints = loyaltyPoints;
		this.favouritePart = favouritePart;	
	}

	// Getters
	public String getPreferredContact() {
		return preferredContact;
	}

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public String getFavouritePart() {
		return favouritePart;
	}

	// Setters
	public void setPreferredContact(String preferredContact) {
		this.preferredContact = preferredContact;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public void setFavouritePart(String favouritePart) {
		this.favouritePart = favouritePart;
	}
			
}
