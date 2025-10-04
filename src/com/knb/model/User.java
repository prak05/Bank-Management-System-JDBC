package com.knb.model;

/**
 * User model representing system users
 * Contains user authentication and profile information
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String name, email, mobile, role, status;

    public User(int userId, String username, String password, String name, String email, 
                String mobile, String role, String status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
        this.status = status;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getStatus() { return status; }
    
    // Setters for mutable fields
    public void setStatus(String status) { this.status = status; }
    public void setEmail(String email) { this.email = email; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("User[%d] - %s (%s)", userId, name, role);
    }
}
