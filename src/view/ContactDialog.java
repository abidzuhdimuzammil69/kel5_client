package view;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import model.Contact;

public class ContactDialog extends JDialog {
    private final JTextField nameField = new JTextField(25);
    private final JTextField phoneField = new JTextField(25);
    private final JTextField emailField = new JTextField(25);
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");

    private Contact contact;

    public ContactDialog(JFrame owner) {
        super(owner, "Add New Contact", true);
        this.contact = new Contact();
        setupComponents();
    }

    public ContactDialog(JFrame owner, Contact contactToEdit) {
        super(owner, "Edit Contact", true);
        this.contact = contactToEdit;
        setupComponents();
        
        nameField.setText(contactToEdit.getName());
        phoneField.setText(contactToEdit.getPhone());
        emailField.setText(contactToEdit.getEmail());
    }

    private void setupComponents() {
        setLayout(new MigLayout("fill, insets 30", "[right]20[grow]", "[][][][]"));
        
        // Title
        add(new JLabel("Contact Information"), "span 2, wrap 15");
        
        // Form fields
        add(new JLabel("Full Name *:"), "");
        add(nameField, "growx, wrap");
        
        add(new JLabel("Phone Number *:"), "");
        add(phoneField, "growx, wrap");
        
        add(new JLabel("Email:"), "");
        add(emailField, "growx, wrap 15");
        
        // Buttons
        JPanel buttonPanel = new JPanel(new MigLayout("", "[]10[]"));
        cancelButton.addActionListener(e -> dispose());
        
        Font boldFont = new Font("Segoe UI", Font.BOLD, 12);
        saveButton.setFont(boldFont);
        saveButton.setBackground(new java.awt.Color(70, 130, 180));
        saveButton.setForeground(java.awt.Color.WHITE);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, "span 2, right");
        
        pack();
        setMinimumSize(new Dimension(500, 250));
        setLocationRelativeTo(getOwner());
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public Contact getContact() {
        contact.setName(nameField.getText().trim());
        contact.setPhone(phoneField.getText().trim());
        contact.setEmail(emailField.getText().trim());
        return contact;
    }

    public boolean validateInput() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required", "Validation Error", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number is required", "Validation Error", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return false;
        }
        
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format", "Validation Error", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        
        return true;
    }
}