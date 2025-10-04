package com.knb;

import com.knb.service.AuthenticationService;
import com.knb.service.DatabaseManager;
import com.knb.view.WelcomeUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class to launch the KNB Banking Application.
 * This is the entry point for the entire system.
 */
public class Main {

    public static void main(String[] args) {
        // Set the system look and feel for a native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel, using default.");
            e.printStackTrace();
        }

        // Launch the application's GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize core services
                DatabaseManager dbManager = new DatabaseManager();
                AuthenticationService authService = new AuthenticationService(dbManager);
                
                // Create and display the main welcome window
                new WelcomeUI(authService).setVisible(true);
            } catch (Exception e) {
                System.err.println("Fatal error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

