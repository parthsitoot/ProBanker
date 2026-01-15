package com.banking.proBanker.Service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailServideImpl implements EmailService{

    private static JavaMailSender mailSender;

    public EmailServideImpl (JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        val future = new CompletableFuture<Void>();

        try {
            val message = mailSender.createMimeMessage();
            val helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // from's address will be atomatically set based on the spring properties in .properties file
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);

            log.info("Sending message to {}", to);
        } catch (MessagingException | MailException e) {
            log.error("Failed to send email to {}", to);
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public String getLoginEmailTemplate(String name, String loginTime, String loginLocation) {
        return "required email syntax";
    }

    @Override
    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp) {
        return "Required email syntax";
    }
}
