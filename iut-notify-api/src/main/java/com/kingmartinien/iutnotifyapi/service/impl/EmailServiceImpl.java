package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.enums.EmailTemplateEnum;
import com.kingmartinien.iutnotifyapi.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendActivationEmail(
            String to,
            String fullName,
            String subject,
            String activationCode,
            String activationUrl,
            EmailTemplateEnum emailTemplateEnum
    ) throws MessagingException {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("to", to);
        properties.put("fullName", fullName);
        properties.put("subject", subject);
        properties.put("activationCode", activationCode);
        properties.put("activationUrl", activationUrl);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom("no-reply@iut-notify.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setSentDate(Date.from(Instant.now()));

        String template = this.templateEngine.process(emailTemplateEnum.getValue(), context);
        mimeMessageHelper.setText(template, true);

        this.mailSender.send(mimeMessage);

    }

}
