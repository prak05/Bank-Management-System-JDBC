package com.knb.model;

/**
 * Account model representing bank account details
 * Contains account information and basic operations
 */
public class Account {
    private int acno, userId;
    private String name, mobileNumber, accountType, accountStatus;
    private double balance;

    public Account(int acno, int userId, String name, double balance, String mobileNumber, String accountType, String accountStatus) {
        this.acno = acno;
        this.userId = userId;
        this.name = name;
        this.balance = balance;
        this.mobileNumber = mobileNumber;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
    }

    // Getters
    public int getAcno() { return acno; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public double getBalance() { return balance; }
    public String getMobileNumber() { return mobileNumber; }
    public String getAccountType() { return accountType; }
    public String getAccountStatus() { return accountStatus; }
    
    // Setters for mutable fields
    public void setBalance(double balance) { this.balance = balance; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    
    @Override
    public String toString() {
        return String.format("Account[%d] - %s (₹%,.2f)", acno, name, balance);
    }
}
