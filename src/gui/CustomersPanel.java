package gui;

import customer.*;
import storage.FileManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

/**
 * Student ID: 21360548
 * Panel for managing customers within the main GUI
 * Table view of customers with sorting
 * Live search + filtering using {@link TableRowSorter}
 */

public class CustomersPanel extends JPanel {

	/** Logic handler for in-memory list of customers */
    private final CustomerMethods customerMethods;

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;
    private JComboBox<String> typeFilter;

    private JButton editBtn;
    private JButton deleteBtn;

    /**
     * Creates the panel and immediately loads customers from memory into the table
     * @param customerMethods reference to customer manager / methods
     */
    public CustomersPanel(CustomerMethods customerMethods) {
        this.customerMethods = customerMethods;
        initUI();
        loadCustomers();
    }

    /**
     * Builds UI layout
     * top bar (title + filters), centre table, bottom bar (actions)
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Manage Customers");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        top.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchField = new JTextField(20);
        typeFilter = new JComboBox<>(new String[]{"All", "Individual", "Business", "Educational"});

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeFilter);

        top.add(filterPanel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Address", "Type"}, 0
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
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        ((DefaultTableCellRenderer) table.getTableHeader()
                .getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewCustomerDetails();
                }
            }
        });

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        };
        searchField.getDocument().addDocumentListener(dl);
        typeFilter.addActionListener(e -> applyFilters());

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JLabel hintLabel = new JLabel("Double-click for more info");
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setFont(hintLabel.getFont().deriveFont(Font.ITALIC, 12f));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        bottomPanel.add(hintLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

        JButton addBtn = new JButton("Add Customer");
        editBtn = new JButton("Edit Customer");
        deleteBtn = new JButton("Delete Customer");
        JButton saveBtn = new JButton("Save Customers");
        JButton refreshBtn = new JButton("Refresh");

        // start disabled until selection
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        // enable when row selected
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() != -1;
            editBtn.setEnabled(selected);
            deleteBtn.setEnabled(selected);
        });

        addBtn.addActionListener(e -> addCustomer());
        editBtn.addActionListener(e -> editCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        saveBtn.addActionListener(e -> saveCustomers());
        refreshBtn.addActionListener(e -> loadCustomers());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(refreshBtn);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Called by {@link MainGUI} when switching to customers panel
     * Ensures table reflects latest data and changes
     */
    public void refresh() {
        loadCustomers();
    }

    /**
     * Reloads the table from in-memory customers list
     * Reapplies filters so the view stays consistent
     */
    private void loadCustomers() {
        tableModel.setRowCount(0);

        ArrayList<Customer> customers = customerMethods.viewAllCustomers();
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getEmail(),
                    c.getAddress(),
                    c.getType()
            });
        }

        tableModel.fireTableDataChanged();
        table.repaint();

        applyFilters(); // keep filters active after reload
    }

    /**
     * @return the window that contains this panel (used as a parent for popups)
     */
    private JFrame getOwnerFrame() {
        Window w = SwingUtilities.getWindowAncestor(this);
        return (w instanceof JFrame) ? (JFrame) w : null;
    }

    /**
     * Reads selected customer ID from the table
     * Converts view row -> model row because sorting /filtering changes row order
     * @return selected customer ID, or null if nothing is selected / parse failed
     */
    private Integer getSelectedCustomerId() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) return null;

        int modelRow = table.convertRowIndexToModel(viewRow);
        Object val = tableModel.getValueAt(modelRow, 0);
        if (!(val instanceof Integer)) return null;
        return (Integer) val;
    }

    /** Opens the add-customer dialog and refreshes the table */
    private void addCustomer() {
        JFrame owner = getOwnerFrame();
        new AddCustomerGUI(owner, customerMethods);
        loadCustomers();
    }

    /** Opens the edit-customer dialog for the selected customer and refreshes */
    private void editCustomer() {
        Integer id = getSelectedCustomerId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a customer first.");
            return;
        }

        Customer c = customerMethods.viewCustomer(id);
        if (c == null) return;

        JFrame owner = getOwnerFrame();
        new EditCustomerGUI(owner, c);
        loadCustomers();
    }

    /**
     * Displays a read only popup containing all details for the selected customer
     * Including subtype specific details , i.e. Individual/Business/Educational
     * 
     */
    private void viewCustomerDetails() {
        Integer id = getSelectedCustomerId();
        if (id == null) return;

        Customer c = customerMethods.viewCustomer(id);
        if (c == null) return;

        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(c.getId()).append("\n");
        sb.append("Name: ").append(c.getName()).append("\n");
        sb.append("Email: ").append(c.getEmail()).append("\n");
        sb.append("Address: ").append(c.getAddress()).append("\n");
        sb.append("Type: ").append(c.getType()).append("\n\n");

        if (c instanceof IndividualCustomer ic) {
            sb.append("Preferred Contact: ").append(ic.getPreferredContact()).append("\n");
            sb.append("Loyalty Points: ").append(ic.getLoyaltyPoints()).append("\n");
            sb.append("Favourite Part: ").append(ic.getFavouritePart()).append("\n");
        } else if (c instanceof BusinessCustomer bc) {
            sb.append("Contact Name: ").append(bc.getContactName()).append("\n");
            sb.append("Business Type: ").append(bc.getBusinessType()).append("\n");
            sb.append("Tax ID: ").append(bc.getTaxId()).append("\n");
            sb.append("Discount Rate: ").append(bc.getDiscountRate()).append("\n");
        } else if (c instanceof EducationalCustomer ec) {
            sb.append("Contact Name: ").append(ec.getContactName()).append("\n");
            sb.append("Institute Type: ").append(ec.getInstituteType()).append("\n");
            sb.append("Department: ").append(ec.getDepartment()).append("\n");
            sb.append("Discount Rate: ").append(ec.getDiscountRate()).append("\n");
        }

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(ta),
                "Customer Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /** Deletes the selected customer from system after user confirmation */
    private void deleteCustomer() {
        Integer id = getSelectedCustomerId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a customer first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected customer?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = customerMethods.deleteCustomer(id);
            if (deleted) loadCustomers();
            else JOptionPane.showMessageDialog(this, "Customer could not be deleted.");
        }
    }

    /** Saves customers to file via {@link FileManager} after confirmation */
    private void saveCustomers() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Save customers to file?",
                "Confirm Save",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            FileManager.saveCustomers(customerMethods);
            JOptionPane.showMessageDialog(this, "Customers saved successfully.");
        }
    }

    /**
     * Applies search and type filtering via {@link RowFilter}
     * Search is case-insensitive and matches any visible column by default
     */
    private void applyFilters() {
        String text = searchField.getText().trim();
        String type = (String) typeFilter.getSelectedItem();

        RowFilter<DefaultTableModel, Object> searchRF = null;
        RowFilter<DefaultTableModel, Object> typeRF = null;

        if (!text.isEmpty()) {
            searchRF = RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text));
        }

        if (type != null && !"All".equals(type)) {
            // Type column index is 4: ID, Name, Email, Address, Type
            typeRF = RowFilter.regexFilter("(?i)^" + java.util.regex.Pattern.quote(type) + "$", 4);
        }

        if (searchRF != null && typeRF != null) {
            sorter.setRowFilter(RowFilter.andFilter(java.util.List.of(searchRF, typeRF)));
        } else if (searchRF != null) {
            sorter.setRowFilter(searchRF);
        } else if (typeRF != null) {
            sorter.setRowFilter(typeRF);
        } else {
            sorter.setRowFilter(null);
        }
    }
}
