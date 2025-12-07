package ru.yandex.practicum.notificationservice.services;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationService {
    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, AtomicInteger> blockedMap = new ConcurrentHashMap<>();

    public NotificationService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Value("${mail.smtp.host}")
    private String smtpHost;
    @Value("${mail.smtp.port}")
    private String smtpPort;
    @Value("${mail.smtp.email}")
    private String smtpEmail;
    @Value("${mail.smtp.password}")
    private String smtpPassword;
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(topics = "notifications")
    public void consume(Map<String, String> message, Acknowledgment acknowledgment) {
        sendEmail(message.get("email"), message.get("message"), acknowledgment);
    }

    public void sendEmail(String email, String message, Acknowledgment acknowledgment) {
        logger.info("Sending email to " + email);
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
            acknowledgment.acknowledge();
            logger.info("Письмо отправлено успешно!");
            setBlocked(email, true);
        } catch (MessagingException e) {
            setBlocked(email, false);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setBlocked(String login, boolean allowed) {
        AtomicInteger value = blockedMap.computeIfAbsent(login, l -> {
            AtomicInteger v = new AtomicInteger(0);
            Gauge.builder("email_send_allowed", v, AtomicInteger::get)
                    .description("1 = email sending is allowed for login, 0 = blocked")
                    .tag("login", l)
                    .register(meterRegistry);
            return v;
        });

        value.set(allowed ? 1 : 0);
    }
}
