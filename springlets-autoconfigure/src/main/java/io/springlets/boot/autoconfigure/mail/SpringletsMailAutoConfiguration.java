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
package io.springlets.boot.autoconfigure.mail;

import io.springlets.mail.config.EnableSpringletsMail;
import io.springlets.mail.config.SpringletsMailConfiguration;
import io.springlets.mail.config.SpringletsMailConfigurer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Springlets mail
 * integration.
 *
 * Activates when no {@link SpringletsMailConfiguration} is found.
 *
 * Once in effect, the auto-configuration allows to configure any property of
 * {@link SpringletsMailConfiguration} using the `springlets.mail.receiver` prefix
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
@ConditionalOnClass(SpringletsMailConfiguration.class)
@ConditionalOnMissingBean(SpringletsMailConfiguration.class)
@ConditionalOnProperty(prefix = "springlets.mail.receiver", name = "enabled", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(SpringletsMailProperties.class)
@EnableSpringletsMail
public class SpringletsMailAutoConfiguration {

  @Bean
  public SpringletsMailConfigurer springBootSpringletsMailConfigurer() {
    return new SpringletsMailConfigurerImpl();
  }

}
