package com.knb.view.manager;

import com.knb.model.User;
import com.knb.model.Account;
import com.knb.service.DatabaseManager;
import com.knb.view.BaseDashboard;
import com.knb.view.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * ManagerDashboard provides comprehensive client management interface
 * Professional SBI-like design with client and account management capabilities
 */
public class ManagerDashboard extends BaseDashboard {

    private DefaultTableModel clientTableModel;

    public ManagerDashboard(User user, DatabaseManager db) {
        super(user, db);
        setTitle("KNB Banking - Manager Dashboard");
        initUI(); // Now call initUI() since we made it protected
    }

    @Override
    protected void createNavigationButtons() {
        String[] navLabels = {
            "Dashboard", "Client Management", "Account Management", "Create Account",
            "Client Reports", "Account Reports"
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
        // Show dashboard by default
        cardLayout.show(contentPanel, "Dashboard");
    }

    @Override
    protected void createContentPanels() {
        contentPanel.add(createDashboardPanel(), "Dashboard");
        contentPanel.add(createClientManagementPanel(), "Client Management");
        contentPanel.add(createAccountManagementPanel(), "Account Management");
        contentPanel.add(createAccountCreationPanel(), "Create Account");
        contentPanel.add(createClientReportsPanel(), "Client Reports");
        contentPanel.add(createAccountReportsPanel(), "Account Reports");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = createContentPanel("Manager Dashboard");

        // Metrics section
        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        metricsPanel.setOpaque(false);
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        try {
            List<User> clients = dbManager.getUsersByRole("client");
            List<Account> accounts = dbManager.getAllAccounts();
            double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
            int activeAccounts = (int) accounts.stream().filter(a -> "ACTIVE".equals(a.getAccountStatus())).count();

            metricsPanel.add(Theme.createMetricCard("Total Clients",
                String.valueOf(clients.size()), Theme.PRIMARY_COLOR));
            metricsPanel.add(Theme.createMetricCard("Active Accounts",
                String.valueOf(activeAccounts), Theme.SUCCESS_COLOR));
            metricsPanel.add(Theme.createMetricCard("Total Balance",
                String.format("₹%,.0f", totalBalance), Theme.INFO_COLOR));
            metricsPanel.add(Theme.createMetricCard("Accounts w/o Link",
                String.valueOf(clients.size() - accounts.size()), Theme.WARNING_COLOR));
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading metrics: " + e.getMessage());
            errorLabel.setForeground(Theme.DANGER_COLOR);
            metricsPanel.add(errorLabel);
        }

        // Client overview section
        JPanel overviewPanel = Theme.createCard();
        overviewPanel.setLayout(new BorderLayout());
        JLabel overviewTitle = new JLabel("Client Overview");
        overviewTitle.setFont(Theme.SUBTITLE_FONT.deriveFont(Font.BOLD));
        overviewTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JTextArea overviewArea = new JTextArea(8, 0);
        Theme.styleTextArea(overviewArea);

        try {
            StringBuilder overview = new StringBuilder();
            overview.append("CLIENT SUMMARY\n");
            overview.append("==============\n\n");

            List<User> clients = dbManager.getUsersByRole("client");
            overview.append(String.format("Total Active Clients: %d\n", clients.size()));

            int accountedClients = 0;
            int unaccountedClients = 0;

            for (User client : clients) {
                Account account = dbManager.getAccountByUserId(client.getUserId());
                if (account != null) {
                    accountedClients++;
                } else {
                    unaccountedClients++;
                }
            }

            overview.append(String.format("Clients with Bank Accounts: %d\n", accountedClients));
            overview.append(String.format("Clients without Bank Accounts: %d\n", unaccountedClients));
            overview.append("\n");
            overview.append("RECENT ACTIVITY\n");
            overview.append("===============\n");
            overview.append("New accounts this month: N/A\n");
            overview.append("Recent transactions: N/A\n");

            overviewArea.setText(overview.toString());
        } catch (Exception e) {
            overviewArea.setText("Error loading overview: " + e.getMessage());
        }

        overviewPanel.add(overviewTitle, BorderLayout.NORTH);
        overviewPanel.add(new JScrollPane(overviewArea), BorderLayout.CENTER);

        // Layout main dashboard
        JPanel mainContent = new JPanel(new BorderLayout(0, 30));
        mainContent.setOpaque(false);
        mainContent.add(metricsPanel, BorderLayout.NORTH);
        mainContent.add(overviewPanel, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClientManagementPanel() {
        JPanel panel = createContentPanel("Client Management");

        // Table setup
        String[] columnNames = {"User ID", "Username", "Full Name", "Email", "Mobile", "Status", "Account Status"};
        clientTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable clientTable = new JTable(clientTableModel);
        Theme.styleTable(clientTable);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshClientTable();

        JScrollPane scrollPane = new JScrollPane(clientTable);
        scrollPane.setPreferredSize(new Dimension(0, 400));

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton refreshButton = new JButton("Refresh Data");
        Theme.styleSecondaryButton(refreshButton);

        JButton viewDetailsButton = new JButton("View Client Details");
        Theme.stylePrimaryButton(viewDetailsButton);

        JButton createAccountButton = new JButton("Create Bank Account");
        Theme.styleSuccessButton(createAccountButton);

        refreshButton.addActionListener(e -> refreshClientTable());
        viewDetailsButton.addActionListener(e -> viewClientDetails(clientTable));
        createAccountButton.addActionListener(e -> createAccountForClient(clientTable));

        buttonPanel.add(refreshButton);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(createAccountButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAccountManagementPanel() {
        return createTableViewerPanel("Account Management",
            new String[]{"Account No", "User ID", "Name", "Balance", "Type", "Status"},
            this::fetchAccounts);
    }

    private JPanel createAccountCreationPanel() {
        JPanel panel = createContentPanel("Create Bank Account");

        JPanel formCard = Theme.createCard();
        formCard.setLayout(new GridBagLayout());
        formCard.setMaximumSize(new Dimension(600, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Client selection
        JLabel clientLabel = new JLabel("Select Client:");
        clientLabel.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(clientLabel, gbc);

        JComboBox<String> clientCombo = new JComboBox<>();
        clientCombo.setFont(Theme.LABEL_FONT);
        clientCombo.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(clientCombo, gbc);

        // Account type
        JLabel typeLabel = new JLabel("Account Type:");
        typeLabel.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formCard.add(typeLabel, gbc);

        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"SAVINGS", "CURRENT"});
        typeCombo.setFont(Theme.LABEL_FONT);
        typeCombo.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(typeCombo, gbc);

        // Initial balance
        JLabel balanceLabel = new JLabel("Initial Balance (₹):");
        balanceLabel.setFont(Theme.LABEL_FONT.deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        formCard.add(balanceLabel, gbc);

        JTextField balanceField = new JTextField("1000.00");
        Theme.styleTextField(balanceField);
        balanceField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(balanceField, gbc);

        // Create button
        JButton createButton = new JButton("Create Account");
        Theme.stylePrimaryButton(createButton);
        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(25, 15, 15, 15);
        formCard.add(createButton, gbc);

        // Populate client dropdown
        refreshClientDropdown(clientCombo);

        JButton refreshClientsButton = new JButton("Refresh Clients");
        Theme.styleSecondaryButton(refreshClientsButton);
        refreshClientsButton.addActionListener(e -> refreshClientDropdown(clientCombo));
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.insets = new Insets(10, 15, 15, 15);
        formCard.add(refreshClientsButton, gbc);

        createButton.addActionListener(e -> {
            handleAccountCreation(clientCombo, typeCombo, balanceField);
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(formCard);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClientReportsPanel() {
        JPanel panel = createContentPanel("Client Reports");

        JTextArea reportArea = new JTextArea();
        Theme.styleTextArea(reportArea);
        reportArea.setRows(20);

        try {
            StringBuilder report = new StringBuilder();
            report.append("KNB BANKING - CLIENT REPORT\n");
            report.append("Generated: ").append(new java.util.Date()).append("\n");
            report.append("Manager: ").append(currentUser.getName()).append("\n\n");

            report.append("=================================\n");
            report.append("CLIENT STATISTICS\n");
            report.append("=================================\n\n");

            List<User> clients = dbManager.getUsersByRole("client");
            report.append(String.format("Total Active Clients: %d\n\n", clients.size()));

            int withAccounts = 0;
            int withoutAccounts = 0;

            report.append("CLIENT BREAKDOWN:\n");
            report.append("-----------------\n");
            for (User client : clients) {
                Account account = dbManager.getAccountByUserId(client.getUserId());
                if (account != null) {
                    withAccounts++;
                    report.append(String.format("✓ %s (%s) - Account: %d, Balance: ₹%,.2f\n",
                        client.getName(), client.getEmail(), account.getAcno(), account.getBalance()));
                } else {
                    withoutAccounts++;
                    report.append(String.format("✗ %s (%s) - No Bank Account\n",
                        client.getName(), client.getEmail()));
                }
            }

            report.append(String.format("\nSUMMARY:\n"));
            report.append(String.format("Clients with Accounts: %d\n", withAccounts));
            report.append(String.format("Clients without Accounts: %d\n", withoutAccounts));
            report.append(String.format("Account Coverage: %.1f%%\n",
                clients.size() > 0 ? (withAccounts * 100.0 / clients.size()) : 0));

            reportArea.setText(report.toString());
        } catch (Exception e) {
            reportArea.setText("Error generating report: " + e.getMessage());
        }

        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAccountReportsPanel() {
        JPanel panel = createContentPanel("Account Reports");

        JTextArea reportArea = new JTextArea();
        Theme.styleTextArea(reportArea);
        reportArea.setRows(20);

        try {
            StringBuilder report = new StringBuilder();
            report.append("KNB BANKING - ACCOUNT REPORT\n");
            report.append("Generated: ").append(new java.util.Date()).append("\n");
            report.append("Manager: ").append(currentUser.getName()).append("\n\n");

            report.append("=================================\n");
            report.append("ACCOUNT STATISTICS\n");
            report.append("=================================\n\n");

            List<Account> accounts = dbManager.getAllAccounts();
            double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
            long savingsCount = accounts.stream().filter(a -> "SAVINGS".equals(a.getAccountType())).count();
            long currentCount = accounts.stream().filter(a -> "CURRENT".equals(a.getAccountType())).count();

            report.append(String.format("Total Accounts: %d\n", accounts.size()));
            report.append(String.format("Total Balance: ₹%,.2f\n", totalBalance));
            report.append(String.format("Average Balance: ₹%,.2f\n\n",
                accounts.size() > 0 ? totalBalance / accounts.size() : 0));

            report.append("ACCOUNT TYPE BREAKDOWN:\n");
            report.append("-----------------------\n");
            report.append(String.format("Savings Accounts: %d\n", savingsCount));
            report.append(String.format("Current Accounts: %d\n\n", currentCount));

            report.append("ACCOUNT DETAILS:\n");
            report.append("----------------\n");
            for (Account account : accounts) {
                report.append(String.format("Account %d - %s\n", account.getAcno(), account.getName()));
                report.append(String.format(" Type: %s | Balance: ₹%,.2f | Status: %s\n\n",
                    account.getAccountType(), account.getBalance(), account.getAccountStatus()));
            }

            reportArea.setText(report.toString());
        } catch (Exception e) {
            reportArea.setText("Error generating report: " + e.getMessage());
        }

        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }

    // Helper methods
    private void refreshClientTable() {
        clientTableModel.setRowCount(0);
        try {
            List<User> clients = dbManager.getUsersByRole("client");
            for (User client : clients) {
                Vector<Object> row = new Vector<>();
                row.add(client.getUserId());
                row.add(client.getUsername());
                row.add(client.getName());
                row.add(client.getEmail());
                row.add(client.getMobile());
                row.add(client.getStatus());

                // Check if client has account
                Account account = dbManager.getAccountByUserId(client.getUserId());
                row.add(account != null ? "Has Account" : "No Account");

                clientTableModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Failed to load client data: " + e.getMessage());
        }
    }

    private void refreshClientDropdown(JComboBox<String> combo) {
        combo.removeAllItems();
        try {
            List<User> clients = dbManager.getUsersByRole("client");
            for (User client : clients) {
                Account existingAccount = dbManager.getAccountByUserId(client.getUserId());
                if (existingAccount == null) { // Only show clients without accounts
                    combo.addItem(String.format("%d - %s (%s)",
                        client.getUserId(), client.getName(), client.getEmail()));
                }
            }

            if (combo.getItemCount() == 0) {
                combo.addItem("No clients available for account creation");
            }
        } catch (Exception e) {
            combo.addItem("Error loading clients");
            showError("Error loading clients: " + e.getMessage());
        }
    }

    private void viewClientDetails(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Please select a client to view details");
            return;
        }

        int userId = (int) clientTableModel.getValueAt(selectedRow, 0);
        String name = (String) clientTableModel.getValueAt(selectedRow, 2);
        String email = (String) clientTableModel.getValueAt(selectedRow, 3);
        String mobile = (String) clientTableModel.getValueAt(selectedRow, 4);

        try {
            Account account = dbManager.getAccountByUserId(userId);
            StringBuilder details = new StringBuilder();
            details.append("CLIENT DETAILS\n");
            details.append("==============\n\n");
            details.append("Name: ").append(name).append("\n");
            details.append("Email: ").append(email).append("\n");
            details.append("Mobile: ").append(mobile).append("\n\n");

            if (account != null) {
                details.append("ACCOUNT INFORMATION\n");
                details.append("===================\n\n");
                details.append("Account Number: ").append(account.getAcno()).append("\n");
                details.append("Account Type: ").append(account.getAccountType()).append("\n");
                details.append("Balance: ₹").append(String.format("%,.2f", account.getBalance())).append("\n");
                details.append("Status: ").append(account.getAccountStatus()).append("\n");
            } else {
                details.append("ACCOUNT INFORMATION\n");
                details.append("===================\n\n");
                details.append("No bank account linked to this client.");
            }

            JTextArea detailsArea = new JTextArea(details.toString(), 15, 40);
            detailsArea.setEditable(false);
            detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(detailsArea);

            JOptionPane.showMessageDialog(this, scrollPane, "Client Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showError("Error loading client details: " + e.getMessage());
        }
    }

    private void createAccountForClient(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Please select a client to create account for");
            return;
        }

        int userId = (int) clientTableModel.getValueAt(selectedRow, 0);
        String name = (String) clientTableModel.getValueAt(selectedRow, 2);

        try {
            // Check if client already has account
            Account existingAccount = dbManager.getAccountByUserId(userId);
            if (existingAccount != null) {
                showWarning("This client already has a bank account (Account #" + existingAccount.getAcno() + ")");
                return;
            }

            // Account creation form
            JTextField balanceField = new JTextField("1000.00");
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"SAVINGS", "CURRENT"});

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.add(new JLabel("Client: " + name));
            panel.add(new JLabel("Account Type:"));
            panel.add(typeCombo);
            panel.add(new JLabel("Initial Balance (₹):"));
            panel.add(balanceField);

            int result = JOptionPane.showConfirmDialog(
                this, panel,
                "Create Bank Account for " + name,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                double balance = Double.parseDouble(balanceField.getText());
                String type = (String) typeCombo.getSelectedItem();

                if (balance < 0) {
                    showError("Initial balance cannot be negative");
                    return;
                }

                User client = dbManager.getUserById(userId);
                dbManager.addAccount(userId, client.getName(), balance, client.getMobile(), type);
                dbManager.logAudit(currentUser.getUserId(), "MANAGER_ACCOUNT_CREATION",
                    "Manager created " + type + " account for " + name + " (ID: " + userId + ") with balance ₹" + balance);

                showSuccess("Account created successfully for " + name + "\nInitial Balance: ₹" + String.format("%,.2f", balance));
                refreshClientTable();
            }
        } catch (NumberFormatException e) {
            showError("Invalid balance amount");
        } catch (Exception e) {
            showError("Could not create account: " + e.getMessage());
        }
    }

    private void handleAccountCreation(JComboBox<String> clientCombo, JComboBox<String> typeCombo, JTextField balanceField) {
        String selectedClient = (String) clientCombo.getSelectedItem();
        if (selectedClient == null || selectedClient.contains("No clients") || selectedClient.contains("Error")) {
            showWarning("No valid client selected");
            return;
        }

        try {
            // Extract user ID from combo selection
            int userId = Integer.parseInt(selectedClient.split(" - ")[0]);
            double balance = Double.parseDouble(balanceField.getText());
            String type = (String) typeCombo.getSelectedItem();

            if (balance < 0) {
                showError("Initial balance cannot be negative");
                return;
            }

            User client = dbManager.getUserById(userId);
            dbManager.addAccount(userId, client.getName(), balance, client.getMobile(), type);
            dbManager.logAudit(currentUser.getUserId(), "MANAGER_ACCOUNT_CREATION",
                "Manager created " + type + " account for " + client.getName() + " (ID: " + userId + ") with balance ₹" + balance);

            showSuccess("Account created successfully for " + client.getName() + "\nInitial Balance: ₹" + String.format("%,.2f", balance));

            // Reset form
            balanceField.setText("1000.00");
            refreshClientDropdown(clientCombo);
        } catch (NumberFormatException e) {
            showError("Invalid number format");
        } catch (Exception e) {
            showError("Could not create account: " + e.getMessage());
        }
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
}
