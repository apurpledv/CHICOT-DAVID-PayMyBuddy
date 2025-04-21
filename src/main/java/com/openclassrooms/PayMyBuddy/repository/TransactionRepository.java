package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
	 * <p>Deletes a Transaction entity using the transaction's id</p>
	 * @param transactionId the Id of the Transaction to delete
	 */
	
	@Modifying
	@Query(value = "DELETE FROM t_transaction WHERE id_transaction = ?1", nativeQuery = true)
	public void deleteByUserFromAndTo(int transactionId);
}
