package com.openclassrooms.PayMyBuddy.model;

import org.springframework.stereotype.Component;

/**
 * Mapper is an entity that creates Data Transfer Objects (DTOs) that facilitate the way data is displayed to the user
 */
@Component
public class Mapper {
    /**
	 * <p>Will return a DTO of a User retrieved from a Connection Entity</p>
     * @param user User Entity connected to ours
     * @param dateConnection date at which the Connection Entity was created
     * @return a DTO containing: [id, username, date_of_creation_of_the_connection]
     */
    public UserDataFromConnectionDTO toUserDataFromConnectionDTO(User user, String dateConnection) {
        return new UserDataFromConnectionDTO(user.getId(), user.getUser(), dateConnection);
    }

    /**
     * <p>Will return a DTO of a Transaction displaying various info to the user</p>
     * @param transactionObj the Transaction Entity to parse from
     * @param contactObj the User Entity linked to this Transaction (either Sender or Receiver)
     * @param benefic whether this Transaction affects our balance positively
     * @return a DTO containing: [transaction_id, user_username, transaction_description, transaction_amount, transaction_date, true/false depending on whether the transaction beneficial to us]
     */
    public TransactionDataDashboardDTO toTransactionDataDashboardDTO(Transaction transactionObj, User contactObj, boolean beneficial) {
        return new TransactionDataDashboardDTO(
            transactionObj.getId(), 
            contactObj.getUser(), 
            transactionObj.getDescription(), 
            transactionObj.getAmount(), 
            transactionObj.getDateTransaction(),
            beneficial
        );
    }

    /**
     * <p>Will return a DTO of a Transaction displaying various info to the user</p>
     * @param transactionViewData a ViewTransactionDataReceiver Entity used to parse through Transactions of a Receiving User
     * @return a DTO containing: [transaction_id, user_username, transaction_description, transaction_amount, transaction_date, true/false depending on whether the transaction beneficial to us]
     */
    public TransactionDataDashboardDTO toTransactionDataDashboardDTO(ViewTransactionDataReceiver transactionViewData) {
        return new TransactionDataDashboardDTO(
            transactionViewData.getTransactionId(), 
            transactionViewData.getContactUser(), 
            transactionViewData.getDescription(), 
            transactionViewData.getAmount(), 
            transactionViewData.getDateTransaction(),
            transactionViewData.isBeneficial()
        );
    }

    /**
     * <p>Will return a DTO of a Transaction displaying various info to the user</p>
     * @param transactionViewData a ViewTransactionDataSender Entity used to parse through Transactions of a Sending User
     * @return a DTO containing: [transaction_id, user_username, transaction_description, transaction_amount, transaction_date, true/false depending on whether the transaction beneficial to us]
     */
    public TransactionDataDashboardDTO toTransactionDataDashboardDTO(ViewTransactionDataSender transactionViewData) {
        return new TransactionDataDashboardDTO(
            transactionViewData.getTransactionId(), 
            transactionViewData.getContactUser(), 
            transactionViewData.getDescription(), 
            transactionViewData.getAmount(), 
            transactionViewData.getDateTransaction(),
            transactionViewData.isBeneficial()
        );
    }
}
