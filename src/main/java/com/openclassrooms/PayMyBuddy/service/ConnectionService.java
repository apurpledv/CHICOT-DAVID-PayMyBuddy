package com.openclassrooms.PayMyBuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;
import com.openclassrooms.PayMyBuddy.util.ConnectionAlreadyExistsException;

@Service
public class ConnectionService {
	@Autowired
	ConnectionRepository ConnectionRepo;
	
	/**
	 * <p>Returns a List of Connection entities registered in the App</p>
	 * @return a List of Connection entities
	 */
	public List<Connection> getConnections() {
		return ConnectionRepo.findAll();
	}
	
	/**
	 * <p>Returns a List of Connection entities linked from a User Id</p>
	 * @return a List of Connection entities
	 */
	public List<Connection> getConnectionsByUserFrom(int userId) {
		return ConnectionRepo.findByUserFrom(userId);
	}
	
	/**
	 * <p>Returns a List of Connection entities linked to a User Id</p>
	 * @return a List of Connection entities
	 */
	public List<Connection> getConnectionsByUserTo(int userId) {
		return ConnectionRepo.findByUserTo(userId);
	}
	
	/**
	 * <p>Returns a List of Connection entities linked to a User Id</p>
	 * @return a Connection entity
	 */
	public Connection getConnectionBetweenUsers(int userFromId, int userToId) {
		return ConnectionRepo.findByUserFromAndTo(userFromId, userToId);
	}
	
	/**
	 * <p>Creates a new Connection entity</p>
	 * @param connection a Connection Entity to add
	 * @return true if everything went right
	 */
	public boolean addConnection(Connection connection) throws Exception {
		if (ConnectionRepo.findByUserFromAndTo(connection.getConnectionId().getUserFrom(), connection.getConnectionId().getUserTo()) != null)
			throw new ConnectionAlreadyExistsException();
		
		ConnectionRepo.save(connection);
		return true;
	}
	
	/**
	 * <p>Updates the data for an existing Connection</p>
	 * @param newConnectionData a Connection Entity to update
	 * @return true if everything went right; false if the Connection doesn't exist
	 */
	public boolean updateConnection(Connection newConnectionData) {
		Connection Connection = getConnectionBetweenUsers(
			newConnectionData.getConnectionId().getUserFrom(),
			newConnectionData.getConnectionId().getUserTo()
		);
		
		if (Connection == null)
			return false;
		
		Connection.setDateAdded(newConnectionData.getDateAdded());
		ConnectionRepo.save(Connection);
		return true;
	}
	
	/**
	 * <p>Deletes an existing connection between two Users</p>
	 * @param userFromId the Id of the User the connection stems from
	 * @param userToId the Id of the User the connection is linked to
	 * @return
	 */
	public boolean deleteConnection(int userFromId, int userToId) {
		ConnectionRepo.deleteByUserFromAndTo(userFromId, userToId);
		return true;
	}
}
