/**
 * 
 */
package io.springlets.security.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.springlets.security.jpa.userdetails.JpaUserDetailsService;
import io.springlets.security.service.api.UserLoginService;

/**
 * @author eruiz
 * 
 */
@Configuration
@EnableJpaRepositories("io.springlets.security")
public class SpringletsSecurityJpaConfiguration {

  @Autowired
  protected UserLoginService userLoginService;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SpringletsSecurityJpaConfigurerAdapter springletsSecurityJpaConfigurerAdapter() {
    return new SpringletsSecurityJpaConfigurerAdapter(
        new JpaUserDetailsService(this.userLoginService));
  }
}
