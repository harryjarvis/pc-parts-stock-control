package storage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import order.*;
import part.*;
import customer.*;
import main.Extras;

/**
 * Student ID: 21360548
 * Handles loading and saving {@link Order} data to and from persistent storage.
 * Orders are stored in a comma-separated format where each line represents
 * a single order, including its customer reference, status, date, and a
 * compact representation of ordered items.
 * When loading, this class reconstructs {@link OrderItem} objects by matching
 * stored part IDs with existing {@link Part} objects in memory.
 */
public class OrderFileHandler {

    /**
     * Loads orders from a file into memory.
     * Each order line contains:
     *   <li>Order ID</li>
     *   <li>Customer ID</li>
     *   <li>Order status</li>
     *   <li>Order date</li>
     *   <li>Item list in the format PART_ID:QTY | PART_ID:QTY</li>
     * Orders are only loaded if the referenced customer and parts exist.
     * @param filePath path to the order data file
     * @param om       order logic handler used to store loaded orders
     * @param pm       part logic handler used to resolve part references
     * @param cm       customer logic handler used to resolve customer references
     */
    public void loadOrders(String filePath, OrderMethods om,
                           PartMethods pm, CustomerMethods cm) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                // Remove BOM if present
                data[0] = data[0].replace("\uFEFF", "").trim();

                int orderId = Integer.parseInt(data[0].trim());
                int customerId = Integer.parseInt(data[1].trim());
                String status = data[2].trim();
                LocalDate date = LocalDate.parse(data[3].trim());

                String itemsString = data[4].trim();

                Customer customer = cm.viewCustomer(customerId);
                if (customer == null) {
                    System.out.println(
                        Extras.errorMsg +
                        "Order " + orderId + " skipped (customer not found)." +
                        Extras.reset
                    );
                    continue;
                }

                ArrayList<OrderItem> itemList = new ArrayList<>();

                // Split stored items into individual part entries
                String[] items = itemsString.split("\\|");
                for (String it : items) {
                    it = it.trim();
                    if (it.isEmpty()) continue;

                    // PART_ID:QTY
                    String[] parts = it.split(":");
                    int partId = Integer.parseInt(parts[0].trim());
                    int qty = Integer.parseInt(parts[1].trim());

                    Part p = pm.viewPart(partId);
                    if (p != null) {
                        itemList.add(new OrderItem(p, qty));
                    }
                }

                Order order = new Order(orderId, customer, itemList, status, date);
                om.addOrderFromLoad(order);
            }

        } catch (Exception e) {
            System.out.println(
                Extras.errorMsg +
                "Warning: Orders Not Loaded! " +
                e.getMessage() +
                Extras.reset
            );
        }
    }

    /**
     * Saves all orders currently stored in memory to a file.
     * Each order is written as a single line containing the order ID,
     * customer ID, status, date, and a compact list of items.
     * Item lists are saved in the format {@code PART_ID:QTY | PART_ID:QTY}
     * to allow efficient reconstruction when loading.
     * @param filePath path to the output order data file
     * @param om       order logic handler containing orders to save
     */
    public void saveOrders(String filePath, OrderMethods om) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (Order o : om.viewAllOrders()) {

                // Build item string: PART_ID:QTY | PART_ID:QTY
                StringBuilder items = new StringBuilder();

                for (OrderItem item : o.getItems()) {
                    items.append(item.getPart().getId())
                         .append(":")
                         .append(item.getQuantity())
                         .append(" | ");
                }

                // Remove trailing separator
                if (items.length() > 3) {
                    items.setLength(items.length() - 3);
                }

                pw.println(
                        o.getId() + "," +
                        o.getCustomer().getId() + "," +
                        o.getStatus() + "," +
                        o.getDate() + "," +
                        items
                );
            }

            System.out.println(
                Extras.successMsg +
                "Orders saved successfully." +
                Extras.reset
            );

        } catch (Exception e) {
            System.out.println(
                Extras.errorMsg +
                "Warning: Orders Not Saved! " +
                e.getMessage() +
                Extras.reset
            );
        }
    }
}
