package com.knb.service;

import com.knb.model.Account;

/**
 * TransactionManager handles all financial transactions
 * Provides secure and reliable transaction processing
 */
public class TransactionManager {
    private final DatabaseManager db;

    public TransactionManager(DatabaseManager db) {
        this.db = db;
    }

    /**
     * Transfer funds between accounts
     * @param from Source account
     * @param to Destination account
     * @param amount Amount to transfer
     * @return true if transfer successful, false otherwise
     */
    public boolean transferFunds(Account from, Account to, double amount) throws Exception {
        // Validation checks
        if (amount <= 0) {
            return false;
        }
        
        if (from.getBalance() < amount) {
            return false;
        }
        
        if (from.getAcno() == to.getAcno()) {
            return false;
        }

        // Perform the transfer
        db.updateAccount(from.getAcno(), from.getBalance() - amount);
        db.updateAccount(to.getAcno(), to.getBalance() + amount);
        
        // Record transactions for both accounts
        db.addTransaction(from.getAcno(), "TRANSFER_OUT", amount, from.getAcno(), to.getAcno(), 
                         "SUCCESS", "To Account " + to.getAcno());
        db.addTransaction(to.getAcno(), "TRANSFER_IN", amount, from.getAcno(), to.getAcno(), 
                         "SUCCESS", "From Account " + from.getAcno());
        
        // Update account objects
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        
        return true;
    }

    /**
     * Deposit money to account
     * @param acc Account to deposit into
     * @param amount Amount to deposit
     * @return true if deposit successful, false otherwise
     */
    public boolean deposit(Account acc, double amount) throws Exception {
        if (amount <= 0) {
            return false;
        }

        db.updateAccount(acc.getAcno(), acc.getBalance() + amount);
        db.addTransaction(acc.getAcno(), "DEPOSIT", amount, 0, acc.getAcno(), 
                         "SUCCESS", "Cash Deposit");
        acc.setBalance(acc.getBalance() + amount);
        return true;
    }

    /**
     * Withdraw money from account
     * @param acc Account to withdraw from
     * @param amount Amount to withdraw
     * @return true if withdrawal successful, false otherwise
     */
    public boolean withdraw(Account acc, double amount) throws Exception {
        if (amount <= 0 || acc.getBalance() < amount) {
            return false;
        }

        db.updateAccount(acc.getAcno(), acc.getBalance() - amount);
        db.addTransaction(acc.getAcno(), "WITHDRAWAL", amount, acc.getAcno(), 0, 
                         "SUCCESS", "Cash Withdrawal");
        acc.setBalance(acc.getBalance() - amount);
        return true;
    }

    /**
     * Pay utility bill
     * @param acc Account to pay from
     * @param amount Bill amount
     * @param utilityName Name of utility service
     * @return true if payment successful, false otherwise
     */
    public boolean payUtility(Account acc, double amount, String utilityName) throws Exception {
        if (amount <= 0 || acc.getBalance() < amount) {
            return false;
        }

        db.updateAccount(acc.getAcno(), acc.getBalance() - amount);
        db.addTransaction(acc.getAcno(), "UTILITY_PAYMENT", amount, acc.getAcno(), 0, 
                         "SUCCESS", "Paid " + utilityName);
        acc.setBalance(acc.getBalance() - amount);
        return true;
    }
}
