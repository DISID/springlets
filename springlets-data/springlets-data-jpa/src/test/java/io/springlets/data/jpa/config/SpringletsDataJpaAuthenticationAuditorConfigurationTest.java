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

import static org.assertj.core.api.Assertions.assertThat;

import io.springlets.data.domain.AuthenticationAuditorAware;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.AuditorAware;

/**
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 *
 */
public class SpringletsDataJpaAuthenticationAuditorConfigurationTest {

  SpringletsDataJpaAuthenticationAuditorConfiguration configuration;

  @Before
  public void setup() {
    configuration = new SpringletsDataJpaAuthenticationAuditorConfiguration();
  }

  /**
   * Test method for {@link io.springlets.data.jpa.config.SpringletsDataJpaAuthenticationAuditorConfiguration#authenticationAuditorAware()}.
   */
  @Test
  public void testAuthenticationAuditorAware() {
    // Setup

    // Exercise
    AuditorAware<String> authenticationAuditorAware = configuration.authenticationAuditorAware();

    // Verify
    assertThat(authenticationAuditorAware).isNotNull()
        .isInstanceOf(AuthenticationAuditorAware.class);
  }

}
