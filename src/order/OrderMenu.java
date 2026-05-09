package order;

import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import customer.*;
import part.*;
import storage.*;
import main.Extras;

/**
 * Student ID: 21360548
 * Console-based menu for managing orders
 * Provides a text UI allowing users to create, view, edit and delete {@link Order} objects. It interacts with {@link OrderMethods} for storage/management and usaes {@link PartMethods} and {@link CustomerMethods}
 * to select customers and parts when creating or modifying orders
 * Orders can be saved to storage using {@link storage.FileManager}
 */
public class OrderMenu {

	/**
	 * Displays a list of all orders currently stored in memory
	 * Each order is shown with its ID, customer name and a compact inline item summary via {@link Order#getItemsInline()}
	 * @param om	order logic handler used to retrieve stored orders
	 */
	public void viewAllOrders(OrderMethods om) {
		ArrayList <Order> allOrders = om.viewAllOrders();
		
		if (allOrders.isEmpty()) {
			System.out.println(Extras.errorMsg+"\nNo orders found."+Extras.reset);
		} else {
			System.out.println(Extras.greenBold+"\nAll Orders"+Extras.reset);
			for (Order o : allOrders) {
				System.out.println(Extras.underline+"ID:" + o.getId()+Extras.reset + " → " + o.getCustomer().getName() + " - " + o.getItemsInline()); // Prints all items in an order
			}
		}
	}
	
	
	/**
	 * Displays a list of all parts currently in stock
	 * This is a helper method used when creating or editing orders so the user can easily reference part IDs
	 * @param pm	part logic handler used to retrieve stored parts
	 */
	public void viewAllParts(PartMethods pm) {
		ArrayList <Part> allParts = pm.viewAllParts();
		
		if (allParts.isEmpty()) {
			System.out.println(Extras.errorMsg+"\nNo parts in stock."+Extras.reset);
		} else {
			System.out.println(Extras.cyanBold+"\nAll Parts"+Extras.reset);
			for (Part p : allParts) {
				System.out.println(Extras.underline+"ID" + p.getId()+Extras.reset+" → "+p.getName());
			}
		}
	}
	
	/**
	 * Displays a list of all customers currently in the system
	 * Helper method used when creating or editing orders so the user can easily reference customer IDs
	 * @param cm	customer logic handler used to retrieve stored customers
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
	 * Prompts the user to enter an Order ID and attempts to find the matching order
	 * If no order exists with the entered ID, an error message is displayed and null is returned
	 * @param scnr	console input
	 * @param om	order logic handler used to search for the order
	 * @return	the matching {@link Order} otherwise null if not found
	 */
	public Order orderIdCheck(Scanner scnr, OrderMethods om) {
		System.out.println(Extras.underline+"\nEnter Order ID:"+Extras.reset);
		int id = scnr.nextInt();
		scnr.nextLine();
		
		Order o = om.viewOrder(id);
		
		if (o == null) {
			System.out.println(Extras.errorMsg+"\nOrder not found."+Extras.reset);
		}
		return o;
	}

    /**
     * Prompts the user to enter a part ID and attempts to find the matching part.
     * If no part exists with the entered ID, an error message is displayed and
     * null is returned.
     * @param scnr scanner used for console input
     * @param pm   part logic handler used to search for the part
     * @return the matching {@link Part}, or null if not found
     */
	public Part partIdCheck(Scanner scnr, PartMethods pm) {
		System.out.println(Extras.underline+"\nEnter Part ID:"+Extras.reset);
		int id = scnr.nextInt();
		scnr.nextLine();
		
		Part p = pm.viewPart(id);
		
		if (p == null) {
			System.out.println(Extras.errorMsg+"\nPart not found."+Extras.reset);
		}
		return p;
	}
	

    /**
     * Prompts the user to enter a customer ID and attempts to find the matching customer.
     * If no customer exists with the entered ID, an error message is displayed and
     * code null is returned.
     * @param scnr scanner used for console input
     * @param cm   customer logic handler used to search for the customer
     * @return the matching {@link Customer}, or null if not found
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
     * Displays the order management menu and handles user choices.
     * Options include adding orders, viewing lists and details, editing orders
     * (including items), deleting orders, and saving to file.
     * @param scnr scanner used for console input
     * @param om   order logic handler used to manage orders in memory
     * @param pm   part logic handler used to retrieve parts for order items
     * @param cm   customer logic handler used to retrieve customers for orders
     */
	public void show(Scanner scnr, OrderMethods om, PartMethods pm, CustomerMethods cm) {
		
		boolean back = false;
		
		while (!back) {
			System.out.println("\n─── " + Extras.greenBg + " Manage Orders " + Extras.reset + " ────────────────────");
			System.out.println("1. Add Order");
			System.out.println("2. View All Orders");
			System.out.println("3. View Order Details");
			System.out.println("4. Edit Order");
			System.out.println("5. Delete Order");
			System.out.println("6. Save To File");
			System.out.println("7. Back → "+Extras.pink+"Main Menu"+Extras.reset);
			System.out.println("─── Choose Option ──────────────────────");
			
			int choice = scnr.nextInt();
			
			switch (choice) {
				case 1:
					addOrder(scnr, om, pm, cm);
					break;
				case 2:
					viewAllOrders(om);
					break;
				case 3:
					viewOrder(scnr, om, pm, cm);
					break;
				case 4:
					editMenu(scnr, om, pm, cm);
					break;
				case 5:
					Order o = orderIdCheck(scnr, om);
					if (o == null) break;
					om.deleteOrder(o.getId());
					System.out.println(Extras.errorMsg+"\nOrder deleted successfully."+Extras.reset);
					break;
				case 6:
					FileManager.saveOrders(om);
					back = true;
					break;
				case 7:
					back = true;
					break;
			}
		}
	}
	
    /**
     * Creates a new order by prompting the user for a customer, date, items, and status.
     * The user selects a customer by ID and then adds one or more parts (with quantities)
     * to the order as {@link OrderItem} objects. If the user presses enter for the date,
     * the current date ({@link LocalDate#now()}) is used.
     * @param scnr scanner used for console input
     * @param om   order logic handler used to add the new order
     * @param pm   part logic handler used to select parts for the order
     * @param cm   customer logic handler used to select the customer for the order
     */
	public void addOrder(Scanner scnr, OrderMethods om, PartMethods pm, CustomerMethods cm) {
		
	    ArrayList<Part> parts = pm.viewAllParts();
	    ArrayList<Customer> customers = cm.viewAllCustomers();
			
	    if (parts.isEmpty() || customers.isEmpty()) {
	        System.out.println(Extras.errorMsg + "\nCannot create an order without both part(s) and customer(s) in the system." + Extras.reset);
	        return;
	    }
			
	    viewAllCustomers(cm);
			
	    System.out.println(Extras.gray + "\nWhich customer has placed the order?" + Extras.reset);
	    Customer customer = customerIdCheck(scnr, cm);
			
	    System.out.println(Extras.lime + Extras.underline + "\nAdding Order..." + Extras.reset);
	    System.out.println(Extras.gray + "Customer: " + customer.getName() + Extras.reset);
	    
	    System.out.println(Extras.underline + "\nOrder Date:" + Extras.reset);
	    System.out.println(Extras.gray + "e.g. 2025-10-08, enter for today's date" + Extras.reset); 
	    String dateInput = scnr.nextLine();
				
	    LocalDate date;
	    if (dateInput.isEmpty()) {
	        date = LocalDate.now(); // Useful "enter" for todays date
	    } else {
	        try {
	            date = LocalDate.parse(dateInput);
	        } catch (Exception e) { // Catches incorrect dates
	            System.out.println(Extras.errorMsg + "Invalid date. Using today's date." + Extras.reset); 
	            date = LocalDate.now();
	        }
	    }
				
	    ArrayList<OrderItem> orderItems = new ArrayList<>();
	    boolean adding = true;

	    while (adding) { // While loop allows users to add multiple items until they are finished

	        viewAllParts(pm);

	        Part part = partIdCheck(scnr, pm);
	        if (part == null) return;

	        System.out.println(Extras.underline + "\nQuantity:" + Extras.reset);
	        int quantity = scnr.nextInt();
	        scnr.nextLine();

	        orderItems.add(new OrderItem(part, quantity));

	        // Ask to add another
	        System.out.println(Extras.underline + "\nAdd another item? (yes/no):" + Extras.reset);
	        String answer = scnr.nextLine().trim().toLowerCase();

	        if (!answer.equals("yes") && !answer.equals("y")) {
	            adding = false;
	        }
	    }
				
	    System.out.println(Extras.underline + "\nOrder Status:" + Extras.reset);
	    System.out.println(Extras.gray + "e.g. Pending, Shipped, Completed" + Extras.reset);
	    String status = scnr.nextLine();
				
	    Order order = new Order(0, customer, orderItems, status, date);
	    om.addOrder(order);
				
	    System.out.println(Extras.successMsg + "\nOrder created successfully!" + Extras.reset);
	}

    /**
     * Displays detailed information about a selected order.
     * Outputs order ID, customer name, item list, status, and date.
     * Item details are shown in a vertical format via {@link Order#getItemsVertical()}.
     * @param scnr scanner used for console input
     * @param om   order logic handler used to retrieve the selected order
     * @param pm   part logic handler (passed for consistency with menu flow)
     * @param cm   customer logic handler (passed for consistency with menu flow)
     */
	public void viewOrder(Scanner scnr, OrderMethods om, PartMethods pm, CustomerMethods cm) {
		
		ArrayList <Order> allOrders = om.viewAllOrders();
		
		viewAllOrders(om);
		
		if (allOrders.isEmpty()) {
			return;
		}
		
		Order o = orderIdCheck(scnr, om);
		if (o == null) return;
		System.out.println(Extras.greenBold+"\nOrder Info"+Extras.reset);
		System.out.println("ID: " + Extras.lightgray + o.getId() + Extras.reset);
		System.out.println("Customer: " + Extras.lightgray + o.getCustomer().getName() + Extras.reset);
		System.out.println("Items: " + Extras.lightgray + o.getItemsVertical() + Extras.reset);
		System.out.println("Status: " + Extras.lightgray + o.getStatus() + Extras.reset);
		System.out.println("Date: " + Extras.lightgray + o.getDate() + Extras.reset);
	}
	
    /**
     * Displays a secondary editing menu for orders.
     * Editing orders is more complex than parts/customers, so this menu allows the user
     * to choose between editing order details, adding items, or removing items.
     * @param scnr scanner used for console input
     * @param om   order logic handler used to retrieve orders
     * @param pm   part logic handler used to select parts
     * @param cm   customer logic handler used to select customers
     */
	public void editMenu(Scanner scnr, OrderMethods om, PartMethods pm, CustomerMethods cm) {
		
		boolean back = false;
		
		while (!back) {
			System.out.println("\n─── " + Extras.greenBg + " Edit Order " + Extras.reset + " ────────────────────");
			System.out.println("1. Edit Order Details");
			System.out.println("2. Add Item To Order");
			System.out.println("3. Remove Item From Order");
			System.out.println("4. Back → "+Extras.pink+"Manage Orders"+Extras.reset);
			System.out.println("─── Choose Option ──────────────────────");
			
			int choice = scnr.nextInt();
			
			switch (choice) {
			case 1: 
				editOrder(scnr, om, cm);
				break;
			case 2:
				addItem(scnr, om, pm);
				break;
			case 3:
				removeItem(scnr, om, pm);
				break;
			case 4:
				back = true;
				break;
			}
		}
	}
	
    /**
     * Edits the main details of an existing order (customer, status, and date).
     * The user may press enter to keep the current value for each field.
     * @param scnr scanner used for console input
     * @param om   order logic handler used to retrieve the selected order
     * @param cm   customer logic handler used when changing the order's customer
     */
	public void editOrder(Scanner scnr, OrderMethods om, CustomerMethods cm) {
		
		ArrayList <Order> allOrders = om.viewAllOrders();
		
		viewAllOrders(om);
		
		if (allOrders.isEmpty()) {
			return;
		}
		
		Order o = orderIdCheck(scnr, om);
		if (o == null) return;
		
		System.out.println(Extras.lime+Extras.underline+"\nEditing Order Details"+Extras.reset);
		System.out.println(Extras.gray+Extras.italic+"Press enter to keep current values."+Extras.reset);
		
		System.out.println(Extras.underline+"\nCurrent Customer:" + Extras.reset + " " + 
						   Extras.gray + Extras.italic + o.getCustomer().getName() + Extras.reset);
		viewAllCustomers(cm);
		System.out.println("\nEnter New Customer ID: ");
		
		String custInput = scnr.nextLine().trim();
		if (!custInput.isEmpty()) {
			try {
				int custId = Integer.parseInt(custInput);
				Customer newCustomer = cm.viewCustomer(custId);
				
				if (newCustomer != null) {
					o.setCustomer(newCustomer);
				} else {
					System.out.println(Extras.errorMsg+"Customer not found. Keeping current."+Extras.reset);
				}
			} catch (Exception e) {
				System.out.println(Extras.errorMsg+"Invalid ID. Keeping current customer."+Extras.reset);
			}
		}
		
		System.out.println(Extras.underline+"\nCurrent Order Status:" + Extras.reset + " " +
						   Extras.gray + Extras.italic + o.getStatus() + Extras.reset);
		System.out.println("Enter New Order Status: ");
		String newStatus = scnr.nextLine();
		if (!newStatus.isEmpty()) {
			o.setStatus(newStatus);
		}
		
		System.out.println(Extras.underline+"\nCurrent Order Date:" + Extras.reset + " " +
						   Extras.gray + Extras.italic + o.getDate() + Extras.reset);
		System.out.println("Enter New Date: ");
		String dateInput = scnr.nextLine().trim();
		
		if (!dateInput.isEmpty()) {
			try {
				LocalDate newDate = LocalDate.parse(dateInput);
				o.setDate(newDate);
			} catch (Exception e) {
				System.out.println(Extras.errorMsg+"Invalid date. Keeping current date."+Extras.reset);
			}
		}
		
		System.out.println(Extras.successMsg+"\nOrder updated successfully!"+Extras.reset);
	}
	
    /**
     * Adds one or more items to an existing order.
     * If the selected part already exists in the order, the quantity is increased.
     * Otherwise, a new {@link OrderItem} is created and added.
     * @param scnr scanner used for console input
     * @param om   order logic handler used to retrieve the selected order
     * @param pm   part logic handler used to select parts
     */
	public void addItem(Scanner scnr, OrderMethods om, PartMethods pm) {
		
		ArrayList <Order> allOrders = om.viewAllOrders();
		
		viewAllOrders(om);
		
		if (allOrders.isEmpty()) {
			return;
		}
		
		Order ord = orderIdCheck(scnr, om);
		if (ord == null) return;
		
		System.out.println(Extras.lime+Extras.underline+"\nAdding Items To " +
							ord.getCustomer().getName() 
							+ "'s Order"+Extras.reset);
		System.out.println(Extras.underline+"Current Items:" + Extras.reset + " " + Extras.gray+Extras.italic+ ord.getItemsVertical());
		
		boolean adding = true;
		
		while (adding) {
			
			viewAllParts(pm);
			
			Part part = partIdCheck(scnr, pm);
			if (part == null) return;
			
			OrderItem existingItem = null;
			for (OrderItem item : ord.getItems()) {
				if (item.getPart().getId() == part.getId()) {
					existingItem = item;
					break;
				}
			}
			
			if (existingItem != null) {
				System.out.println(Extras.gray+Extras.italic+"\nPart already in order."+Extras.reset);
			}
			
			System.out.println(Extras.underline+"\nEnter quantity to add:"+Extras.reset);
			int qty = scnr.nextInt();
			scnr.nextLine();
			
			if (existingItem != null) {
				existingItem.setQuantity(existingItem.getQuantity() + qty);
				System.out.println(Extras.successMsg+"\nUpdated quantity for "
							+ part.getName() + Extras.reset);
				System.out.println(Extras.gray+Extras.italic+"New Quantity: " + existingItem.getQuantity() + Extras.reset);
			} else {
				ord.getItems().add(new OrderItem(part, qty));
				System.out.println("\033[3;32m\nAdded " + part.getName() + " (x" + qty + ")\033[0m");
			}
			
			System.out.println(Extras.underline+"\nAdd more items? (yes/no):"+Extras.reset);
			String again = scnr.nextLine().trim().toLowerCase();
			if (!again.equals("yes") && !again.equals("y")) {
				adding = false;
			}
		}
	}
	
    /**
     * Removes quantities of an item from an existing order.
     * If the removal quantity equals the current quantity, the item is removed entirely.
     * If the removal quantity is smaller, the quantity is reduced.
     * @param scnr scanner used for console input
     * @param om   order logic handler used to retrieve the selected order
     * @param pm   part logic handler used to select parts
     */
	public void removeItem(Scanner scnr, OrderMethods om, PartMethods pm) {
		
		ArrayList <Order> allOrders = om.viewAllOrders();
	    
	    viewAllOrders(om);
	    
	    if (allOrders.isEmpty()) {
	    	return;
	    }

	    Order ord = orderIdCheck(scnr, om);
	    if (ord == null) return;

	    System.out.println(Extras.lime+Extras.underline+"\nRemoving Items From " 
	                       + ord.getCustomer().getName() 
	                       + "'s Order"+Extras.reset);

	    boolean removing = true;

	    while (removing) {
	    	
	    	if (ord.getItems().isEmpty()) {
	    		System.out.println(Extras.errorMsg+"No more items in this order."+Extras.reset);
	    	}

	        System.out.println(Extras.underline+"Current Items:"+Extras.reset + ord.getItemsVerticalId());

	        Part part = partIdCheck(scnr, pm);
	        if (part == null) return;

	        OrderItem targetItem = null;
	        for (OrderItem item : ord.getItems()) {
	            if (item.getPart().getId() == part.getId()) {
	                targetItem = item;
	                break;
	            }
	        }

	        // If item not found in order
	        if (targetItem == null) {
	            System.out.println(Extras.errorMsg+"\nThis part is not in the order."+Extras.reset);
	        } else {

	            System.out.println(Extras.underline+"\nEnter quantity to remove:"+Extras.reset);
	            int qty = scnr.nextInt();
	            scnr.nextLine();

	            if (qty <= 0) {
	                System.out.println(Extras.errorMsg+"Invalid quantity."+Extras.reset);
	            }
	            else if (qty > targetItem.getQuantity()) {
	                System.out.println(Extras.errorMsg+"Cannot remove more than current quantity (" 
	                                   + targetItem.getQuantity() + ")."+Extras.reset);
	            }
	            else if (qty == targetItem.getQuantity()) {
	                // Remove entire item
	                ord.getItems().remove(targetItem);
	                System.out.println(Extras.successMsg+"\nRemoved " 
	                                   + part.getName() + "."+Extras.reset);
	            }
	            else {
	                // Subtract quantity
	                targetItem.setQuantity(targetItem.getQuantity() - qty);
	                System.out.println(Extras.successMsg+"\nRemoved " 
	                                   + qty + " of " + part.getName() + "."+Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline+"\nRemove more items? (yes/no):"+Extras.reset);
	        String again = scnr.nextLine().trim().toLowerCase();

	        if (!again.equals("yes") && !again.equals("y")) {
	            removing = false;
	        }
	    }
	}

}
