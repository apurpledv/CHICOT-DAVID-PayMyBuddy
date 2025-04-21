package com.openclassrooms.PayMyBuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.util.TransactionAlreadyExistsException;

/**
 * <p>TransactionService is an entity that handles the work with Transactions</p>
 */
@Service
public class TransactionService {
    @Autowired
	TransactionRepository TransactionRepo;
	
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
		
		TransactionRepo.save(transaction);
		return true;
	}
	
	/**
	 * <p>Updates the data for an existing Transaction</p>
	 * @param newData a Transaction Entity to update
	 * @return true if everything went right; false if the Transaction doesn't exist
	 */
	public boolean updateTransaction(Transaction newData) {
        Transaction ExistingTransaction = TransactionRepo.findExistingTransaction(newData.getSender(), newData.getReceiver(), newData.getDateTransaction());
        
		if (ExistingTransaction == null)
			return false;
		
        ExistingTransaction.setDescription(newData.getDescription());
        ExistingTransaction.setAmount(newData.getAmount());
		TransactionRepo.save(ExistingTransaction);
		return true;
	}
	
	/**
	 * <p>Deletes an existing Transaction</p>
	 * @param transactionId the Id of the Transaction to delete
	 * @return true if everything went right
	 */
	public boolean deleteTransaction(int transactionId) {
		TransactionRepo.deleteById(transactionId);
		return true;
	}
}
