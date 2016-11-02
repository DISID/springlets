package io.springlets.mail.domain;

/**
 * = Data Transfer Object _DefaultMessage_
 *
 * Basic data of email.
 */
public class DefaultMessage {

  private final String subject;

  private final String content;

  private final String from;

  /**
   * Constructor
   *
   * @param subject
   * @param content
   * @param from
   */
  public DefaultMessage (String subject, String content, String from) {
    this.subject = subject;
    this.content = content;
    this.from = from;
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }

  public String getFrom() {
    return from;
  }

}
