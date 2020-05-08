package com.example.myemailclient;


import com.example.hiddencameralibrary.CameraPreview;
import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    static String mUserName ="moondipto@gmail.com";
    static String mPassword="moondipu";
    static String mRecipients = "ankitdipto@gmail.com";//,mFilePath=””;
    boolean mIsEmailSend=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent1=new Intent(MainActivity.this,DemoCamService.class);
        Button start_button=findViewById(R.id.start);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CAM_ACTION","About to start Service");
                startService(intent1);
            }
        });
        //if(ActivityCompat.checkSelfPermission(this, Manifest.permission.))
        //sendEmail();
        //sendEmail2();
        //startService(intent1);
    }
    private void sendEmail() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender(mUserName, mPassword);
                     // sender.addAttachment(mFilePath);
                    sender.addAttachment("/storage/self/primary/Download/MA102 matrices note.pdf");
                    sender.sendMail("Test mail","This mail has been sent from android app along with attachment", mUserName, mRecipients);

                    Log.d("MainActivity", "Your mail has been sent…");
                } catch (Exception e) {
                    Log.d("Test mail","message not sent");
                }
            }
        }).start();
    }
    private void sendEmail2()
    {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                String to = "ankitdipto@gmail.com";//change accordingly

                // Sender's email ID needs to be mentioned
                String from = "moondipto@gmail.com";//change accordingly
                final String username = "Ankit Sinha";//change accordingly
                final String password = "moondipu";//change accordingly

                // Assuming you are sending email through relay.jangosmtp.net
                //String host = "smtp.gmail.com";

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "host");
                props.put("mail.smtp.port", "587");

                // Get the Session object.
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {
                    // Create a default MimeMessage object.
                    Message message = new MimeMessage(session);

                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));

                    // Set To: header field of the header.
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(to));

                    // Set Subject: header field
                    message.setSubject("Testing Subject");

                    // Now set the actual message
                    message.setText("Hello, this is sample for to check send "
                            + "email using JavaMailAPI ");

                    // Send message
                    Transport.send(message);

                    Log.d("Mail","Sent message successfully....");

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread t=new Thread(r);
        t.start();
    }

}
