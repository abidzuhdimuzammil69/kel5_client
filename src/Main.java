import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

import controller.ContactController;
import view.ContactFrame;

public class Main {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightFlatIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to set FlatLaf theme");
        }

        // Start application on EDT
        SwingUtilities.invokeLater(() -> {
            ContactFrame frame = new ContactFrame();
            new ContactController(frame);
            frame.setVisible(true);
            
            // Show welcome message
            javax.swing.JOptionPane.showMessageDialog(frame,
                "Welcome to Contact Management System!\n\n" +
                "Features:\n" +
                "• Add, Edit, Delete Contacts\n" +
                "• Search Contacts\n" +
                "• View Contact History\n" +
                "\nConnected to: " + System.getProperty("api.base.url", "http://localhost"),
                "Welcome", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
    }
}