package com.turf.management.view;

import com.turf.management.model.Turf;
import com.turf.management.service.TurfService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ViewAllTurfsPanel extends JPanel {

    private TurfService turfService;
    private JTable table;
    private DefaultTableModel model;

    public ViewAllTurfsPanel(TurfService service) {
        this.turfService = service;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel lblTitle = new JLabel("All Turfs Gallery");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTitle, BorderLayout.NORTH);

        // Table Columns
        String[] columns = {"Sr. No.", "Image", "Name", "Type", "Price", "Location", "Status"};
        
        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) return ImageIcon.class; 
                return Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(80); 
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(80);

        loadData(); // Initial Load

        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton btnRefresh = new JButton("Refresh List");
        btnRefresh.addActionListener(e -> loadData());
        add(btnRefresh, BorderLayout.SOUTH);
    }

    // --- CHANGE: Method ko 'public' banaya taaki Dashboard isse call kar sake ---
    public void loadData() {
        model.setRowCount(0); 
        List<Turf> turfs = turfService.getAllTurfs();

        int srNo = 1; 

        for (Turf t : turfs) {
            ImageIcon imageIcon = null;
            try {
                String imgUrl = t.getImageUrl(); 
                if (imgUrl != null && imgUrl.length() > 0) { 
                    URL url = new URL(imgUrl);
                    BufferedImage img = ImageIO.read(url);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(80, 70, Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(scaled);
                    }
                }
            } catch (Exception e) { }

            model.addRow(new Object[]{
                srNo++,    
                imageIcon, 
                t.getName(),
                t.getType(),
                t.getPrice(),
                t.getLocation(),
                t.getStatus()
            });
        }
    }
}