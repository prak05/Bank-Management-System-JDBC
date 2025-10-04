package com.knb.service;

import com.knb.model.User;

/**
 * AuthenticationService handles user authentication and registration
 * Provides secure login and user management functionality
 */
public class AuthenticationService {
    private final DatabaseManager db;

    public AuthenticationService(DatabaseManager db) {
        this.db = db;
    }

    /**
     * Authenticate user with username and password
     * @param username User's login username
     * @param password User's login password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String username, String password) {
        try {
            return db.getUserByCredentials(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Register new user with provided details
     * @param username Unique username for login
     * @param password User's password
     * @param name Full name of user
     * @param email Email address
     * @param mobile Mobile number
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String username, String password, String name, String email, String mobile) {
        try {
            db.addUser(username, password, name, email, mobile, "client");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Reset password for a user
     * @param userId User ID to reset password for
     * @param newPassword New password to set
     * @return true if successful, false otherwise
     */
    public boolean resetPassword(int userId, String newPassword) {
        try {
            db.setUserPassword(userId, newPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
