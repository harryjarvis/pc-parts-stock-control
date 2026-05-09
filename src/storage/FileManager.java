package storage;

import part.PartMethods;
import customer.CustomerMethods;
import order.OrderMethods;

/**
 * Student ID: 21360548
 * Central access point for loading and saving data (csv's)
 * Delegates persistence operations to appropriate file handler classes for parts, customers and orders
 * File paths are defined once and reused to ensure consistency and portability
 */
public class FileManager {

    private static final String partFileName = "parts.csv";
    
    private static final String customerFileName = "customers.csv";
    
    private static final String orderFileName = "orders.csv";

    /**
     * Saves all parts currently in memory to persistent storage
     * @param pm	part logic handler containing parts to save
     */
    public static void saveParts(PartMethods pm) {
        PartFileHandler handler = new PartFileHandler();
        handler.saveParts(partFileName, pm);
    }

    /**
     * Loads all parts from persistent storage into memory
     * @param pm	part logic handler used to store loaded parts
     */
    public static void loadParts(PartMethods pm) {
        PartFileHandler handler = new PartFileHandler();
        handler.loadParts(partFileName, pm);
    }
    
    /**
     * Saves all customers currently in memory to persistent storage
     * @param cm	customer logic handler containing customers to save
     */
    public static void saveCustomers(CustomerMethods cm) {
    	CustomerFileHandler handler = new CustomerFileHandler();
    	handler.saveCustomers(customerFileName, cm);
    }
    
    /**
     * Loads all customers from persistent storage into memory
     * @param cm	customer logic handler used to store loaded customers
     */
    public static void loadCustomers(CustomerMethods cm) {
    	CustomerFileHandler handler = new CustomerFileHandler();
    	handler.loadCustomers(customerFileName, cm);
    }
    
    /**
     * Loads all orders from persistent storage into memory
     * @param om	order logic handler used to store loaded orders
     * @param pm	part logic handler used to store loaded parts
     * @param cm	customer logic handler used to store loaded customers
     */
    public static void loadOrders(OrderMethods om, PartMethods pm, CustomerMethods cm) {
    	OrderFileHandler handler = new OrderFileHandler();
    	handler.loadOrders(orderFileName, om, pm, cm);
    }
    
    /**
     * Saves all orders in memory to persistent storage
     * @param om	order logic handler containing orders to save
     */
    public static void saveOrders(OrderMethods om) {
    	OrderFileHandler handler = new OrderFileHandler();
    	handler.saveOrders(orderFileName, om);
    }
}
