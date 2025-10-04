package com.knb.view;

import javax.swing.*;
import java.awt.*;
import com.knb.model.User;
import com.knb.service.AuthenticationService;
import com.knb.service.DatabaseManager;
import com.knb.service.TransactionManager;
import com.knb.view.admin.AdminDashboard;
import com.knb.view.client.ClientDashboard;
import com.knb.view.manager.ManagerDashboard;

/**
 * LoginUI provides professional authentication interface
 * SBI-like styling with role-based access control
 */
public class LoginUI extends JFrame {

    private final AuthenticationService authService;
    private final String expectedRole;

    public LoginUI(AuthenticationService authService) {
        this(authService, "client"); // Default to client
    }

    public LoginUI(AuthenticationService authService, String role) {
        this.authService = authService;
        this.expectedRole = role;
        initialize();
    }

    private void initialize() {
        String titleRole = expectedRole != null ? " - " + capitalize(expectedRole) + " Login" : "";
        setTitle("KNB Banking" + titleRole);
        setSize(500, 600);
        setMinimumSize(new Dimension(450, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);

        // Create main content
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLoginPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 120));

        // Logo section
        JPanel logoSection = new JPanel();
        logoSection.setLayout(new BoxLayout(logoSection, BoxLayout.Y_AXIS));
        logoSection.setOpaque(false);
        logoSection.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        JLabel logoLabel = new JLabel("KNB");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel(capitalize(expectedRole) + " Login");
        roleLabel.setFont(Theme.SUBTITLE_FONT);
        roleLabel.setForeground(new Color(255, 255, 255, 200));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoSection.add(logoLabel);
        logoSection.add(Box.createVerticalStrut(5));
        logoSection.add(roleLabel);

        headerPanel.add(logoSection, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createLoginPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Login card
        JPanel loginCard = new JPanel();
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        loginCard.setLayout(new GridBagLayout());
        loginCard.setMaximumSize(new Dimension(400, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(Theme.TITLE_FONT);
        welcomeLabel.setForeground(Theme.TEXT_COLOR);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginCard.add(welcomeLabel, gbc);

        JLabel instructionLabel = new JLabel("Please sign in to your account");
        instructionLabel.setFont(Theme.LABEL_FONT);
        instructionLabel.setForeground(Theme.LIGHT_TEXT_COLOR);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 25, 10);
        loginCard.add(instructionLabel, gbc);

        // Username field
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        loginCard.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        Theme.styleTextField(usernameField);
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1;
        gbc.gridwidth = 2;
        loginCard.add(usernameField, gbc);

        // Password field
        gbc.insets = new Insets(15, 10, 5, 10);
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        loginCard.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        Theme.stylePasswordField(passwordField); // Use the new method
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 1;
        gbc.gridwidth = 2;
        loginCard.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Sign In");
        Theme.stylePrimaryButton(loginButton);
        loginButton.setPreferredSize(new Dimension(0, 45));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 15, 10);
        loginCard.add(loginButton, gbc);

        // Register button (only for clients)
        if ("client".equalsIgnoreCase(expectedRole)) {
            JButton registerButton = new JButton("Create New Account");
            Theme.styleSecondaryButton(registerButton);
            registerButton.setPreferredSize(new Dimension(0, 40));
            gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
            gbc.insets = new Insets(5, 10, 10, 10);
            loginCard.add(registerButton, gbc);

            registerButton.addActionListener(e -> {
                this.dispose();
                new RegistrationUI(authService).setVisible(true);
            });
        }

        // Event handlers
        loginButton.addActionListener(e -> handleLogin(usernameField, passwordField));

        // Enter key support
        getRootPane().setDefaultButton(loginButton);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(loginCard);
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(248, 249, 250));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        footer.setPreferredSize(new Dimension(0, 50));

        JButton backButton = new JButton("← Back to Welcome");
        Theme.styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            this.dispose();
            new WelcomeUI(authService).setVisible(true);
        });

        footer.add(backButton);
        return footer;
    }

    private void handleLogin(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        User user;
        try {
            user = authService.authenticate(username, password);
        } catch (Exception ex) {
            showError("Authentication error: " + ex.getMessage());
            return;
        }

        if (user == null) {
            showError("Invalid username or password.");
            return;
        }

        if (!expectedRole.equalsIgnoreCase(user.getRole())) {
            showError("You are not authorized to login as " + capitalize(expectedRole) + ".");
            return;
        }

        // Successful login
        this.dispose();
        DatabaseManager db = new DatabaseManager();

        switch (user.getRole().toLowerCase()) {
            case "admin":
                new AdminDashboard(user, db).setVisible(true);
                break;
            case "manager":
                new ManagerDashboard(user, db).setVisible(true);
                break;
            case "client":
                new ClientDashboard(user, db, new TransactionManager(db)).setVisible(true);
                break;
            default:
                showError("Unknown user role. Access denied.");
                new WelcomeUI(authService).setVisible(true);
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
}
