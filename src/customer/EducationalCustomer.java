package customer;

public class EducationalCustomer extends Customer {
	
	private String contactName;
	private String instituteType;
	private String department;
	private double discountRate;
	
	// Constructor
	public EducationalCustomer(int id, String name, String email, String password, String address,
			String contactName, String instituteType, String department, double discountRate) {
		
		super(id, name, email, password, address, "Educational"); // bypass type with "Educational"
		
		this.contactName = contactName;
		this.instituteType = instituteType;
		this.department = department;
		this.discountRate = discountRate;
	}

	// Getters
	public String getContactName() {
		return contactName;
	}

	public String getInstituteType() {
		return instituteType;
	}

	public String getDepartment() {
		return department;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	// Setters
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public void setInstituteType(String instituteType) {
		this.instituteType = instituteType;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}
	
}
