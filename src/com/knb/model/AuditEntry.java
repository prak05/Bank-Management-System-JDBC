package com.knb.model;

import java.sql.Timestamp;

/**
 * AuditEntry model for tracking system activities
 * Stores audit log information for security and monitoring
 */
public class AuditEntry {
    public int logId, userId;
    public Timestamp dateTime;
    public String action, details;

    public AuditEntry(int logId, int userId, Timestamp dateTime, String action, String details) {
        this.logId = logId;
        this.userId = userId;
        this.dateTime = dateTime;
        this.action = action;
        this.details = details;
    }

    @Override
    public String toString() {
        return String.format("Audit[%d] - User %d: %s at %s", logId, userId, action, dateTime);
    }
}
