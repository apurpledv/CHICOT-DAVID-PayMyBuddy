package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

@Data
public class TransactionDataDashboardDTO {
    private int transactionId;
    private String contactUser;
    private String description;
    private double amount;
    private String dateTransaction;
    private boolean benefic;

    public TransactionDataDashboardDTO(int transactionId, String contactUser, String description, double amount, String dateTransaction, boolean benefic) {
        this.transactionId = transactionId;
        this.contactUser = contactUser;
        this.description = description;
        this.amount = amount;
        this.dateTransaction = dateTransaction;
        this.benefic = benefic;
    }
}
