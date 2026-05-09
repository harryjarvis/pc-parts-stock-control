package gui;

import part.Part;
import part.CPU;
import part.GPU;
import part.Motherboard;
import part.Case;
import java.awt.Window;
import javax.swing.*;
import java.awt.*;

/**
 * Student ID: 21360548
 * Dialog for editing an existing {@link part.Part}
 * UI is built dynamically based on the subtype of the part
 * When saved, fields are validated and then applied back onto the same object instance
 * Thus, lists and tables referencing it are automatically updated
 */

public class EditPartGUI extends JDialog {

    private final Part part;

    private JTextField nameField;
    private JTextField manufacturerField;
    private JTextField priceField;

    private JTextField cpuSocketField;
    private JTextField cpuCoresField;
    private JTextField cpuThreadsField;
    private JTextField cpuBaseClockField;
    private JTextField cpuBoostClockField;

    private JTextField gpuChipsetField;
    private JTextField gpuVramField;
    private JTextField gpuBaseClockField;
    private JTextField gpuBoostClockField;
    private JTextField gpuLengthField;
    private JTextField gpuColourField;

    private JTextField mbSocketField;
    private JTextField mbFormFactorField;
    private JTextField mbChipsetField;
    private JTextField mbMaxMemoryField;
    private JCheckBox mbWifiBox;
    private JTextField mbColourField;

    private JTextField caseTypeField;
    private JTextField caseMaxGpuLengthField;
    private JTextField caseColourField;
    private JTextField caseLengthField;
    private JTextField caseWidthField;
    private JTextField caseHeightField;
    private JTextField caseVolumeField;

    private JPanel formPanel;
    
    /** 
     * Creates and shows the edit part dialog
     * @param parent parent window used for centering and modal behaviour
     * @param part the part object to edit (changes are applied to this instance)
     */
    public EditPartGUI(Window parent, Part part) {
        super(parent, "Edit Part", ModalityType.APPLICATION_MODAL);
        this.part = part;

        editPartGUI();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Builds the main layout
     * title bar
     * scrollable form content
     * save/cancel buttons
     */
    private void editPartGUI() {
        setSize(450, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Editing: " + part.getName() + " (" + part.getType() + ")");
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(title, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(0, 2, 6, 6));
        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);

        buildForm(); // builds fields based on the part type

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
     * Adds the common part fields and then subtype-specific fields based on the runtime class of {@link #part}. This avoids multiple dialogs or tabs
     */
    private void buildForm() {
        nameField = new JTextField(part.getName(), 15);
        manufacturerField = new JTextField(part.getManufacturer(), 15);
        priceField = new JTextField(String.valueOf(part.getPrice()), 10);

        addRow("Name:", nameField);
        addRow("Manufacturer:", manufacturerField);
        addRow("Price (£):", priceField);

        if (part instanceof CPU c) {
            cpuSocketField = new JTextField(c.getSocket(), 10);
            cpuCoresField = new JTextField(String.valueOf(c.getCores()), 5);
            cpuThreadsField = new JTextField(String.valueOf(c.getThreads()), 5);
            cpuBaseClockField = new JTextField(String.valueOf(c.getBaseClock()), 5);
            cpuBoostClockField = new JTextField(String.valueOf(c.getBoostClock()), 5);

            addRow("Socket:", cpuSocketField);
            addRow("Cores:", cpuCoresField);
            addRow("Threads:", cpuThreadsField);
            addRow("Base Clock (GHz):", cpuBaseClockField);
            addRow("Boost Clock (GHz):", cpuBoostClockField);
        }
        else if (part instanceof GPU g) {
            gpuChipsetField = new JTextField(g.getChipset(), 15);
            gpuVramField = new JTextField(String.valueOf(g.getVram()), 5);
            gpuBaseClockField = new JTextField(String.valueOf(g.getBaseClock()), 5);
            gpuBoostClockField = new JTextField(String.valueOf(g.getBoostClock()), 5);
            gpuLengthField = new JTextField(String.valueOf(g.getLength()), 6);
            gpuColourField = new JTextField(g.getColour(), 10);

            addRow("Chipset:", gpuChipsetField);
            addRow("VRAM:", gpuVramField);
            addRow("Base Clock (GHz):", gpuBaseClockField);
            addRow("Boost Clock (GHz):", gpuBoostClockField);
            addRow("Length (mm):", gpuLengthField);
            addRow("Colour:", gpuColourField);
        }
        else if (part instanceof Motherboard m) {
            mbSocketField = new JTextField(m.getSocket(), 10);
            mbFormFactorField = new JTextField(m.getFormFactor(), 10);
            mbChipsetField = new JTextField(m.getChipset(), 10);
            mbMaxMemoryField = new JTextField(String.valueOf(m.getMaxMemory()), 6);
            mbWifiBox = new JCheckBox();
            mbWifiBox.setSelected(m.isWifi());
            mbColourField = new JTextField(m.getColour(), 10);

            addRow("Socket:", mbSocketField);
            addRow("Form Factor:", mbFormFactorField);
            addRow("Chipset:", mbChipsetField);
            addRow("Max Memory (GB):", mbMaxMemoryField);
            addRow("WiFi:", mbWifiBox);
            addRow("Colour:", mbColourField);
        }
        else if (part instanceof Case ca) {
            caseTypeField = new JTextField(ca.getCaseType(), 12);
            caseMaxGpuLengthField = new JTextField(String.valueOf(ca.getMaxGpuLength()), 6);
            caseColourField = new JTextField(ca.getColour(), 10);
            caseLengthField = new JTextField(String.valueOf(ca.getLength()), 6);
            caseWidthField = new JTextField(String.valueOf(ca.getWidth()), 6);
            caseHeightField = new JTextField(String.valueOf(ca.getHeight()), 6);
            caseVolumeField = new JTextField(String.valueOf(ca.getVolume()), 6);

            addRow("Case Type:", caseTypeField);
            addRow("Max GPU Length (mm):", caseMaxGpuLengthField);
            addRow("Colour:", caseColourField);
            addRow("Length (mm):", caseLengthField);
            addRow("Width (mm):", caseWidthField);
            addRow("Height (mm):", caseHeightField);
            addRow("Volume (L):", caseVolumeField);
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
     * Validates user input back onto the existing part instance
     * Updates the same object reference which is useful because existing lists/tables still refer to the same part instance
     */
    private void saveChanges() {
        try {
            String newName = nameField.getText().trim();
            String newMan = manufacturerField.getText().trim();

            if (newName.isEmpty() || newMan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and manufacturer cannot be empty.");
                return;
            }

            double newPrice = Double.parseDouble(priceField.getText().trim());

            part.setName(newName);
            part.setManufacturer(newMan);
            part.setPrice(newPrice);

            if (part instanceof CPU c) {
                c.setSocket(cpuSocketField.getText().trim());
                c.setCores(Integer.parseInt(cpuCoresField.getText().trim()));
                c.setThreads(Integer.parseInt(cpuThreadsField.getText().trim()));
                c.setBaseClock(Double.parseDouble(cpuBaseClockField.getText().trim()));
                c.setBoostClock(Double.parseDouble(cpuBoostClockField.getText().trim()));
            }
            else if (part instanceof GPU g) {
                g.setChipset(gpuChipsetField.getText().trim());
                g.setVram(gpuVramField.getText().trim());
                g.setBaseClock(Double.parseDouble(gpuBaseClockField.getText().trim()));
                g.setBoostClock(Double.parseDouble(gpuBoostClockField.getText().trim()));
                g.setLength(Integer.parseInt(gpuLengthField.getText().trim()));
                g.setColour(gpuColourField.getText().trim());
            }
            else if (part instanceof Motherboard m) {
                m.setSocket(mbSocketField.getText().trim());
                m.setFormFactor(mbFormFactorField.getText().trim());
                m.setChipset(mbChipsetField.getText().trim());
                m.setMaxMemory(Integer.parseInt(mbMaxMemoryField.getText().trim()));
                m.setWifi(mbWifiBox.isSelected());
                m.setColour(mbColourField.getText().trim());
            }
            else if (part instanceof Case ca) {
                ca.setCaseType(caseTypeField.getText().trim());
                ca.setMaxGpuLength(Integer.parseInt(caseMaxGpuLengthField.getText().trim()));
                ca.setColour(caseColourField.getText().trim());
                ca.setLength(Double.parseDouble(caseLengthField.getText().trim()));
                ca.setWidth(Double.parseDouble(caseWidthField.getText().trim()));
                ca.setHeight(Double.parseDouble(caseHeightField.getText().trim()));
                ca.setVolume(Double.parseDouble(caseVolumeField.getText().trim()));
            }

            dispose();

        } catch (NumberFormatException ex) { // prevents the GUI from crashing if the user types invalid numbers
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
