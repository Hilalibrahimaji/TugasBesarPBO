package worker.peminjaman;

import api.PeminjamanApiClient;
import model.Peminjaman;
import model.Buku;

import javax.swing.*;
import java.util.List;

public class LoadPeminjamanWorker extends SwingWorker<Object[], Void> {
    private PeminjamanApiClient apiClient;
    private Exception exception;

    public LoadPeminjamanWorker(PeminjamanApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    protected Object[] doInBackground() throws Exception {
        try {
            System.out.println("Loading data from API...");
            List<Peminjaman> peminjamanList = apiClient.findAll();
            List<Buku> bukuList = apiClient.findAllBuku();
            
            System.out.println("Loaded " + peminjamanList.size() + " peminjaman");
            System.out.println("Loaded " + bukuList.size() + " books");
            
            return new Object[]{peminjamanList, bukuList};
        } catch (Exception e) {
            exception = e;
            throw e;
        }
    }

    @Override
    protected void done() {
        if (exception != null) {
            exception.printStackTrace();
        }
    }
}