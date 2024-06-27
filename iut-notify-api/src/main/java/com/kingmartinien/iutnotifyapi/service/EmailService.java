package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.User;
import com.kingmartinien.iutnotifyapi.enums.EmailTemplateEnum;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

@Async
public interface EmailService {

    void sendActivationEmail(
            String to,
            String fullName,
            String subject,
            String activationCode,
            String activationUrl,
            EmailTemplateEnum emailTemplateEnum
    ) throws MessagingException;

    void sendResetPasswordEmail(User user, String code, String url) throws MessagingException;

}
