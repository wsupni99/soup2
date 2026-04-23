package ru.itis.soup2.services.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.IOException;
import java.io.StringWriter;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String mailFrom;

    private final JavaMailSender javaMailSender;
    private final Configuration freeMarkerConfig;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;

        freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
        freeMarkerConfig.setDefaultEncoding("UTF-8");
        freeMarkerConfig.setTemplateLoader(
                new SpringTemplateLoader(new ClassRelativeResourceLoader(this.getClass()), "/")
        );
        freeMarkerConfig.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Async
    @Override
    public void sendTaskNotification(String toEmail, String subject, String templateName, Object model) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mailFrom);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            Template template = freeMarkerConfig.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            String html = writer.toString();

            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}