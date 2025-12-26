package view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import model.Peminjaman;
import model.Buku;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PeminjamanDialog extends JDialog {
    private Peminjaman peminjaman;
    private List<Buku> bukuList;
    private boolean isEditMode;
    
    private JTextField txtId;
    private JComboBox<Buku> cmbBuku;
    private JTextField txtPeminjam;
    private JFormattedTextField txtTanggalPinjam;
    private JFormattedTextField txtTanggalKembali;
    private JComboBox<String> cmbStatus;
    private JButton btnSave;
    private JButton btnCancel;
    
    public PeminjamanDialog(Frame parent, List<Buku> bukuList) {
        this(parent, null, bukuList);
        this.isEditMode = false;
        setTitle("Tambah Peminjaman Baru");
    }
    
    public PeminjamanDialog(Frame parent, Peminjaman peminjaman, List<Buku> bukuList) {
        super(parent, "Edit Peminjaman", true);
        this.peminjaman = peminjaman;
        this.bukuList = bukuList;
        this.isEditMode = peminjaman != null;
        
        initComponents();
        initData();
        applyModernUI();
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(isEditMode ? "✏️ Edit Data Peminjaman" : "➕ Tambah Data Peminjaman");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Isi formulir di bawah dengan data yang valid");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        
        // Icon
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        headerPanel.add(iconLabel, BorderLayout.EAST);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Row 1: ID (hidden for add mode)
        if (isEditMode) {
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("ID Peminjaman:"), gbc);
            
            gbc.gridx = 1;
            txtId = new JTextField(20);
            txtId.setEditable(false);
            txtId.setBackground(new Color(240, 240, 240));
            formPanel.add(txtId, gbc);
        }
        
        // Row 2: Buku
        int rowOffset = isEditMode ? 1 : 0;
        
        gbc.gridx = 0; gbc.gridy = rowOffset;
        JLabel lblBuku = new JLabel("Buku*");
        lblBuku.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblBuku, gbc);
        
        gbc.gridx = 1;
        cmbBuku = new JComboBox<>();
        cmbBuku.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Buku) {
                    Buku buku = (Buku) value;
                    if (buku.getId() == 0) {
                        setText("Pilih Buku...");
                    } else {
                        setText(buku.getJudul() + " (" + buku.getPenulis() + ")");
                    }
                }
                return c;
            }
        });
        formPanel.add(cmbBuku, gbc);
        
        // Row 3: Nama Peminjam
        gbc.gridx = 0; gbc.gridy = rowOffset + 1;
        JLabel lblPeminjam = new JLabel("Nama Peminjam*");
        lblPeminjam.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblPeminjam, gbc);
        
        gbc.gridx = 1;
        txtPeminjam = new JTextField(20);
        formPanel.add(txtPeminjam, gbc);
        
        // Row 4: Tanggal Pinjam & Kembali
        gbc.gridx = 0; gbc.gridy = rowOffset + 2;
        JLabel lblTanggalPinjam = new JLabel("Tanggal Pinjam*");
        lblTanggalPinjam.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblTanggalPinjam, gbc);
        
        gbc.gridx = 1;
        txtTanggalPinjam = new JFormattedTextField(new java.text.SimpleDateFormat("yyyy-MM-dd"));
        txtTanggalPinjam.setValue(LocalDate.now());
        formPanel.add(txtTanggalPinjam, gbc);
        
        gbc.gridx = 2;
        JLabel lblTanggalKembali = new JLabel("Tanggal Kembali");
        lblTanggalKembali.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblTanggalKembali, gbc);
        
        gbc.gridx = 3;
        txtTanggalKembali = new JFormattedTextField(new java.text.SimpleDateFormat("yyyy-MM-dd"));
        formPanel.add(txtTanggalKembali, gbc);
        
        // Row 5: Status
        gbc.gridx = 0; gbc.gridy = rowOffset + 3;
        JLabel lblStatus = new JLabel("Status*");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(new String[]{"dipinjam", "dikembalikan"});
        formPanel.add(cmbStatus, gbc);
        
        // Required fields note
        gbc.gridx = 0; gbc.gridy = rowOffset + 4;
        gbc.gridwidth = 4;
        JLabel lblNote = new JLabel("* Menandakan field wajib diisi");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(Color.GRAY);
        formPanel.add(lblNote, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        btnCancel = createStyledButton("❌ Batal", new Color(149, 165, 166));
        btnSave = createStyledButton(isEditMode ? "💾 Simpan Perubahan" : "➕ Tambah Data", 
                                    new Color(46, 204, 113));
        
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        
        // Add panels to dialog
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void initData() {
        // Add default "Select book" option
        cmbBuku.addItem(new Buku(0, "Pilih Buku...", "", "", 0));
        
        // Add all books
        for (Buku buku : bukuList) {
            cmbBuku.addItem(buku);
        }
        
        if (isEditMode && peminjaman != null) {
            // Fill form with existing data
            txtId.setText(String.valueOf(peminjaman.getId()));
            txtId.setBackground(new Color(240, 240, 240));
            
            // Select the book
            for (int i = 0; i < cmbBuku.getItemCount(); i++) {
                Buku buku = cmbBuku.getItemAt(i);
                if (buku.getId() == peminjaman.getBukuId()) {
                    cmbBuku.setSelectedIndex(i);
                    break;
                }
            }
            
            txtPeminjam.setText(peminjaman.getNamaPeminjam());
            txtTanggalPinjam.setValue(peminjaman.getTanggalPinjam());
            
            if (peminjaman.getTanggalKembali() != null) {
                txtTanggalKembali.setValue(peminjaman.getTanggalKembali());
            }
            
            cmbStatus.setSelectedItem(peminjaman.getStatus());
        } else {
            // Set defaults for new record
            cmbBuku.setSelectedIndex(0);
            cmbStatus.setSelectedItem("dipinjam");
        }
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void applyModernUI() {
        try {
            // Set font for all components
            Font font = new Font("Segoe UI", Font.PLAIN, 12);
            setFontForComponent(this, font);
            
            // Add padding to text fields
            for (Component comp : getComponents()) {
                if (comp instanceof JTextField) {
                    ((JTextField) comp).setBorder(BorderFactory.createCompoundBorder(
                        ((JTextField) comp).getBorder(),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setFontForComponent(Container container, Font font) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                setFontForComponent((Container) component, font);
            }
            component.setFont(font);
        }
    }
    
    public Peminjaman getPeminjaman() {
        try {
            // Validation
            if (cmbBuku.getSelectedItem() == null || ((Buku) cmbBuku.getSelectedItem()).getId() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Pilih buku yang akan dipinjam!", 
                    "Validasi Error", 
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            String namaPeminjam = txtPeminjam.getText().trim();
            if (namaPeminjam.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Nama peminjam harus diisi!", 
                    "Validasi Error", 
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            String tanggalPinjamStr = txtTanggalPinjam.getText().trim();
            if (tanggalPinjamStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Tanggal pinjam harus diisi!", 
                    "Validasi Error", 
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            // Create Peminjaman object
            Buku selectedBuku = (Buku) cmbBuku.getSelectedItem();
            LocalDate tanggalPinjam = LocalDate.parse(tanggalPinjamStr);
            LocalDate tanggalKembali = txtTanggalKembali.getText().trim().isEmpty() ? 
                null : LocalDate.parse(txtTanggalKembali.getText().trim());
            String status = (String) cmbStatus.getSelectedItem();
            
            Peminjaman p = new Peminjaman();
            if (isEditMode) {
                p.setId(Integer.parseInt(txtId.getText()));
            }
            p.setBukuId(selectedBuku.getId());
            p.setJudulBuku(selectedBuku.getJudul());
            p.setNamaPeminjam(namaPeminjam);
            p.setTanggalPinjam(tanggalPinjam);
            p.setTanggalKembali(tanggalKembali);
            p.setStatus(status);
            
            return p;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public JButton getSaveButton() {
        return btnSave;
    }
    
    public JButton getCancelButton() {
        return btnCancel;
    }
}