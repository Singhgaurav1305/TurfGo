package com.turf.management.view;

import javax.swing.*;
import java.awt.*;

// Ye class Image ko smooth stretch karne ka kaam karegi
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            // Image load karo
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
            System.err.println("Image nahi mili! Path check karo: " + imagePath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // MAGIC LINE: Ye image ko window ke barabar faila dega
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}