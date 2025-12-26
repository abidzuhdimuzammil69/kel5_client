package worker.contact;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.ContactApiClient;
import model.Contact;
import view.ContactFrame;

public class DeleteContactWorker extends SwingWorker<Void, Void> {
    private final ContactFrame frame;
    private final ContactApiClient contactApiClient;
    private final Contact contact;

    public DeleteContactWorker(ContactFrame frame, ContactApiClient contactApiClient, Contact contact) {
        this.frame = frame;
        this.contactApiClient = contactApiClient;
        this.contact = contact;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Deleting contact...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        contactApiClient.delete(contact.getId());
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Contact deleted successfully");
            JOptionPane.showMessageDialog(frame,
                "Contact has been deleted.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to delete contact");
            JOptionPane.showMessageDialog(frame,
                "Error deleting contact: \n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}