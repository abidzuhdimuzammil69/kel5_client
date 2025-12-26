package view;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import view.tablemodel.ContactTableModel;

public class ContactFrame extends JFrame {
    private final JTextField searchField = new JTextField(30);
    private final JButton addButton = new JButton("Add New Contact");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton deleteButton = new JButton("Delete");
    private final JLabel totalRecordsLabel = new JLabel("0 Contacts");

    private final JTable contactTable = new JTable();
    private final ContactTableModel contactTableModel = new ContactTableModel();
    private final JProgressBar progressBar = new JProgressBar();

    public ContactFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Contact Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[]10[]10[grow]10[]10[]"));

        contactTable.setModel(contactTableModel);
        contactTable.setAutoCreateRowSorter(true);
        contactTable.setFillsViewportHeight(true);
        progressBar.setStringPainted(true);

        // Set column widths
        contactTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        contactTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        contactTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Phone
        contactTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Email
        contactTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Created
        contactTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Updated

        add(new JLabel("Contact List"), "wrap, span 2");
        add(createSearchPanel(), "growx, w 80%");
        add(createButtonPanel(), "wrap, right, w 20%");
        add(new JScrollPane(contactTable), "grow, wrap, span 2");
        add(progressBar, "growx, h 20!, wrap, span 2");
        add(totalRecordsLabel, "right, span 2");

        pack();
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new MigLayout(""));
        panel.add(new JLabel("Search:"));
        panel.add(searchField, "growx");
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new MigLayout("right"));
        
        // Style buttons
        Font boldFont = new Font("Segoe UI", Font.BOLD, 12);
        addButton.setFont(boldFont);
        refreshButton.setFont(boldFont);
        deleteButton.setFont(boldFont);
        
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(addButton);
        
        return panel;
    }

    // Getters
    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JTable getContactTable() {
        return contactTable;
    }

    public ContactTableModel getContactTableModel() {
        return contactTableModel;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getTotalRecordsLabel() {
        return totalRecordsLabel;
    }
}