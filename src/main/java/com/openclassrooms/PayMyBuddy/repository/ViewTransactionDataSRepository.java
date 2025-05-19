package com.openclassrooms.PayMyBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.PayMyBuddy.model.ViewTransactionDataSender;

@Repository
public interface ViewTransactionDataSRepository extends CrudRepository<ViewTransactionDataSender, Integer> {
    @Query(value = "SELECT * FROM view_transactionsDataSender WHERE sender = ?1", nativeQuery = true)
    List<ViewTransactionDataSender> getTransactions(int senderId);
}
