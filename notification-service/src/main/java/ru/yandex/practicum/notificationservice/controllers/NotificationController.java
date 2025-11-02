package ru.yandex.practicum.notificationservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.notificationservice.services.NotificationService;

@RestController
public class NotificationController {
    Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;
    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendNotification(@RequestParam String email, @RequestParam String message) {
        logger.info("Email: " + email + "Message: " + message);
        notificationService.sendEmail(email, message);
        return ResponseEntity.ok().build();
    }
}
