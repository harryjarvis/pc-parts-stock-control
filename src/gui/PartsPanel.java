package gui;

import part.*;
import storage.FileManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import java.awt.*;
import java.util.ArrayList;

/**
 * Student ID: 21360548
 * Panel for managing parts within the main GUI
 * Table view of parts with sorting (including numeric sort for price)
 * Live search + type filtering using {@link TableRowSorter}
 */
public class PartsPanel extends JPanel {

	/** Logic handler that owns the in-memory list of parts */
    private final PartMethods partMethods;

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;
    private JComboBox<String> typeFilter;

    private JButton editBtn;
    private JButton deleteBtn;

    /**
     * Creates the panel and immediately loads parts from memory into the table
     * @param partMethods reference to the part manager
     */
    public PartsPanel(PartMethods partMethods) {
        this.partMethods = partMethods;
        partsPane();
        loadParts();
    }

    /**
     * Builds the UI layout
     * top bar (title + filters), centre table, bottom bar (actions)
     */
    private void partsPane() {
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Manage Parts");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        top.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchField = new JTextField(20);
        typeFilter = new JComboBox<>(new String[]{"All", "CPU", "GPU", "Motherboard", "Case"});

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeFilter);

        top.add(filterPanel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Manufacturer", "Price (£)", "Type"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
            	// Price should sort numerically, not alphabetically
            	return switch (columnIndex) {
            	case 0 -> Integer.class;
            	case 3 -> Double.class;
            	default -> String.class;
            	};
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);

        // Sort / filter performed by the row sorted
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Column widths for readability
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(170);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        
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
                    viewPartDetails();
                }
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JLabel hintLabel = new JLabel("Double-click for more info.");
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setFont(hintLabel.getFont().deriveFont(Font.ITALIC, 12f));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        bottomPanel.add(hintLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

        JButton addBtn = new JButton("Add Part");
        editBtn = new JButton("Edit Part");
        deleteBtn = new JButton("Delete Part");
        JButton saveBtn = new JButton("Save Parts");
        JButton refreshBtn = new JButton("Refresh");

        // Grey out until selected
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() != -1;
            editBtn.setEnabled(selected);
            deleteBtn.setEnabled(selected);
        });

        addBtn.addActionListener(e -> addPart());
        editBtn.addActionListener(e -> editPart());
        deleteBtn.addActionListener(e -> deletePart());
        saveBtn.addActionListener(e -> saveParts());
        refreshBtn.addActionListener(e -> loadParts());

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
        typeFilter.addActionListener(e -> applyFilters());
    }

    /**
     * Called by {@link MainGUI} when switching to this panel
     * Ensures table reflects latest data and changes
     */
    public void refresh() {
        loadParts();
    }

    /**
     * Reloads the table from in-memory parts list
     * Reapplies filters so the view stays consistent
     */
    private void loadParts() {
        tableModel.setRowCount(0);

        ArrayList<Part> parts = partMethods.viewAllParts();
        for (Part p : parts) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getManufacturer(),
                    p.getPrice(),
                    p.getType()
            });
        }

        tableModel.fireTableDataChanged();
        table.repaint();

        applyFilters();
    }

    /**
     * Applies both search and type filtering via {@link RowFilter}
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

    /**
     * @return the window that contains this panel (used as parent for dialogs/popups)
     */
    private Window getOwnerWindow() {
        return SwingUtilities.getWindowAncestor(this);
    }

    /**
     * Reads the selected part ID from the table
     * Converts view row -> model row because sorting/filtering changes row order
     * @return selected part ID, or null if nothing is selected / parse failed
     */
    private Integer getSelectedPartId() {
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

    /** Opens the add-part dialog and refreshes the table afterwards */
    private void addPart() {
        Window owner = getOwnerWindow();
        new AddPartGUI(owner, partMethods);
        loadParts();
    }

    /** Opens the edit-part dialog for the selected part and refreshes afterwards */
    private void editPart() {
        Integer id = getSelectedPartId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a part first.");
            return;
        }

        Part p = partMethods.viewPart(id);
        if (p == null) return;

        Window owner = getOwnerWindow();
        new EditPartGUI(owner, p);
        loadParts();
    }

    /**
     * Displays a read-only popup containing all details for the selected part
     * Including subtype specific details
     */
    private void viewPartDetails() {
        Integer id = getSelectedPartId();
        if (id == null) return;

        Part p = partMethods.viewPart(id);
        if (p == null) return;

        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(p.getId()).append("\n");
        sb.append("Name: ").append(p.getName()).append("\n");
        sb.append("Manufacturer: ").append(p.getManufacturer()).append("\n");
        sb.append("Price: £").        append(p.getPrice()).append("\n");
        sb.append("Type: ").append(p.getType()).append("\n\n");

        if (p instanceof CPU c) {
            sb.append("Socket: ").append(c.getSocket()).append("\n");
            sb.append("Cores: ").append(c.getCores()).append("\n");
            sb.append("Threads: ").append(c.getThreads()).append("\n");
            sb.append("Base Clock: ").append(c.getBaseClock()).append(" GHz\n");
            sb.append("Boost Clock: ").append(c.getBoostClock()).append(" GHz\n");
        } else if (p instanceof GPU g) {
            sb.append("Chipset: ").append(g.getChipset()).append("\n");
            sb.append("VRAM: ").append(g.getVram()).append("\n");
            sb.append("Base Clock: ").append(g.getBaseClock()).append(" GHz\n");
            sb.append("Boost Clock: ").append(g.getBoostClock()).append(" GHz\n");
            sb.append("Length: ").append(g.getLength()).append(" mm\n");
            sb.append("Colour: ").append(g.getColour()).append("\n");
        } else if (p instanceof Motherboard m) {
            sb.append("Socket: ").append(m.getSocket()).append("\n");
            sb.append("Form Factor: ").append(m.getFormFactor()).append("\n");
            sb.append("Chipset: ").append(m.getChipset()).append("\n");
            sb.append("Max Memory: ").append(m.getMaxMemory()).append(" GB\n");
            sb.append("WiFi: ").append(m.isWifi() ? "Yes" : "No").append("\n");
            sb.append("Colour: ").append(m.getColour()).append("\n");
        } else if (p instanceof Case ca) {
            sb.append("Size: ").append(ca.getCaseType()).append("\n");
            sb.append("Max GPU Length: ").append(ca.getMaxGpuLength()).append(" mm\n");
            sb.append("Colour: ").append(ca.getColour()).append("\n");
            sb.append("Dimensions: ")
              .append(ca.getLength()).append(" x ")
              .append(ca.getWidth()).append(" x ")
              .append(ca.getHeight()).append(" mm\n");
            sb.append("Volume: ").append(ca.getVolume()).append(" L\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(textArea),
                "Part Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /** Deletes the selected part after user confirmation, then refreshes the table */

    private void deletePart() {
        Integer id = getSelectedPartId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a part first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected part?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            partMethods.deletePart(id);
            loadParts();
        }
    }

    /** Saves parts to file via {@link FileManager} after confirmation */
    private void saveParts() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Save parts to file?",
                "Confirm Save",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            FileManager.saveParts(partMethods);
            JOptionPane.showMessageDialog(this, "Parts saved successfully.");
        }
    }
}
