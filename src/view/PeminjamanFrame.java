package view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import controller.PeminjamanController;
import view.tablemodel.PeminjamanTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class PeminjamanFrame extends JFrame {
    // Komponen UI
    private JTable peminjamanTable;
    private JTextField searchField;
    private JButton btnRefresh;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JComboBox<String> filterStatusCombo;
    private JLabel totalRecordsLabel;
    private JProgressBar progressBar;
    private PeminjamanTableModel tableModel;
    private PeminjamanController controller;

    public PeminjamanFrame() {
        initComponents();
        controller = new PeminjamanController(this);
        applyTableStyling();
        applyModernUI();
    }

    private void initComponents() {
        setTitle("Manajemen Peminjaman Buku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Toolbar Panel
        JPanel toolbarPanel = createToolbarPanel();
        
        // Filter Panel
        JPanel filterPanel = createFilterPanel();
        
        // Table Panel
        JScrollPane tableScrollPane = createTablePanel();
        
        // Status Panel
        JPanel statusPanel = createStatusPanel();

        // Main container dengan padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(toolbarPanel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.CENTER);
        
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        setSize(1200, 700);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Sistem Manajemen Peminjaman Buku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Perpustakaan Digital");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        
        // Icon atau logo
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        headerPanel.add(iconLabel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createToolbarPanel() {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbarPanel.setBorder(BorderFactory.createTitledBorder("Aksi"));
        
        btnAdd = createStyledButton("➕ Tambah", new Color(46, 204, 113));
        btnEdit = createStyledButton("✏️ Edit", new Color(52, 152, 219));
        btnDelete = createStyledButton("🗑️ Hapus", new Color(231, 76, 60));
        btnRefresh = createStyledButton("🔄 Refresh", new Color(155, 89, 182));
        
        toolbarPanel.add(btnAdd);
        toolbarPanel.add(btnEdit);
        toolbarPanel.add(btnDelete);
        toolbarPanel.add(btnRefresh);
        
        return toolbarPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter & Pencarian"));
        
        // Search field
        JLabel searchLabel = new JLabel("Cari:");
        searchField = new JTextField(20);
        searchField.setToolTipText("Cari berdasarkan nama peminjam, judul buku, atau status");
        
        // Status filter
        JLabel statusLabel = new JLabel("Status:");
        filterStatusCombo = new JComboBox<>(new String[]{"Semua", "Dipinjam", "Dikembalikan"});
        
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(statusLabel);
        filterPanel.add(filterStatusCombo);
        
        return filterPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new PeminjamanTableModel();
        peminjamanTable = new JTable(tableModel);
        
        // Set column widths
        peminjamanTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        peminjamanTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Judul Buku
        peminjamanTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Peminjam
        peminjamanTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Tanggal Pinjam
        peminjamanTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Tanggal Kembali
        peminjamanTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        
        peminjamanTable.setRowHeight(30);
        peminjamanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(peminjamanTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Peminjaman"));
        
        return scrollPane;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        statusPanel.setBackground(new Color(245, 245, 245));
        
        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        progressBar.setVisible(false);
        
        // Total records
        totalRecordsLabel = new JLabel("0 Data Peminjaman");
        totalRecordsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Version info
        JLabel versionLabel = new JLabel("v1.0.0 © 2024");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(Color.GRAY);
        
        statusPanel.add(progressBar, BorderLayout.WEST);
        statusPanel.add(totalRecordsLabel, BorderLayout.CENTER);
        statusPanel.add(versionLabel, BorderLayout.EAST);
        
        return statusPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
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

    private void applyTableStyling() {
        // Header styling
        JTableHeader header = peminjamanTable.getTableHeader();
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Cell renderer for status column
        peminjamanTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String status = value.toString();
                    c.setForeground(Color.BLACK);
                    
                    if (status.equals("dipinjam")) {
                        c.setBackground(new Color(255, 235, 156)); // Yellow
                        ((JLabel) c).setText("📖 Dipinjam");
                    } else if (status.equals("dikembalikan")) {
                        c.setBackground(new Color(200, 230, 201)); // Green
                        ((JLabel) c).setText("✅ Dikembalikan");
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    
                    if (isSelected) {
                        c.setBackground(table.getSelectionBackground());
                        c.setForeground(table.getSelectionForeground());
                    }
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return c;
            }
        });
        
        // Alternating row colors
        peminjamanTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                }
                
                return c;
            }
        });
    }

    private void applyModernUI() {
        try {
            // Set FlatLaf theme
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            
            // Customize some UI properties
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.rowHeight", 30);
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 12));
            
            // Update UI
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== GETTER METHODS ==========
    
    public JButton getAddButton() { return btnAdd; }
    public JButton getEditButton() { return btnEdit; }
    public JButton getDeleteButton() { return btnDelete; }
    public JButton getRefreshButton() { return btnRefresh; }
    public JTable getPeminjamanTable() { return peminjamanTable; }
    public JTextField getSearchField() { return searchField; }
    public JComboBox<String> getFilterStatusCombo() { return filterStatusCombo; }
    public JLabel getTotalRecordsLabel() { return totalRecordsLabel; }
    public JProgressBar getProgressBar() { return progressBar; }
    public PeminjamanTableModel getPeminjamanTableModel() { return tableModel; }

    // ========== HELPER METHODS ==========
    
    public void updateTable(java.util.List<model.Peminjaman> peminjamanList) {
        tableModel.setPeminjamanList(peminjamanList);
    }
    
    public void showLoading(boolean show) {
        progressBar.setVisible(show);
        progressBar.setIndeterminate(show);
        progressBar.setString(show ? "Memuat data..." : "Ready");
    }
    
    public void updateTotalRecords(int count) {
        totalRecordsLabel.setText(String.format("%,d Data Peminjaman", count));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            PeminjamanFrame frame = new PeminjamanFrame();
            frame.setVisible(true);
        });
    }
}