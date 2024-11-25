package org.example.final_project.service;

import org.example.final_project.model.EmailModel;

public interface IEmailService {
    boolean sendEmail(EmailModel email);
}
