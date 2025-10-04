package com.knb.model;

import java.sql.Timestamp;

/**
 * Transaction model representing financial transactions
 * Contains transaction details and status information
 */
public class Transaction {
    public int txId, acno, fromAcno, toAcno;
    public String type, status, remarks;
    public double amount;
    public Timestamp dateTime;

    public Transaction(int txId, int acno, int fromAcno, int toAcno, String type, double amount, 
                      Timestamp dateTime, String status, String remarks) {
        this.txId = txId;
        this.acno = acno;
        this.fromAcno = fromAcno;
        this.toAcno = toAcno;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
        this.status = status;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return String.format("Transaction[%d] - %s: ₹%,.2f (%s)", txId, type, amount, status);
    }
}
