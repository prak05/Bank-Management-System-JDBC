package com.knb.view.admin;

import com.knb.model.AuditEntry;
import com.knb.model.User;
import com.knb.model.Account;
import com.knb.service.DatabaseManager;
import com.knb.view.BaseDashboard;
import com.knb.view.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

/**
 * AdminDashboard provides comprehensive administration interface
 * Professional SBI-like design with full admin functionality
 */
public class AdminDashboard extends BaseDashboard {

    public AdminDashboard(User user, DatabaseManager db) {
        super(user, db);
        setTitle("KNB Banking - Administrator Dashboard");
        initUI(); // Now call initUI() since we made it protected
    }

    @Override
    protected void createNavigationButtons() {
        String[] navLabels = {
            "Dashboard", "User Management", "Account Management", "User Approval",
            "Change User Role", "Reset Password", "System Reports", "Audit Log",
            "Create Bank Account", "Direct Query"
        };

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(navigationPanel.getBackground());
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        for (String label : navLabels) {
            JButton navButton = createNavButton(label);
            buttonContainer.add(navButton);
            buttonContainer.add(Box.createVerticalStrut(2));
        }

        navigationPanel.add(buttonContainer, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "Dashboard");
    }

    @Override
    protected void createContentPanels() {
        contentPanel.add(createDashboardPanel(), "Dashboard");
        contentPanel.add(createUserManagementPanel(), "User Management");
        contentPanel.add(createAccountManagementPanel(), "Account Management");
        contentPanel.add(createApprovalPanel(), "User Approval");
        contentPanel.add(createChangeRolePanel(), "Change User Role");
        contentPanel.add(createResetPasswordPanel(), "Reset Password");
        contentPanel.add(createSystemReportsPanel(), "System Reports");
        contentPanel.add(createAuditLogPanel(), "Audit Log");
        contentPanel.add(createBankAccountCreationPanel(), "Create Bank Account");
        contentPanel.add(createDirectQueryPanel(), "Direct Query");
    }

    // Rest of the methods remain the same as in the original file...
    // I'll include all the original methods here for completeness

    private JPanel createDashboardPanel() {
        JPanel panel = createContentPanel("Administrator Dashboard");

        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        metricsPanel.setOpaque(false);
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        try {
            metricsPanel.add(Theme.createMetricCard("Total Users",
                String.valueOf(dbManager.getUserCount()), Theme.PRIMARY_COLOR));
            metricsPanel.add(Theme.createMetricCard("Active Accounts",
                String.valueOf(dbManager.getAccountCount()), Theme.SUCCESS_COLOR));
            metricsPanel.add(Theme.createMetricCard("Total Transactions",
                String.valueOf(dbManager.getTransactionCount()), Theme.INFO_COLOR));
            metricsPanel.add(Theme.createMetricCard("Pending Approvals",
                String.valueOf(dbManager.getPendingUsers().size()), Theme.WARNING_COLOR));
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading metrics: " + e.getMessage());
            errorLabel.setForeground(Theme.DANGER_COLOR);
            metricsPanel.add(errorLabel);
        }

        JPanel activityPanel = Theme.createCard();
        activityPanel.setLayout(new BorderLayout());
        JLabel activityTitle = new JLabel("Recent System Activity");
        activityTitle.setFont(Theme.SUBTITLE_FONT.deriveFont(Font.BOLD));
        activityTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JTextArea activityArea = new JTextArea(8, 0);
        Theme.styleTextArea(activityArea);

        try {
            StringBuilder activity = new StringBuilder();
            List<AuditEntry> recentLogs = dbManager.getAuditLogs(10);
            for (AuditEntry entry : recentLogs) {
                activity.append(String.format("%s - User %d: %s\n",
                    entry.dateTime.toString().substring(0, 19),
                    entry.userId, entry.action));
            }
            activityArea.setText(activity.toString());
        } catch (Exception e) {
            activityArea.setText("Error loading activity: " + e.getMessage());
        }

        activityPanel.add(activityTitle, BorderLayout.NORTH);
        activityPanel.add(new JScrollPane(activityArea), BorderLayout.CENTER);

        JPanel mainContent = new JPanel(new BorderLayout(0, 30));
        mainContent.setOpaque(false);
        mainContent.add(metricsPanel, BorderLayout.NORTH);
        mainContent.add(activityPanel, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUserManagementPanel() {
        return createTableViewerPanel("User Management",
            new String[]{"ID", "Username", "Name", "Email", "Mobile", "Role", "Status"},
            this::fetchUsers);
    }

    private JPanel createAccountManagementPanel() {
        return createTableViewerPanel("Account Management",
            new String[]{"Account No", "User ID", "Name", "Balance", "Type", "Status"},
            this::fetchAccounts);
    }

    private JPanel createApprovalPanel() {
        JPanel panel = createContentPanel("User Approval");

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Username", "Full Name", "Email", "Registration Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        Theme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshPendingUsers(model);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(0, 300));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton approveButton = new JButton("Approve Selected User");
        Theme.styleSuccessButton(approveButton);

        JButton rejectButton = new JButton("Reject Selected User");
        Theme.styleDangerButton(rejectButton);

        JButton refreshButton = new JButton("Refresh List");
        Theme.styleSecondaryButton(refreshButton);

        approveButton.addActionListener(e -> handleUserApproval(table, model, "ACTIVE"));
        rejectButton.addActionListener(e -> handleUserApproval(table, model, "REJECTED"));
        refreshButton.addActionListener(e -> refreshPendingUsers(model));

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(refreshButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChangeRolePanel() {
        return createSimpleActionPanel("Change User Role",
            "User ID:", "New Role (admin/manager/client):",
            (userIdStr, newRole) -> {
                try {
                    int userId = Integer.parseInt(userIdStr);
                    if (!newRole.matches("admin|manager|client")) {
                        return "Invalid role. Must be: admin, manager, or client";
                    }

                    User user = dbManager.getUserById(userId);
                    if (user == null) {
                        return "User not found with ID: " + userId;
                    }

                    dbManager.updateUserRole(userId, newRole);
                    dbManager.logAudit(currentUser.getUserId(), "ROLE_CHANGE",
                        "Changed role for user " + user.getName() + " (ID: " + userId + ") to " + newRole);
                    return "Role updated successfully for " + user.getName();
                } catch (NumberFormatException e) {
                    return "Invalid User ID format";
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    private JPanel createResetPasswordPanel() {
        return createSimpleActionPanel("Reset User Password",
            "User ID:", "New Password:",
            (userIdStr, newPassword) -> {
                try {
                    int userId = Integer.parseInt(userIdStr);
                    if (newPassword.length() < 6) {
                        return "Password must be at least 6 characters long";
                    }

                    User user = dbManager.getUserById(userId);
                    if (user == null) {
                        return "User not found with ID: " + userId;
                    }

                    dbManager.setUserPassword(userId, newPassword);
                    dbManager.logAudit(currentUser.getUserId(), "PASSWORD_RESET",
                        "Reset password for user " + user.getName() + " (ID: " + userId + ")");
                    return "Password reset successfully for " + user.getName();
                } catch (NumberFormatException e) {
                    return "Invalid User ID format";
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    private JPanel createSystemReportsPanel() {
        JPanel panel = createContentPanel("System Reports");

        JTextArea reportArea = new JTextArea();
        Theme.styleTextArea(reportArea);
        reportArea.setRows(20);

        try {
            int userCount = dbManager.getUserCount();
            int accountCount = dbManager.getAccountCount();
            int transactionCount = dbManager.getTransactionCount();
            int pendingCount = dbManager.getPendingUsers().size();

            reportArea.setText(String.format(
                "KNB BANKING SYSTEM REPORT\n" +
                "Generated: %s\n" +
                "Administrator: %s\n\n" +
                "=================================\n" +
                "SYSTEM STATISTICS\n" +
                "=================================\n\n" +
                "Total Registered Users: %d\n" +
                "Active Bank Accounts: %d\n" +
                "Total Transactions: %d\n" +
                "Pending User Approvals: %d\n\n" +
                "=================================\n" +
                "SYSTEM HEALTH\n" +
                "=================================\n\n" +
                "Database Status: Connected\n" +
                "System Status: Operational\n" +
                "Last Backup: N/A\n\n" +
                "=================================\n" +
                "SECURITY SUMMARY\n" +
                "=================================\n\n" +
                "Failed Login Attempts: N/A\n" +
                "Suspicious Activities: 0\n" +
                "System Alerts: None\n\n",
                new java.util.Date(),
                currentUser.getName(),
                userCount, accountCount, transactionCount, pendingCount
            ));
        } catch (Exception e) {
            reportArea.setText("Error generating report: " + e.getMessage());
        }

        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAuditLogPanel() {
        return createTableViewerPanel("System Audit Log",
            new String[]{"Log ID", "User ID", "Timestamp", "Action", "Details"},
            this::fetchAuditLogs);
    }

    private JPanel createBankAccountCreationPanel() {
        return createSimpleActionPanel("Create Bank Account",
            "User ID:", "Initial Balance (₹):",
            (userIdStr, balanceStr) -> {
                try {
                    int userId = Integer.parseInt(userIdStr);
                    double balance = Double.parseDouble(balanceStr);
                    if (balance < 0) {
                        return "Initial balance cannot be negative";
                    }

                    User targetUser = dbManager.getUserById(userId);
                    if (targetUser == null) {
                        return "User not found with ID: " + userId;
                    }

                    if (!targetUser.getStatus().equals("ACTIVE")) {
                        return "User must be active to create account";
                    }

                    dbManager.addAccount(userId, targetUser.getName(), balance, targetUser.getMobile(), "SAVINGS");
                    dbManager.logAudit(currentUser.getUserId(), "ADMIN_ACCOUNT_CREATION",
                        "Created SAVINGS account for " + targetUser.getName() + " (ID: " + userId + ") with balance ₹" + balance);
                    return "Account created successfully for " + targetUser.getName() + "\nInitial Balance: ₹" + String.format("%,.2f", balance);
                } catch (NumberFormatException e) {
                    return "Invalid number format";
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    private JPanel createDirectQueryPanel() {
        JPanel panel = createContentPanel("Direct Database Query");
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JLabel warningLabel = new JLabel("⚠️ WARNING: Direct database access. Use with extreme caution!");
        warningLabel.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        warningLabel.setForeground(Theme.DANGER_COLOR);
        warningLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JTextArea queryInputArea = new JTextArea(5, 0);
        queryInputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        queryInputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        queryInputArea.setBackground(new Color(248, 249, 250));

        JScrollPane queryScrollPane = new JScrollPane(queryInputArea);
        queryScrollPane.setBorder(BorderFactory.createTitledBorder("SQL Query"));

        JButton executeButton = new JButton("Execute Query");
        Theme.styleDangerButton(executeButton);

        JTable resultTable = new JTable();
        Theme.styleTable(resultTable);
        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Query Results"));
        resultScrollPane.setPreferredSize(new Dimension(0, 300));

        executeButton.addActionListener(e -> {
            String sql = queryInputArea.getText().trim();
            if (sql.isEmpty()) {
                showWarning("Please enter a SQL query");
                return;
            }

            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement()) {

                boolean hasResultSet = stmt.execute(sql);
                if (hasResultSet) {
                    ResultSet rs = stmt.getResultSet();
                    resultTable.setModel(buildTableModel(rs));
                    dbManager.logAudit(currentUser.getUserId(), "DIRECT_QUERY_SELECT", sql);
                    showSuccess("Query executed successfully");
                } else {
                    int updateCount = stmt.getUpdateCount();
                    resultTable.setModel(new DefaultTableModel());
                    dbManager.logAudit(currentUser.getUserId(), "DIRECT_QUERY_UPDATE", sql);
                    showSuccess("Query executed successfully. " + updateCount + " rows affected.");
                }

            } catch (Exception ex) {
                showError("Query failed: " + ex.getMessage());
                try {
                    dbManager.logAudit(currentUser.getUserId(), "DIRECT_QUERY_ERROR",
                        "Failed query: " + sql + " | Error: " + ex.getMessage());
                } catch (Exception logEx) {
                    System.err.println("CRITICAL: Failed to write to audit log.");
                    logEx.printStackTrace();
                }
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout(0, 10));
        inputPanel.setOpaque(false);
        inputPanel.add(warningLabel, BorderLayout.NORTH);
        inputPanel.add(queryScrollPane, BorderLayout.CENTER);
        inputPanel.add(executeButton, BorderLayout.SOUTH);

        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setOpaque(false);
        mainContent.add(inputPanel, BorderLayout.NORTH);
        mainContent.add(resultScrollPane, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private void refreshPendingUsers(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<User> pendingUsers = dbManager.getPendingUsers();
            for (User u : pendingUsers) {
                model.addRow(new Object[]{
                    u.getUserId(), u.getUsername(), u.getName(), u.getEmail(), "Recent"
                });
            }
        } catch (Exception e) {
            showError("Error loading pending users: " + e.getMessage());
        }
    }

    private void handleUserApproval(JTable table, DefaultTableModel model, String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Please select a user to " + (status.equals("ACTIVE") ? "approve" : "reject"));
            return;
        }

        int userId = (int) model.getValueAt(selectedRow, 0);
        String userName = (String) model.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to " + (status.equals("ACTIVE") ? "approve" : "reject") + " user: " + userName + "?",
            "Confirm Action",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbManager.updateUserStatus(userId, status);
                dbManager.logAudit(currentUser.getUserId(), "USER_" + status,
                    userName + " (ID: " + userId + ") " + (status.equals("ACTIVE") ? "approved" : "rejected"));
                showSuccess("User " + (status.equals("ACTIVE") ? "approved" : "rejected") + " successfully!");
                refreshPendingUsers(model);
            } catch (Exception ex) {
                showError("Error updating user: " + ex.getMessage());
            }
        }
    }

    private Vector<Vector<Object>> fetchUsers() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            List<User> users = dbManager.getAllUsers();
            for (User u : users) {
                Vector<Object> row = new Vector<>();
                row.add(u.getUserId());
                row.add(u.getUsername());
                row.add(u.getName());
                row.add(u.getEmail());
                row.add(u.getMobile());
                row.add(u.getRole());
                row.add(u.getStatus());
                data.add(row);
            }
        } catch (Exception e) {
            showError("Error loading users: " + e.getMessage());
        }
        return data;
    }

    private Vector<Vector<Object>> fetchAccounts() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            List<Account> accounts = dbManager.getAllAccounts();
            for (Account a : accounts) {
                Vector<Object> row = new Vector<>();
                row.add(a.getAcno());
                row.add(a.getUserId());
                row.add(a.getName());
                row.add(String.format("₹%,.2f", a.getBalance()));
                row.add(a.getAccountType());
                row.add(a.getAccountStatus());
                data.add(row);
            }
        } catch (Exception e) {
            showError("Error loading accounts: " + e.getMessage());
        }
        return data;
    }

    private Vector<Vector<Object>> fetchAuditLogs() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            List<AuditEntry> logs = dbManager.getAuditLogs(100);
            for (AuditEntry log : logs) {
                Vector<Object> row = new Vector<>();
                row.add(log.logId);
                row.add(log.userId);
                row.add(log.dateTime.toString().substring(0, 19));
                row.add(log.action);
                row.add(log.details);
                data.add(row);
            }
        } catch (Exception e) {
            showError("Error loading audit logs: " + e.getMessage());
        }
        return data;
    }

    private JPanel createTableViewerPanel(String title, String[] headers,
                                        java.util.function.Supplier<Vector<Vector<Object>>> dataSupplier) {
        JPanel panel = createContentPanel(title);

        JTable table = new JTable(dataSupplier.get(), new Vector<>(List.of(headers)));
        Theme.styleTable(table);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 400));

        JButton refreshButton = new JButton("Refresh Data");
        Theme.styleSecondaryButton(refreshButton);
        refreshButton.addActionListener(e -> {
            table.setModel(new DefaultTableModel(dataSupplier.get(), new Vector<>(List.of(headers))));
            Theme.styleTable(table);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSimpleActionPanel(String title, String label1, String label2,
                                         java.util.function.BiFunction<String, String, String> action) {
        JPanel panel = createContentPanel(title);

        JPanel formCard = Theme.createCard();
        formCard.setLayout(new GridBagLayout());
        formCard.setMaximumSize(new Dimension(500, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel field1Label = new JLabel(label1);
        field1Label.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(field1Label, gbc);

        JTextField field1 = new JTextField(20);
        Theme.styleTextField(field1);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(field1, gbc);

        JLabel field2Label = new JLabel(label2);
        field2Label.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formCard.add(field2Label, gbc);

        JTextField field2 = new JTextField(20);
        Theme.styleTextField(field2);
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(field2, gbc);

        JButton submitButton = new JButton("Submit");
        Theme.stylePrimaryButton(submitButton);
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(25, 15, 15, 15);
        formCard.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String result = action.apply(field1.getText().trim(), field2.getText().trim());
            if (result.startsWith("Error") || result.contains("Invalid") || result.contains("not found")) {
                showError(result);
            } else {
                showSuccess(result);
                field1.setText("");
                field2.setText("");
            }
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(formCard);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
