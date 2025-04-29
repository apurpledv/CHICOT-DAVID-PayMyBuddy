package com.openclassrooms.PayMyBuddy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.PayMyBuddy.model.Mapper;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.TransactionDataDashboardDTO;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.util.TransactionAlreadyExistsException;

/**
 * <p>TransactionService is an entity that handles the work with Transactions</p>
 */
@Service
public class TransactionService {
	@Autowired
	UserRepository UserRepo;

    @Autowired
	TransactionRepository TransactionRepo;

	@Autowired
	Mapper MapperDTO;
	
	/**
	 * <p>Returns a List of Connection entities registered in the App</p>
	 * @return a List of Connection entities
	 */
	public List<Transaction> getTransactions() {
		return TransactionRepo.findAll();
	}
	
	/**
	 * <p>Returns a List of Transaction entities initiated by a User (found using their Id)</p>
	 * @return a List of Transaction entities
	 */
	public List<Transaction> getTransactionsBySender(int senderId) {
		return TransactionRepo.findBySender(senderId);
	}
	
	/**
	 * <p>Returns a List of Transaction entities directed to a User (found using their Id)</p>
	 * @return a List of Transaction entities
	 */
	public List<Transaction> getTransactionsByReceiver(int receiverId) {
		return TransactionRepo.findByReceiver(receiverId);
	}
	
	/**
	 * <p>Returns a List of Transaction entities between two Users</p>
	 * @return a List of Transaction entities
	 */
	public List<Transaction> getTransactionsBySenderAndReceiver(int senderId, int receiverId) {
		return TransactionRepo.findBySenderAndReceiver(senderId, receiverId);
	}

    /**
	 * <p>Returns an existing Transaction entity found using its sender Id, receiver Id, and date</p>
	 * @return a Transaction entity
	 */
	public Transaction getExistingTransaction(int senderId, int receiverId, String transactionDate) {
		return TransactionRepo.findExistingTransaction(senderId, receiverId, transactionDate);
	}
	
	/**
	 * <p>Creates a new Transaction entity</p>
	 * @param transaction a Transaction Entity to add
	 * @return true if everything went right
     * @throws TransactionAlreadyExistsException if a Transaction already exists (same Sender, Receiver, Amount and Date)
	 */
	public boolean addTransaction(Transaction transaction) throws Exception {
        List<Transaction> TransactionsList = TransactionRepo.findBySender(transaction.getSender());
        for (Transaction existingTransaction : TransactionsList) {
            if (existingTransaction.equals(transaction))
                throw new TransactionAlreadyExistsException();
        }
		
		TransactionRepo.addTransaction(transaction.getSender(), transaction.getReceiver(), transaction.getDescription(), transaction.getAmount());
		return true;
	}

	/**
	 * <p>Returns a List of Transaction entities between two Users</p>
	 * @return a List of Transaction entities
	 */
	public List<TransactionDataDashboardDTO> getAllTransactionsDataOfUser(int userId) {
		List<TransactionDataDashboardDTO> TransactionsDTOList = new ArrayList<TransactionDataDashboardDTO>();

		// Fill the list with transactions where we're the sender, and those where we're the receiver
		List<Transaction> TransactionsList = getTransactionsBySender(userId);
		TransactionsList.addAll(getTransactionsByReceiver(userId));

		// Create DTO
		for (Transaction transaction : TransactionsList) {
			// We only pass one User Entity: the 'Contact' (either Sender or Receiver) the transaction is about
			User ContactObj = UserRepo.findById(transaction.getReceiver());

			// Expresses whether the transaction impacts the User's balance positively or not (ie: Sending = negative, Receiver = positive)
			boolean Benefic = false;

			if (transaction.getSender() != userId) {
				ContactObj = UserRepo.findById(transaction.getSender());
				Benefic = true;
			}
			
			TransactionsDTOList.add(MapperDTO.toTransactionDataDashboardDTO(transaction, ContactObj, Benefic));
		}

		return TransactionsDTOList;
	}
}
