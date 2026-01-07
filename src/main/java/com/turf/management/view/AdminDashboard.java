package com.turf.management.view;

import com.turf.management.model.Turf;
import com.turf.management.model.User;
import com.turf.management.service.BookingService; 
import com.turf.management.service.TurfService;
import com.turf.management.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    // Services
    private UserService userService;
    private TurfService turfService;
    private BookingService bookingService; 
    private String currentUserEmail;

    // Components for Inventory Tab
    JTextField txtId, txtName, txtLocation, txtPrice, txtImageUrl;
    JComboBox<String> cmbType, cmbStatus;
    JTable turfTable;
    DefaultTableModel tableModel;
    
    // Analytics Labels
    JLabel lblTotalTurfs, lblTotalUsers, lblTotalBookings, lblTotalEarnings;

    // --- CUSTOM BACKGROUND PANEL CLASS ---
    class BackgroundPanel extends JPanel {
        private Image img;

        public BackgroundPanel() {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/images/bg.jpg"));
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    img = icon.getImage();
                } else {
                    img = new ImageIcon(getClass().getResource("/bg.jpg")).getImage();
                }
            } catch (Exception e) {
                System.out.println("Image not found. Make sure bg.jpg is in resources folder.");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Constructor
    public AdminDashboard(UserService uService, TurfService tService, BookingService bService, String email) {
        this.userService = uService;
        this.turfService = tService;
        this.bookingService = bService; 
        this.currentUserEmail = email;

        setTitle("Admin Dashboard - Logged in as: " + email);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setSize(1366, 768); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel); 

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 33, 33, 220)); 
        JLabel lblTitle = new JLabel("  TURF ADMIN CONSOLE");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        topPanel.add(lblTitle, BorderLayout.CENTER);
        topPanel.add(btnLogout, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- TABS SETUP ---
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // 1. Home Tab
        tabs.addTab("Home (Analytics)", createHomePanel());
        
        // 2. Inventory Tab
        tabs.addTab("Manage Inventory", createInventoryPanel());
        
        // 3. View All Turfs Tab (Using external panel)
        ViewAllTurfsPanel viewTurfsPanel = new ViewAllTurfsPanel(turfService);
        tabs.addTab("View All Turfs", viewTurfsPanel);
        
        // 4. View Customers Tab
        tabs.addTab("View All Customers", new ViewAllCustomersPanel(userService));
        
        // 5. Bookings Tab
        tabs.addTab("View All Bookings", createBookingPanel());

        // 6. Settings Tab
        tabs.addTab("Settings", createSettingsPanel());

        add(tabs, BorderLayout.CENTER);

        // Auto Refresh Logic
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedComponent() == viewTurfsPanel) {
                viewTurfsPanel.loadData(); 
            }
            if (tabs.getSelectedIndex() == 0) {
                updateAnalytics();
            }
        });

        // Logout Action
        btnLogout.addActionListener(e -> {
            new LoginFrame(userService, turfService, bookingService).setVisible(true); 
            this.dispose();
        });

        refreshInventoryTable();
        updateAnalytics(); 
    }

    // Helper for future use
    private String generateBookingId() {
        int randomNum = (int)(Math.random() * 9000) + 1000; 
        return "#BK-" + randomNum;
    }

    // --- BOOKING PANEL ---
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); 
        
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Recent Bookings");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblTitle);
        panel.add(header, BorderLayout.NORTH);

        String[] columns = {"Booking ID", "Customer Name", "Price", "Payment", "Status"};
        DefaultTableModel bookingModel = new DefaultTableModel(columns, 0);
        
        // --- DUMMY DATA REMOVED as requested ---
        // Table starts empty
        
        JTable bookingTable = new JTable(bookingModel);
        bookingTable.setRowHeight(30);
        bookingTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 40, 40)); 
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(new Color(255, 255, 255, 200)); 
        lblTotalTurfs = new JLabel("0"); panel.add(createCard("Total Turfs", lblTotalTurfs, new Color(0, 153, 204)));
        lblTotalUsers = new JLabel("0"); panel.add(createCard("Active Customers", lblTotalUsers, new Color(0, 204, 102)));
        lblTotalBookings = new JLabel("0"); panel.add(createCard("Total Bookings", lblTotalBookings, new Color(255, 153, 51)));
        lblTotalEarnings = new JLabel("Rs 0"); panel.add(createCard("Total Earnings", lblTotalEarnings, new Color(153, 51, 255)));
        return panel;
    }

    private JPanel createCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblTitle = new JLabel(title); lblTitle.setForeground(Color.WHITE); lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20)); 
        valueLabel.setForeground(Color.WHITE); valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 40)); valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(lblTitle, BorderLayout.NORTH); card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createInventoryPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new Dimension(350, 0));
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add / Edit Turf"));

        txtId = new JTextField(); txtId.setVisible(false); formPanel.add(txtId);
        JLabel l1 = new JLabel("Name:"); l1.setBounds(20, 40, 80, 25); formPanel.add(l1);
        txtName = new JTextField(); txtName.setBounds(100, 40, 200, 25); formPanel.add(txtName);
        JLabel l2 = new JLabel("Location:"); l2.setBounds(20, 80, 80, 25); formPanel.add(l2);
        txtLocation = new JTextField(); txtLocation.setBounds(100, 80, 200, 25); formPanel.add(txtLocation);
        JLabel l3 = new JLabel("Price:"); l3.setBounds(20, 120, 80, 25); formPanel.add(l3);
        txtPrice = new JTextField(); txtPrice.setBounds(100, 120, 200, 25); formPanel.add(txtPrice);
        JLabel l4 = new JLabel("Type:"); l4.setBounds(20, 160, 80, 25); formPanel.add(l4);
        String[] types = {"Cricket", "Football", "Badminton"};
        cmbType = new JComboBox<>(types); cmbType.setBounds(100, 160, 200, 25); formPanel.add(cmbType);
        JLabel l5 = new JLabel("Status:"); l5.setBounds(20, 200, 80, 25); formPanel.add(l5);
        String[] stats = {"Available", "Maintenance", "Closed"};
        cmbStatus = new JComboBox<>(stats); cmbStatus.setBounds(100, 200, 200, 25); formPanel.add(cmbStatus);
        JLabel l6 = new JLabel("Image URL:"); l6.setBounds(20, 240, 80, 25); formPanel.add(l6);
        txtImageUrl = new JTextField(); txtImageUrl.setBounds(100, 240, 200, 25); formPanel.add(txtImageUrl);

        JButton btnSave = new JButton("Save / Add");
        btnSave.setBounds(30, 300, 120, 35); btnSave.setBackground(new Color(0, 128, 0)); btnSave.setForeground(Color.WHITE);
        formPanel.add(btnSave);
        JButton btnClear = new JButton("Clear Form");
        btnClear.setBounds(180, 300, 120, 35); formPanel.add(btnClear);

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = {"Hidden_ID", "Sr. No.", "Name", "Location", "Price", "Type", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        turfTable = new JTable(tableModel);
        turfTable.getColumnModel().getColumn(0).setMinWidth(0); turfTable.getColumnModel().getColumn(0).setMaxWidth(0); turfTable.getColumnModel().getColumn(0).setWidth(0);
        tablePanel.add(new JScrollPane(turfTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        JButton btnDelete = new JButton("Delete Selected"); btnDelete.setBackground(Color.RED); btnDelete.setForeground(Color.WHITE);
        JButton btnEdit = new JButton("Edit Selected"); JButton btnRefresh = new JButton("Refresh List");
        actionPanel.add(btnEdit); actionPanel.add(btnDelete); actionPanel.add(btnRefresh);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            try {
                if(txtName.getText().isEmpty() || txtPrice.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Fill required fields!"); return; }
                Turf t = new Turf();
                if(!txtId.getText().isEmpty()) t.setId(Long.parseLong(txtId.getText()));
                t.setName(txtName.getText()); t.setLocation(txtLocation.getText()); t.setPrice(Double.parseDouble(txtPrice.getText()));
                t.setType(cmbType.getSelectedItem().toString()); t.setStatus(cmbStatus.getSelectedItem().toString()); t.setImageUrl(txtImageUrl.getText());
                turfService.addTurf(t);
                JOptionPane.showMessageDialog(this, "Saved Successfully!");
                clearForm(); refreshInventoryTable(); updateAnalytics();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        btnDelete.addActionListener(e -> {
            int row = turfTable.getSelectedRow();
            if(row != -1) {
                Long id = (Long) tableModel.getValueAt(row, 0); 
                if(JOptionPane.showConfirmDialog(this, "Delete Selected Turf?") == JOptionPane.YES_OPTION) {
                    turfService.deleteTurf(id); refreshInventoryTable(); updateAnalytics();
                }
            } else JOptionPane.showMessageDialog(this, "Select a row first!");
        });

        btnEdit.addActionListener(e -> {
            int row = turfTable.getSelectedRow();
            if(row != -1) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 2).toString());
                txtLocation.setText(tableModel.getValueAt(row, 3).toString());
                txtPrice.setText(tableModel.getValueAt(row, 4).toString());
            }
        });
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> refreshInventoryTable());
        mainPanel.add(formPanel, BorderLayout.WEST); mainPanel.add(tablePanel, BorderLayout.CENTER);
        return mainPanel;
    }

    // --- SETTINGS PANEL (Updated with Auto-Clear) ---
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(null); panel.setBackground(Color.WHITE);
        
        JLabel l1 = new JLabel("CHANGE PASSWORD"); l1.setFont(new Font("Arial", Font.BOLD, 16)); l1.setBounds(50, 30, 200, 30); panel.add(l1);
        JLabel l2 = new JLabel("New Password:"); l2.setBounds(50, 70, 120, 30); panel.add(l2);
        JPasswordField txtNewPass = new JPasswordField(); txtNewPass.setBounds(180, 70, 200, 30); panel.add(txtNewPass);
        JButton btnChangePass = new JButton("Update Password"); btnChangePass.setBounds(180, 110, 150, 30); panel.add(btnChangePass);
        
        JSeparator sep1 = new JSeparator(); sep1.setBounds(50, 160, 400, 10); panel.add(sep1);
        
        JLabel l3 = new JLabel("ADD NEW ADMIN"); l3.setFont(new Font("Arial", Font.BOLD, 16)); l3.setBounds(50, 180, 200, 30); panel.add(l3);
        JLabel l4 = new JLabel("Name:"); l4.setBounds(50, 220, 100, 30); panel.add(l4);
        JTextField txtAdmName = new JTextField(); txtAdmName.setBounds(180, 220, 200, 30); panel.add(txtAdmName);
        JLabel l5 = new JLabel("Email:"); l5.setBounds(50, 260, 100, 30); panel.add(l5);
        JTextField txtAdmEmail = new JTextField(); txtAdmEmail.setBounds(180, 260, 200, 30); panel.add(txtAdmEmail);
        JLabel l6 = new JLabel("Password:"); l6.setBounds(50, 300, 100, 30); panel.add(l6);
        JPasswordField txtAdmPass = new JPasswordField(); txtAdmPass.setBounds(180, 300, 200, 30); panel.add(txtAdmPass);
        JButton btnAddAdmin = new JButton("Create Admin"); btnAddAdmin.setBounds(180, 340, 150, 30); panel.add(btnAddAdmin);
        
        JSeparator sep2 = new JSeparator(); sep2.setBounds(50, 400, 400, 10); panel.add(sep2);
        
        JLabel l7 = new JLabel("DELETE USER / ADMIN"); l7.setFont(new Font("Arial", Font.BOLD, 16)); l7.setForeground(Color.RED); l7.setBounds(50, 420, 250, 30); panel.add(l7);
        JLabel l8 = new JLabel("User Email:"); l8.setBounds(50, 460, 100, 30); panel.add(l8);
        JTextField txtDelEmail = new JTextField(); txtDelEmail.setBounds(180, 460, 200, 30); panel.add(txtDelEmail);
        JButton btnDeleteUser = new JButton("Delete User"); btnDeleteUser.setBackground(Color.RED); btnDeleteUser.setForeground(Color.WHITE);
        btnDeleteUser.setBounds(180, 500, 150, 30); panel.add(btnDeleteUser);

        btnChangePass.addActionListener(e -> {
            boolean success = userService.changePassword(currentUserEmail, new String(txtNewPass.getPassword()));
            if(success) {
                JOptionPane.showMessageDialog(this, "Password Changed Successfully!");
                txtNewPass.setText(""); // Auto Clear
            } else {
                JOptionPane.showMessageDialog(this, "Error: User not found!");
            }
        });

        btnAddAdmin.addActionListener(e -> {
            if(txtAdmEmail.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Fill Email!"); return; }
            User u = new User(); u.setName(txtAdmName.getText()); u.setEmail(txtAdmEmail.getText()); u.setPassword(new String(txtAdmPass.getPassword())); u.setRole("Admin");
            userService.registerUser(u); 
            JOptionPane.showMessageDialog(this, "New Admin Added!");
            // Auto Clear
            txtAdmName.setText(""); txtAdmEmail.setText(""); txtAdmPass.setText("");
            updateAnalytics();
        });

        btnDeleteUser.addActionListener(e -> {
            String emailToDelete = txtDelEmail.getText().trim();

            if(JOptionPane.showConfirmDialog(this, "Delete user: " + emailToDelete + "?") == JOptionPane.YES_OPTION) {
                boolean deleted = userService.deleteUserByEmail(emailToDelete);
                if(deleted) { 
                    JOptionPane.showMessageDialog(this, "User Deleted!"); 
                    txtDelEmail.setText(""); // <--- FIX: Clear TextField
                    updateAnalytics(); 
                } else { 
                    JOptionPane.showMessageDialog(this, "User not found!"); 
                }
            }
        });
        return panel;
    }

    private void clearForm() { txtId.setText(""); txtName.setText(""); txtLocation.setText(""); txtPrice.setText(""); txtImageUrl.setText(""); }
    private void refreshInventoryTable() {
        tableModel.setRowCount(0);
        List<Turf> turfs = turfService.getAllTurfs();
        int srNo = 1;
        for (Turf t : turfs) { tableModel.addRow(new Object[]{t.getId(), srNo++, t.getName(), t.getLocation(), t.getPrice(), t.getType(), t.getStatus()}); }
    }
    public void updateAnalytics() {
        lblTotalTurfs.setText(String.valueOf(turfService.getTurfCount()));
        lblTotalUsers.setText(String.valueOf(userService.getUserCount()));
        if(bookingService != null) {
            lblTotalBookings.setText(String.valueOf(bookingService.getTotalBookingsCount()));
            lblTotalEarnings.setText("Rs " + bookingService.getTotalEarnings());
        }
    }
}