package gui;

import order.*;
import part.PartMethods;
import customer.CustomerMethods;
import storage.FileManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.ArrayList;

/**
 * Student ID: 21360548
 * Panel for managing orders within the main GUI
 * The table view of orders with sorting
 * Live search filtering across visible columns
 * Status dropdown filtering
 * Add/edit/delete/save actions
 * Double click an order to view full details including items
 * This panel delegates data operations to {@link OrderMethods} and uses
 * {@link CustomerMethods}/{@link PartMethods} for the add/edit dialogs
 */
public class OrdersPanel extends JPanel {

    private final OrderMethods orderMethods;
    private final CustomerMethods customerMethods;
    private final PartMethods partMethods;

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;
    private JComboBox<String> statusFilter;

    private JButton editBtn;
    private JButton deleteBtn;

    /**
     * Creates the panel and immediately loads orders into the table
     * @param om order logic
     * @param cm customer logic
     * @param pm part logic
     */
    public OrdersPanel(OrderMethods om, CustomerMethods cm, PartMethods pm) {
        this.orderMethods = om;
        this.customerMethods = cm;
        this.partMethods = pm;

        orderGUI();
        loadOrders();
    }

    /**
     * Builds UI layout
     * top bar (title + filters), centre table, bottom bar (actions)
     */
    private void orderGUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Manage Orders");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        top.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchField = new JTextField(20);

        statusFilter = new JComboBox<>(new String[]{
                "All", "Pending", "Shipped", "Completed", "Cancelled"
        });

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);

        top.add(filterPanel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Order ID", "Customer", "Items", "Total (£)", "Status", "Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Variable Column widths for readability
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        // Centre cell alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        ((DefaultTableCellRenderer) table.getTableHeader()
                .getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Double-click to view details
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewOrderDetails();
                }
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JLabel hintLabel = new JLabel("Double-click for more info");
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setFont(hintLabel.getFont().deriveFont(Font.ITALIC, 12f));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        bottomPanel.add(hintLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

        JButton addBtn = new JButton("Add Order");
        editBtn = new JButton("Edit Order");
        deleteBtn = new JButton("Delete Order");
        JButton saveBtn = new JButton("Save Orders");
        JButton refreshBtn = new JButton("Refresh");

        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() != -1;
            editBtn.setEnabled(selected);
            deleteBtn.setEnabled(selected);
        });

        addBtn.addActionListener(e -> addOrder());
        editBtn.addActionListener(e -> editOrder());
        deleteBtn.addActionListener(e -> deleteOrder());
        saveBtn.addActionListener(e -> saveOrders());
        refreshBtn.addActionListener(e -> loadOrders());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(refreshBtn);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        };
        searchField.getDocument().addDocumentListener(dl);
        statusFilter.addActionListener(e -> applyFilters());
    }

    /**
     * Called by {@link MainGUI} when switching to this panel
     * Ensures table is up-to-date
     */
    public void refresh() {
        loadOrders();
    }

    /**
     * Reloads the table from in-memory orders list
     * Reapplies filters
     */
    private void loadOrders() {
        tableModel.setRowCount(0);

        ArrayList<Order> orders = orderMethods.viewAllOrders();
        for (Order o : orders) {
            String custName = (o.getCustomer() == null) ? "Unknown" : o.getCustomer().getName();
            int itemCount = (o.getItems() == null) ? 0 : o.getItems().size();

            tableModel.addRow(new Object[]{
                    o.getId(),
                    custName,
                    itemCount,
                    String.format("%.2f", o.getTotal()),
                    o.getStatus(),
                    String.valueOf(o.getDate())
            });
        }

        tableModel.fireTableDataChanged();
        table.repaint();

        applyFilters();
    }

    /**
     * Applies both search and status filtering via {@link RowFilter}
     * Search is case-insensitive and matches visible columns by default
     */
    private void applyFilters() {
        String text = searchField.getText().trim();
        String status = (String) statusFilter.getSelectedItem();

        RowFilter<DefaultTableModel, Object> searchRF = null;
        RowFilter<DefaultTableModel, Object> statusRF = null;

        if (!text.isEmpty()) {
        	// Prevents regex breaking characters in user input
            searchRF = RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text));
        }

        if (status != null && !"All".equals(status)) {
            // Status column index = 3 (Order ID, Customer, Items, Status, Date)
            statusRF = RowFilter.regexFilter("(?i)^" + java.util.regex.Pattern.quote(status) + "$", 3);
        }

        if (searchRF != null && statusRF != null) {
            sorter.setRowFilter(RowFilter.andFilter(java.util.List.of(searchRF, statusRF)));
        } else if (searchRF != null) {
            sorter.setRowFilter(searchRF);
        } else if (statusRF != null) {
            sorter.setRowFilter(statusRF);
        } else {
            sorter.setRowFilter(null);
        }
    }

    /**
     * @return the window that contains this panel (used as parent for popups)
     */
    private Window getOwnerWindow() {
        return SwingUtilities.getWindowAncestor(this);
    }

    /**
     * Reads the selected order ID from the table
     * Converts view row -> model row because sorting/filtering changes row order
     * @return selected order ID, or null if nothing is selected / parse failed
     */
    private Integer getSelectedOrderId() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) return null;

        int modelRow = table.convertRowIndexToModel(viewRow);
        Object val = tableModel.getValueAt(modelRow, 0);

        if (val instanceof Integer) return (Integer) val;
        try {
            return Integer.parseInt(String.valueOf(val));
        } catch (Exception e) {
            return null;
        }
    }

    /** Opens the add-order dialog, refreshing the table afterwards */
    private void addOrder() {
        Window owner = getOwnerWindow();
        new AddOrderGUI(owner, orderMethods, customerMethods, partMethods);
        loadOrders();
    }

    /** Opens the edit-order dialog for the selected order and refreshes after */
    private void editOrder() {
        Integer id = getSelectedOrderId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        Order o = orderMethods.viewOrder(id);
        if (o == null) return;

        Window owner = getOwnerWindow();
        new EditOrderGUI(owner, o, customerMethods, partMethods);
        loadOrders();
    }

    /** Deletes selected order after confirmation, refreshing afterwards */
    private void deleteOrder() {
        Integer id = getSelectedOrderId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected order?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            orderMethods.deleteOrder(id);
            loadOrders();
        }
    }

    /** Save orders to file via {@link FileManager} after confirmation */
    private void saveOrders() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Save orders to file?",
                "Confirm Save",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            FileManager.saveOrders(orderMethods);
            JOptionPane.showMessageDialog(this, "Orders saved successfully.");
        }
    }

    /** 
     * Displays a read only popup containing full order information,
     * including each order item
     */
    private void viewOrderDetails() {
        Integer id = getSelectedOrderId();
        if (id == null) return;

        Order o = orderMethods.viewOrder(id);
        if (o == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(o.getId()).append("\n");

        if (o.getCustomer() != null) {
            sb.append("Customer: ")
              .append(o.getCustomer().getName())
              .append(" (ID ")
              .append(o.getCustomer().getId())
              .append(")\n");
        } else {
            sb.append("Customer: Unknown\n");
        }

        sb.append("Status: ").append(o.getStatus()).append("\n");
        sb.append("Date: ").append(o.getDate()).append("\n\n");

        sb.append("Items:\n");

        if (o.getItems() == null || o.getItems().isEmpty()) {
            sb.append("  (No items)\n");
        } else {
            for (OrderItem item : o.getItems()) {
                sb.append("  • ")
                  .append(item.getPart().getName())
                  .append(" (Part ID ")
                  .append(item.getPart().getId())
                  .append(") x")
                  .append(item.getQuantity())
                  .append("  = £")
                  .append(String.format("%.2f", item.getLineTotal()))
                  .append("\n");
            }
        }

        sb.append("\nOrder Total: £")
          .append(String.format("%.2f", o.getTotal()))
          .append("\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(ta),
                "Order Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}
