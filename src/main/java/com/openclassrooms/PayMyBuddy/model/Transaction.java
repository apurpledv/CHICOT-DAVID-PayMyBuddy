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
@Table(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private int id;

    @Column(name = "sender")
    private int sender;

    @Column(name = "receiver")
    private int receiver;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private double amount;

    @Column(name = "date_transaction")
    private String dateTransaction;

    public int hashCode() {
        return 2;
    }

    public boolean equals(Transaction transaction) {
        if (this.sender == transaction.sender && this.receiver == transaction.receiver && this.amount == transaction.amount && this.dateTransaction == transaction.dateTransaction)
            return true;
        else
            return false;
    }
}
