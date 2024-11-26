package org.example.final_project.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.final_project.model.EmailModel;
import org.example.final_project.service.IEmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public boolean sendEmail(EmailModel email) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email.getRecipient());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), true);
            emailSender.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
