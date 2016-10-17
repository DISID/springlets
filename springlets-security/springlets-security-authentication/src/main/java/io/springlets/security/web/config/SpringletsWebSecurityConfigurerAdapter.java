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

package io.springlets.security.web.config;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
class SpringletsWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  boolean disableConcurrency = true;

  // Don't make final to allow test cases faking them
  private static String DEFAULT_POLICY_DIRECTIVES = "script-src 'self' 'unsafe-inline' ";

  private static String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";

  private static String LOGIN_FORM_URL = "/login";

  private static String X_CONTENT_SECURITY_POLICY_HEADER = "X-Content-Security-Policy";

  private static String X_WEBKIT_CSP_POLICY_HEADER = "X-WebKit-CSP";

  /**
   * {@inheritDoc}
   * 
   * Initializes the default {@link UserDetailsService} causing the {@link AuthenticationManagerBuilder} 
   * creates automatically the {@link DaoAuthenticationProvider} that delegates on the given
   * {@link UserDetailsService}.
   * 
   * Also setup the {@link BCryptPasswordEncoder} to use with the {@link DaoAuthenticationProvider}
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // Session management
    
    if (disableConcurrency) {
      http.sessionManagement().maximumSessions(1).expiredUrl("/login?expired");
    }

    // CSP settings

    http.headers()
        .addHeaderWriter(new StaticHeadersWriter(X_CONTENT_SECURITY_POLICY_HEADER,
            DEFAULT_POLICY_DIRECTIVES))
        .addHeaderWriter(new StaticHeadersWriter(CONTENT_SECURITY_POLICY_HEADER,
            DEFAULT_POLICY_DIRECTIVES))
        .addHeaderWriter(
            new StaticHeadersWriter(X_WEBKIT_CSP_POLICY_HEADER, DEFAULT_POLICY_DIRECTIVES));

    // Authentication

    http.formLogin().loginPage(LOGIN_FORM_URL).permitAll().and()

    // se añade redirección personalizada en caso de excepción por acceso no autorizado
    .exceptionHandling().authenticationEntryPoint(new SpringletsWebAuthenticationEntryPoint())
    .accessDeniedHandler(new SpringletsWebAccessDeniedHandlerImpl());

  }
}
