package io.springlets.mail.integration.mail.api;

import io.springlets.mail.domain.DefaultMessage;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.NamingException;

/**
 * = API emails receiver
 *
 * Interface that adds support to receive emails
 */
public interface EmailReceiverService {

  /**
   * Get emails of configured account
   *
   * @return receivedEmails List of {@link DefaultMessage} that haven't been read
   * @throws MessagingException Exception produced when connecting or retrieve emails
   * @throws IOException Exception produced when get an email
   * @throws NamingException Exception produced when use jndi connection
   */
  public List<DefaultMessage> getEmails()
      throws MessagingException, IOException, NamingException;

}
