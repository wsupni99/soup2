package ru.itis.soup2.services.mail;

public interface MailService {
    void sendTaskNotification(String toEmail, String subject, String templateName, Object model);
}