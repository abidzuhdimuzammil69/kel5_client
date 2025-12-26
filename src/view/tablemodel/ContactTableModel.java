package view.tablemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Contact;

public class ContactTableModel extends AbstractTableModel {
    private List<Contact> contactList = new ArrayList<>();
    private final String[] columnNames = { "ID", "Name", "Phone", "Email", "Created", "Updated" };

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        fireTableDataChanged();
    }

    public Contact getContactAt(int rowIndex) {
        return contactList.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return contactList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contact contact = contactList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> contact.getId();
            case 1 -> contact.getName();
            case 2 -> contact.getPhone();
            case 3 -> contact.getEmail();
            case 4 -> contact.getCreatedAt();
            case 5 -> contact.getUpdatedAt();
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Integer.class;
            default -> String.class;
        };
    }
}