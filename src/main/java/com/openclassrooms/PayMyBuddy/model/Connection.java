package com.openclassrooms.PayMyBuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_connection")
public class Connection {
	@EmbeddedId
	ConnectionIdentifier ConnectionId;
 
    @Column(name = "date_added")
    private String DateAdded;
}
