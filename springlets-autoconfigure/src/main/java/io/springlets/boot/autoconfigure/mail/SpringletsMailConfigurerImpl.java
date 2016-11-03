/*
 * Copyright 2012-2016 the original author or authors.
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

package io.springlets.boot.autoconfigure.mail;

import io.springlets.mail.config.SpringletsMailConfigurer;
import io.springlets.mail.config.SpringletsMailSettings;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * A {@link SpringletsMailConfigurer} that applies configuration items from the
 * Spring Boot `springlets.mail.receiver` namespace to Springlets Mail.
 *
 * Based on https://github.com/spring-projects/spring-data-rest[Spring Data REST] project.
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
class SpringletsMailConfigurerImpl implements SpringletsMailConfigurer {

	@Autowired
	private SpringletsMailProperties mailProperties;

	@Override
	public void configureSpringletsMailSettings(SpringletsMailSettings config) {
		this.mailProperties.applyTo(config);
	}

}
