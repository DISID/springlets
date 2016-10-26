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

package io.springlets.security.config;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.springlets.security.jpa.JpaUserDetailsService;

/**
 * A {@link SecurityConfigurer} that registers a InMemoryUserDetailsManager to have
 * in memory authentication for easier development.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
class SpringletsSecurityInMemoryAuthenticationConfigurer extends GlobalAuthenticationConfigurerAdapter {

  /**
   * {@inheritDoc}
   * 
   * Initializes the InMemoryUserDetailsManager with the following users:
   * 
   * * user/password, role USER
   * * admin/password, role ADMIN
   * 
   * The method {@link AuthenticationManagerBuilder#inMemoryAuthentication()} is not
   * used to avoid to override the default UserDetailsService. Note Springlets configure
   * the JpaUserDetailsService as the default one.
   * 
   * Use the passwords as given, they aren't encrypted.
   */
  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {

    // Añadir el AuthenticationProvider en memoria preconfigurado con un usuario
    // especifico de la aplicación que tendrá acceso a las vistas de monitorización
    // como /healthcheck
    InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemAuthProvConfigurer =
        new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>();

    inMemAuthProvConfigurer
      .withUser("user").password("password").roles("USER");

    inMemAuthProvConfigurer
      .withUser("admin").password("password").roles("USER", "ADMIN");

    inMemAuthProvConfigurer.configure(auth);
  }

}
