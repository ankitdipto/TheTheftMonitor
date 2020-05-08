package com.example.myemailclient;

import javax.mail.Authenticator;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Service;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public final class GMailSender extends Authenticator {

    private String mailhost ;
    final private String user;
    final private String password;
    private Session session;

    private Multipart _multipart = new MimeMultipart();
    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(final String user, final String password) {
        this.user = user;
        this.password = password;
        mailhost="ankitdipto@gmail.com";
        Properties props = new Properties();
        /*
         * props.setProperty(“mail.transport.protocol”, “smtp”);
         * props.setProperty(“mail.host”, mailhost); props.put(“mail.smtp.auth”,
         * “true”); props.put(“mail.smtp.port”, “465”);
         * props.put(“mail.smtp.socketFactory.port”, “465”);
         * props.put(“mail.smtp.socketFactory.class”,
         * “javax.net.ssl.SSLSocketFactory”);
         * props.put(“mail.smtp.socketFactory.fallback”, “false”);
         * props.setProperty(“mail.smtp.quitwait”, “false”);
         */

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        /*props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");*/
        props.put("mail.smtp.ssl.enable","true");
        props.setProperty("mail.host", mailhost);
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    /**
     * Use
     * {@link #sendMail(String subject, String body, String sender, String recipients)}
     *
     */

    public synchronized void sendMail(String subject, String body,
                                      String sender, String recipients) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(
                    body.getBytes(),"multipart/mixed"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);

// Put parts in message
            message.setContent(_multipart);
            if (recipients.indexOf(",") > 0)
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(recipients));

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAttachment(String filename) throws Exception {
/* BodyPart messageBodyPart = new MimeBodyPart();
DataSource source = new FileDataSource(filename);
messageBodyPart.setDataHandler(new DataHandler(source));
messageBodyPart.setFileName(“download Pdf”);

_multipart.addBodyPart(messageBodyPart);*/
/* Message message = new MimeMessage(session);
Multipart _multipart = new MimeMultipart();
BodyPart messageBodyPart = new MimeBodyPart();
File sdCard =Environment.getExternalStorageDirectory();
String path=sdCard.getAbsolutePath() + “/pdf/”+”/test.pdf”;
messageBodyPart.setFileName(path);
messageBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
_multipart.addBodyPart(messageBodyPart);

// Put parts in message
message.setContent(_multipart);*/

        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        _multipart.addBodyPart(messageBodyPart);

        BodyPart messageBodyPart2 = new MimeBodyPart();
        messageBodyPart2.setText("subject43");

        _multipart.addBodyPart(messageBodyPart2);
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
else
            return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}

