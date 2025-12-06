package worker.mahasiswa;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.MahasiswaApiClient;
import model.Mahasiswa;
import view.MahasiswaFrame;

public class LoadMahasiswaWorker extends SwingWorker<List<Mahasiswa>, Void> {
    private final MahasiswaFrame frame;
    private final MahasiswaApiClient mahasiswaApiClient;

    public LoadMahasiswaWorker(MahasiswaFrame frame, MahasiswaApiClient mahasiswaApiClient) {
        this.frame = frame;
        this.mahasiswaApiClient = mahasiswaApiClient;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Loading mahasiswa data...");
    }

    @Override
    protected List<Mahasiswa> doInBackground() throws Exception {
        return mahasiswaApiClient.findAll();
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            List<Mahasiswa> result = get();
            frame.getProgressBar().setString(result.size() + " records loaded");
        } catch (Exception e) {
            frame.getProgressBar().setString("Failed to load data");
            JOptionPane.showMessageDialog(frame,
                    "Error loading data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}