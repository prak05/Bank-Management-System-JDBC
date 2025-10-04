package com.knb.view.client;

import com.knb.model.Account;
import com.knb.model.Transaction;
import com.knb.model.User;
import com.knb.service.DatabaseManager;
import com.knb.service.TransactionManager;
import com.knb.view.BaseDashboard;
import com.knb.view.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ClientDashboard extends BaseDashboard {

    private final TransactionManager txManager;
    private Account currentAccount;
    private JLabel balanceLabel;

    public ClientDashboard(User user, DatabaseManager db, TransactionManager txManager) {
        // Step 1: Call the super constructor. It no longer initializes the UI.
        super(user, db);
        this.txManager = txManager;

        try {
            // Step 2: Load the critical data required for this dashboard.
            this.currentAccount = dbManager.getAccountByUserId(user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            // Show error on a null parent since the frame is not yet visible.
            JOptionPane.showMessageDialog(null, "A critical error occurred while loading your account data.", "Loading Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit constructor if data loading fails
        }
        
        // Step 3: Set window properties.
        setTitle("KNB Banking - Client Dashboard");
        
        // Step 4: Now that data is loaded, initialize the UI.
        initUI();
    }

    @Override
    protected void createNavigationButtons() {
        String[] navLabels = {"Account Summary", "Transfer Funds", "View Statement"};
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(Theme.NAV_BACKGROUND);
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        for (String label : navLabels) {
            JButton navButton = createNavButton(label);
            buttonContainer.add(navButton);
            
            // If no account is linked, disable all buttons except the summary
            if (currentAccount == null && !label.equals("Account Summary")) {
                navButton.setEnabled(false);
                navButton.setToolTipText("Please contact support to link a bank account.");
            }
        }
        
        // Use a container for buttons and add it to the north
        JPanel navContent = new JPanel(new BorderLayout());
        navContent.setOpaque(false);
        navContent.add(buttonContainer, BorderLayout.NORTH);

        navigationPanel.add(navContent, BorderLayout.CENTER);
        
        cardLayout.show(contentPanel, "Account Summary");
    }

    @Override
    protected void createContentPanels() {
        // This method definitively decides which view to show
        if (currentAccount == null) {
            // Show the user-friendly "No Account Linked" panel
            contentPanel.add(createNoAccountPanel(), "Account Summary");
            contentPanel.add(new JPanel(), "Transfer Funds"); // Add empty panels to prevent errors
            contentPanel.add(new JPanel(), "View Statement");
        } else {
            // Show the full dashboard with all features
            contentPanel.add(createSummaryPanel(), "Account Summary");
            contentPanel.add(createTransferPanel(), "Transfer Funds");
            contentPanel.add(createStatementPanel(), "View Statement");
        }
    }

    private JPanel createNoAccountPanel() {
        JPanel wrapper = createContentPanel("Account Summary");
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.CARD_BACKGROUND);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JLabel iconLabel = new JLabel("🏦"); // Using an emoji as a simple icon
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        
        JLabel messageLabel = new JLabel("No Bank Account is Linked to Your Profile");
        messageLabel.setFont(Theme.HEADER_FONT);
        messageLabel.setForeground(Theme.TEXT_COLOR);
        
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center;'>To access your banking features, please visit a branch or contact an administrator<br>to have a bank account created and linked to your user profile.</div></html>");
        instructionLabel.setFont(Theme.SUBTITLE_FONT);
        instructionLabel.setForeground(Theme.LIGHT_TEXT_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        panel.add(iconLabel, gbc);
        panel.add(messageLabel, gbc);
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(instructionLabel, gbc);
        
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = createContentPanel("Account Summary");

        JPanel detailsCard = Theme.createCard();
        detailsCard.setLayout(new GridLayout(4, 2, 20, 20));

        detailsCard.add(createFieldLabel("Account Holder:"));
        detailsCard.add(createValueLabel(currentAccount.getName()));
        detailsCard.add(createFieldLabel("Account Number:"));
        detailsCard.add(createValueLabel(String.valueOf(currentAccount.getAcno())));
        detailsCard.add(createFieldLabel("Account Type:"));
        detailsCard.add(createValueLabel(currentAccount.getAccountType()));
        detailsCard.add(createFieldLabel("Current Balance:"));
        
        balanceLabel = createValueLabel(String.format("₹%,.2f", currentAccount.getBalance()));
        balanceLabel.setFont(Theme.HEADER_FONT);
        balanceLabel.setForeground(Theme.SUCCESS_COLOR);
        detailsCard.add(balanceLabel);

        panel.add(detailsCard, BorderLayout.CENTER);
        return panel;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.BUTTON_BOLD_FONT);
        label.setForeground(Theme.LIGHT_TEXT_COLOR);
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.SUBTITLE_FONT);
        label.setForeground(Theme.TEXT_COLOR);
        return label;
    }

    private JPanel createTransferPanel() {
        JPanel panel = createContentPanel("Transfer Funds");

        JPanel formCard = Theme.createCard();
        formCard.setLayout(new GridBagLayout());
        formCard.setMaximumSize(new Dimension(500, 300));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0;
        formCard.add(new JLabel("Recipient Account No:"), gbc);
        JTextField toAccountField = new JTextField(20);
        Theme.styleTextField(toAccountField);
        gbc.gridx = 1; formCard.add(toAccountField, gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        formCard.add(new JLabel("Amount (₹):"), gbc);
        JTextField amountField = new JTextField(20);
        Theme.styleTextField(amountField);
        gbc.gridx = 1; formCard.add(amountField, gbc);

        JButton transferButton = new JButton("Transfer Now");
        Theme.stylePrimaryButton(transferButton);
        gbc.gridy = 2; gbc.gridx = 1; 
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        formCard.add(transferButton, gbc);
        
        // Action Listener
        transferButton.addActionListener(e -> {
            try {
                int toAcno = Integer.parseInt(toAccountField.getText());
                double amount = Double.parseDouble(amountField.getText());
                Account toAccount = dbManager.getAccount(toAcno);

                if (toAccount == null) {
                    showError("Recipient account not found.");
                    return;
                }
                if (currentAccount.getBalance() < amount) {
                    showError("Insufficient balance.");
                    return;
                }
                boolean success = txManager.transferFunds(currentAccount, toAccount, amount);
                if (success) {
                    showSuccess("Transfer successful!");
                    refreshBalance();
                    cardLayout.show(contentPanel, "Account Summary");
                } else {
                    showError("Transfer failed. Please check your balance.");
                }
            } catch (Exception ex) {
                showError("Invalid input. Please check the details and try again.");
            }
        });
        
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(formCard);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private void refreshBalance() {
        try {
            this.currentAccount = dbManager.getAccountByUserId(currentUser.getUserId());
            if (balanceLabel != null) {
                balanceLabel.setText(String.format("₹%,.2f", currentAccount.getBalance()));
            }
        } catch (Exception e) {
            showError("Could not refresh balance: " + e.getMessage());
        }
    }

    private JPanel createStatementPanel() {
        JPanel panel = createContentPanel("Transaction Statement");

        String[] columns = {"Transaction ID", "Date/Time", "Type", "Amount (₹)", "Remarks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        Theme.styleTable(table);

        try {
            List<Transaction> transactions = dbManager.getTransactionsForAccount(currentAccount.getAcno());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (transactions.isEmpty()) {
                model.addRow(new Object[]{"No transactions found for this account.", "", "", "", ""});
            } else {
                for (Transaction tx : transactions) {
                    model.addRow(new Object[]{tx.txId, sdf.format(tx.dateTime), tx.type, String.format("%,.2f", tx.amount), tx.remarks});
                }
            }
        } catch (Exception e) {
            model.addRow(new Object[]{"Error loading transaction history.", "", "", "", ""});
            e.printStackTrace();
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}

