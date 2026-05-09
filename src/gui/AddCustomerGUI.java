package gui;

import customer.*;
import java.awt.Window;
import javax.swing.*;
import java.awt.*;

/** 
 * Student ID: 21360548
 * Dialog for creating a new {@link customer.Customer}.
 * Supports multiple customer types, individual, business, educational
 * dynamically updates the form based on selected type
 */

public class AddCustomerGUI extends JDialog {

	/** reference to logic handler for customers */
    private final CustomerMethods customerMethods;

    private JTextField nameField = new JTextField(15);
    private JTextField emailField = new JTextField(15);
    private JTextField passwordField = new JTextField(15);
    private JTextField addressField = new JTextField(15);

    private JTextField preferredContactField = new JTextField(15);
    private JTextField loyaltyPointsField = new JTextField(6);
    private JTextField favouritePartField = new JTextField(15);

    private JTextField businessContactNameField = new JTextField(15);
    private JTextField businessTypeField = new JTextField(15);
    private JTextField taxIdField = new JTextField(15);
    private JTextField businessDiscountRateField = new JTextField(6);

    private JTextField eduContactNameField = new JTextField(15);
    private JTextField instituteTypeField = new JTextField(15);
    private JTextField departmentField = new JTextField(15);
    private JTextField eduDiscountRateField = new JTextField(6);

    private JComboBox<String> typeCombo;
    private JPanel formPanel;
    
    /**
     * Creates and shows the add customer dialog
    * @param parent window for modal behaviour
    * @param customerMethods reference to customer logic
    */

    public AddCustomerGUI(Window parent, CustomerMethods customerMethods) {
        super(parent, "Add Customer", ModalityType.APPLICATION_MODAL);
        this.customerMethods = customerMethods;

        addCustomerGUI();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Builds main dialog layout and UI components
     */
    private void addCustomerGUI() {
        setSize(450, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Top panel to select customer type
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Customer Type:"));

        // Changing type dynamically rebuilds the form
        typeCombo = new JComboBox<>(new String[]{
                "Individual", "Business", "Educational"
        });
        
        // Scrollable form depending on field count
        typeCombo.addActionListener(e -> updateForm());

        topPanel.add(typeCombo);
        add(topPanel, BorderLayout.NORTH);

        formPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> saveCustomer());
        cancelBtn.addActionListener(e -> dispose());

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        updateForm();
    }

    /**
     * Rebuilds form dynamically based on selected type
     * Avoids multiple dialogs and promotes OOP
     */
    private void updateForm() {
        formPanel.removeAll();
        formPanel.setLayout(new GridLayout(0, 2, 6, 6));

        addRow("Name:", nameField);
        addRow("Email:", emailField);
        addRow("Password:", passwordField);
        addRow("Address:", addressField);

        String type = (String) typeCombo.getSelectedItem();

        if ("Individual".equals(type)) {
            addRow("Preferred Contact:", preferredContactField);
            addRow("Loyalty Points:", loyaltyPointsField);
            addRow("Favourite Part:", favouritePartField);
        }
        else if ("Business".equals(type)) {
            addRow("Contact Name:", businessContactNameField);
            addRow("Business Type:", businessTypeField);
            addRow("Tax ID:", taxIdField);
            addRow("Discount Rate:", businessDiscountRateField);
        }
        else if ("Educational".equals(type)) {
            addRow("Contact Name:", eduContactNameField);
            addRow("Institute Type:", instituteTypeField);
            addRow("Department:", departmentField);
            addRow("Discount Rate:", eduDiscountRateField);
        }

        formPanel.revalidate(); // refresh UI after changes
        formPanel.repaint();
    }

    /** 
     * Helper method to add a label-field pair to the form
     */
    private void addRow(String label, JComponent field) {
        formPanel.add(new JLabel(label));
        formPanel.add(field);
    }

    /** 
     * Validates input, creates the appropriate customer subtype 
     * Delegates persistence to CustomerMethods
     * Uses defensive parsing to prevent invalid inputs
     */
    private void saveCustomer() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all common fields.");
                return;
            }

            String type = (String) typeCombo.getSelectedItem();

            // Instantiate correct customer type
            if ("Individual".equals(type)) {
                String preferredContact = preferredContactField.getText().trim();
                int loyaltyPoints = Integer.parseInt(loyaltyPointsField.getText().trim());
                String favouritePart = favouritePartField.getText().trim();

                IndividualCustomer ic = new IndividualCustomer(
                        0, name, email, password, address,
                        preferredContact, loyaltyPoints, favouritePart
                );
                customerMethods.addCustomer(ic);
            }
            else if ("Business".equals(type)) {
                String contactName = businessContactNameField.getText().trim();
                String businessType = businessTypeField.getText().trim();
                String taxId = taxIdField.getText().trim();
                double discountRate = Double.parseDouble(businessDiscountRateField.getText().trim());

                BusinessCustomer bc = new BusinessCustomer(
                        0, name, email, password, address,
                        contactName, businessType, taxId, discountRate
                );
                customerMethods.addCustomer(bc);
            }
            else if ("Educational".equals(type)) {
                String contactName = eduContactNameField.getText().trim();
                String instituteType = instituteTypeField.getText().trim();
                String department = departmentField.getText().trim();
                double discountRate = Double.parseDouble(eduDiscountRateField.getText().trim());

                EducationalCustomer ec = new EducationalCustomer(
                        0, name, email, password, address,
                        contactName, instituteType, department, discountRate
                );
                customerMethods.addCustomer(ec);
            }

            dispose(); // Closes window after save

        } catch (NumberFormatException ex) { // Checks for invalid entries
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values (loyalty points / discount rate).",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
