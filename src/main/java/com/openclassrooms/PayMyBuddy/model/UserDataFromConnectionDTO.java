package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

@Data
public class UserDataFromConnectionDTO {
    private int id;
    private String user;

    public UserDataFromConnectionDTO(int id, String user) {
        this.id = id;
        this.user = user;
    }
}
