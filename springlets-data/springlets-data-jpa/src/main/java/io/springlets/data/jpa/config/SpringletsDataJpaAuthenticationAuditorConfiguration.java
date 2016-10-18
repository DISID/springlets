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
package io.springlets.data.jpa.config;

import io.springlets.data.domain.AuthenticationAuditorAware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to register the {@link AuthenticationAuditorAware}
 * class as the {@link AuditorAware} implementation to use, as well as
 * enabling the Spring Data's JPA auditing
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableJpaAuditing
public class SpringletsDataJpaAuthenticationAuditorConfiguration {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return new AuthenticationAuditorAware();
  }

}
