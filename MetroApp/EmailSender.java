package MetroApp;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String senderEmail, String senderPassword, String smtpHost, String smtpPort, String recipientEmail, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email. Error: " + e.getMessage());
        }
    }
}



//javac -cp "lib\mysql-connector-j-9.4.0.jar;lib\jakarta.mail-2.0.1.jar;lib\jakarta.activation-api-2.1.3.jar" -d bin MetroApp\MetroApp.java MetroApp\Graph_M.java MetroApp\MapView.java MetroApp\EmailSender.java
//java -cp "bin;lib\mysql-connector-j-9.4.0.jar;lib\jakarta.mail-2.0.1.jar;lib\jakarta.activation-api-2.1.3.jar" MetroApp.MetroApp