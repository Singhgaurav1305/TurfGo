package com.turf.management.view;

import com.turf.management.model.Booking;
import com.turf.management.service.BookingService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewBookingPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private BookingService bookingService;
    private AdminDashboard dashboard; // Reference to update analytics

    public ViewBookingPanel(BookingService service, AdminDashboard dashboard) {
        this.bookingService = service;
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        
        // Table Columns
        String[] columns = {"ID", "Turf Name", "Customer", "Contact", "Date", "Slot", "Amount", "Status", "Action"};
        
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only Button column is editable
            }
        };

        table = new JTable(model);
        table.setRowHeight(40); // Row height badhaya taki clear dikhe
        
        // Set Custom Renderer & Editor for Button
        table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox()));

        loadBookingData();

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadBookingData() {
        model.setRowCount(0); // Clear old data
        List<Booking> bookings = bookingService.getAllBookings();

        for (Booking b : bookings) {
            Object[] row = {
                b.getId(),
                b.getTurf().getName(), // Turf Name
                b.getUser().getName(), // Customer Name
                b.getUser().getPhone(), // Contact
                b.getBookingDate(),
                b.getTimeSlot(),
                b.getTotalAmount(),
                b.getStatus(),
                "Verify" // Button Label
            };
            model.addRow(row);
        }
    }

    // --- Inner Class for Button Look ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            String status = (String) table.getValueAt(row, 7);
            
            if ("Confirmed".equals(status)) {
                setBackground(Color.GREEN);
                setText("Verified");
                setEnabled(false);
            } else {
                setBackground(Color.ORANGE);
                setEnabled(true);
            }
            return this;
        }
    }

    // --- Inner Class for Button Action ---
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            currentRow = row;
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // VERIFY LOGIC HERE
                Long bookingId = (Long) table.getValueAt(currentRow, 0);
                bookingService.verifyBooking(bookingId); // DB Update
                
                // Refresh Table
                loadBookingData();
                
                // Refresh Home Analytics
                dashboard.updateAnalytics(); 
                JOptionPane.showMessageDialog(button, "Booking Verified!");
            }
            isPushed = false;
            return label;
        }
    }
}