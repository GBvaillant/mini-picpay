package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.entity.Transaction;
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
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository,NotificationService notificationService, UserService userService, RestTemplate restTemplate) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.notificationService = notificationService;
    }


    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = authorizedTransaction(sender, transaction.value());
        if(!isAuthorized) {
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAccount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(java.time.LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        transactionRepository.save(newTransaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);

        notificationService.sendNotification(sender, "Transação realizada com sucesso!");
        notificationService.sendNotification(receiver, "Você recebeu uma transação de " + sender.getName() + "!");

        return newTransaction;
    }

    public boolean authorizedTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizedResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (authorizedResponse.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = authorizedResponse.getBody();
            if (body != null && body.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                if (data != null && data.containsKey("authorization")) {
                    return Boolean.TRUE.equals(data.get("authorization"));
                }
            }
        }
        return false;
    }
}
