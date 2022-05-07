package TarsinApp.Scheduler;

import TarsinApp.FinalProjectApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Slf4j
public class SendMailScheduler {

    @Scheduled(cron = "5 1 * * * *")
    public static void sendMessage(){
        String sendTo = "boolnworld@gmail.com";
        String sendFrom = "itschooltesting@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("itschooltesting@gmail.com", "ItSchool123");
            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            message.setSubject("My project is finally done");
            message.setText("Hey. I just wanted to say that my project is done. This is proof that i know how to use Java Scheduler!  Bye :D");
            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException m) {
            m.printStackTrace();
        }
    }
}
