package controller;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import api.ContactApiClient;
import model.Contact;
import view.ContactDialog;
import view.ContactFrame;
import worker.contact.*;

public class ContactController {
    private final ContactFrame frame;
    private final ContactApiClient contactApiClient = new ContactApiClient();
    private List<Contact> allContacts = new ArrayList<>();
    private List<Contact> displayedContacts = new ArrayList<>();
    private Timer searchTimer;

    public ContactController(ContactFrame frame) {
        this.frame = frame;
        setupEventListeners();
        loadAllContacts();
    }

    private void setupEventListeners() {
        // Add button
        frame.getAddButton().addActionListener(e -> openContactDialog(null));
        
        // Refresh button
        frame.getRefreshButton().addActionListener(e -> loadAllContacts());
        
        // Delete button
        frame.getDeleteButton().addActionListener(e -> deleteSelectedContact());
        
        // Double-click to edit
        frame.getContactTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = frame.getContactTable().getSelectedRow();
                    if (selectedRow >= 0) {
                        int modelRow = frame.getContactTable().convertRowIndexToModel(selectedRow);
                        openContactDialog(displayedContacts.get(modelRow));
                    }
                }
            }
        });
        
        // Search with debouncing
        searchTimer = new Timer(300, e -> applySearchFilter());
        searchTimer.setRepeats(false);
        
        frame.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTimer.restart();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTimer.restart();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTimer.restart();
            }
        });
    }

    private void openContactDialog(Contact contactToEdit) {
        ContactDialog dialog;
        if (contactToEdit == null) {
            dialog = new ContactDialog(frame);
        } else {
            dialog = new ContactDialog(frame, contactToEdit);
        }
        
        dialog.getSaveButton().addActionListener(e -> {
            if (!dialog.validateInput()) {
                return;
            }
            
            Contact contact = dialog.getContact();
            SwingWorker<Void, Void> worker;
            
            if (contactToEdit == null) {
                worker = new SaveContactWorker(frame, contactApiClient, contact);
            } else {
                worker = new UpdateContactWorker(frame, contactApiClient, contact);
            }
            
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    dialog.dispose();
                    loadAllContacts();
                }
            });
            worker.execute();
        });
        
        dialog.setVisible(true);
    }

    private void deleteSelectedContact() {
        int selectedRow = frame.getContactTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a contact to delete.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int modelRow = frame.getContactTable().convertRowIndexToModel(selectedRow);
        Contact contact = displayedContacts.get(modelRow);
        
        int confirm = JOptionPane.showConfirmDialog(frame,
            "Are you sure you want to delete contact:\n" + 
            contact.getName() + " (" + contact.getPhone() + ")?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            DeleteContactWorker worker = new DeleteContactWorker(frame, contactApiClient, contact);
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    loadAllContacts();
                }
            });
            worker.execute();
        }
    }

    private void loadAllContacts() {
        loadAllContacts(null);
    }

    private void loadAllContacts(String searchKeyword) {
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Loading contacts...");
        
        LoadContactWorker worker = new LoadContactWorker(frame, contactApiClient, searchKeyword);
        worker.addPropertyChangeListener(evt -> {
            if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                try {
                    allContacts = worker.get();
                    displayedContacts = new ArrayList<>(allContacts);
                    frame.getContactTableModel().setContactList(displayedContacts);
                    updateTotalRecordsLabel();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Failed to load contacts: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    frame.getProgressBar().setIndeterminate(false);
                    frame.getProgressBar().setString("Ready");
                }
            }
        });
        worker.execute();
    }

    private void applySearchFilter() {
        String keyword = frame.getSearchField().getText().trim();
        if (keyword.isEmpty()) {
            displayedContacts = new ArrayList<>(allContacts);
        } else {
            loadAllContacts(keyword);
            return;
        }
        frame.getContactTableModel().setContactList(displayedContacts);
        updateTotalRecordsLabel();
    }

    private void updateTotalRecordsLabel() {
        frame.getTotalRecordsLabel().setText(displayedContacts.size() + " Contacts");
    }
}