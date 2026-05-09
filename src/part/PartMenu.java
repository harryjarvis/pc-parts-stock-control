package part;

import java.util.Scanner;
import java.util.ArrayList;
import storage.*;
import main.Extras;

/**
 * Student ID: 21360548
 * Console based menu for managing parts
 * Provides a text UI for creating, viewing, editing and deleting {@link Part} objects using {@link PartMethods}
 * Supports saving via {@link storage.FileManager}
 * Part specific details are displayed using {@link CPU}, {@link GPU}, {@link Motherboard}, {@link Case}
 */
public class PartMenu {
	
	/**
	 * Displays a list of all parts currently stored in memory
	 * @param pm	part logic handler used to retrieve parts
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
	 * Helper method, prompting user to enter a part ID to attempt to find matching part
	 * Used as a helper when a method needs to select a part, if no part exists an error message is displayed and null is returned
	 * @param scnr	console input
	 * @param pm	part logic handler used to search for the customer
	 * @return	the matching {@link Part} or null if not found
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
	 * Displays the part management menu and handles user choice
	 * Options include adding, viewing, listing details, editing, deleting and saving to file
	 * @param scnr	console input
	 * @param pm	part logic handler used to manage parts in memory
	 */
	public void show(Scanner scnr, PartMethods pm) {
		
		boolean back = false;
		
		while (!back) {
			System.out.println("\n─── " + Extras.cyanBg + " Manage Parts " + Extras.reset + " ─────────────────────");
			System.out.println("1. Add Part");
			System.out.println("2. View All Parts");
			System.out.println("3. View Part Details");
			System.out.println("4. Edit Part");
			System.out.println("5. Delete Part");
			System.out.println("6. Save To File");
			System.out.println("7. Back → " + Extras.pink + "Main Menu" + Extras.reset);
			System.out.println("─── Choose Option ──────────────────────");

			
			int choice = scnr.nextInt();
			
			switch (choice) {
				case 1:
					addPart(scnr, pm);
					break;
				case 2:
					viewAllParts(pm);
					break;
				case 3:
					viewPart(scnr, pm);
					break;
				case 4:
					editPart(scnr, pm);
					break;
				case 5:
					viewAllParts(pm);
					Part p = partIdCheck(scnr, pm);
					if (p == null) break;
					pm.deletePart(p.getId());
					System.out.println(Extras.successMsg+"\nPart deleted."+Extras.reset);
					break;
				case 6:
					FileManager.saveParts(pm);
					back = true;
					break;
				case 7:
					back = true;
					break;
			}
		}
	}
	
	/**
	 * Adds a new part to the system by prompting a user for part type and details
	 * Based on the selected type, creates an object based on the subclass {@link CPU}, {@link GPU}, {@link Motherboard}, {@link Case}
	 * @param scnr	console input
	 * @param pm	logic handler
	 */
	public void addPart(Scanner scnr, PartMethods pm) {
		
		System.out.println("\n─── "+Extras.cyanBg+" Select Part Type "+Extras.reset+" ─────────────────");
		System.out.println("1. CPU");
		System.out.println("2. GPU");
		System.out.println("3. Motherboard");
		System.out.println("4. Case");
		System.out.println("─── Choose option ──────────────────────");
		
		int partType = scnr.nextInt();
		scnr.nextLine();
		
		String type = switch(partType) { // Switch stops repeat code
			case 1 -> "CPU";
			case 2 -> "GPU";
			case 3 -> "Motherboard";
			case 4 -> "Case";
			default -> null;
		};
		
		if (type == null) {
			System.out.println(Extras.errorMsg + "Invalid part type." + Extras.reset);
		}
		
		System.out.println(Extras.cyan+Extras.underline+"\nAdding " + type +"..."+Extras.reset);
		
		System.out.println(Extras.underline+"\nEnter Name:"+Extras.reset);
		System.out.println(Extras.gray+"e.g. Ryzen 7600X, Windforce OC SFF, B550-A Pro, H5 Flow"+Extras.reset); // Example names in italic gray are neat and help users
		String name = scnr.nextLine();
		
		System.out.println(Extras.underline+"\nEnter Manufacturer:"+Extras.reset);
		System.out.println(Extras.gray+"e.g. AMD, Gigabyte, Msi, NZXT"+Extras.reset);
		String manufacturer = scnr.nextLine();
		
		System.out.println(Extras.underline+"\nEnter Price (£):"+Extras.reset);
		double price = scnr.nextDouble();
		scnr.nextLine();
		
		Part newPart = null;
		
		switch (type) {
			case "CPU": {
				System.out.println(Extras.underline+"\nEnter Socket:"+Extras.reset);
				System.out.println(Extras.gray+"e.g. AM5"+Extras.reset);
				String socket = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Core Count:"+Extras.reset);
				int cores = scnr.nextInt();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Thread Count:"+Extras.reset);
				int threads = scnr.nextInt();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Base Clock (GHz):"+Extras.reset);
				double baseClock = scnr.nextDouble();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Boost Clock (GHz):"+Extras.reset);
				double boostClock = scnr.nextDouble();
				scnr.nextLine();
				
				newPart = new CPU(0, name, manufacturer, price, 
						socket, cores, threads, baseClock, boostClock);
				break;
			}
				
			case "GPU": { 
				System.out.println(Extras.underline+"\nEnter Chipset:"+Extras.reset);
				System.out.println(Extras.gray+"e.g. GeForce RTX 5070 Ti"+Extras.reset);
				String chipset = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Memory:"+Extras.reset);
				System.out.println(Extras.gray+"e.g. 16GB GDDR7"+Extras.reset);
				String vram = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Base Clock (GHz):"+Extras.reset);
				double baseClock = scnr.nextDouble();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Boost Clock (GHz):"+Extras.reset);
				double boostClock = scnr.nextDouble();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Length (mm):"+Extras.reset);
				int length = scnr.nextInt();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Colour:"+Extras.reset);
				String colour = scnr.nextLine();
				
				newPart = new GPU(0, name, manufacturer, price, 
						chipset, vram, baseClock, boostClock, length, colour);
				break;
			}
			
			case "Motherboard": {
				System.out.println(Extras.underline+"\nEnter Socket:"+Extras.reset);
				System.out.println(Extras.gray+"e.g. AM5"+Extras.reset);
				String socket = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Form Factor:"+Extras.reset);
				System.out.println(Extras.gray+"e.g. ATX"+Extras.reset);
				String formFactor = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Chipset:"+Extras.reset);
				System.out.println(Extras.gray+"e.g. AMD B650"+Extras.reset);
				String chipset = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Max Memory (GBs):"+Extras.reset);
				int maxMemory = scnr.nextInt();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nWiFi? (yes/no):"+Extras.reset);
				String wifiInput = scnr.nextLine().trim().toLowerCase();
				boolean wifi = wifiInput.equals("yes");
				
				System.out.println(Extras.underline+"\nEnter Colour:"+Extras.reset);
				String colour = scnr.nextLine();
				
				newPart = new Motherboard(0, name, manufacturer, price, 
						socket, formFactor, chipset, maxMemory, wifi, colour);
				break;
			}
			
			case "Case": {
				System.out.println(Extras.underline+"\nEnter Size:"+Extras.reset);
				System.out.println(Extras.gray+"e.g. ATX Mid Tower"+Extras.reset);
				String caseType = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Max GPU Length (mm):"+Extras.reset);
				int maxGpuLength = scnr.nextInt();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Colour:"+Extras.reset);
				String colour = scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Length (mm):"+Extras.reset);
				double length = scnr.nextDouble();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Width (mm):"+Extras.reset);
				double width = scnr.nextDouble();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Height (mm):"+Extras.reset);
				double height = scnr.nextDouble();
				scnr.nextLine();
				
				System.out.println(Extras.underline+"\nEnter Volume (L):"+Extras.reset);
				double volume = scnr.nextDouble();
				scnr.nextLine();
				
				newPart = new Case(0, name, manufacturer, price, caseType, 
						maxGpuLength, colour, length, width, height, volume);
				break;
	
			}
		}
		
		pm.addPart(newPart);
		System.out.println("\n"+Extras.successMsg+type+ " added successfully!"+Extras.reset);
	}
	
	/**
	 * Displays detailed information on a part
	 * @param scnr	console input
	 * @param pm	logic handler
	 */
	public void viewPart(Scanner scnr, PartMethods pm) {
	    
	    ArrayList<Part> allParts = pm.viewAllParts();
	    
	    viewAllParts(pm);
	    
	    if (allParts.isEmpty()) return; {
	    } 
	    
	    Part p = partIdCheck(scnr, pm);
	    if (p == null) return;
	    
	    String typeName = p.getType();
	    
	    System.out.println("\n" + Extras.cyanBold + typeName + Extras.reset);
	    
	    System.out.println("ID: " + Extras.lightgray + p.getId() + Extras.reset);
	    System.out.println("Name: " + Extras.lightgray + p.getName() + Extras.reset);
	    System.out.println("Manufacturer: " + Extras.lightgray + p.getManufacturer() + Extras.reset);
	    System.out.println("Price: " + Extras.lightgray + "£" + p.getPrice() + Extras.reset);

	    if (p instanceof CPU c) {
	        System.out.println("Socket: " + Extras.lightgray + c.getSocket() + Extras.reset);
	        System.out.println("Core Count: " + Extras.lightgray + c.getCores() + Extras.reset);
	        System.out.println("Thread Count: " + Extras.lightgray + c.getThreads() + Extras.reset);
	        System.out.println("Base Clock: " + Extras.lightgray + c.getBaseClock() + " GHz" + Extras.reset);
	        System.out.println("Boost Clock: " + Extras.lightgray + c.getBoostClock() + " GHz" + Extras.reset);
	    }

	    else if (p instanceof GPU g) {
	        System.out.println("Chipset: " + Extras.lightgray + g.getChipset() + Extras.reset);
	        System.out.println("VRAM: " + Extras.lightgray + g.getVram() + Extras.reset);
	        System.out.println("Base Clock: " + Extras.lightgray + g.getBaseClock() + " GHz" + Extras.reset);
	        System.out.println("Boost Clock: " + Extras.lightgray + g.getBoostClock() + " GHz" + Extras.reset);
	        System.out.println("Length: " + Extras.lightgray + g.getLength() + " mm" + Extras.reset);
	        System.out.println("Colour: " + Extras.lightgray + g.getColour() + Extras.reset);
	    }

	    else if (p instanceof Motherboard m) {
	        System.out.println("Socket: " + Extras.lightgray + m.getSocket() + Extras.reset);
	        System.out.println("Form Factor: " + Extras.lightgray + m.getFormFactor() + Extras.reset);
	        System.out.println("Chipset: " + Extras.lightgray + m.getChipset() + Extras.reset);
	        System.out.println("Max Memory: " + Extras.lightgray + m.getMaxMemory() + " GB" + Extras.reset);
	        System.out.println("WiFi: " + Extras.lightgray + (m.isWifi() ? "Yes" : "No") + Extras.reset);
	        System.out.println("Colour: " + Extras.lightgray + m.getColour() + Extras.reset);
	    }

	    else if (p instanceof Case ca) {
	        System.out.println("Size: " + Extras.lightgray + ca.getCaseType() + Extras.reset);
	        System.out.println("Max GPU Length: " + Extras.lightgray + ca.getMaxGpuLength() + " mm" + Extras.reset);
	        System.out.println("Colour: " + Extras.lightgray + ca.getColour() + Extras.reset);
	        System.out.println("Dimensions (LxWxH): " + Extras.lightgray +
	                           ca.getLength() + " mm × " +
	                           ca.getWidth() + " mm × " +
	                           ca.getHeight() + " mm" + Extras.reset);
	        System.out.println("Volume: " + Extras.lightgray + ca.getVolume() + " L" + Extras.reset);
	    }
	}

	/**
	 * Allows the user to edit a part
	 * The user selects an ID based on a list they are given, can then choose to keep the value or input a new one
	 * Type specific fields are editable depending on the part subclass
	 * @param scnr	console input
	 * @param pm	logic handler
	 */
	public void editPart(Scanner scnr, PartMethods pm) {
		
		ArrayList<Part> allParts = pm.viewAllParts();

	    viewAllParts(pm);
	    
	    if (allParts.isEmpty()) {
	    	return;
	    }

	    Part p = partIdCheck(scnr, pm);
	    if (p == null) return;

	    System.out.println(Extras.cyan + Extras.underline + "\nEditing " + p.getName() + Extras.reset);
	    System.out.println(Extras.gray + Extras.italic + "Press enter to keep current values." + Extras.reset);

	    System.out.println(Extras.underline + "\nCurrent Name:" + Extras.reset + " " +
	                       Extras.gray + Extras.italic + p.getName() + Extras.reset);
	    System.out.println("Enter New Name: ");
	    String newName = scnr.nextLine();
	    if (!newName.isEmpty()) p.setName(newName);

	    System.out.println(Extras.underline + "\nCurrent Manufacturer:" + Extras.reset + " " +
	                       Extras.gray + Extras.italic + p.getManufacturer() + Extras.reset);
	    System.out.println("Enter New Manufacturer: ");
	    String newManufacturer = scnr.nextLine();
	    if (!newManufacturer.isEmpty()) p.setManufacturer(newManufacturer);

	    System.out.println(Extras.underline + "\nCurrent Price:" + Extras.reset + " " +
	                       Extras.gray + Extras.italic + "£" + p.getPrice() + Extras.reset);
	    System.out.println("Enter New Price (£): ");
	    String priceInput = scnr.nextLine().trim();
	    if (!priceInput.isEmpty()) {
	        try {
	            p.setPrice(Double.parseDouble(priceInput));
	        } catch (NumberFormatException e) {
	            System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	        }
	    }

	    if (p instanceof CPU cpu) {

	        System.out.println(Extras.underline + "\nCurrent Socket:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + cpu.getSocket() + Extras.reset);
	        System.out.println("Enter New Socket: ");
	        String newSocket = scnr.nextLine();
	        if (!newSocket.isEmpty()) cpu.setSocket(newSocket);

	        System.out.println(Extras.underline + "\nCurrent Core Count:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + cpu.getCores() + Extras.reset);
	        System.out.println("Enter New Core Count: ");
	        String coresInput = scnr.nextLine().trim();
	        if (!coresInput.isEmpty()) {
	            try {
	                cpu.setCores(Integer.parseInt(coresInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Thread Count:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + cpu.getThreads() + Extras.reset);
	        System.out.println("Enter New Thread Count: ");
	        String threadsInput = scnr.nextLine().trim();
	        if (!threadsInput.isEmpty()) {
	            try {
	                cpu.setThreads(Integer.parseInt(threadsInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Base Clock:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + cpu.getBaseClock() + " GHz" + Extras.reset);
	        System.out.println("Enter New Base Clock: ");
	        String baseClockInput = scnr.nextLine().trim();
	        if (!baseClockInput.isEmpty()) {
	            try {
	                cpu.setBaseClock(Double.parseDouble(baseClockInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Boost Clock:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + cpu.getBoostClock() + " GHz" + Extras.reset);
	        System.out.println("Enter New Boost Clock: ");
	        String boostClockInput = scnr.nextLine().trim();
	        if (!boostClockInput.isEmpty()) {
	            try {
	                cpu.setBoostClock(Double.parseDouble(boostClockInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	    }

	    else if (p instanceof GPU gpu) {

	        System.out.println(Extras.underline + "\nCurrent Chipset:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + gpu.getChipset() + Extras.reset);
	        System.out.println("Enter New Chipset: ");
	        String newChipset = scnr.nextLine();
	        if (!newChipset.isEmpty()) gpu.setChipset(newChipset);

	        System.out.println(Extras.underline + "\nCurrent Memory:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + gpu.getVram() + Extras.reset);
	        System.out.println("Enter New Memory: ");
	        String newVram = scnr.nextLine();
	        if (!newVram.isEmpty()) gpu.setVram(newVram);

	        System.out.println(Extras.underline + "\nCurrent Base Clock:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + gpu.getBaseClock() + " GHz" + Extras.reset);
	        System.out.println("Enter New Base Clock: ");
	        String baseClockInput = scnr.nextLine().trim();
	        if (!baseClockInput.isEmpty()) {
	            try {
	                gpu.setBaseClock(Double.parseDouble(baseClockInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Boost Clock:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + gpu.getBoostClock() + " GHz" + Extras.reset);
	        System.out.println("Enter New Boost Clock: ");
	        String boostClockInput = scnr.nextLine().trim();
	        if (!boostClockInput.isEmpty()) {
	            try {
	                gpu.setBoostClock(Double.parseDouble(boostClockInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Length:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + gpu.getLength() + " mm" + Extras.reset);
	        System.out.println("Enter New Length: ");
	        String lengthInput = scnr.nextLine().trim();
	        if (!lengthInput.isEmpty()) {
	            try {
	                gpu.setLength(Integer.parseInt(lengthInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Colour:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + gpu.getColour() + Extras.reset);
	        System.out.println("Enter New Colour: ");
	        String newColour = scnr.nextLine();
	        if (!newColour.isEmpty()) gpu.setColour(newColour);
	    }

	    else if (p instanceof Motherboard m) {

	        System.out.println(Extras.underline + "\nCurrent Socket:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + m.getSocket() + Extras.reset);
	        System.out.println("Enter New Socket: ");
	        String newSocket = scnr.nextLine();
	        if (!newSocket.isEmpty()) m.setSocket(newSocket);

	        System.out.println(Extras.underline + "\nCurrent Form Factor:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + m.getFormFactor() + Extras.reset);
	        System.out.println("Enter New Form Factor: ");
	        String newFormFactor = scnr.nextLine();
	        if (!newFormFactor.isEmpty()) m.setFormFactor(newFormFactor);

	        System.out.println(Extras.underline + "\nCurrent Chipset:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + m.getChipset() + Extras.reset);
	        System.out.println("Enter New Chipset: ");
	        String newChipset = scnr.nextLine();
	        if (!newChipset.isEmpty()) m.setChipset(newChipset);

	        System.out.println(Extras.underline + "\nCurrent Max Memory:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + m.getMaxMemory() + " GB" + Extras.reset);
	        System.out.println("Enter New Max Memory: ");
	        String memInput = scnr.nextLine().trim();
	        if (!memInput.isEmpty()) {
	            try {
	                m.setMaxMemory(Integer.parseInt(memInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrently has WiFi?:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + (m.isWifi() ? "Yes" : "No") + Extras.reset);
	        System.out.println("Still has WiFi built in? (yes/no): ");
	        String wifiInput = scnr.nextLine().trim().toLowerCase();
	        if (!wifiInput.isEmpty()) {
	            if (wifiInput.equals("yes")) m.setWifi(true);
	            else if (wifiInput.equals("no")) m.setWifi(false);
	            else System.out.println(Extras.errorMsg + "Invalid input. Keeping current value." + Extras.reset);
	        }

	        System.out.println(Extras.underline + "\nCurrent Colour:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + m.getColour() + Extras.reset);
	        System.out.println("Enter New Colour: ");
	        String newColour = scnr.nextLine();
	        if (!newColour.isEmpty()) m.setColour(newColour);
	    }

	    else if (p instanceof Case pc) {

	        System.out.println(Extras.underline + "\nCurrent Size:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + pc.getCaseType() + Extras.reset);
	        System.out.println("Enter New Type: ");
	        String newType = scnr.nextLine();
	        if (!newType.isEmpty()) pc.setCaseType(newType);

	        System.out.println(Extras.underline + "\nCurrent Max GPU Length:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + pc.getMaxGpuLength() + " mm" + Extras.reset);
	        System.out.println("Enter New Max GPU Length: ");
	        String mglInput = scnr.nextLine().trim();
	        if (!mglInput.isEmpty()) {
	            try {
	                pc.setMaxGpuLength(Integer.parseInt(mglInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Colour:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + pc.getColour() + Extras.reset);
	        System.out.println("Enter New Colour: ");
	        String newColour = scnr.nextLine();
	        if (!newColour.isEmpty()) pc.setColour(newColour);

	        System.out.println(Extras.underline + "\nCurrent Length:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + pc.getLength() + " mm" + Extras.reset);
	        System.out.println("Enter New Length: ");
	        String lenInput = scnr.nextLine().trim();
	        if (!lenInput.isEmpty()) {
	            try {
	                pc.setLength(Double.parseDouble(lenInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Width:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + pc.getWidth() + " mm" + Extras.reset);
	        System.out.println("Enter New Width: ");
	        String widthInput = scnr.nextLine().trim();
	        if (!widthInput.isEmpty()) {
	            try {
	                pc.setWidth(Double.parseDouble(widthInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Height:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + pc.getHeight() + " mm" + Extras.reset);
	        System.out.println("Enter New Height: ");
	        String heightInput = scnr.nextLine().trim();
	        if (!heightInput.isEmpty()) {
	            try {
	                pc.setHeight(Double.parseDouble(heightInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }

	        System.out.println(Extras.underline + "\nCurrent Volume:" + Extras.reset + " " +
	                           Extras.gray + Extras.italic + pc.getVolume() + " L" + Extras.reset);
	        System.out.println("Enter New Volume: ");
	        String volInput = scnr.nextLine().trim();
	        if (!volInput.isEmpty()) {
	            try {
	                pc.setVolume(Double.parseDouble(volInput));
	            } catch (NumberFormatException e) {
	                System.out.println(Extras.errorMsg + "Invalid number. Keeping current value." + Extras.reset);
	            }
	        }
	    }

	    System.out.println(Extras.successMsg + "\nPart updated successfully!" + Extras.reset);
	}
	
}
