package worker.mahasiswa;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.MahasiswaApiClient;
import model.Mahasiswa;
import view.MahasiswaFrame;

public class UpdateMahasiswaWorker extends SwingWorker<Void, Void> {
    private final MahasiswaFrame frame;
    private final MahasiswaApiClient mahasiswaApiClient;
    private final Mahasiswa mahasiswa;

    public UpdateMahasiswaWorker(MahasiswaFrame frame, MahasiswaApiClient mahasiswaApiClient, Mahasiswa mahasiswa) {
        this.frame = frame;
        this.mahasiswaApiClient = mahasiswaApiClient;
        this.mahasiswa = mahasiswa;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Updating mahasiswa data...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        mahasiswaApiClient.update(mahasiswa);
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Mahasiswa updated successfully");
            JOptionPane.showMessageDialog(frame,
                    "Mahasiswa record has been updated.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to update mahasiswa");
            JOptionPane.showMessageDialog(frame,
                    "Error updating data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}