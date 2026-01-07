package com.turf.management.view;

import com.turf.management.model.User;
import com.turf.management.service.BookingService;
import com.turf.management.service.TurfService;
import com.turf.management.service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private UserService userService;
    private TurfService turfService;
    private BookingService bookingService;

    // Components
    private JTextField txtName, txtEmail, txtPhone;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;

    public RegisterFrame(UserService uService, TurfService tService, BookingService bService) {
        this.userService = uService;
        this.turfService = tService;
        this.bookingService = bService;

        setTitle("Turf Management System - Register");
        
        // --- 1. RESPONSIVE CODE (Full Screen) ---
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- 2. BACKGROUND IMAGE SETUP ---
        // Custom Panel for Background Image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/images/bg.jpg"));
                    Image img = icon.getImage();
                    // Stretch image to full screen
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    System.out.println("Image nahi mili: " + e.getMessage());
                }
            }
        };

        // Layout to center the Register Box
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // --- 3. REGISTER BOX DESIGN ---
        JPanel regBox = new JPanel(null);
        regBox.setPreferredSize(new Dimension(450, 500)); // Thoda bada box
        regBox.setBackground(new Color(255, 255, 255, 220)); // Transparent White
        regBox.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2)); // Green Border

        JLabel lblTitle = new JLabel("REGISTER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(34, 139, 34)); // Green Text
        lblTitle.setBounds(150, 20, 150, 40);
        regBox.add(lblTitle);

        // --- Fields ---
        JLabel lblName = new JLabel("Name:"); 
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setBounds(50, 80, 80, 25); 
        regBox.add(lblName);
        
        txtName = new JTextField(); 
        txtName.setBounds(140, 80, 250, 30); 
        regBox.add(txtName);

        JLabel lblEmail = new JLabel("Email:"); 
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setBounds(50, 130, 80, 25); 
        regBox.add(lblEmail);
        
        txtEmail = new JTextField(); 
        txtEmail.setBounds(140, 130, 250, 30); 
        regBox.add(txtEmail);

        JLabel lblPass = new JLabel("Password:"); 
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setBounds(50, 180, 80, 25); 
        regBox.add(lblPass);
        
        txtPassword = new JPasswordField(); 
        txtPassword.setBounds(140, 180, 250, 30); 
        regBox.add(txtPassword);

        JLabel lblPhone = new JLabel("Phone:"); 
        lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPhone.setBounds(50, 230, 80, 25); 
        regBox.add(lblPhone);
        
        txtPhone = new JTextField(); 
        txtPhone.setBounds(140, 230, 250, 30); 
        regBox.add(txtPhone);

        JLabel lblRole = new JLabel("Role:"); 
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRole.setBounds(50, 280, 80, 25); 
        regBox.add(lblRole);
        
        String[] roles = {"Customer", "Admin"}; 
        cmbRole = new JComboBox<>(roles); 
        cmbRole.setBounds(140, 280, 250, 30); 
        regBox.add(cmbRole);

        // --- Buttons ---
        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(50, 350, 150, 40);
        btnRegister.setBackground(new Color(34, 139, 34)); // Dark Green
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        regBox.add(btnRegister);

        JButton btnBack = new JButton("Back to Login");
        btnBack.setBounds(240, 350, 150, 40);
        btnBack.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        regBox.add(btnBack);

        // Add Box to Center
        backgroundPanel.add(regBox);

        // --- ACTION LISTENERS ---
        btnRegister.addActionListener(e -> {
            try {
                if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || 
                    new String(txtPassword.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!");
                    return;
                }

                User u = new User();
                u.setName(txtName.getText());
                u.setEmail(txtEmail.getText());
                u.setPassword(new String(txtPassword.getPassword()));
                u.setPhone(txtPhone.getText());
                u.setRole(cmbRole.getSelectedItem().toString());

                if (userService.registerUser(u)) {
                    JOptionPane.showMessageDialog(this, "Registration Successful! Please Login.");
                    new LoginFrame(userService, turfService, bookingService).setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Email already exists!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnBack.addActionListener(e -> {
            new LoginFrame(userService, turfService, bookingService).setVisible(true);
            this.dispose();
        });
    }
}