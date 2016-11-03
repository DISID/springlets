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
package io.springlets.mail.config;

import io.springlets.mail.MailReceiverService;
import io.springlets.mail.MailReceiverServiceImpl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * Configuration class to register the following beans:
 *
 * * {@link StringTrimmerAdvice}.
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
public class SpringletsMailConfiguration implements InitializingBean {

	/**
	 * Each SpringletsMailConfigurer will configure a feature of the Springlets
	 * Mail
	 */
	@Autowired(required = false)
	List<SpringletsMailConfigurer> configurers = Collections.emptyList();

	private SpringletsMailConfigurerDelegate configurerDelegate;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.configurerDelegate = new SpringletsMailConfigurerDelegate(configurers);
	}

	@Bean
	public MailReceiverService mailReceiverService() {
		return new MailReceiverServiceImpl(configMailSettings());
	}

	/**
	 * Main configuration for the Springlets Mail.
	 */
	@Bean
	public SpringletsMailSettings configMailSettings() {

		SpringletsMailSettings mailSettings = new SpringletsMailSettings();

		configurerDelegate.configureSpringletsMailSettings(mailSettings);

		return mailSettings;
	}

}
