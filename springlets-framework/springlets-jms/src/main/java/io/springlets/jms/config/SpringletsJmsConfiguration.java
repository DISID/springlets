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
package io.springlets.jms.config;

import io.springlets.jms.JmsMessageSenderService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * Configuration class to register {@link JmsMessageSenderService}.
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
public class SpringletsJmsConfiguration {

	/**
	 *
	 * {@link MessageConverter} bean configured with
	 * {@link SimpleMessageConverter} implementation.
	 *
	 * If you want to replace the default MessageConverter completely, define
	 * a @{@link Bean} of that type and mark it as @{@link Primary}.
	 *
	 * @return SimpleMessageConverter
	 */
	@Bean
	public MessageConverter messageConverter() {
		return new SimpleMessageConverter();
	}

	/**
	 * Service to send JMS messages
	 *
	 * @return {@link JmsMessageSenderService}
	 */
	@Bean
	public JmsMessageSenderService jmsMessageSendingService() {
		return new JmsMessageSenderService();
	}
}
