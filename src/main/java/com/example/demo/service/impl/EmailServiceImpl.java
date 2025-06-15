package com.example.demo.service.impl;

import com.example.demo.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendOtpEmail(String to, String code) {
        if (mailSender == null) {
            logger.warn("MailSender not configured. OTP for {} is {}", to, code);
            return;
        }
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(to);
            helper.setFrom(new InternetAddress(fromAddress, "Workstation Management"));
            helper.setSubject("Your One-Time Login Code");

            String html = "<div style='font-family:Arial,sans-serif;'>" +
                    "<h2 style='color:#4a90e2;'>Workstation Management</h2>" +
                    "<p>Hello,</p>" +
                    "<p>Your one-time verification code is:</p>" +
                    "<p style='font-size:24px;font-weight:bold;letter-spacing:4px;color:#2c3e50;'>" + code + "</p>" +
                    "<p>This code is valid for <strong>30 seconds</strong>. Please do NOT share it with anyone.</p>" +
                    "<p>If you did not request this code, you can safely ignore this email.</p>" +
                    "<br/>" +
                    "<p style='color:#6c757d;'>Thank you,<br/>Workstation Management Team</p>" +
                    "</div>";

            helper.setText(html, true);
            mailSender.send(mimeMessage);
            logger.info("OTP e-mail sent to {}", to);
        } catch (Exception ex) {
            logger.error("Failed to send OTP e-mail to {}: {}", to, ex.getMessage());
        }
    }
} 