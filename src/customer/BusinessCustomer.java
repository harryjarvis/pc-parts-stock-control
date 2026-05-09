package customer;

public class BusinessCustomer extends Customer {
	
	private String contactName;
	private String businessType;
	private String taxId;
	private double discountRate;
	
	public BusinessCustomer(int id, String name, String email, String password, String address,
			String contactName, String businessType, String taxId, double discountRate) {
		
		super(id, name, email, password, address, "Business"); // bypass type with "Business"
		
		this.contactName = contactName;
		this.businessType = businessType;
		this.taxId = taxId;
		this.discountRate = discountRate;
	}

	public String getContactName() {
		return contactName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public String getTaxId() {
		return taxId;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}
	
}
