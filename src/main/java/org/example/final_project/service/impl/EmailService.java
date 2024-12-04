package org.example.final_project.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.model.EmailModel;
import org.example.final_project.service.IEmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService implements IEmailService {
    JavaMailSender emailSender;

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
