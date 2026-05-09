package customer;

import java.util.Scanner;
import java.util.ArrayList;
import storage.*;
import main.Extras;

/**
 * Student ID: 21360548
 * Console based menu for managing customers
 * Provides a text UI for creating, viewing, editing and deleting {@link Customer} objects using {@link CustomerMethods}
 * Supports saving via {@link storage.FileManager}
 * Customer specific details are displayed using {@link IndividualCustomer}, {@link BusinessCustomer}, {@link EducationalCustomer}
 */
public class CustomerMenu {
	
	/**
	 * Displays a list of all customers currently stored in memory
	 * @param cm customer logic handler used to retrieve customers
	 */
	public void viewAllCustomers(CustomerMethods cm) {
		ArrayList<Customer> allCustomers = cm.viewAllCustomers();
		
		if (allCustomers.isEmpty()) {
			System.out.println(Extras.errorMsg+"\nNo customers found."+Extras.reset);
		} else {
			System.out.println(Extras.yellowBold+"\nAll Customers"+Extras.reset);
			for (Customer c : allCustomers) {
				System.out.println(Extras.underline+"ID" + c.getId()+Extras.reset + " → " + c.getName());
			}
		}
	}
	
	/**
	 * Helper method, prompting user to enter a customer ID to attempt to find matching customer
	 * Used as a helper when a method needs to select a customer, if no customer exists an error message is displayed and null is returned
	 * @param scnr	used for console input
	 * @param cm	customer logic handler used to search for the customer
	 * @return	the matching {@link Customer} or null if not found
	 */
	public Customer customerIdCheck(Scanner scnr, CustomerMethods cm) {
		System.out.println(Extras.underline+"\nEnter Customer ID:"+Extras.reset);
		int id = scnr.nextInt();
		scnr.nextLine();
		
		Customer c = cm.viewCustomer(id);
		
		if (c == null) {
			System.out.println(Extras.errorMsg+"\nCustomer not found."+Extras.reset);
		}
		return c;
	}
	
	/**
	 * Displays the customer management menu and handles user choice
	 * Options include adding, viewing and listing details, editing, deleting and saving to file
	 * @param scnr	used for console input
	 * @param cm	customer logic handler used to manage customers in memory
	 */
	public void show(Scanner scnr, CustomerMethods cm) {
		
		boolean back = false;
		
		while (!back) {
			System.out.println("\n─── " +Extras.yellowBg + " Manage Customers " +Extras.reset +" ─────────────────");
			System.out.println("1. Add Customer");
			System.out.println("2. View All Customers");
			System.out.println("3. View Customer Details");
			System.out.println("4. Edit Customer");
			System.out.println("5. Delete Customer");
			System.out.println("6. Save To File");
			System.out.println("7. Back → " + Extras.pink + "Main Menu" + Extras.reset);
			System.out.println("─── Choose Option ──────────────────────");
			
			int choice = scnr.nextInt();
			
			switch (choice) {
				case 1:
					addCustomer(scnr, cm);
					break;
				case 2:
					viewAllCustomers(cm);
					break;
				case 3:
					viewCustomer(scnr, cm);
					break;
				case 4:
					editCustomer(scnr, cm);
					break;
				case 5:
					viewAllCustomers(cm);
					Customer c = customerIdCheck(scnr, cm);
					if (c == null) break;
					cm.deleteCustomer(c.getId());
					System.out.println(Extras.successMsg+"\nCustomer deleted successfully."+Extras.reset);
					break;
				case 6:
					FileManager.saveCustomers(cm);
					back = true;
					break;
				case 7:
					back = true;
					break;
			}
		}
	}
	
	/**
	 * Adds a new customer by prompting the user for customer type and details
	 * Based on the selected type, creates an object based on the subclass {@link IndividualCustomer}, {@link BusinessCustomer}, {@link EducationalCustomer}
	 * @param scnr	console input
	 * @param cm	logic handler
	 */
	public void addCustomer(Scanner scnr, CustomerMethods cm) {

	    System.out.println("\n─── " + Extras.yellowBg + " Select Customer Type " + Extras.reset + " ─────────────");
	    System.out.println("1. Individual");
	    System.out.println("2. Business");
	    System.out.println("3. Educational");
	    System.out.println("─── Choose option ──────────────────────");

	    int customerType = scnr.nextInt();
	    scnr.nextLine();

	    String type = switch(customerType) {
		    case 1 -> "Individual";
		    case 2 -> "Business";
		    case 3 -> "Educational";
		    default -> null;
	    };

	    System.out.println(Extras.yellow + Extras.underline + "\nAdding " + type + " Customer..." + Extras.reset);

	    System.out.println(Extras.underline + "\nEnter Name:" + Extras.reset);
	    System.out.println(Extras.gray + "e.g. John Doe, Apple Inc., Harvard" + Extras.reset);
	    String name = scnr.nextLine();

	    System.out.println(Extras.underline + "\nEnter Email:" + Extras.reset);
	    System.out.println(Extras.gray + "e.g. supplies@mmu.ac.uk" + Extras.reset);
	    String email = scnr.nextLine();

	    System.out.println(Extras.underline + "\nEnter Password:" + Extras.reset);
	    String password = scnr.nextLine();

	    System.out.println(Extras.underline + "\nEnter Address:" + Extras.reset);
	    System.out.println(Extras.gray + "e.g. Lower Ormond St, Manchester, M15 6BX" + Extras.reset);
	    String address = scnr.nextLine();

	    Customer newCustomer = null;

	    switch (type) {

	        case "Individual": {
	            System.out.println(Extras.underline + "\nEnter Preferred Contact Method:" + Extras.reset);
	            String preferredContact = scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Loyalty Points:" + Extras.reset);
	            int loyaltyPoints = scnr.nextInt();
	            scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Favourite Part:" + Extras.reset);
	            System.out.println(Extras.gray + "e.g. Video Cards" + Extras.reset);
	            String favouritePart = scnr.nextLine();

	            newCustomer = new IndividualCustomer(0,
	                    name, email, password, address,
	                    preferredContact, loyaltyPoints, favouritePart);
	            break;
	        }

	        case "Business": {
	            System.out.println(Extras.underline + "\nEnter Contact Name:" + Extras.reset);
	            String contactName = scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Business Type:" + Extras.reset);
	            System.out.println(Extras.gray + "e.g. Automobile Manufacturer" + Extras.reset);
	            String businessType = scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Tax ID:" + Extras.reset);
	            System.out.println(Extras.gray + "e.g. 1573813741" + Extras.reset);
	            String taxId = scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Discount Rate:" + Extras.reset);
	            System.out.println(Extras.gray + "e.g. 0.15" + Extras.reset);
	            double discountRate = scnr.nextDouble();
	            scnr.nextLine();

	            newCustomer = new BusinessCustomer(0,
	                    name, email, password, address,
	                    contactName, businessType, taxId, discountRate);
	            break;
	        }

	        case "Educational": {
	            System.out.println(Extras.underline + "\nEnter Contact Name:" + Extras.reset);
	            String contactName = scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Institute Type:" + Extras.reset);
	            System.out.println(Extras.gray + "e.g. Public University" + Extras.reset);
	            String instituteType = scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Department:" + Extras.reset);
	            System.out.println(Extras.gray + "e.g. Mathematics & Engineering" + Extras.reset);
	            String department = scnr.nextLine();

	            System.out.println(Extras.underline + "\nEnter Discount Rate:" + Extras.reset);
	            System.out.println(Extras.gray + "e.g. 0.15" + Extras.reset);
	            double discountRate = scnr.nextDouble();
	            scnr.nextLine();

	            newCustomer = new EducationalCustomer(0,
	                    name, email, password, address,
	                    contactName, instituteType, department, discountRate);
	            break;
	        }
	    }

	    cm.addCustomer(newCustomer);
	    System.out.println("\n" + Extras.successMsg + type + " added successfully!" + Extras.reset);
	}

	/**
	 * Displays detailed information on a customer
	 * @param scnr	console input
	 * @param cm	logic handler
	 */
	public void viewCustomer(Scanner scnr, CustomerMethods cm) {

	    ArrayList<Customer> allCustomers = cm.viewAllCustomers();

	    viewAllCustomers(cm);

	    if (allCustomers.isEmpty()) {
	        return;
	    } // Don't attempt to list if no customers are present

	    Customer c = customerIdCheck(scnr, cm);
	    if (c == null) return;

	    String hiddenPassword = "*".repeat(c.getPassword().length()); // Hide password
	    String typeName = c.getType();

	    System.out.println("\n" + Extras.yellowBold + typeName + " Customer" + Extras.reset);

	    System.out.println("ID: " + Extras.gray + c.getId() + Extras.reset);
	    System.out.println("Name: " + Extras.gray + c.getName() + Extras.reset);
	    System.out.println("Email: " + Extras.gray + c.getEmail() + Extras.reset);
	    System.out.println("Password: " + Extras.gray + hiddenPassword + Extras.reset);
	    System.out.println("Address: " + Extras.gray + c.getAddress() + Extras.reset);

	    if (c instanceof IndividualCustomer ic) {
	        System.out.println("Preferred Contact Method: " + Extras.gray + ic.getPreferredContact() + Extras.reset);
	        System.out.println("Loyalty Points: " + Extras.gray + ic.getLoyaltyPoints() + Extras.reset);
	        System.out.println("Favourite Part: " + Extras.gray + ic.getFavouritePart() + Extras.reset);
	    }

	    else if (c instanceof BusinessCustomer bc) {
	        System.out.println("Contact Name: " + Extras.gray + bc.getContactName() + Extras.reset);
	        System.out.println("Business Type: " + Extras.gray + bc.getBusinessType() + Extras.reset);
	        System.out.println("Tax ID: " + Extras.gray + bc.getTaxId() + Extras.reset);
	        System.out.println("Discount Rate: " + Extras.gray + String.format("%.0f%%", bc.getDiscountRate() * 100) + Extras.reset);
	    }

	    else if (c instanceof EducationalCustomer ec) {
	        System.out.println("Contact Name: " + Extras.gray + ec.getContactName() + Extras.reset);
	        System.out.println("Institute Type: " + Extras.gray + ec.getInstituteType() + Extras.reset);
	        System.out.println("Department: " + Extras.gray + ec.getDepartment() + Extras.reset);
	        System.out.println("Discount Rate: " + Extras.gray + String.format("%.0f%%", ec.getDiscountRate() * 100) + Extras.reset); // Converts to percentage
	    }
	}
	
	/**
	 * Allows the user to edit a customer
	 * The user selects an ID based on a list they are given, they can then choose to keep the value or input a new one
	 * Type specific fields are editable depending on the customers subclass
	 * @param scnr console input
	 * @param cm logic handler
	 */
	public void editCustomer(Scanner scnr, CustomerMethods cm) {
		
		ArrayList<Customer> allCustomers = cm.viewAllCustomers();

	    viewAllCustomers(cm); // Lists customers already available
	    
	    if (allCustomers.isEmpty()) {
	    	return;
	    }

	    Customer c = customerIdCheck(scnr, cm);
	    if (c == null) return;

	    System.out.println(Extras.yellow + Extras.underline + "\nEditing " + c.getName() + Extras.reset);
	    System.out.println(Extras.gray + Extras.italic + "Press enter to keep current values." + Extras.reset);

	    System.out.println(Extras.underline + "\nCurrent Name:" + Extras.reset + " " +
	                       Extras.gray + Extras.italic + c.getName() + Extras.reset);
	    System.out.println("Enter New Name: ");
	    String newName = scnr.nextLine();
	    if (!newName.isEmpty()) c.setName(newName); // Helpful method so as not to re-write every info thats being kept

	    System.out.println(Extras.underline + "\nCurrent Email:" + Extras.reset + " " +
	                       Extras.gray + Extras.italic + c.getEmail() + Extras.reset);
	    System.out.println("Enter New Email: ");
	    String newEmail = scnr.nextLine();
	    if (!newEmail.isEmpty()) c.setEmail(newEmail);

	    String hiddenPassword = "*".repeat(c.getPassword().length());
	    System.out.println(Extras.underline + "\nCurrent Password:" + Extras.reset + " " +
	                       Extras.gray + Extras.italic + hiddenPassword + Extras.reset);
	    System.out.println("Enter New Password: ");
	    String newPassword = scnr.nextLine();
	    if (!newPassword.isEmpty()) c.setPassword(newPassword);

	    System.out.println(Extras.underline + "\nCurrent Address:" + Extras.reset + " " +
	                       Extras.gray + Extras.italic + c.getAddress() + Extras.reset);
	    System.out.println("Enter New Address: ");
	    String newAddress = scnr.nextLine();
	    if (!newAddress.isEmpty()) c.setAddress(newAddress);

	    if (c instanceof IndividualCustomer ic) {

	        System.out.println(Extras.underline + "\nCurrent Preferred Contact Method:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + ic.getPreferredContact() + Extras.reset);
	        System.out.println("Enter New Preferred Contact Method: ");
	        String newPreferredContact = scnr.nextLine();
	        if (!newPreferredContact.isEmpty()) ic.setPreferredContact(newPreferredContact);

	        System.out.println(Extras.underline + "\nCurrent No. Loyalty Points:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + ic.getLoyaltyPoints() + Extras.reset);
	        System.out.println("Enter New No. Loyalty Points: ");
	        String lpInput = scnr.nextLine().trim();
	        if (!lpInput.isEmpty()) {
	            try {
	                ic.setLoyaltyPoints(Integer.parseInt(lpInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Favourite Part:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + ic.getFavouritePart() + Extras.reset);
	        System.out.println("Enter New Favourite Part: ");
	        String newFavouritePart = scnr.nextLine();
	        if (!newFavouritePart.isEmpty()) ic.setFavouritePart(newFavouritePart);
	    }

	    else if (c instanceof BusinessCustomer bc) {

	        System.out.println(Extras.underline + "\nCurrent Contact Name:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + bc.getContactName() + Extras.reset);
	        System.out.println("Enter New Contact Name: ");
	        String newContactName = scnr.nextLine();
	        if (!newContactName.isEmpty()) bc.setContactName(newContactName);

	        System.out.println(Extras.underline + "\nCurrent Business Type:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + bc.getBusinessType() + Extras.reset);
	        System.out.println("Enter New Business Type: ");
	        String newBusinessType = scnr.nextLine();
	        if (!newBusinessType.isEmpty()) bc.setBusinessType(newBusinessType);

	        System.out.println(Extras.underline + "\nCurrent Tax ID:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + bc.getTaxId() + Extras.reset);
	        System.out.println("Enter New Tax ID: ");
	        String newTaxId = scnr.nextLine();
	        if (!newTaxId.isEmpty()) bc.setTaxId(newTaxId);

	        System.out.println(Extras.underline + "\nCurrent Discount Rate:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + String.format("%.0f%%", bc.getDiscountRate() * 100) + Extras.reset);
	        System.out.println("Enter New Discount Rate (decimal): ");
	        String drInput = scnr.nextLine().trim();
	        if (!drInput.isEmpty()) {
	            try {
	                bc.setDiscountRate(Double.parseDouble(drInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }
	    }

	    else if (c instanceof EducationalCustomer ec) {

	        System.out.println(Extras.underline + "\nCurrent Contact Name:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + ec.getContactName() + Extras.reset);
	        System.out.println("Enter New Contact Name: ");
	        String newContactName = scnr.nextLine();
	        if (!newContactName.isEmpty()) ec.setContactName(newContactName);

	        System.out.println(Extras.underline + "\nCurrent Institute Type:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + ec.getInstituteType() + Extras.reset);
	        System.out.println("Enter New Institute Type: ");
	        String newInstituteType = scnr.nextLine();
	        if (!newInstituteType.isEmpty()) ec.setInstituteType(newInstituteType);

	        System.out.println(Extras.underline + "\nCurrent Department:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + ec.getDepartment() + Extras.reset);
	        System.out.println("Enter New Department: ");
	        String newDepartment = scnr.nextLine();
	        if (!newDepartment.isEmpty()) ec.setDepartment(newDepartment);

	        System.out.println(Extras.underline + "\nCurrent Discount Rate:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + String.format("%.0f%%", ec.getDiscountRate() * 100) + Extras.reset);
	        System.out.println("Enter New Discount Rate (decimal): ");
	        String drInput = scnr.nextLine().trim();
	        if (!drInput.isEmpty()) {
	            try {
	                ec.setDiscountRate(Double.parseDouble(drInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }
	    }

	    System.out.println("\n" + Extras.successMsg + "Customer updated successfully!" + Extras.reset);
	}


}
