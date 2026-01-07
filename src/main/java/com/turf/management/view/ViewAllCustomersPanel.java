package com.turf.management.view;

import com.turf.management.model.User;
import com.turf.management.service.UserService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewAllCustomersPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private UserService userService;

    // Constructor mein Service pass karenge
    public ViewAllCustomersPanel(UserService userService) {
        this.userService = userService;
        setLayout(new BorderLayout());

        // 1. Table Setup
        String[] columns = {"Customer ID", "Full Name", "Email", "Phone Number", "Role"};
        model = new DefaultTableModel(columns, 0);
        
        table = new JTable(model);
        table.setRowHeight(30); // Row height thoda badhaya taki clear dikhe
        
        // 2. Data Load karna
        loadCustomerData();

        // 3. ScrollPane mein add karna (Header dikhne ke liye zaruri hai)
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadCustomerData() {
        model.setRowCount(0); // Purana data clear karein
        
        // Service se sare users mangwayein
        List<User> users = userService.getAllUsers(); 

        for (User u : users) {
            // Sirf 'USER' role walo ko dikhana hai (Admin ko nahi)
            // Agar aapke database mein role "CUSTOMER" hai toh wo check karein
            if ("USER".equalsIgnoreCase(u.getRole())) { 
                Object[] row = {
                    u.getId(),
                    u.getName(),
                    u.getEmail(),
                    u.getPhone(),
                    u.getRole()
                };
                model.addRow(row);
            }
        }
    }
}