package com.libraryhub.identity.utility;


import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MailgunEmailService {

    private final EmailTemplateLoader emailTemplateLoader;

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.from}")
    private String fromEmail;

    @Value("mailgun.url")
    private String url;

    @Value("${send_email_verification_toggle}")
    private boolean sendEmailVerificationToggle;

    public void sendSimpleEmail(String toEmail, String subject, String name) throws IOException {

        ClassPathResource resource = new ClassPathResource("templates/verificationtemplate.html");
        String htmlContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);


        MailgunMessagesApi mailgun = MailgunClient.config(apiKey).createApi(MailgunMessagesApi.class);

        Message message = Message.builder()
                .from(fromEmail)
                .to(toEmail)
                .subject(subject)
                .html(htmlContent)
                .build();

        MessageResponse response = mailgun.sendMessage(domain, message);
    }


    public void sendVerificationEmail(String toEmail,
                                      String name,
                                      String subject,
                                      String verificationUrl) {
        try {
            String htmlTemplate = emailTemplateLoader.loadTemplate("verificationtemplate.html");
            htmlTemplate = htmlTemplate
                    .replace("{{name}}", name)
                    .replace("{{verification_link}}", verificationUrl);
            MailgunMessagesApi mailgun = MailgunClient.config(apiKey).createApi(MailgunMessagesApi.class);

            Message message = Message.builder()
                    .from(fromEmail)
                    .to(toEmail)
                    .subject(subject)
                    .html(htmlTemplate)
                    .build();
            MessageResponse response = null;
            if(sendEmailVerificationToggle) {
                 response = mailgun.sendMessage(domain, message);
                System.out.println("Email Verification Mail sent successfully to: " + toEmail);
                System.out.println("Mailgun Response ID: " + response.getId());
            }else{
                System.out.println("Email Verification Mail Not Sent, Toggle is off");
            }
        }catch (Exception ex){
            System.out.println(Arrays.toString(ex.getStackTrace()));
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
