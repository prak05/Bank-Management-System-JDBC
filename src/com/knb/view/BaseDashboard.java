package com.knb.view;

import com.knb.model.User;
import com.knb.service.DatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * BaseDashboard provides common dashboard functionality
 * Implements professional SBI-like layout and styling
 */
public abstract class BaseDashboard extends JFrame {

    protected User currentUser;
    protected DatabaseManager dbManager;
    protected JPanel navigationPanel = new JPanel();
    protected JPanel contentPanel = new JPanel();
    protected CardLayout cardLayout = new CardLayout();

    public BaseDashboard(User user, DatabaseManager db) {
        this.currentUser = user;
        this.dbManager = db;
        // Don't call initUI() here - let child classes control when to initialize
    }

    // Made this protected so child classes can call it when ready
    protected void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen by default
        setupLayout();
        createContentPanels();
        createNavigationButtons();
        setVisible(true);
    }

    protected void setupLayout() {
        // Main layout
        setLayout(new BorderLayout());

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Main content area
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(2);
        splitPane.setBorder(null);

        // Navigation panel
        setupNavigationPanel();
        splitPane.setLeftComponent(navigationPanel);

        // Content panel
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Theme.BACKGROUND_COLOR);
        splitPane.setRightComponent(contentPanel);

        add(splitPane, BorderLayout.CENTER);

        // Footer
        add(createFooter(), BorderLayout.SOUTH);
    }

    protected void setupNavigationPanel() {
        navigationPanel.setLayout(new BorderLayout());
        navigationPanel.setBackground(Theme.NAV_BACKGROUND);
        navigationPanel.setPreferredSize(new Dimension(280, getHeight()));
        navigationPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        // User info panel
        JPanel userInfoPanel = createUserInfoPanel();
        navigationPanel.add(userInfoPanel, BorderLayout.NORTH);
    }

    protected JPanel createUserInfoPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(Theme.PRIMARY_COLOR);
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // User avatar (placeholder)
        JLabel avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(60, 60));
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(new Color(255, 255, 255, 50));
        avatarLabel.setBorder(BorderFactory.createEmptyBorder());
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setText(String.valueOf(currentUser.getName().charAt(0)).toUpperCase());
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        avatarLabel.setForeground(Color.WHITE);

        // User details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setFont(Theme.SUBTITLE_FONT.deriveFont(Font.BOLD));
        nameLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel(currentUser.getRole().toUpperCase());
        roleLabel.setFont(Theme.LABEL_FONT);
        roleLabel.setForeground(new Color(255, 255, 255, 180));

        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(roleLabel);

        userPanel.add(avatarLabel, BorderLayout.WEST);
        userPanel.add(detailsPanel, BorderLayout.CENTER);

        return userPanel;
    }

    protected JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));

        // Bank logo and name
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        logoPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("KNB");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 10));

        JLabel taglineLabel = new JLabel("Banking Solutions");
        taglineLabel.setFont(Theme.LABEL_FONT);
        taglineLabel.setForeground(new Color(255, 255, 255, 180));
        taglineLabel.setBorder(BorderFactory.createEmptyBorder(23, 0, 15, 0));

        logoPanel.add(logoLabel);
        logoPanel.add(taglineLabel);

        // System info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        infoPanel.setOpaque(false);

        JLabel timeLabel = new JLabel(new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm").format(new java.util.Date()));
        timeLabel.setFont(Theme.LABEL_FONT);
        timeLabel.setForeground(new Color(255, 255, 255, 180));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(23, 0, 15, 0));

        infoPanel.add(timeLabel);

        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(infoPanel, BorderLayout.EAST);

        return headerPanel;
    }

    protected JPanel createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setPreferredSize(new Dimension(0, 50));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        // Left side - status
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(Theme.LABEL_FONT);
        statusLabel.setForeground(Theme.SUCCESS_COLOR);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Right side - logout
        JButton logoutButton = new JButton("Logout");
        Theme.styleDangerButton(logoutButton);
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        leftPanel.setOpaque(false);
        leftPanel.add(statusLabel);

        footerPanel.add(leftPanel, BorderLayout.WEST);
        footerPanel.add(buttonPanel, BorderLayout.EAST);

        return footerPanel;
    }

    protected JButton createNavButton(String label) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, label);
            // Update button selection visual feedback could be added here
        });
        Theme.styleNavButton(button);
        return button;
    }

    protected abstract void createNavigationButtons();
    protected abstract void createContentPanels();

    /**
     * Create a professional content panel with title
     */
    protected JPanel createContentPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title bar
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_COLOR);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        panel.add(titlePanel, BorderLayout.NORTH);

        return panel;
    }

    /**
     * Show success message
     */
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show error message
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show warning message
     */
    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
