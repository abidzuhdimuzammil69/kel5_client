package worker.mahasiswa;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.MahasiswaApiClient;
import model.Mahasiswa;
import view.MahasiswaFrame;

public class DeleteMahasiswaWorker extends SwingWorker<Void, Void> {
    private final MahasiswaFrame frame;
    private final MahasiswaApiClient mahasiswaApiClient;
    private final Mahasiswa mahasiswa;

    public DeleteMahasiswaWorker(MahasiswaFrame frame, MahasiswaApiClient mahasiswaApiClient, Mahasiswa mahasiswa) {
        this.frame = frame;
        this.mahasiswaApiClient = mahasiswaApiClient;
        this.mahasiswa = mahasiswa;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Deleting mahasiswa record...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        mahasiswaApiClient.delete(mahasiswa.getId());
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Mahasiswa deleted successfully");
            JOptionPane.showMessageDialog(frame,
                    "Mahasiswa record has been deleted.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to delete mahasiswa");
            JOptionPane.showMessageDialog(frame,
                    "Error deleting data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}