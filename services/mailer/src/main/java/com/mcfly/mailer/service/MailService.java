package com.mcfly.mailer.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public MailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendEmailConfirmation(String email, String url) throws MessagingException {
        final Context thymeleafContext = new Context();
        thymeleafContext.setVariable("email", email);
        thymeleafContext.setVariable("url", url);
        final String process = templateEngine.process("emailConfirmation", thymeleafContext);
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Welcome, " + email);
        helper.setText(process, true);
        helper.setTo(email);
        javaMailSender.send(mimeMessage);
    }
}
