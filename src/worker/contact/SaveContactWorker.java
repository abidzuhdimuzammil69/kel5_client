package worker.contact;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.ContactApiClient;
import model.Contact;
import view.ContactFrame;

public class SaveContactWorker extends SwingWorker<Void, Void> {
    private final ContactFrame frame;
    private final ContactApiClient contactApiClient;
    private final Contact contact;

    public SaveContactWorker(ContactFrame frame, ContactApiClient contactApiClient, Contact contact) {
        this.frame = frame;
        this.contactApiClient = contactApiClient;
        this.contact = contact;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Saving new contact...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        contactApiClient.create(contact);
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get(); // To catch any exception
            frame.getProgressBar().setString("Contact saved successfully");
            JOptionPane.showMessageDialog(frame,
                "New contact has been saved.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to save contact");
            JOptionPane.showMessageDialog(frame,
                "Error saving contact: \n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}