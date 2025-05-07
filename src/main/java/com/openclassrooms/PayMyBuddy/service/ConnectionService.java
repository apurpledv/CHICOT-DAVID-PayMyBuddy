package com.openclassrooms.PayMyBuddy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.PayMyBuddy.model.Connection;
import com.openclassrooms.PayMyBuddy.model.ConnectionId;
import com.openclassrooms.PayMyBuddy.model.Mapper;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;
import com.openclassrooms.PayMyBuddy.repository.ConnectionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.util.ConnectionAlreadyExistsException;

/**
 * <p>ConnectionService is an entity that handles the work with Connections</p>
 */
@Service
public class ConnectionService {
	@Autowired
	UserRepository UserRepo;

	@Autowired
	ConnectionRepository ConnectionRepo;

	@Autowired
	Mapper MapperDTO;
	
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
	 * @throws ConnectionAlreadyExistsException if a Connection already exists between two Users (same User->User)
	 */
	public boolean addConnection(Connection connection) {
		if (ConnectionRepo.findByUserFromAndTo(connection.getConnectionId().getUserFrom(), connection.getConnectionId().getUserTo()) != null)
			return false;

		ConnectionRepo.addConnection(connection.getConnectionId().getUserFrom(), connection.getConnectionId().getUserTo());
		return true;
	}
	
	/**
	 * <p>Deletes an existing Connection between two Users</p>
	 * @param userFromId the Id of the User the connection stems from
	 * @param userToId the Id of the User the connection is linked to
	 * @return true if everything went right
	 */
	public boolean deleteConnection(int userFromId, int userToId) {
		ConnectionRepo.deleteByUserFromAndTo(userFromId, userToId);
		return true;
	}

	public List<UserDataFromConnectionDTO> getUsersConnectedToUser(int userFromId) {
		List<UserDataFromConnectionDTO> DTOList = new ArrayList<UserDataFromConnectionDTO>();

		List<Connection> ConnectionsList = ConnectionRepo.findByUserFrom(userFromId);
		for (Connection connection : ConnectionsList) {
			User userConnectedTo = UserRepo.findById(connection.getConnectionId().getUserTo());
			if (userConnectedTo == null)
				continue;

			DTOList.add(MapperDTO.toUserDataFromConnectionDTO(userConnectedTo, connection.getDateAdded()));
		}

		return DTOList;
	}

	public Connection createConnectionEntityFromEmail(int userId, String desiredEmail) {
		User UserFound = UserRepo.findByEmail(desiredEmail);
		if (UserFound == null)
			return null;

		if (UserFound.getId() == userId)
			return null;

		Connection NewConnection = new Connection();
		ConnectionId Id = new ConnectionId();
			Id.setUserFrom(userId);
			Id.setUserTo(UserFound.getId());
		NewConnection.setConnectionId(Id);

		return NewConnection;
	}
}
