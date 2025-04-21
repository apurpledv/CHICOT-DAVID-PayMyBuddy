package com.openclassrooms.PayMyBuddy.util;

/**
 * UserAlreadyExistsException is thrown if we're trying to create a new User that already exists (same Id, User or Email)
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
		super();
	}
}
