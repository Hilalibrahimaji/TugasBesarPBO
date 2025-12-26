package worker.peminjaman;

import api.PeminjamanApiClient;
import model.Peminjaman;
import view.PeminjamanFrame;

import javax.swing.*;

public class SavePeminjamanWorker extends SwingWorker<Boolean, Void> {
    private PeminjamanFrame frame;
    private PeminjamanApiClient apiClient;
    private Peminjaman peminjaman;
    private Exception exception;

    public SavePeminjamanWorker(PeminjamanFrame frame, PeminjamanApiClient apiClient, Peminjaman peminjaman) {
        this.frame = frame;
        this.apiClient = apiClient;
        this.peminjaman = peminjaman;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            apiClient.create(peminjaman);
            return true;
        } catch (Exception e) {
            exception = e;
            return false;
        }
    }

    @Override
    protected void done() {
        try {
            boolean success = get();
            if (success) {
                JOptionPane.showMessageDialog(frame,
                    "Data peminjaman berhasil disimpan!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            } else if (exception != null) {
                throw exception;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Gagal menyimpan data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}