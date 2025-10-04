package com.knb.view;

import com.knb.service.AuthenticationService;
import javax.swing.*;
import java.awt.*;

/**
 * RegistrationUI provides professional user registration interface
 * Professional SBI-like styling for new account creation
 */
public class RegistrationUI extends JFrame {

    private final AuthenticationService authService;

    public RegistrationUI(AuthenticationService authService) {
        this.authService = authService;
        initUI();
    }

    private void initUI() {
        setTitle("KNB Banking - New Account Registration");
        setSize(550, 700);
        setMinimumSize(new Dimension(500, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createRegistrationPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 100));

        JPanel logoSection = new JPanel();
        logoSection.setLayout(new BoxLayout(logoSection, BoxLayout.Y_AXIS));
        logoSection.setOpaque(false);
        logoSection.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel logoLabel = new JLabel("KNB Banking");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("New Account Registration");
        subtitleLabel.setFont(Theme.SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoSection.add(logoLabel);
        logoSection.add(Box.createVerticalStrut(5));
        logoSection.add(subtitleLabel);

        headerPanel.add(logoSection, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createRegistrationPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Registration card
        JPanel registrationCard = new JPanel();
        registrationCard.setBackground(Color.WHITE);
        registrationCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        registrationCard.setLayout(new GridBagLayout());
        registrationCard.setMaximumSize(new Dimension(450, 550));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        registrationCard.add(titleLabel, gbc);

        // Form fields
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 3, 10);

        // Username
        addFormLabel(registrationCard, "Username *", 0, 1, gbc);
        JTextField usernameField = addFormField(registrationCard, 0, 2, gbc);

        // Password
        addFormLabel(registrationCard, "Password *", 0, 3, gbc);
        JPasswordField passwordField = addPasswordField(registrationCard, 0, 4, gbc);

        // Full Name
        addFormLabel(registrationCard, "Full Name *", 0, 5, gbc);
        JTextField nameField = addFormField(registrationCard, 0, 6, gbc);

        // Email
        addFormLabel(registrationCard, "Email Address", 0, 7, gbc);
        JTextField emailField = addFormField(registrationCard, 0, 8, gbc);

        // Mobile Number
        addFormLabel(registrationCard, "Mobile Number", 0, 9, gbc);
        JTextField mobileField = addFormField(registrationCard, 0, 10, gbc);

        // Register button
        JButton registerButton = new JButton("Create Account");
        Theme.stylePrimaryButton(registerButton);
        registerButton.setPreferredSize(new Dimension(0, 45));
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        registrationCard.add(registerButton, gbc);

        // Already have account link
        JButton loginLinkButton = new JButton("Already have an account? Sign In");
        loginLinkButton.setFont(Theme.LABEL_FONT);
        loginLinkButton.setForeground(Theme.SECONDARY_COLOR);
        loginLinkButton.setBorderPainted(false);
        loginLinkButton.setContentAreaFilled(false);
        loginLinkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLinkButton.addActionListener(e -> {
            this.dispose();
            new LoginUI(authService, "client").setVisible(true);
        });

        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 0, 10);
        registrationCard.add(loginLinkButton, gbc);

        // Register button action
        registerButton.addActionListener(e -> handleRegistration(
            usernameField, passwordField, nameField, emailField, mobileField
        ));

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(registrationCard);
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }

    private void addFormLabel(JPanel parent, String text, int x, int y, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        label.setForeground(Theme.TEXT_COLOR);
        gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = 2;
        parent.add(label, gbc);
    }

    private JTextField addFormField(JPanel parent, int x, int y, GridBagConstraints gbc) {
        JTextField textField = new JTextField(20);
        Theme.styleTextField(textField);
        gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 10, 8, 10);
        parent.add(textField, gbc);
        gbc.insets = new Insets(8, 10, 3, 10); // Reset insets
        return textField;
    }

    private JPasswordField addPasswordField(JPanel parent, int x, int y, GridBagConstraints gbc) {
        JPasswordField passwordField = new JPasswordField(20);
        Theme.stylePasswordField(passwordField); // Use the new method
        gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 10, 8, 10);
        parent.add(passwordField, gbc);
        gbc.insets = new Insets(8, 10, 3, 10); // Reset insets
        return passwordField;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(248, 249, 250));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        footer.setPreferredSize(new Dimension(0, 50));

        JButton backButton = new JButton("← Back to Login");
        Theme.styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            this.dispose();
            new LoginUI(authService, "client").setVisible(true);
        });

        footer.add(backButton);
        return footer;
    }

    private void handleRegistration(JTextField usernameField, JPasswordField passwordField,
                                  JTextField nameField, JTextField emailField, JTextField mobileField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String mobile = mobileField.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
            showError("Username, Password, and Full Name are required fields.");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }

        // Attempt registration
        boolean registered = authService.registerUser(username, password, name, email, mobile);

        if (registered) {
            JOptionPane.showMessageDialog(
                this,
                "Registration successful!\n\nYour account has been created and is pending approval.\n" +
                "An administrator will review your request and activate your account.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
            this.dispose();
            new LoginUI(authService, "client").setVisible(true);
        } else {
            showError("Registration failed.\n\nThe username may already exist or there was a database error.\nPlease try again with a different username.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }
}
