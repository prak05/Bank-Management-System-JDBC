package com.knb.service;

import java.sql.*;
import java.util.*;
import com.knb.model.*;

/**
 * DatabaseManager handles all database operations
 * Provides comprehensive data access layer for the banking system
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "Prak";
    private static final String PASS = "prak05";

    /**
     * Get database connection
     * @return Connection object
     * @throws Exception if connection fails
     */
    public Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Close database resources safely
     */
    public void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }

    // ==================== USER OPERATIONS ====================

    /**
     * Get user by credentials for authentication
     */
    public User getUserByCredentials(String username, String password) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users_KNBtbl WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
            return null;
        }
    }

    /**
     * Get user by ID
     */
    public User getUserById(int id) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users_KNBtbl WHERE user_id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
            return null;
        }
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() throws Exception {
        List<User> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users_KNBtbl")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapUser(rs));
            return list;
        }
    }

    /**
     * Get users by role with active status
     */
    public List<User> getUsersByRole(String role) throws Exception {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users_KNBtbl WHERE role = ? AND status = 'ACTIVE' ORDER BY user_id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
            return list;
        }
    }

    /**
     * Add new user
     */
    public void addUser(String username, String password, String name, String email, String mobile, String role) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO users_KNBtbl(username, password, name, email, mobile, role, status) VALUES (?,?,?,?,?,?, 'PENDING')")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, email);
            ps.setString(5, mobile);
            ps.setString(6, role);
            ps.executeUpdate();
        }
    }

    /**
     * Update user details
     */
    public void updateUser(int userId, String name, String email, String mobile, String status) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE users_KNBtbl SET name = ?, email = ?, mobile = ?, status = ? WHERE user_id = ?")) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, mobile);
            ps.setString(4, status);
            ps.setInt(5, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Delete user
     */
    public void deleteUser(int userId) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users_KNBtbl WHERE user_id=?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Set user password
     */
    public void setUserPassword(int userId, String password) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE users_KNBtbl SET password=? WHERE user_id=?")) {
            ps.setString(1, password);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Map ResultSet to User object
     */
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("mobile"),
                rs.getString("role"),
                rs.getString("status")
        );
    }

    // ==================== ACCOUNT OPERATIONS ====================

    /**
     * Get all accounts
     */
    public List<Account> getAllAccounts() throws Exception {
        List<Account> ret = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts_KNBtbl")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ret.add(mapAccount(rs));
            return ret;
        }
    }

    /**
     * Get account by user ID
     */
    public Account getAccountByUserId(int userId) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts_KNBtbl WHERE user_id=?")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapAccount(rs);
            return null;
        }
    }

    /**
     * Get account by account number
     */
    public Account getAccount(int acno) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts_KNBtbl WHERE acno=?")) {
            ps.setInt(1, acno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapAccount(rs);
            return null;
        }
    }

    /**
     * Add new account with duplicate check
     */
    public void addAccount(int userId, String name, double balance, String mobile, String type) throws Exception {
        // This check prevents creating an account for a user who already has one.
        if (getAccountByUserId(userId) != null) {
            throw new Exception("This user already has a bank account linked.");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO accounts_KNBtbl(acno, user_id, name, balance, mobile_number, account_type, account_status) " +
                             "VALUES (acno_seq_KNBtbl.NEXTVAL, ?, ?, ?, ?, ?, 'ACTIVE')")) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setDouble(3, balance);
            ps.setString(4, mobile);
            ps.setString(5, type);
            ps.executeUpdate();
        }
    }

    /**
     * Update account balance
     */
    public void updateAccount(int acno, double newBalance) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE accounts_KNBtbl SET balance=? WHERE acno=?")) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, acno);
            ps.executeUpdate();
        }
    }

    /**
     * Map ResultSet to Account object
     */
    private Account mapAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("acno"),
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getDouble("balance"),
                rs.getString("mobile_number"),
                rs.getString("account_type"),
                rs.getString("account_status")
        );
    }

    // ==================== TRANSACTION OPERATIONS ====================

    /**
     * Add new transaction
     */
    public void addTransaction(int acno, String type, double amount, int fromAcno, int toAcno, String status, String remarks) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO transactions_KNBtbl(acno, type, amount, from_acno, to_acno, status, remarks) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, acno);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setInt(4, fromAcno);
            ps.setInt(5, toAcno);
            ps.setString(6, status);
            ps.setString(7, remarks);
            ps.executeUpdate();
        }
    }

    /**
     * Get transactions for specific account
     */
    public List<Transaction> getTransactionsForAccount(int acno) throws Exception {
        List<Transaction> ret = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM transactions_KNBtbl WHERE acno=? ORDER BY date_time DESC")) {
            ps.setInt(1, acno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ret.add(new Transaction(
                        rs.getInt("tx_id"),
                        rs.getInt("acno"),
                        rs.getInt("from_acno"),
                        rs.getInt("to_acno"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date_time"),
                        rs.getString("status"),
                        rs.getString("remarks")
                ));
            }
            return ret;
        }
    }

    // ==================== ADMIN OPERATIONS ====================

    /**
     * Get accounts for branch summary
     */
    public List<Account> getAccountsForBranchSummary() throws Exception {
        return getAllAccounts();
    }

    /**
     * Get top customers by balance
     */
    public List<User> getTopCustomers() throws Exception {
        List<User> ret = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT u.* FROM users_KNBtbl u JOIN accounts_KNBtbl a ON u.user_id = a.user_id " +
                             "ORDER BY a.balance DESC FETCH FIRST 10 ROWS ONLY")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ret.add(mapUser(rs));
            return ret;
        }
    }

    /**
     * Log audit entry
     */
    public void logAudit(int userId, String action, String details) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO audit_log_KNBtbl(user_id, action, details) VALUES (?, ?, ?)")) {
            ps.setInt(1, userId);
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();
        }
    }

    /**
     * Update user role
     */
    public void updateUserRole(int userId, String role) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE users_KNBtbl SET role=? WHERE user_id=?")) {
            ps.setString(1, role);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Get configuration value (requires config_KNBtbl table)
     */
    public String getConfigValue(String key) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT value FROM config_KNBtbl WHERE key=?")) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("value") : null;
        }
    }

    /**
     * Set configuration value
     */
    public void setConfigValue(String key, String value) throws Exception {
        try (Connection conn = getConnection()) {
            try (PreparedStatement up = conn.prepareStatement("UPDATE config_KNBtbl SET value=? WHERE key=?")) {
                up.setString(1, value);
                up.setString(2, key);
                int rows = up.executeUpdate();
                if (rows == 0) {
                    try (PreparedStatement ins = conn.prepareStatement("INSERT INTO config_KNBtbl(key, value) VALUES (?, ?)")) {
                        ins.setString(1, key);
                        ins.setString(2, value);
                        ins.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Delete configuration value
     */
    public void deleteConfigValue(String key) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM config_KNBtbl WHERE key=?")) {
            ps.setString(1, key);
            ps.executeUpdate();
        }
    }

    /**
     * Get audit logs with limit
     */
    public List<AuditEntry> getAuditLogs(int limit) throws Exception {
        List<AuditEntry> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM (SELECT * FROM audit_log_KNBtbl ORDER BY action_time DESC) WHERE ROWNUM <= ?")) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new AuditEntry(
                    rs.getInt("audit_id"), rs.getInt("user_id"), rs.getTimestamp("action_time"),
                    rs.getString("action"), rs.getString("details")));
            return list;
        }
    }

    /**
     * Get pending users for approval
     */
    public List<User> getPendingUsers() throws Exception {
        List<User> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM users_KNBtbl WHERE status='PENDING'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapUser(rs));
            return list;
        }
    }

    /**
     * Update user status
     */
    public void updateUserStatus(int userId, String status) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE users_KNBtbl SET status=? WHERE user_id=?")) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    // ==================== STATISTICS ====================

    /**
     * Get total user count
     */
    public int getUserCount() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users_KNBtbl");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Get total account count
     */
    public int getAccountCount() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM accounts_KNBtbl");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Get total transaction count
     */
    public int getTransactionCount() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM transactions_KNBtbl");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
