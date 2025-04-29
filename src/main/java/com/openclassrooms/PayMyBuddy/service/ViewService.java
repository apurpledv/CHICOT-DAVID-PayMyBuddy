package com.openclassrooms.PayMyBuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.TransactionDataDashboardDTO;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDataFromConnectionDTO;

import reactor.core.publisher.Mono;

@Service
public class ViewService {
    private final WebClient webClient;

    @Autowired
    public ViewService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public boolean verifyUser(User user) {
        ResponseEntity<Boolean> Response = null;
        try {
            Response = webClient.get()
                .uri("http://localhost:8080/user/verify?email=" + user.getEmail() + "&password=" + user.getPassword())
                .retrieve()
                .toEntity(Boolean.class)
                .block();
        } catch (Exception e) {
            return false;
        }

        if (Response.getStatusCode() != HttpStatus.OK)
            return false;

        return Response.getBody();
    }

    public boolean registerUser(User user) {
        ResponseEntity<HttpStatus> Response = null;
        try {
            Response = webClient.post()
                .uri("http://localhost:8080/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .toEntity(HttpStatus.class)
                .block();
        } catch (Exception e) {
            return false;
        }

        if (Response.getStatusCode() != HttpStatus.OK)
            return false;

        return true;
    }

    public List<UserDataFromConnectionDTO> getUserConnections(int userId) {
        Mono<List<UserDataFromConnectionDTO>> Response = null;
        try {
            Response = webClient.get()
                .uri("http://localhost:8080/user/connectionsFrom?userId=" + userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDataFromConnectionDTO>>() {});
        } catch (Exception e) {
            return null;
        }

        List<UserDataFromConnectionDTO> usersData = Response.block();

        return usersData;
    }

    public List<TransactionDataDashboardDTO> getTransactionsList(int userId) {
        Mono<List<TransactionDataDashboardDTO>> Response = null;
        try {
            Response = webClient.get()
                .uri("http://localhost:8080/transaction/summary?userId=" + userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TransactionDataDashboardDTO>>() {});
        } catch (Exception e) {
            return null;
        }

        List<TransactionDataDashboardDTO> transactionsData = Response.block();

        return transactionsData;
    }

    public boolean sendTransaction(Transaction transaction) {
        ResponseEntity<HttpStatus> Response = null;
        try {
            Response = webClient.post()
                .uri("http://localhost:8080/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .retrieve()
                .toEntity(HttpStatus.class)
                .block();
        } catch (Exception e) {
            return false;
        }

        if (Response.getStatusCode() != HttpStatus.OK)
            return false;

        return true;
    }
}
