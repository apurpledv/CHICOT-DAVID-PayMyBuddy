package com.openclassrooms.PayMyBuddy.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ConnectionIdentifier implements Serializable {
	private static final long serialVersionUID = 6029058571007789507L;

	@Column(name = "user_from", nullable = false)
	private int UserFrom;
	
	@Column(name = "user_to", nullable = false)
	private int UserTo;
}
