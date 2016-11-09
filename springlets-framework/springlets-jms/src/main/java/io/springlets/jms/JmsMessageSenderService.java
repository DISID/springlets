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
package io.springlets.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Message;

/**
 * = Service to send JMS messages
 *
 * @author Manuel Iborra
 */
@Service
public class JmsMessageSenderService {

	public static final Logger LOG = LoggerFactory.getLogger(JmsMessageSenderService.class);

	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	private DestinationResolver destinationResolver;

	@Autowired
	private MessageConverter messageConverter;

	/**
	 * Converts and sends the object established into second parameter to Queue
	 * defined by name established in 'queueName' parameter.
	 * {@link SimpleMessageConverter} is used for conversion.
	 *
	 * @param queueName Queue which will receive the message
	 * @param objToSend Object to send
	 * @throws JMSException
	 */
	public void convertAndSend(String queueName, Object objToSend) throws JMSException {
		Connection connection = this.connectionFactory.createConnection();
		try {

			// Create the MessageProducer
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

			Queue theQueue = (Queue) this.destinationResolver.resolveDestinationName(session, queueName, true);

			try {

				MessageProducer messageProducer = session.createProducer(theQueue);

				// Convert the object
				MessageConverter msgConverter = this.messageConverter;
				if (msgConverter == null) {
					msgConverter = new SimpleMessageConverter();
				}
				Message message = msgConverter.toMessage(objToSend, session);

				// Start Queue connection
				connection.start();

				// Send the message
				messageProducer.send(message);

				if (LOG.isInfoEnabled()) {
					LOG.info("JMS NOTIFICATION SENT. Destination: '{}'. Message: '{}'.", queueName, objToSend);
				}

			} finally {
				session.close();
			}
		} finally {
			connection.close();
		}
	}
}
