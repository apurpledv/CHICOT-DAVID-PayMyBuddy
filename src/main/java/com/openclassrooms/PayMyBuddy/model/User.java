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
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int Id;
 
    @Column(name = "user")
    private String user;
 
    @Column(name = "email")
    private String email;
 
    @Column(name = "password")
    private String password;
}
