package main;

import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import customer.*;
import gui.*;
import order.*;
import part.*;
import storage.*;

/**
 * Entry point for the stock control system
 * This class initialises the program, loads saved data and allows the user to choose between console and gui mode
 * Console menu uses menu classes such as {@link PartMenu} to interact with the system
 * GUI mode launches {@link gui.MainGUI} using Swing
 */

public class Main {
	
	/**
	 * Starts the application
	 * Displays a startup logo, initialises logic handlers, loads stored data using {@link storage.FileManager} and presents a launch menu to run console or gui mode
	 * @param args
	 */

    public static void main(String[] args) {
    	
    	// ASCII Art
    	Extras.printLogoAnimated();

        Scanner scnr = new Scanner(System.in);

        PartMethods partMethods = new PartMethods();
        CustomerMethods customerMethods = new CustomerMethods();
        OrderMethods orderMethods = new OrderMethods();
        
        FileManager.loadParts(partMethods);
        FileManager.loadCustomers(customerMethods);
        FileManager.loadOrders(orderMethods, partMethods, customerMethods);

        boolean menuOpen = true;

        while (menuOpen) {
        	
        	System.out.println("\n─── " + Extras.whiteBg + Extras.HarryPCPartsBlackBg + Extras.reset + " ────────────────");
        	System.out.println(Extras.pink+"1."+Extras.reset+" Run Program (Console)");
        	System.out.println("2. Run Program (GUI)");
        	System.out.println(Extras.red+"3."+Extras.reset+" Exit →");
        	System.out.println("─── Choose Option ─────────────────────");

            int choice = scnr.nextInt();
            scnr.nextLine();

            switch (choice) {
                case 1 -> runConsole(scnr, partMethods, customerMethods, orderMethods);
                
                case 2 -> {
                    menuOpen = false;
                    SwingUtilities.invokeLater(() -> {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Match user's OS theme
                        } catch (Exception ignored) {}

                        new gui.MainGUI(partMethods, customerMethods, orderMethods);
                    });
                }

                default -> System.out.println(Extras.errorMsg+"\nInvalid option."+Extras.reset);
            }
        }

        scnr.close();
    }
    
    /**
     * Runs the console version of the program
     * Routes the user into parts, customers or orders via {@link PartMenu}, {@link CustomerMenu}, {@link OrderMenu}
     * @param scnr used for user input
     * @param pm logic handler for parts
     * @param cm logic handler for customers
     * @param om logic handler for orders
     */
    public static void runConsole(Scanner scnr, PartMethods pm,
                                  CustomerMethods cm, OrderMethods om) {

        PartMenu partMenu = new PartMenu();
        CustomerMenu customerMenu = new CustomerMenu();
        OrderMenu orderMenu = new OrderMenu();

        boolean running = true;

        while (running) {

        	System.out.println("\n─── " + Extras.pinkBg + " Main Menu " + Extras.reset + " ────────────────────────");
        	System.out.println(Extras.cyan+"1."+Extras.reset+" Manage " + Extras.underline + "Parts" + Extras.reset);
        	System.out.println(Extras.yellow+"2."+Extras.reset+" Manage " + Extras.underline + "Customers" + Extras.reset);
        	System.out.println(Extras.lime+"3."+Extras.reset+" Manage " + Extras.underline + "Orders" + Extras.reset);
        	System.out.println(Extras.red+"4."+Extras.reset+" Back → " + Extras.HarryPCParts);
        	System.out.println("─── Choose Option ──────────────────────");

            int choice = scnr.nextInt();
            scnr.nextLine();

            switch (choice) {
                case 1 -> partMenu.show(scnr, pm);
                case 2 -> customerMenu.show(scnr, cm);
                case 3 -> orderMenu.show(scnr, om, pm, cm);
                case 4 -> running = false;
                default -> System.out.println(Extras.errorMsg+"Invalid option."+Extras.reset);
            }
        }
    }

}
