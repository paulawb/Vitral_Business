package com.vitral.auth.infraestructure.email;

import com.vitral.auth.domain.model.EmailMessage;
import com.vitral.auth.domain.model.gateway.EmailGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailGatewayImpl implements EmailGateway {

    private final JavaMailSender mailSender;

    public EmailGatewayImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailMessage email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            mailSender.send(message);
            log.info("Email sent successfully to: {}", email.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", email.getTo(), e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}