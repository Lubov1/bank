package ru.yandex.practicum.notificationservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.notificationservice.services.NotificationService;

@RestController
@AllArgsConstructor
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendNotification(@RequestParam String email, @RequestParam String message) {
        System.out.println("Email: " + email + "Message: " + message);
        notificationService.sendEmail(email, message);
        return ResponseEntity.ok().build();
    }
}
