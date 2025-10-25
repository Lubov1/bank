package ru.yandex.practicum.notificationservice.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class NotificationService {
    @Value("${mail.smtp.host}")
    private String smtpHost;
    @Value("${mail.smtp.port}")
    private String smtpPort;
    @Value("${mail.smtp.email}")
    private String smtpEmail;
    @Value("${mail.smtp.password}")
    private String smtpPassword;

    public void sendEmail(String email, String message) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpEmail, smtpPassword);
            }
        });

        // Создание сообщения
        Message emailMessage = new MimeMessage(session);
        try {
            emailMessage.setFrom(new InternetAddress(smtpEmail));

            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            emailMessage.setSubject("Уведомление");
            emailMessage.setText(message);

            // Отправка
            Transport.send(emailMessage);

            System.out.println("Письмо отправлено успешно!");
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
