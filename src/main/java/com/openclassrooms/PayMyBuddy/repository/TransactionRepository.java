package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.PayMyBuddy.model.Transaction;

/**
 * <p>TransactionRepository is an entity that handles Data directly for Transactions (Adding; Modifying; Removing..)</p>
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    /**
	 * <p>Returns a List of all Transaction entities registered</p>
	 * @return a List of Transaction entities
	 */
	public List<Transaction> findAll();

    /**
	 * <p>Returns a List of all Transaction entities found using the sender's id</p>
     * @param senderId id of the User who the transaction originates from
	 * @return a List of Transaction entities
	 */
    public List<Transaction> findBySender(int senderId);

    /**
	 * <p>Returns a List of all Transaction entities found using the receiver's id</p>
     * @param receiverId id of the User who the transaction is directed to
	 * @return a List of Transaction entities
	 */
    public List<Transaction> findByReceiver(int receiverId);

    /**
	 * <p>Returns a List of every Transaction entity between two Users</p>
	 * @param senderId the Id of the User the transaction stems from
	 * @param receiverId the Id of the User the transaction is directed to
	 * @return a unique Transaction entity between two Users
	 */
	@Query(value = "SELECT t_transaction.id_transaction, t_transaction.description, t_transaction.amount, t_transaction.date_transaction FROM t_transaction INNER JOIN t_user ON t_user.id_user = t_transaction.sender OR t_user.id_user = t_transaction.receiver WHERE t_user.id_user = ?1", nativeQuery = true)
	public List<Transaction> findAllTransactionsOfUser(int userId);

	/**
	 * <p>Returns a List of every Transaction entity between two Users</p>
	 * @param senderId the Id of the User the transaction stems from
	 * @param receiverId the Id of the User the transaction is directed to
	 * @return a unique Transaction entity between two Users
	 */
	@Query(value = "SELECT * FROM t_transaction WHERE sender = ?1 AND receiver = ?2", nativeQuery = true)
	public List<Transaction> findBySenderAndReceiver(int senderId, int receiverId);

    /**
	 * <p>Returns a Transaction entity found using the transaction's: sender Id, receiver Id, and date</p>
     * @param senderId id of the User the transaction stems from
     * @param receiverId id of the User the transaction is directed to
     * @param date date at which the Transaction was initiated
	 * @return a Transaction entitiy
	 */
    @Query(value = "SELECT * FROM t_transaction WHERE sender = ?1 AND receiver = ?2 AND date_transaction = ?3 LIMIT 1", nativeQuery = true)
    public Transaction findExistingTransaction(int senderId, int receiverId, String date);

    /**
	 * <p>Adds a Transaction entity</p>
	 * @param senderId id of the sending User
	 * @param receiverId id of the receiving User
	 * @param description description of the Transaction
	 * @param amount amount of the Transaction
	 */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO t_transaction (sender, receiver, description, amount, date_transaction) VALUES (?1, ?2, ?3, ?4, NOW())", nativeQuery = true)
	public int addTransaction(int senderId, int receiverId, String description, double amount);

	/**
	 * <p>Deletes a Transaction entity</p>
	 * @param amount amount of the Transaction to delete
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM t_transaction WHERE amount = ?1", nativeQuery = true)
	public void deleteByAmount(double amount);
}
