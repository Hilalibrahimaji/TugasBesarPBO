package model;

import java.time.LocalDate;

public class Peminjaman {
    private int id;
    private int bukuId;
    private String judulBuku;
    private String namaPeminjam;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private String status;

    // Constructors
    public Peminjaman() {}

    public Peminjaman(int id, int bukuId, String judulBuku, String namaPeminjam, 
                    LocalDate tanggalPinjam, LocalDate tanggalKembali, String status) {
        this.id = id;
        this.bukuId = bukuId;
        this.judulBuku = judulBuku;
        this.namaPeminjam = namaPeminjam;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getBukuId() { return bukuId; }
    public void setBukuId(int bukuId) { this.bukuId = bukuId; }
    
    public String getJudulBuku() { return judulBuku; }
    public void setJudulBuku(String judulBuku) { this.judulBuku = judulBuku; }
    
    public String getNamaPeminjam() { return namaPeminjam; }
    public void setNamaPeminjam(String namaPeminjam) { this.namaPeminjam = namaPeminjam; }
    
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(LocalDate tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }
    
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(LocalDate tanggalKembali) { this.tanggalKembali = tanggalKembali; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Peminjaman{" +
                "id=" + id +
                ", bukuId=" + bukuId +
                ", judulBuku='" + judulBuku + '\'' +
                ", namaPeminjam='" + namaPeminjam + '\'' +
                ", tanggalPinjam=" + tanggalPinjam +
                ", tanggalKembali=" + tanggalKembali +
                ", status='" + status + '\'' +
                '}';
    }
}