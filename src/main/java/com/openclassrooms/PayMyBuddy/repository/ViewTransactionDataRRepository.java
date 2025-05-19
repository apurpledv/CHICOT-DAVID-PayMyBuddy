package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.PayMyBuddy.model.ViewTransactionDataReceiver;

@Repository
public interface ViewTransactionDataRRepository extends CrudRepository<ViewTransactionDataReceiver, Integer> {
    @Query(value = "SELECT * FROM view_transactionsDataReceiver WHERE receiver = ?1", nativeQuery = true)
    List<ViewTransactionDataReceiver> getTransactions(int receiverId);
}
