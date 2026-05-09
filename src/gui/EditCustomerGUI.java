package gui;

import customer.*;
import java.awt.Window;
import javax.swing.*;
import java.awt.*;

/**
 * Student ID: 21360548
 * Dialog for editing an existing {@link customer.Customer}.
 * UI is built dynamically based on the type of customer
 * When saved, fields are validated and applied back onto the same object instance
 * Lists and tables are automatically updated
 */

public class EditCustomerGUI extends JDialog {

    private final Customer customer;

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;   // never display plaintext
    private JTextField addressField;

    private JTextField preferredContactField;
    private JTextField loyaltyPointsField;
    private JTextField favouritePartField;

    private JTextField businessContactNameField;
    private JTextField businessTypeField;
    private JTextField taxIdField;
    private JTextField businessDiscountRateField;

    private JTextField eduContactNameField;
    private JTextField instituteTypeField;
    private JTextField departmentField;
    private JTextField eduDiscountRateField;

    private JPanel formPanel;

    /**
     * Creates and shows the edit part dialog
     * @param parent parent window used for centering and model behaviour
     * @param customer the customer object to edit
     */
    public EditCustomerGUI(Window parent, Customer customer) {
        super(parent, "Edit Customer (ID: " + customer.getId() + ")", ModalityType.APPLICATION_MODAL);
        this.customer = customer;

        editCustomerGUI();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Builds main layout
     * title bar
     * scrollable form content
     * save/cancel buttons
     */
    private void editCustomerGUI() {
        setSize(450, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Editing: " + customer.getName() + " (" + customer.getType() + ")");
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(title, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(0, 2, 6, 6));
        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);

        buildForm(); // builds fields based on selected customer type

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> saveChanges());
        cancelBtn.addActionListener(e -> dispose());

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds the common customer fields and then type-specific fields based on class of {@link #customer}.
     */
    private void buildForm() {
        nameField = new JTextField(customer.getName(), 15);
        emailField = new JTextField(customer.getEmail(), 15);

        // Don't prefill password with real password
        passwordField = new JPasswordField(15);
        passwordField.setText(""); // user can type a new one if they want

        addressField = new JTextField(customer.getAddress(), 15);

        addRow("Name:", nameField);
        addRow("Email:", emailField);
        addRow("New Password:", passwordField);
        addRow("Address:", addressField);

        if (customer instanceof IndividualCustomer ic) {
            preferredContactField = new JTextField(ic.getPreferredContact(), 15);
            loyaltyPointsField = new JTextField(String.valueOf(ic.getLoyaltyPoints()), 6);
            favouritePartField = new JTextField(ic.getFavouritePart(), 15);

            addRow("Preferred Contact:", preferredContactField);
            addRow("Loyalty Points:", loyaltyPointsField);
            addRow("Favourite Part:", favouritePartField);
        }
        else if (customer instanceof BusinessCustomer bc) {
            businessContactNameField = new JTextField(bc.getContactName(), 15);
            businessTypeField = new JTextField(bc.getBusinessType(), 15);
            taxIdField = new JTextField(bc.getTaxId(), 15);
            businessDiscountRateField = new JTextField(String.valueOf(bc.getDiscountRate()), 6);

            addRow("Contact Name:", businessContactNameField);
            addRow("Business Type:", businessTypeField);
            addRow("Tax ID:", taxIdField);
            addRow("Discount Rate:", businessDiscountRateField);
        }
        else if (customer instanceof EducationalCustomer ec) {
            eduContactNameField = new JTextField(ec.getContactName(), 15);
            instituteTypeField = new JTextField(ec.getInstituteType(), 15);
            departmentField = new JTextField(ec.getDepartment(), 15);
            eduDiscountRateField = new JTextField(String.valueOf(ec.getDiscountRate()), 6);

            addRow("Contact Name:", eduContactNameField);
            addRow("Institute Type:", instituteTypeField);
            addRow("Department:", departmentField);
            addRow("Discount Rate:", eduDiscountRateField);
        }
    }

    /**
     * Helper to add a label + input component pair to the form panel
     */
    private void addRow(String label, JComponent field) {
        formPanel.add(new JLabel(label));
        formPanel.add(field);
    }

    /**
     * Validates user input back onto the existing customer instance
     * Updates the same object reference
     */
    private void saveChanges() {
        try {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newAddress = addressField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, email, and address cannot be empty.");
                return;
            }

            customer.setName(newName);
            customer.setEmail(newEmail);
            customer.setAddress(newAddress);

            // Only update password if user typed one
            String newPassword = new String(passwordField.getPassword()).trim();
            if (!newPassword.isEmpty()) {
                customer.setPassword(newPassword);
            }

            if (customer instanceof IndividualCustomer ic) {
                ic.setPreferredContact(preferredContactField.getText().trim());
                ic.setLoyaltyPoints(Integer.parseInt(loyaltyPointsField.getText().trim()));
                ic.setFavouritePart(favouritePartField.getText().trim());
            }
            else if (customer instanceof BusinessCustomer bc) {
                bc.setContactName(businessContactNameField.getText().trim());
                bc.setBusinessType(businessTypeField.getText().trim());
                bc.setTaxId(taxIdField.getText().trim());
                bc.setDiscountRate(Double.parseDouble(businessDiscountRateField.getText().trim()));
            }
            else if (customer instanceof EducationalCustomer ec) {
                ec.setContactName(eduContactNameField.getText().trim());
                ec.setInstituteType(instituteTypeField.getText().trim());
                ec.setDepartment(departmentField.getText().trim());
                ec.setDiscountRate(Double.parseDouble(eduDiscountRateField.getText().trim()));
            }

            dispose();

        } catch (NumberFormatException ex) { // prevents GUI from crashing due to invalid inputs
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values (loyalty points / discount rate).",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
