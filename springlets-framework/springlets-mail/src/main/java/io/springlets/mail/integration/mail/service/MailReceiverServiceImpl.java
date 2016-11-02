package io.springlets.mail.integration.mail.service;

import io.springlets.mail.integration.mail.api.MailReceiverService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * = Implementation of {@link MailReceiverService}
 *
 * Make the receipt of emails.
 */
@Service
public class MailReceiverServiceImpl implements MailReceiverService {

  @Value("${springlets.mail.receiver.host:}")
  private String host;

  @Value("${springlets.mail.receiver.port:}")
  private String port;

  @Value("${springlets.mail.receiver.protocol:}")
  private String protocol;

  @Value("${springlets.mail.receiver.username:}")
  private String username;

  @Value("${springlets.mail.receiver.password:}")
  private String password;

  @Value("${springlets.mail.receiver.starttls.enable:true}")
  private String starttlsEnabled;

  @Value("${springlets.mail.receiver.jndi.name:}")
  private String jndiName;

  @Override
  public List<SimpleMailMessage> getEmails()
      throws MessagingException, IOException, NamingException {
    Session emailSession = null;
    Store store = null;
    Folder emailFolder = null;
    List<SimpleMailMessage> receivedEmails = new ArrayList<SimpleMailMessage>();
    try {
      if (!StringUtils.isEmpty(jndiName)) {
        InitialContext ic = new InitialContext();
        emailSession = (Session) ic.lookup(jndiName);
        store = emailSession.getStore();
        store.connect();
      }
      else {

        // Set connection properties
        Properties properties = new Properties();
        String prefix = "mail.".concat(protocol);
        properties.put(String.format("%s.host", prefix), host);
        properties.put(String.format("%s.port", prefix), port);
        properties.put("mail.store.protocol", protocol);
        properties.put(String.format("%s.starttls.enable", prefix), starttlsEnabled);

        // Create the session and the object Store to get the emails
        emailSession = Session.getDefaultInstance(properties);
        store = emailSession.getStore();
        store.connect(username, password);
      }

      // Get the folder that contains the emails and open it
      emailFolder = store.getFolder("INBOX");
      emailFolder.open(Folder.READ_ONLY);

      // Get emails that haven't been read
      Message[] messagesReceived = emailFolder.getMessages();
      for (Message message : messagesReceived) {
        Object content = message.getContent();
        String body = null;
        if (content instanceof Multipart) {
          Multipart multipart = (Multipart) content;
          BodyPart bodyPart = multipart.getBodyPart(0);
          if (bodyPart != null) {
            body = bodyPart.getContent().toString();
          }
        }
        else if (content instanceof String) {
          body = (String) content;
        }
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(message.getSubject());
        email.setText(body);
        email.setFrom(message.getFrom()[0].toString());
        email.setSentDate(message.getSentDate());
        receivedEmails.add(email);
      }
    }
    finally {
      // Close objects Folder and Store
      if (emailFolder != null && emailFolder.isOpen()) {
        emailFolder.close(false);
      }
      if (store != null && store.isConnected()) {
        store.close();
      }

    }
    return receivedEmails;

  }

}
