package com.openclassrooms.PayMyBuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "view_TransactionsDataReceiver")
public class ViewTransactionDataReceiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private int transactionId;
    
    @Column(name = "receiver")
    private int receiverId;

    @Column(name = "contact")
    private String contactUser;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private double amount;

    @Column(name = "date_Transaction")
    private String dateTransaction;
    
    @Column(name = "beneficial")
    private boolean beneficial;
}
