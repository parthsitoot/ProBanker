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
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">OneStopBank</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #333;\">A login attempt was made on your account at:</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Time: <strong style=\"color: #3f51b5;\">" + loginTime
                + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #555;\">Location: <strong style=\"color: #3f51b5;\">"
                + loginLocation + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #333;\">If this was you, no further action is required. If you suspect any unauthorized access, please change your password immediately and contact our support team.</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The OneStopBank Team</p>" + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:onestopbank@google.com\" style=\"color: #3f51b5; text-decoration: none;\">onestopbank@google.com</a></p>"
                + "<div style=\"margin-top: 20px;\">"
                + "</div>" + "</div>" + "</div>" + "</div>";
    }

    @Override
    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">OneStopBank</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Account Number: <strong style=\"color: #3f51b5;\">"
                + accountNumber + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #333;\">Thank you for choosing OneStopBank. Use the following OTP to complete your login procedures. The OTP is valid for "
                + OtpServiceImpl.OTP_EXPIRY_MINUTES + " minutes:</p>"
                + "<h2 style=\"background: #3f51b5; margin: 20px 0; width: max-content; padding: 10px 20px; color: #fff; border-radius: 4px;\">"
                + otp + "</h2>" + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The OneStopBank Team</p>"
                + "</div>" + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:onestopbank@google.com\" style=\"color: #3f51b5; text-decoration: none;\">onestopbank@google.com</a></p>"
                + "<div style=\"margin-top: 20px;\">"
                + "</div>" + "</div>" + "</div>" + "</div>";
    }
}
