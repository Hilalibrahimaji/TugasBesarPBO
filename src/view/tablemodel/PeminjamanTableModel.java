package view.tablemodel;

import model.Peminjaman;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanTableModel extends AbstractTableModel {
    private List<Peminjaman> peminjamanList;
    private final String[] columnNames = {"ID", "Buku", "Peminjam", "Tanggal Pinjam", "Tanggal Kembali", "Status"};
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PeminjamanTableModel() {
        this.peminjamanList = new ArrayList<>();
    }

    public PeminjamanTableModel(List<Peminjaman> peminjamanList) {
        this.peminjamanList = peminjamanList != null ? peminjamanList : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return peminjamanList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Peminjaman peminjaman = peminjamanList.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return peminjaman.getId();
            case 1: return peminjaman.getJudulBuku();
            case 2: return peminjaman.getNamaPeminjam();
            case 3: return peminjaman.getTanggalPinjam() != null ? 
                         peminjaman.getTanggalPinjam().format(formatter) : "";
            case 4: return peminjaman.getTanggalKembali() != null ? 
                         peminjaman.getTanggalKembali().format(formatter) : "";
            case 5: return peminjaman.getStatus();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            case 1: 
            case 2: 
            case 5: return String.class;
            case 3: 
            case 4: return String.class;
            default: return Object.class;
        }
    }

    public Peminjaman getPeminjamanAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < peminjamanList.size()) {
            return peminjamanList.get(rowIndex);
        }
        return null;
    }

    // Getter and Setter
    public List<Peminjaman> getPeminjamanList() {
        return peminjamanList;
    }

    public void setPeminjamanList(List<Peminjaman> peminjamanList) {
        this.peminjamanList = peminjamanList != null ? peminjamanList : new ArrayList<>();
        fireTableDataChanged();
    }
}