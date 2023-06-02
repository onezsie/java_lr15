package mainPackage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SendEmail {
    public static void sendEmail(String token, String path) {
        String to = "z98dima@mail.ru";
        String from = "z98dima@gmail.com";
        Properties properties = System.getProperties();

        // Настройка mail сервера
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("z98dima@gmail.com", token);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Внимание! Загрязнение воздуха.");

            Multipart multipart = new MimeMultipart();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            MimeBodyPart textPart = new MimeBodyPart();
            try {
                File f = new File(path);
                attachmentPart.attachFile(f);
                textPart.setText("В данный момент в городе обнаружено превышение вредных веществ в воздухе. " +
                        "Данные во вложении.");
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {
                e.printStackTrace();
            }
            message.setContent(multipart);
            System.out.println("Отправление сообщения...");
            Transport.send(message);
            System.out.println("Сообщение успешно отправлено.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
