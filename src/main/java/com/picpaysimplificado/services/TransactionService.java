package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.entity.User;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;

    public TransactionService(TransactionRepository transactionRepository, UserService userService, RestTemplate restTemplate) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.restTemplate = restTemplate;
    }



    public void createTransaction(TransactionDTO transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());

    }

    public boolean authorizedTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizedResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        boolean message = (boolean) authorizedResponse.getBody().get("authorization");

        if(authorizedResponse.getStatusCode() == HttpStatus.OK && message) {
            return true;
        } else return false;

    }


}
