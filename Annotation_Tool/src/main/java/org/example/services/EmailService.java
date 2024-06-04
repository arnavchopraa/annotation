package org.example.services;

import org.example.exceptions.EmailException;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class EmailService {
    private final String username = "annotationtool.sp@gmail.com";
    private final String password = "nvud kbac dhxp jyut";
    private final String host = "smtp.gmail.com";
    private final String port = "465";

    /**
     * Sends an email to a recipient, from the address annotationtool.sp@gmail.com
     *
     * @param recipient The intended recipient of the email
     * @param subject The subject of the email
     * @param content The body of the email
     * @throws EmailException if something went wrong while sending the email
     */
    public void sendEmail(String recipient, String subject, String content) throws EmailException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

        } catch (MessagingException e) {
            throw new EmailException("Sender address could not be processed");
        }

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        try {
            mimeBodyPart.setContent(content, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new EmailException("Content could not be processed");
        }

        Multipart multipart = new MimeMultipart();
        try {
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
        } catch (MessagingException e) {
            throw new EmailException("Content could not be added to email");
        }

        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new EmailException(e);
        }
    }

    /**
     * Generates a random password of length 8 for initial authentication
     *
     * @return a string consisting of 8 random characters.
     */
    public String generateRandomCode() {
        int passwordSize = 8;
        Random random = new Random();
        String regex = "[0-9a-zA-Z]";
        StringBuilder stringBuilder = new StringBuilder();
        while(passwordSize > 0) {
            int no = random.nextInt(75) + 48;
            char c = (char) no;
            if(Character.toString(c).matches(regex)) {
                stringBuilder.append(c);
                passwordSize--;
            }
        }
        return stringBuilder.toString();
    }
}
