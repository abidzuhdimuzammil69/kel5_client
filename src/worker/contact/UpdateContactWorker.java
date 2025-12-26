package worker.contact;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.ContactApiClient;
import model.Contact;
import view.ContactFrame;

public class UpdateContactWorker extends SwingWorker<Void, Void> {
    private final ContactFrame frame;
    private final ContactApiClient contactApiClient;
    private final Contact contact;

    public UpdateContactWorker(ContactFrame frame, ContactApiClient contactApiClient, Contact contact) {
        this.frame = frame;
        this.contactApiClient = contactApiClient;
        this.contact = contact;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Updating contact...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        contactApiClient.update(contact);
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Contact updated successfully");
            JOptionPane.showMessageDialog(frame,
                "Contact has been updated.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to update contact");
            JOptionPane.showMessageDialog(frame,
                "Error updating contact: \n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}