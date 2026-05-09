package gui;

import customer.Customer;
import java.awt.Window;
import customer.CustomerMethods;
import order.Order;
import order.OrderItem;
import order.OrderMethods;
import part.Part;
import part.PartMethods;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Student ID: 21360548
 * Dialog for creating a new {@link order.Order).
 * Allows the user to choose a customer, set date/status and build an order item list
 * Item behaviour matches the console version: if a part already exists in the list
 * then adding it again increases its quantity rather than creating a duplicate line
 */

public class AddOrderGUI extends JDialog {

	/** Reference to in-memory order logic (add/view/delete) */
    private final OrderMethods orderMethods;
    
    /** Reference to customer list for selection in the UI */
    private final CustomerMethods customerMethods;
    
    /** Reference to parts list for selection in the UI */
    private final PartMethods partMethods;

    private JComboBox<Customer> customerCombo;
    private JTextField dateField;
    private JTextField statusField;

    private JComboBox<Part> partCombo;
    private JSpinner qtySpinner;

    private DefaultListModel<OrderItem> itemsModel;
    private JList<OrderItem> itemsList;
    
    /**
     * Creates and shows the Add Order dialog
     * @param parent parent window used for centering and modal behaviour
     * @param orderMethods reference to order logic
     * @param customerMethods reference to customer logic (For customer selection)
     * @param partMethods reference to part logic (For part selection)
     */

    public AddOrderGUI(
            Window parent,
            OrderMethods orderMethods,
            CustomerMethods customerMethods,
            PartMethods partMethods
    ) {
        super(parent, "Add Order", ModalityType.APPLICATION_MODAL);

        this.orderMethods = orderMethods;
        this.customerMethods = customerMethods;
        this.partMethods = partMethods;

        addOrderGUI();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    /**
     * Builds the dialog layout:
     * top has basic order metadata (customer/date/status)
     * center has item builder + current item list
     * bottom has create/cancel actions
     */

    private void addOrderGUI() {
        setSize(700, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new GridLayout(0, 2, 8, 8));
        top.setBorder(BorderFactory.createTitledBorder("Order Details"));

        // Custom renderer so user sees "ID - Name" instead of Customer.toString()
        customerCombo = new JComboBox<>(customerMethods.viewAllCustomers().toArray(new Customer[0]));
        customerCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer c) {
                    setText("ID " + c.getId() + " - " + c.getName());
                }
                return this;
            }
        });

        // Default to today's date
        dateField = new JTextField(LocalDate.now().toString(), 10); // yyyy-MM-dd
        statusField = new JTextField("Pending", 10);

        top.add(new JLabel("Customer:"));
        top.add(customerCombo);
        top.add(new JLabel("Date (yyyy-MM-dd):"));
        top.add(dateField);
        top.add(new JLabel("Status:"));
        top.add(statusField);

        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel itemBuilder = new JPanel(new GridLayout(0, 2, 8, 8));
        itemBuilder.setBorder(BorderFactory.createTitledBorder("Add Item"));

        partCombo = new JComboBox<>(partMethods.viewAllParts().toArray(new Part[0]));
        partCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Part p) {
                    setText("ID " + p.getId() + " - " + p.getName());
                }
                return this;
            }
        });

        // Spinner prevents invalid quantity input - minimum of 1
        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        JButton addItemBtn = new JButton("Add / Update Item");
        JButton removeItemBtn = new JButton("Remove Selected Item");

        addItemBtn.addActionListener(e -> addOrUpdateItem());
        removeItemBtn.addActionListener(e -> removeSelectedItem());

        itemBuilder.add(new JLabel("Part:"));
        itemBuilder.add(partCombo);
        itemBuilder.add(new JLabel("Quantity:"));
        itemBuilder.add(qtySpinner);
        itemBuilder.add(addItemBtn);
        itemBuilder.add(removeItemBtn);

        center.add(itemBuilder, BorderLayout.NORTH);

        itemsModel = new DefaultListModel<>();
        itemsList = new JList<>(itemsModel);
        itemsList.setVisibleRowCount(10);
        itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroll = new JScrollPane(itemsList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Order Items"));

        center.add(listScroll, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveBtn = new JButton("Create Order");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> createOrder());
        cancelBtn.addActionListener(e -> dispose());

        bottom.add(saveBtn);
        bottom.add(cancelBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    /** 
     * Adds a new item to the order, or increases its quantity
     * Mirrors behaviour of console menu
     */
    private void addOrUpdateItem() {
        Part selectedPart = (Part) partCombo.getSelectedItem();
        if (selectedPart == null) return;

        int qty = (int) qtySpinner.getValue();
        if (qty <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be at least 1.");
            return;
        }

        for (int i = 0; i < itemsModel.size(); i++) {
            OrderItem existing = itemsModel.get(i);
            if (existing.getPart().getId() == selectedPart.getId()) {
                existing.setQuantity(existing.getQuantity() + qty);
                itemsList.repaint();
                return;
            }
        }

        itemsModel.addElement(new OrderItem(selectedPart, qty));
    }

    /**
     * Removes the selected item from the list
     */
    private void removeSelectedItem() {
        int idx = itemsList.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to remove.");
            return;
        }
        itemsModel.remove(idx);
    }

    /**
     * Validates inputs, constructs a new {@link order.Order} and adds it via {@link OrderMethods}.
     * Defensive parsing for the date field to prevent invalid date input
     */
    private void createOrder() {
        Customer customer = (Customer) customerCombo.getSelectedItem();
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Select a customer.");
            return;
        }

        LocalDate date;
        String dateText = dateField.getText().trim();
        if (dateText.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateText);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date. Use yyyy-MM-dd.");
                return;
            }
        }

        String status = statusField.getText().trim();
        if (status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an order status (e.g. Pending).");
            return;
        }

        if (itemsModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least 1 item to the order.");
            return;
        }

        ArrayList<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < itemsModel.size(); i++) {
            items.add(itemsModel.get(i));
        }

        Order order = new Order(0, customer, items, status, date);
        orderMethods.addOrder(order);

        JOptionPane.showMessageDialog(this, "Order created successfully.");
        dispose();
    }
}
