package worker.peminjaman;

import api.PeminjamanApiClient;
import model.Peminjaman;
import view.PeminjamanFrame;

import javax.swing.*;

public class DeletePeminjamanWorker extends SwingWorker<Boolean, Void> {
    private PeminjamanFrame frame;
    private PeminjamanApiClient apiClient;
    private Peminjaman peminjaman;
    private Exception exception;

    public DeletePeminjamanWorker(PeminjamanFrame frame, PeminjamanApiClient apiClient, Peminjaman peminjaman) {
        this.frame = frame;
        this.apiClient = apiClient;
        this.peminjaman = peminjaman;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            frame.getProgressBar().setString("Menghapus data...");
            apiClient.delete(peminjaman.getId());
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
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame,
                        "<html><b>Data berhasil dihapus!</b><br><br>" +
                        "Buku: " + peminjaman.getJudulBuku() + "<br>" +
                        "Peminjam: " + peminjaman.getNamaPeminjam() + "</html>",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                });
            } else if (exception != null) {
                throw exception;
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(frame,
                    "<html><b>Gagal menghapus data!</b><br><br>" +
                    "Error: " + e.getMessage() + "</html>",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            });
        } finally {
            SwingUtilities.invokeLater(() -> {
                frame.getProgressBar().setString("Ready");
                frame.getProgressBar().setVisible(false);
            });
        }
    }
}