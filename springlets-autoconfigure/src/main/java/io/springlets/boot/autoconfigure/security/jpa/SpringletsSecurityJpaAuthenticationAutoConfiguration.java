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
package io.springlets.boot.autoconfigure.security.jpa;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.springlets.security.config.EnableSpringletsSecurityAuthentication;
import io.springlets.security.config.SpringletsSecurityProperties;
import io.springlets.security.jpa.JpaUserDetailsService;

/**
 * @{@link SpringletsSecurityJpaAuthenticationAutoConfiguration Auto-configuration} to setup
 * Spring Security authentication based on JPA Authentication Provider.
 *
 * @see EnableSpringletsSecurityAuthentication
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@AutoConfigureAfter(JpaBaseConfiguration.class)
@ConditionalOnClass(JpaUserDetailsService.class)
@Configuration
@EnableJpaRepositories(basePackageClasses = JpaUserDetailsService.class)
@EntityScan(basePackageClasses = JpaUserDetailsService.class)
@EnableSpringletsSecurityAuthentication
public class SpringletsSecurityJpaAuthenticationAutoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "springlets.security.auth.in-memory", name = "enabled", matchIfMissing = true)
  @ConfigurationProperties(prefix = "springlets.security")
  public SpringletsSecurityProperties springletsSecurityProperties() {
      return new SpringletsSecurityProperties();
  }
}