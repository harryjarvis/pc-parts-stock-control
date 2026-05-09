package gui;

import javax.swing.*;
import java.awt.*;
import part.PartMethods;
import customer.CustomerMethods;
import order.OrderMethods;

/**
 * Main window for GUI mode
 * Left navigation panel with "Manage" buttons
 * Right content area using a {@link CardLayout} to switch between panels
 * Panels are refreshed on navigation so the tables always reflect latest memory data
 */

public class MainGUI extends JFrame {

	/** Logic handler for parts */
    private final PartMethods partMethods;
    
    /** Logic handler for customers */
    private final CustomerMethods customerMethods;
    
    /** Logic handler for orders */
    private final OrderMethods orderMethods;

    /** Manages switching between content panels */
    private CardLayout cardLayout;
    
    /** Container holding the different panels */
    private JPanel cards;

    /**
     * Constructs and displays the main GUI windows
     * @param pm Part logic handler
     * @param cm Customer logic handler
     * @param om Order logic handler
     */
    public MainGUI(PartMethods pm, CustomerMethods cm, OrderMethods om) {
        this.partMethods = pm;
        this.customerMethods = cm;
        this.orderMethods = om;

        mainCard();
    }

    /**
     * Builds full UI
     * left nav and right card-based content panels
     */
    private void mainCard() {
        setTitle("Harry's PC Parts");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel nav = new JPanel();
        nav.setLayout(new GridLayout(0, 1, 10, 10));
        nav.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        nav.setPreferredSize(new Dimension(220, 0));

        JButton partsBtn = new JButton("Manage Parts");
        JButton customersBtn = new JButton("Manage Customers");
        JButton ordersBtn = new JButton("Manage Orders");
        JButton exitBtn = new JButton("Exit");

        nav.add(partsBtn);
        nav.add(customersBtn);
        nav.add(ordersBtn);
        nav.add(exitBtn);

        add(nav, BorderLayout.WEST);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        PartsPanel partsPanel = new PartsPanel(partMethods);
        CustomersPanel customersPanel = new CustomersPanel(customerMethods);
        OrdersPanel ordersPanel = new OrdersPanel(orderMethods, customerMethods, partMethods);

        cards.add(partsPanel, "PARTS");
        cards.add(customersPanel, "CUSTOMERS");
        cards.add(ordersPanel, "ORDERS");

        add(cards, BorderLayout.CENTER);

        // Switch actions
        partsBtn.addActionListener(e -> {
            partsPanel.refresh();
            cardLayout.show(cards, "PARTS");
        });

        customersBtn.addActionListener(e -> {
            customersPanel.refresh();
            cardLayout.show(cards, "CUSTOMERS");
        });

        ordersBtn.addActionListener(e -> {
            ordersPanel.refresh();
            cardLayout.show(cards, "ORDERS");
        });

        exitBtn.addActionListener(e -> dispose());

        // Default screen
        cardLayout.show(cards, "PARTS");

        setVisible(true);
    }
}
