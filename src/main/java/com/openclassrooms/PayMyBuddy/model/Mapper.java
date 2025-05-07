package com.openclassrooms.PayMyBuddy.model;

import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public UserDataFromConnectionDTO toUserDataFromConnectionDTO(User user, String dateConnection) {
        return new UserDataFromConnectionDTO(user.getId(), user.getUser(), dateConnection);
    }

    public TransactionDataDashboardDTO toTransactionDataDashboardDTO(Transaction transactionObj, User contactObj, boolean benefic) {
        return new TransactionDataDashboardDTO(
            transactionObj.getId(), 
            contactObj.getUser(), 
            transactionObj.getDescription(), 
            transactionObj.getAmount(), 
            transactionObj.getDateTransaction(),
            benefic);
    }
}
