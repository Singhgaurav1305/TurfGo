package com.turf.management;

import com.turf.management.service.BookingService; // Import add karein
import com.turf.management.service.TurfService;
import com.turf.management.service.UserService;
import com.turf.management.view.LoginFrame;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import javax.swing.*;
import java.awt.*;

// Note: Agar aap Spring Boot use kar rahe hain toh Annotations rakhein
@org.springframework.boot.autoconfigure.SpringBootApplication
public class TurfManagement {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(TurfManagement.class)
                .headless(false)
                .run(args);

        EventQueue.invokeLater(() -> {
            // 1. Services Get Karein
            UserService userService = context.getBean(UserService.class);
            TurfService turfService = context.getBean(TurfService.class);
            BookingService bookingService = context.getBean(BookingService.class); // NEW: Get BookingService

            // 2. LoginFrame mein teeno services pass karein
            LoginFrame loginFrame = new LoginFrame(userService, turfService, bookingService);
            loginFrame.setVisible(true);
        });
    }
}