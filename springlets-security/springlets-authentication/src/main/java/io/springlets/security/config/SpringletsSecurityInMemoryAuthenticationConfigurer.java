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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;

/**
 * A {@link SecurityConfigurer} that registers a InMemoryUserDetailsManager to have
 * in memory authentication for easier development.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
class SpringletsSecurityInMemoryAuthenticationConfigurer
    extends GlobalAuthenticationConfigurerAdapter implements ApplicationContextAware {

  private static final Logger LOG =
      LoggerFactory.getLogger(SpringletsSecurityInMemoryAuthenticationConfigurer.class);

  private ConfigurableApplicationContext applicationContext;

  private SpringletsSecurityProperties securityProperties;

  public SpringletsSecurityInMemoryAuthenticationConfigurer(SpringletsSecurityProperties properties) {
    this.securityProperties = properties;
  }

  @Override
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    this.applicationContext = (ConfigurableApplicationContext) ctx;
  }

  /**
   * {@inheritDoc}
   * 
   * Initializes the InMemoryUserDetailsManager with the following users:
   * 
   * * user/password, role USER
   * * admin/password, role ADMIN
   * 
   * Neither the method {@link AuthenticationManagerBuilder#inMemoryAuthentication()} 
   * nor {@link AuthenticationManagerBuilder#apply(SecurityConfigurerAdapter)} 
   * are used to avoid to override the default UserDetailsService. Note that Springlets 
   * configure the JpaUserDetailsService as the default one.
   * 
   * Use the passwords as given, they aren't encrypted.
   */
  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {

    // clear credentials after success authentication
    auth.eraseCredentials(securityProperties.getAuth().getInMemory().isEraseCredentials());

    // Configure a in memory AuthenticationProvider with two users
    InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemAuthProvConfigurer =
        new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>();

    String userPassw = UUID.randomUUID().toString();
    inMemAuthProvConfigurer.withUser("user").password(userPassw).roles("USER");

    String adminPassw = UUID.randomUUID().toString();
    inMemAuthProvConfigurer.withUser("admin").password(adminPassw).roles("USER",
        "ADMIN");

    LOG.info("\n\nDefault security users [USERNAME : PASSWORD : ROLES]:\n" +
             "\n  [user  : {} : ROLE_USER]\n" +
             "\n  [admin : {} : ROLE_USER, ROLE_ADMIN]\n", userPassw , adminPassw);

    // Add users credentials to Environment for dev purposes

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("springlets.security.auth.in-memory.enabled", true);
    map.put("springlets.security.auth.in-memory.user.name", "user");
    map.put("springlets.security.auth.in-memory.user.password", userPassw);

    MutablePropertySources sources = this.applicationContext.getEnvironment().getPropertySources();
    sources.addLast(new MapPropertySource("springlets", map));

    inMemAuthProvConfigurer.configure(auth);
  }
}