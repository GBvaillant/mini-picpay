package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.NotificationDTO;
import com.picpaysimplificado.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    public NotificationService (RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(User user, String message) throws Exception {

        NotificationDTO notificationRequest = new NotificationDTO(user.getEmail(), message);
        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequest, String.class);

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
            System.out.println("Erro ao enviar notificação: " + notificationResponse.getStatusCode());
            throw new Exception("Serviço de notificação fora do ar: " + notificationResponse.getStatusCode());
        }
    }
}
