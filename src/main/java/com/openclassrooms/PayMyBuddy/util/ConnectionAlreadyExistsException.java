package com.openclassrooms.PayMyBuddy.util;

/**
 * ConnectionAlreadyExistsException is thrown if we're trying to create a new Connection that already exists (same UserFrom & UserTo)
 */
public class ConnectionAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2866974440653463505L;

	public ConnectionAlreadyExistsException() {
		super();
	}
}
