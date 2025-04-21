package com.openclassrooms.PayMyBuddy.util;

/**
 * TransactionAlreadyExistsException is thrown if we're trying to create a new Transaction that already exists (same sender Id, receiver Id, amount & date)
 */
public class TransactionAlreadyExistsException extends Exception {
    public TransactionAlreadyExistsException() {
		super();
	}
}
