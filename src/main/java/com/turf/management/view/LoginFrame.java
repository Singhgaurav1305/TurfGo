package com.turf.management.view;

import com.turf.management.service.BookingService;
import com.turf.management.service.TurfService;
import com.turf.management.service.UserService;
import com.turf.management.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private UserService userService;
    private TurfService turfService;
    private BookingService bookingService;

    public LoginFrame(UserService uService, TurfService tService, BookingService bService) {
        this.userService = uService;
        this.turfService = tService;
        this.bookingService = bService;

        setTitle("Turf Management - Login");
        
        // --- 1. RESPONSIVE CODE (Full Screen) ---
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- 2. BACKGROUND IMAGE SETUP ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/images/bg.jpg"));
                    Image img = icon.getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    System.out.println("Image nahi mili: " + e.getMessage());
                }
            }
        };

        // Layout set kiya taaki Login Box beech (center) mein aaye
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // --- 3. LOGIN BOX DESIGN ---
        JPanel loginBox = new JPanel(null); 
        // Height badha di (350 -> 420) taaki Role field fit ho jaye
        loginBox.setPreferredSize(new Dimension(400, 420)); 
        loginBox.setBackground(new Color(255, 255, 255, 220)); 
        loginBox.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2)); 

        JLabel lblTitle = new JLabel("LOGIN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(0, 153, 204));
        lblTitle.setBounds(150, 20, 100, 40);
        loginBox.add(lblTitle);

        // --- Email Field ---
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setBounds(40, 80, 100, 25);
        loginBox.add(lblEmail);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(40, 105, 320, 30);
        loginBox.add(txtEmail);

        // --- Password Field ---
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setBounds(40, 150, 100, 25);
        loginBox.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(40, 175, 320, 30);
        loginBox.add(txtPass);

        // --- NEW: Role Field ---
        JLabel lblRole = new JLabel("Select Role:");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRole.setBounds(40, 220, 100, 25);
        loginBox.add(lblRole);

        // Note: RegisterFrame mein aapne "User" aur "Admin" use kiya tha.
        // Yahan display ke liye "Customer" dikha rahe hain par logic handle karega.
        String[] roles = {"Customer", "Admin"}; 
        JComboBox<String> cmbRole = new JComboBox<>(roles);
        cmbRole.setBounds(40, 245, 320, 30);
        cmbRole.setBackground(Color.WHITE);
        loginBox.add(cmbRole);

        // --- Buttons (Position shifted down) ---
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(0, 153, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBounds(40, 310, 150, 40); // Y moved down
        loginBox.add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(0, 204, 102));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBounds(210, 310, 150, 40); // Y moved down
        loginBox.add(btnRegister);

        backgroundPanel.add(loginBox);

        // --- BUTTON ACTIONS ---
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText();
            String pass = new String(txtPass.getPassword());
            String selectedRole = cmbRole.getSelectedItem().toString();

            // 1. Check Database for Email & Password
            User user = userService.loginUser(email, pass);
            
            if (user != null) {
                // 2. Check if Role Matches
                // Database mein "User" save hota hai, UI mein "Customer" hai.
                // Isliye hum map kar rahe hain:
                
                String dbRole = user.getRole(); // Database se role (User/Admin)
                
                boolean roleMatches = false;
                
                if (selectedRole.equals("Admin") && dbRole.equalsIgnoreCase("Admin")) {
                    roleMatches = true;
                } 
                else if (selectedRole.equals("Customer") && 
                        (dbRole.equalsIgnoreCase("User") || dbRole.equalsIgnoreCase("Customer"))) {
                    roleMatches = true;
                }

                if (roleMatches) {
                    // LOGIN SUCCESS
                    this.dispose(); 
                    if (selectedRole.equals("Admin")) {
                        new AdminDashboard(userService, turfService, bookingService, email).setVisible(true);
                    } else {
                        // Customer Dashboard (Jab banega tab yahan link karna)
                        JOptionPane.showMessageDialog(this, "Customer Dashboard Coming Soon!");
                        // new CustomerDashboard(...).setVisible(true);
                    }
                } else {
                    // ROLE MISMATCH ERROR
                    JOptionPane.showMessageDialog(this, 
                        "Wrong Role Selected! This account is registered as " + dbRole, 
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email or Password!");
            }
        });

        btnRegister.addActionListener(e -> {
            this.dispose();
            new RegisterFrame(userService, turfService, bookingService).setVisible(true);
        });
    }
}