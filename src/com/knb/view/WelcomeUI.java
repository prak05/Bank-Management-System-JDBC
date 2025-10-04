package com.knb.view;

import com.knb.service.AuthenticationService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * WelcomeUI provides the main entry point for the banking application
 * Professional SBI-like welcome screen with role-based access
 */
public class WelcomeUI extends JFrame {
    private final AuthenticationService authService;

    public WelcomeUI(AuthenticationService authService) {
        this.authService = authService;
        initUI();
    }

    private void initUI() {
        setTitle("KNB Banking - Welcome");
        setSize(1000, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Set background
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);
        
        // Header Panel
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = createMainContent();
        add(mainPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(0, 120));
        
        // Bank branding
        JPanel brandingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        brandingPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("KNB");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 10));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 20, 0));
        
        JLabel bankName = new JLabel("Banking Solutions");
        bankName.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        bankName.setForeground(Color.WHITE);
        
        JLabel tagline = new JLabel("Your Trusted Financial Partner");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tagline.setForeground(new Color(255, 255, 255, 180));
        
        textPanel.add(bankName);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(tagline);
        
        brandingPanel.add(logoLabel);
        brandingPanel.add(textPanel);
        
        header.add(brandingPanel, BorderLayout.WEST);
        
        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Welcome message
        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        JLabel welcomeLabel = new JLabel("Welcome to KNB Banking Portal");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Theme.TEXT_COLOR);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Please select your access level to continue");
        subtitleLabel.setFont(Theme.SUBTITLE_FONT);
        subtitleLabel.setForeground(Theme.LIGHT_TEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(subtitleLabel);
        
        mainPanel.add(welcomePanel);
        
        // Access panels
        JPanel accessPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        accessPanel.setOpaque(false);
        accessPanel.setMaximumSize(new Dimension(800, 300));
        
        // Personal Banking Panel
        accessPanel.add(createAccessCard(
            "Personal Banking",
            "Access your personal accounts, transfer funds, view statements and manage your finances securely.",
            "Access Personal Banking",
            "client",
            Theme.SUCCESS_COLOR
        ));
        
        // Corporate & Staff Panel
        accessPanel.add(createAccessCard(
            "Corporate & Staff Portal",
            "Administrative and management portal for authorized bank personnel and corporate clients.",
            "Staff & Corporate Access",
            "staff",
            Theme.INFO_COLOR
        ));
        
        mainPanel.add(accessPanel);
        
        return mainPanel;
    }

    private JPanel createAccessCard(String title, String description, String buttonText, String accessType, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 25, 30, 25)
        ));
        
        // Icon panel
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(80, 80));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(accentColor);
        iconLabel.setBorder(BorderFactory.createEmptyBorder());
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setText(accessType.equals("client") ? "💳" : "🏢");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(Color.WHITE);
        
        iconPanel.add(iconLabel);
        card.add(iconPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Theme.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(Theme.LABEL_FONT);
        descArea.setForeground(Theme.LIGHT_TEXT_COLOR);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(descArea);
        card.add(contentPanel, BorderLayout.CENTER);
        
        // === BUTTON FIX STARTS HERE ===
        JButton accessButton = new JButton(buttonText);
        Theme.styleSecondaryButton(accessButton); // Using the global theme style
        
        accessButton.addActionListener(e -> {
            setVisible(false);
            if ("staff".equals(accessType)) {
                // Show role selection dialog
                String[] options = {"Admin", "Manager"};
                int choice = JOptionPane.showOptionDialog(
                    this,
                    "Select your staff role:",
                    "Staff Login",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
                );
                
                if (choice == 0) { // Admin
                    new LoginUI(authService, "admin").setVisible(true);
                } else if (choice == 1) { // Manager
                    new LoginUI(authService, "manager").setVisible(true);
                } else {
                    setVisible(true); // Show welcome screen again
                }
            } else { // Client
                new LoginUI(authService, accessType).setVisible(true);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(accessButton);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(248, 249, 250));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        footer.setPreferredSize(new Dimension(0, 60));
        
        JLabel copyrightLabel = new JLabel("© 2024 KNB Banking Solutions. All rights reserved.");
        copyrightLabel.setFont(Theme.LABEL_FONT);
        copyrightLabel.setForeground(Theme.LIGHT_TEXT_COLOR);
        
        footer.add(copyrightLabel);
        
        return footer;
    }
}

