package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

@Data
public class UserDataFromConnectionDTO {
    private int id;
    private String user;
    private String dateConnection;

    public UserDataFromConnectionDTO(int id, String user, String dateConnection) {
        this.id = id;
        this.user = user;
        this.dateConnection = dateConnection;
    }
}
