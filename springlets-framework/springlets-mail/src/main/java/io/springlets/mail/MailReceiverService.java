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


import org.springframework.mail.SimpleMailMessage;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.NamingException;

/**
 * = API emails receiver
 *
 * Interface that adds support to receive emails
 */
public interface MailReceiverService {

  /**
   * Get emails of configured account
   *
   * @return receivedEmails List of {@link DefaultMessage} that haven't been read
   * @throws MessagingException Exception produced when connecting or retrieve emails
   * @throws IOException Exception produced when get an email
   * @throws NamingException Exception produced when use jndi connection
   */
  public List<SimpleMailMessage> getEmails()
      throws MessagingException, IOException, NamingException;

}
