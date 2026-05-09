package gui;

import customer.Customer;
import customer.CustomerMethods;
import order.Order;
import order.OrderItem;
import part.Part;
import part.PartMethods;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Student ID: 21360548
 * Modal dialog for editing an existing {@link order.Order}.
 * Dialog mirrors console functionality:
 * Edit core order details (customer, date, status)
 * Add items or increase quantities
 * Remove items or reduce quantities
 * Changes are applied directly to the existing {@link Order} instance.
 */
public class EditOrderGUI extends JDialog {

    /** The order being edited. */
    private final Order order;

    /** Access to customers for combo population. */
    private final CustomerMethods customerMethods;

    /** Access to parts for combo population. */
    private final PartMethods partMethods;

    private JComboBox<Customer> customerCombo;
    private JTextField dateField;
    private JTextField statusField;

    private JComboBox<Part> addPartCombo;
    private JSpinner addQtySpinner;

    private JSpinner removeQtySpinner;

    // Shared data model across all tabs (lists read/write this)
    private DefaultListModel<OrderItem> itemsModel;

    // keep a reference to the remove tab list so remove actions know what’s selected
    private JList<OrderItem> removeTabList;

    /**
     * Creates and shows the edit order dialog.
     * @param parent parent window used for centering and modal behaviour
     * @param order the order instance to edit
     * @param customerMethods access to customer list / lookup
     * @param partMethods access to part list / lookup
     */
    public EditOrderGUI(
            Window parent,
            Order order,
            CustomerMethods customerMethods,
            PartMethods partMethods
    ) {
        super(parent, "Edit Order (ID: " + order.getId() + ")", ModalityType.APPLICATION_MODAL);

        this.order = order;
        this.customerMethods = customerMethods;
        this.partMethods = partMethods;

        editOrderGUI();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void editOrderGUI() {
        setSize(800, 560);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Copy existing order items into a ListModel so UI edits are reflected live
        itemsModel = new DefaultListModel<>();
        if (order.getItems() != null) {
            for (OrderItem oi : order.getItems()) {
                itemsModel.addElement(oi);
            }
        }

        // each tab gets its own JList instance, but all share the same model.
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Order Details", buildDetailsTab());
        tabs.addTab("Add / Update Items", buildAddItemsTab());
        tabs.addTab("Remove Items", buildRemoveItemsTab());

        add(tabs, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> saveChanges());
        cancelBtn.addActionListener(e -> dispose());

        bottom.add(saveBtn);
        bottom.add(cancelBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Tab 1: allows editing of customer, date and status
     * while also displaying the current item list.
     */
    private JPanel buildDetailsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Edit Details"));

        customerCombo = new JComboBox<>(customerMethods.viewAllCustomers().toArray(new Customer[0]));
        customerCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer c) setText("ID " + c.getId() + " - " + c.getName());
                return this;
            }
        });

        // Pre-select current customer (if present)
        if (order.getCustomer() != null) {
            for (int i = 0; i < customerCombo.getItemCount(); i++) {
                Customer c = customerCombo.getItemAt(i);
                if (c.getId() == order.getCustomer().getId()) {
                    customerCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        dateField = new JTextField(order.getDate() == null ? "" : order.getDate().toString(), 10);
        statusField = new JTextField(order.getStatus() == null ? "" : order.getStatus(), 10);

        form.add(new JLabel("Customer:"));
        form.add(customerCombo);

        form.add(new JLabel("Date (yyyy-MM-dd):"));
        form.add(dateField);

        form.add(new JLabel("Status:"));
        form.add(statusField);

        panel.add(form, BorderLayout.NORTH);

        // New list instance for this tab
        JList<OrderItem> detailsList = createItemsList();
        JScrollPane sp = new JScrollPane(detailsList);
        sp.setBorder(BorderFactory.createTitledBorder("Current Items"));
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Tab 2 provides an interface to add a new item or increase quantities of existing items.
     */
    private JPanel buildAddItemsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Add / Update"));

        addPartCombo = new JComboBox<>(partMethods.viewAllParts().toArray(new Part[0]));
        addPartCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Part p) setText("ID " + p.getId() + " - " + p.getName());
                return this;
            }
        });

        addQtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        JButton addBtn = new JButton("Add / Increase Quantity");
        addBtn.addActionListener(e -> addOrIncreaseItem());

        form.add(new JLabel("Part:"));
        form.add(addPartCombo);

        form.add(new JLabel("Quantity to add:"));
        form.add(addQtySpinner);

        form.add(addBtn);
        form.add(new JLabel("")); // spacer

        panel.add(form, BorderLayout.NORTH);

        // New list instance for this tab
        JList<OrderItem> addTabList = createItemsList();
        JScrollPane sp = new JScrollPane(addTabList);
        sp.setBorder(BorderFactory.createTitledBorder("Current Items"));
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Adds a new item or increases quantity if the part already exists in the order.
     */
    private void addOrIncreaseItem() {
        Part part = (Part) addPartCombo.getSelectedItem();
        if (part == null) return;

        int qty = (int) addQtySpinner.getValue();
        if (qty <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be at least 1.");
            return;
        }

        for (int i = 0; i < itemsModel.size(); i++) {
            OrderItem existing = itemsModel.get(i);
            if (existing.getPart().getId() == part.getId()) {
                existing.setQuantity(existing.getQuantity() + qty);

                // Force list refresh (model still holds same object reference)
                itemsModel.set(i, existing);
                return;
            }
        }

        itemsModel.addElement(new OrderItem(part, qty));
    }

    /**
     * Tab 3 allows user to reduce quantity or remove an item completely.
     */
    private JPanel buildRemoveItemsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // New list instance for this tab, keep reference for selection
        removeTabList = createItemsList();
        JScrollPane sp = new JScrollPane(removeTabList);
        sp.setBorder(BorderFactory.createTitledBorder("Select an item to remove/reduce"));
        panel.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(0, 2, 8, 8));
        bottom.setBorder(BorderFactory.createTitledBorder("Remove Options"));

        removeQtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        JButton reduceBtn = new JButton("Remove Quantity");
        JButton removeAllBtn = new JButton("Remove Entire Item");

        reduceBtn.addActionListener(e -> removeQuantity());
        removeAllBtn.addActionListener(e -> removeEntireItem());

        bottom.add(new JLabel("Quantity to remove:"));
        bottom.add(removeQtySpinner);

        bottom.add(reduceBtn);
        bottom.add(removeAllBtn);

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Reduces selected item quantity. If it reaches zero, removes the item.
     * Includes defensive checks for invalid remove amounts.
     */
    private void removeQuantity() {
        int idx = removeTabList.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Select an item first.");
            return;
        }

        OrderItem item = itemsModel.get(idx);
        int qtyToRemove = (int) removeQtySpinner.getValue();

        if (qtyToRemove <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be at least 1.");
            return;
        }

        if (qtyToRemove > item.getQuantity()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cannot remove more than current quantity (" + item.getQuantity() + ")."
            );
            return;
        }

        if (qtyToRemove == item.getQuantity()) {
            itemsModel.remove(idx);
        } else {
            item.setQuantity(item.getQuantity() - qtyToRemove);
            itemsModel.set(idx, item);
        }
    }

    /**
     * Removes the selected item from the order entirely.
     */
    private void removeEntireItem() {
        int idx = removeTabList.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Select an item first.");
            return;
        }
        itemsModel.remove(idx);
    }

    /**
     * Validates input and commits UI changes back to the existing {@link Order}.
     */
    private void saveChanges() {
        if (itemsModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "An order must contain at least 1 item.");
            return;
        }

        Customer selectedCustomer = (Customer) customerCombo.getSelectedItem();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Select a customer.");
            return;
        }

        String status = statusField.getText().trim();
        if (status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Status cannot be empty.");
            return;
        }

        LocalDate date;
        String dateText = dateField.getText().trim();
        if (dateText.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateText);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date. Use yyyy-MM-dd.");
                return;
            }
        }

        order.setCustomer(selectedCustomer);
        order.setStatus(status);
        order.setDate(date);

        // Replace items list with edited model contents
        order.getItems().clear();
        for (int i = 0; i < itemsModel.size(); i++) {
            order.getItems().add(itemsModel.get(i));
        }

        JOptionPane.showMessageDialog(this, "Order updated successfully.");
        dispose();
    }

    /**
     * Creates a new {@link JList} bound to the shared {@link #itemsModel}.
     * Each tab must use its own JList instance (Swing components can only be in one container).
     */
    private JList<OrderItem> createItemsList() {
        JList<OrderItem> list = new JList<>(itemsModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(10);

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof OrderItem oi) {
                    setText("Part ID " + oi.getPart().getId() + " - " + oi.getPart().getName()
                            + "   x" + oi.getQuantity());
                }
                return this;
            }
        });

        return list;
    }
}
