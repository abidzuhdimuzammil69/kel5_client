package worker.contact;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.ContactApiClient;
import model.Contact;
import view.ContactFrame;

public class LoadContactWorker extends SwingWorker<List<Contact>, Void> {
    private final ContactFrame frame;
    private final ContactApiClient contactApiClient;
    private final String searchKeyword;

    public LoadContactWorker(ContactFrame frame, ContactApiClient contactApiClient, String searchKeyword) {
        this.frame = frame;
        this.contactApiClient = contactApiClient;
        this.searchKeyword = searchKeyword;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Loading contacts...");
    }

    @Override
    protected List<Contact> doInBackground() throws Exception {
        return contactApiClient.findAll(searchKeyword);
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            List<Contact> result = get();
            frame.getProgressBar().setString(result.size() + " contacts loaded");
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to load contacts");
            JOptionPane.showMessageDialog(frame,
                "Error loading contacts: \n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}