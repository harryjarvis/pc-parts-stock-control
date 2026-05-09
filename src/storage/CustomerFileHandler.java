package storage;

import java.io.*;
import customer.*;
import main.Extras;

/**
 * Student ID: 21360548
 * Handles loading and saving {@link Customer} data to and from persistent storage.
 * This class is responsible for reading customer data from a file and reconstructing
 * the appropriate customer subclass ({@link IndividualCustomer},
 * {@link BusinessCustomer}, or {@link EducationalCustomer}) based on the stored type.
 * It also serialises customer objects back to file in a comma-separated format.
 */
public class CustomerFileHandler {

    /**
     * Loads customer data from a file into memory.
     * Each line of the file represents a single customer. The first value indicates
     * the customer type, which determines which subclass is instantiated.
     * Existing customer IDs are preserved, and the {@link CustomerMethods} ID counter
     * is updated accordingly.
     * @param filePath path to the customer data file
     * @param cm       customer logic handler used to store loaded customers
     */
    public void loadCustomers(String filePath, CustomerMethods cm) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                // Split CSV line into fields
                String[] data = line.split(",");

                // Remove BOM if present
                data[0] = data[0].replace("\uFEFF", "").trim();

                String type = data[0].trim();

                int id = Integer.parseInt(data[1].trim());
                String name = data[2].trim();
                String email = data[3].trim();
                String password = data[4].trim();
                String address = data[5].trim();

                Customer customer = switch (type) {

                    case "IndividualCustomer", "Individual" -> {
                        String preferredContact = data[6].trim();
                        int loyaltyPoints = Integer.parseInt(data[7].trim());
                        String favouritePart = data[8].trim();

                        yield new IndividualCustomer(
                                id, name, email, password, address,
                                preferredContact, loyaltyPoints, favouritePart
                        );
                    }

                    case "BusinessCustomer", "Business" -> {
                        String contactName = data[6].trim();
                        String businessType = data[7].trim();
                        String taxId = data[8].trim();
                        double discountRate = Double.parseDouble(data[9].trim());

                        yield new BusinessCustomer(
                                id, name, email, password, address,
                                contactName, businessType, taxId, discountRate
                        );
                    }

                    case "EducationalCustomer", "Educational" -> {
                        String contactName = data[6].trim();
                        String instituteType = data[7].trim();
                        String department = data[8].trim();
                        double discountRate = Double.parseDouble(data[9].trim());

                        yield new EducationalCustomer(
                                id, name, email, password, address,
                                contactName, instituteType, department, discountRate
                        );
                    }

                    default -> {
                        System.out.println(
                                Extras.errorMsg + "Unknown customer type: " + type + Extras.reset
                        );
                        yield null;
                    }
                };

                if (customer != null) {
                    cm.addCustomerFromLoad(customer);
                }
            }

        } catch (Exception e) {
            System.out.println(
                    Extras.errorMsg + "Warning: Customers Not Loaded! "
                            + e.getMessage() + Extras.reset
            );
            e.printStackTrace();
        }
    }

    /**
     * Saves all customers currently stored in memory to a file.
     * Each customer is written as a single line in a comma-separated format.
     * The customer type is written first so that the correct subclass can be
     * reconstructed when loading.
     *
     * @param filePath path to the output customer data file
     * @param cm       customer logic handler containing customers to save
     */
    public void saveCustomers(String filePath, CustomerMethods cm) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (Customer c : cm.viewAllCustomers()) {

                pw.print(c.getType() + "," +
                         c.getId() + "," +
                         c.getName() + "," +
                         c.getEmail() + "," +
                         c.getPassword() + "," +
                         c.getAddress());

                if (c instanceof IndividualCustomer ic) {
                    pw.println("," +
                               ic.getPreferredContact() + "," +
                               ic.getLoyaltyPoints() + "," +
                               ic.getFavouritePart());
                }

                else if (c instanceof BusinessCustomer bc) {
                    pw.println("," +
                               bc.getContactName() + "," +
                               bc.getBusinessType() + "," +
                               bc.getTaxId() + "," +
                               bc.getDiscountRate());
                }

                else if (c instanceof EducationalCustomer ec) {
                    pw.println("," +
                               ec.getContactName() + "," +
                               ec.getInstituteType() + "," +
                               ec.getDepartment() + "," +
                               ec.getDiscountRate());
                }
            }

            System.out.println(
                    Extras.successMsg + "\nCustomers saved successfully!" + Extras.reset
            );

        } catch (Exception e) {
            System.out.println(
                    Extras.errorMsg + "\nWarning: Customers Not Saved! "
                            + e.getMessage() + Extras.reset
            );
            e.printStackTrace();
        }
    }
}