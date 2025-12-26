package controller;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import api.PeminjamanApiClient;
import model.Peminjaman;
import model.Buku;
import view.PeminjamanDialog;
import view.PeminjamanFrame;
import worker.peminjaman.*;

public class PeminjamanController {
    private final PeminjamanFrame frame;
    private final PeminjamanApiClient peminjamanApiClient = new PeminjamanApiClient();

    private List<Peminjaman> allPeminjaman = new ArrayList<>();
    private List<Peminjaman> displayedPeminjaman = new ArrayList<>();
    private List<Buku> allBuku = new ArrayList<>();

    public PeminjamanController(PeminjamanFrame frame) {
        this.frame = frame;
        setupEventListeners();
        loadAllData();
    }

    private void setupEventListeners() {
        // Button actions
        frame.getAddButton().addActionListener(e -> openPeminjamanDialog(null));
        frame.getRefreshButton().addActionListener(e -> {
            frame.getProgressBar().setString("Memperbarui data...");
            loadAllData();
        });
        frame.getEditButton().addActionListener(e -> editSelectedPeminjaman());
        frame.getDeleteButton().addActionListener(e -> deleteSelectedPeminjaman());
        
        // Double click to edit
        frame.getPeminjamanTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedPeminjaman();
                }
            }
        });
        
        // Search filter
        frame.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });
        
        // Status filter
        frame.getFilterStatusCombo().addActionListener(e -> applyFilters());
    }

    private void openPeminjamanDialog(Peminjaman peminjamanToEdit) {
        PeminjamanDialog dialog;
        if (peminjamanToEdit == null) {
            dialog = new PeminjamanDialog(frame, allBuku);
        } else {
            dialog = new PeminjamanDialog(frame, peminjamanToEdit, allBuku);
        }
        
        dialog.getSaveButton().addActionListener(e -> {
            Peminjaman peminjaman = dialog.getPeminjaman();
            if (peminjaman == null) return;
            
            dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            SwingWorker<Boolean, Void> worker;
            if (peminjamanToEdit == null) {
                worker = new SavePeminjamanWorker(frame, peminjamanApiClient, peminjaman);
            } else {
                worker = new UpdatePeminjamanWorker(frame, peminjamanApiClient, peminjaman);
            }
            
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    dialog.dispose();
                    try {
                        if (worker.get()) {
                            loadAllData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            worker.execute();
        });
        
        dialog.setVisible(true);
    }

    private void editSelectedPeminjaman() {
        int selectedRow = frame.getPeminjamanTable().getSelectedRow();
        if (selectedRow >= 0) {
            openPeminjamanDialog(displayedPeminjaman.get(selectedRow));
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Pilih peminjaman yang akan diedit!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedPeminjaman() {
        int selectedRow = frame.getPeminjamanTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, 
                "Pilih peminjaman yang akan dihapus!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Peminjaman peminjaman = displayedPeminjaman.get(selectedRow);
        
        // Custom confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(frame,
                "<html><b>Hapus peminjaman ini?</b><br><br>" +
                "Buku: " + peminjaman.getJudulBuku() + "<br>" +
                "Peminjam: " + peminjaman.getNamaPeminjam() + "<br>" +
                "Status: " + peminjaman.getStatus() + "</html>",
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            DeletePeminjamanWorker worker = new DeletePeminjamanWorker(frame, peminjamanApiClient, peminjaman);
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    try {
                        if (worker.get()) {
                            loadAllData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            worker.execute();
        }
    }

    private void loadAllData() {
        frame.showLoading(true);
        
        LoadPeminjamanWorker worker = new LoadPeminjamanWorker(peminjamanApiClient);
        worker.addPropertyChangeListener(evt -> {
            if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                try {
                    Object[] results = worker.get();
                    if (results != null && results.length == 2) {
                        allPeminjaman = (List<Peminjaman>) results[0];
                        allBuku = (List<Buku>) results[1];
                        
                        applyFilters(); // Apply current filters
                        frame.updateTotalRecords(displayedPeminjaman.size());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Gagal memuat data: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    frame.showLoading(false);
                }
            }
        });
        worker.execute();
    }

    private void applyFilters() {
        String searchKeyword = frame.getSearchField().getText().toLowerCase().trim();
        String selectedStatus = (String) frame.getFilterStatusCombo().getSelectedItem();
        
        displayedPeminjaman = new ArrayList<>();
        
        for (Peminjaman peminjaman : allPeminjaman) {
            // Apply search filter
            boolean matchesSearch = searchKeyword.isEmpty() ||
                peminjaman.getNamaPeminjam().toLowerCase().contains(searchKeyword) ||
                peminjaman.getJudulBuku().toLowerCase().contains(searchKeyword) ||
                peminjaman.getStatus().toLowerCase().contains(searchKeyword);
            
            // Apply status filter
            boolean matchesStatus = selectedStatus.equals("Semua") || 
                peminjaman.getStatus().equals(selectedStatus.toLowerCase());
            
            if (matchesSearch && matchesStatus) {
                displayedPeminjaman.add(peminjaman);
            }
        }
        
        frame.updateTable(displayedPeminjaman);
        frame.updateTotalRecords(displayedPeminjaman.size());
    }
}