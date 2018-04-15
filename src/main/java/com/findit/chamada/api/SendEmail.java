package com.findit.chamada.api;

import com.sun.mail.smtp.SMTPTransport;

import com.sendgrid.*;
import org.jooq.util.derby.sys.Sys;

import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

public class SendEmail {

    public static void Send__(String recipientEmail, String ccEmail, String title, String message) throws AddressException, MessagingException {
        System.err.println("Send__");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        final String username = System.getenv("SENDGRID_USERNAME");
        final String password = System.getenv("SENDGRID_PASSWORD");
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.socketFactory.port", "587");
        props.setProperty("mail.smtps.auth", "true");

    /*
    If set to false, the QUIT command is sent and the connection is immediately closed. If set
    to true (the default), causes the transport to wait for the response to the QUIT command.

    ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
            http://forum.java.sun.com/thread.jspa?threadID=5205249
            smtpsend.java - demo program from javamail
    */
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

        if (ccEmail.length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
        }

        msg.setSubject(title);
        msg.setText(message, "utf-8");
        msg.setSentDate(new Date());

        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }

    public static void send(String fromEmail, String toEmail, String subject, String htmlContent) throws IOException {
        Email from = new Email(System.getenv("SENDGRID_USERNAME"));
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }
}
