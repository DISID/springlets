package io.springlets.mail.integration.mail.service;

import io.springlets.mail.domain.DefaultMessage;
import io.springlets.mail.integration.mail.api.EmailReceiverService;

import org.springframework.beans.factory.annotation.Value;
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
 * = Implementation of {@link EmailReceiverService}
 *
 * Make the receipt of emails.
 */
@Service
public class EmailReceiverServiceImpl implements EmailReceiverService {

  @Value("${application.mail.inbox.host:}")
  private String host;

  @Value("${application.mail.inbox.port:}")
  private String port;

  @Value("${application.mail.inbox.protocol:}")
  private String protocol;

  @Value("${application.mail.inbox.user:}")
  private String username;

  @Value("${application.mail.inbox.password:}")
  private String password;

  @Value("${application.mail.inbox.starttls.enable:true}")
  private String starttlsEnabled;

  @Value("${application.mail.inbox.jndi.name:}")
  private String jndiName;

  @Override
  public List<DefaultMessage> getEmails()
      throws MessagingException, IOException, NamingException {
    Session emailSession = null;
    Store store = null;
    Folder emailFolder = null;
    List<DefaultMessage> receivedEmails = new ArrayList<DefaultMessage>();
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
        DefaultMessage email = new DefaultMessage(message.getSubject(), body,
            message.getFrom()[0].toString());
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
