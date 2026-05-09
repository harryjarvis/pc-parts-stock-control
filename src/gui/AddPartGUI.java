package gui;

import part.*;
import javax.swing.*;
import java.awt.*;

/** 
 * Student ID: 21360548
 * Dialog for creating a new {@link part.Part}.
 * Supports multiple types, cpu, video card, motherboard and case
 * dynamically updates based on selected type
 */

public class AddPartGUI extends JDialog {

	/** Reference to logic handler for parts */
    private PartMethods partMethods;

    private JComboBox<String> typeCombo;
    private JPanel formPanel;
    
    /** 
     * Creates and shows the add part dialog
     * @param parent parent window used for centering and model behaviour
     * @param partMethods reference to part logic
     */

    public AddPartGUI(Window parent, PartMethods partMethods) {
        super(parent, "Add Part", ModalityType.APPLICATION_MODAL); // modal dialog
        this.partMethods = partMethods;

        addPartGUI();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    /**
     * Builds dialog layout
     * top: part type selector
     * centre: dynamic form panel
     * bottom: save/cancel buttons
     */
    
    private void addPartGUI() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Part Type:"));

        typeCombo = new JComboBox<>(new String[]{
                "CPU", "GPU", "Motherboard", "Case"
        });

        typeCombo.addActionListener(e -> updateForm());

        topPanel.add(typeCombo);
        add(topPanel, BorderLayout.NORTH);

        formPanel = new JPanel();
        add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> savePart());
        cancelBtn.addActionListener(e -> dispose());

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        updateForm(); // load initial form
    }
    
    /**
     * Rebuilds form panel based on selected type
     * This keeps one dialog while still supporting multiple subclasses
     */
    private void updateForm() {
        formPanel.removeAll();

        String type = (String) typeCombo.getSelectedItem();

        switch (type) {
            case "CPU" -> buildCPUForm();
            case "GPU" -> buildGPUForm();
            case "Motherboard" -> buildMotherboardForm();
            case "Case" -> buildCaseForm();
        }

        formPanel.revalidate();
        formPanel.repaint();
    }

    // Super class fields
    private JTextField nameField = new JTextField(15);
    private JTextField manufacturerField = new JTextField(15);
    private JTextField priceField = new JTextField(10);

    // CPU fields
    private JTextField socketField = new JTextField(10);
    private JTextField coresField = new JTextField(5);
    private JTextField threadsField = new JTextField(5);
    private JTextField baseClockField = new JTextField(5);
    private JTextField boostClockField = new JTextField(5);
    
    // GPU fields
    private JTextField chipsetField = new JTextField(10);
    private JTextField vramField = new JTextField(5);
    private JTextField gpuBaseClockField = new JTextField(5);
    private JTextField gpuBoostClockField = new JTextField(5);
    private JTextField lengthField = new JTextField(5);
    private JTextField colourField = new JTextField(10);

    // Motherboard fields
    private JTextField mbSocketField = new JTextField(10);
    private JTextField formFactorField = new JTextField(10);
    private JTextField mbChipsetField = new JTextField(10);
    private JTextField maxMemoryField = new JTextField(5);
    private JCheckBox wifiCheckBox = new JCheckBox();

    // Case fields
    private JTextField caseTypeField = new JTextField(10);
    private JTextField maxGpuLengthField = new JTextField(5);
    private JTextField caseColourField = new JTextField(10);
    private JTextField lengthMmField = new JTextField(5);
    private JTextField widthMmField = new JTextField(5);
    private JTextField heightMmField = new JTextField(5);
    private JTextField volumeLField = new JTextField(5);


    private void buildCPUForm() {
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));

        addRow("Name:", nameField);
        addRow("Manufacturer:", manufacturerField);
        addRow("Price (£):", priceField);
        addRow("Socket:", socketField);
        addRow("Cores:", coresField);
        addRow("Threads:", threadsField);
        addRow("Base Clock (GHz):", baseClockField);
        addRow("Boost Clock (GHz):", boostClockField);
    }
    
    private void buildGPUForm() {
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));

        addRow("Name:", nameField);
        addRow("Manufacturer:", manufacturerField);
        addRow("Price (£):", priceField);

        addRow("Chipset:", chipsetField);
        addRow("VRAM (GB):", vramField);
        addRow("Base Clock (GHz):", gpuBaseClockField);
        addRow("Boost Clock (GHz):", gpuBoostClockField);
        addRow("Length (mm):", lengthField);
        addRow("Colour:", colourField);
    }

    private void buildMotherboardForm() {
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));

        addRow("Name:", nameField);
        addRow("Manufacturer:", manufacturerField);
        addRow("Price (£):", priceField);

        addRow("Socket:", mbSocketField);
        addRow("Form Factor:", formFactorField);
        addRow("Chipset:", mbChipsetField);
        addRow("Max Memory (GB):", maxMemoryField);
        addRow("WiFi:", wifiCheckBox);
        addRow("Colour:", colourField);
    }

    private void buildCaseForm() {
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));

        addRow("Name:", nameField);
        addRow("Manufacturer:", manufacturerField);
        addRow("Price (£):", priceField);

        addRow("Case Type:", caseTypeField);
        addRow("Max GPU Length (mm):", maxGpuLengthField);
        addRow("Colour:", caseColourField);
        addRow("Length (mm):", lengthMmField);
        addRow("Width (mm):", widthMmField);
        addRow("Height (mm):", heightMmField);
        addRow("Volume (L):", volumeLField);
    }

    /**
     * Helper for adding a label + input component pair to the form
     */
    private void addRow(String label, JComponent field) {
        formPanel.add(new JLabel(label));
        formPanel.add(field);
    }

    /**
     * Validates inputs, builds the appropriate subclass instance based on selected type
     * delegates storage to {@link PartMethods#addPart(Part)}.
     * Invalid numbers are caught and shown to the user instead of crashing
     */
    private void savePart() {

        try {
            String name = nameField.getText().trim();
            String manufacturer = manufacturerField.getText().trim();
            double price = Double.parseDouble(priceField.getText());

            if (name.isEmpty() || manufacturer.isEmpty()) {
                throw new IllegalArgumentException("Name and manufacturer required");
            }

            String type = (String) typeCombo.getSelectedItem();

            switch (type) {

                case "CPU" -> {
                    CPU cpu = new CPU(
                            0,
                            name,
                            manufacturer,
                            price,
                            socketField.getText(),
                            Integer.parseInt(coresField.getText()),
                            Integer.parseInt(threadsField.getText()),
                            Double.parseDouble(baseClockField.getText()),
                            Double.parseDouble(boostClockField.getText())
                    );
                    partMethods.addPart(cpu);
                }

                case "GPU" -> {
                    GPU gpu = new GPU(
                            0,
                            name,
                            manufacturer,
                            price,
                            chipsetField.getText(),
                            vramField.getText(),
                            Double.parseDouble(gpuBaseClockField.getText()),
                            Double.parseDouble(gpuBoostClockField.getText()),
                            Integer.parseInt(lengthField.getText()),
                            colourField.getText()
                    );
                    partMethods.addPart(gpu);
                }

                case "Motherboard" -> {
                    Motherboard mb = new Motherboard(
                            0,
                            name,
                            manufacturer,
                            price,
                            mbSocketField.getText(),
                            formFactorField.getText(),
                            mbChipsetField.getText(),
                            Integer.parseInt(maxMemoryField.getText()),
                            wifiCheckBox.isSelected(),
                            colourField.getText()
                    );
                    partMethods.addPart(mb);
                }

                case "Case" -> {
                    Case pcCase = new Case(
                            0,
                            name,
                            manufacturer,
                            price,
                            caseTypeField.getText(),
                            Integer.parseInt(maxGpuLengthField.getText()),
                            caseColourField.getText(),
                            Double.parseDouble(lengthMmField.getText()),
                            Double.parseDouble(widthMmField.getText()),
                            Double.parseDouble(heightMmField.getText()),
                            Double.parseDouble(volumeLField.getText())
                    );
                    partMethods.addPart(pcCase);
                }
            }

            dispose(); // close dialog if successful

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


}
