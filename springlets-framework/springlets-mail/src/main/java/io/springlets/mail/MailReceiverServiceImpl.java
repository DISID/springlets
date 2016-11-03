/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.springlets.mail;

import io.springlets.mail.config.SpringletsMailSettings;

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

  private SpringletsMailSettings mailSettings;

  public MailReceiverServiceImpl(SpringletsMailSettings mailSettings) {
 	this.mailSettings = mailSettings;
 }

  @Override
  public List<SimpleMailMessage> getEmails()
      throws MessagingException, IOException, NamingException {
    Session emailSession = null;
    Store store = null;
    Folder emailFolder = null;
    List<SimpleMailMessage> receivedEmails = new ArrayList<SimpleMailMessage>();
    try {
      if (!StringUtils.isEmpty(mailSettings.getJndiName())) {
        InitialContext ic = new InitialContext();
        emailSession = (Session) ic.lookup(mailSettings.getJndiName());
        store = emailSession.getStore();
        store.connect();
      }
      else {

        // Set connection properties
        Properties properties = new Properties();
        String prefix = "mail.".concat(mailSettings.getProtocol());
        properties.put(String.format("%s.host", prefix), mailSettings.getHost());
        properties.put(String.format("%s.port", prefix), mailSettings.getPort());
        properties.put("mail.store.protocol", mailSettings.getProtocol());
        properties.put(String.format("%s.starttls.enable", prefix), mailSettings.getStarttlsEnabled());

        // Create the session and the object Store to get the emails
        emailSession = Session.getDefaultInstance(properties);
        store = emailSession.getStore();
        store.connect(mailSettings.getUsername(), mailSettings.getPassword());
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
