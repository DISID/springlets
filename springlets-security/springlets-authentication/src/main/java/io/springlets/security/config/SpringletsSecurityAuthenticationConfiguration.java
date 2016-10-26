/**
 * 
 */
package io.springlets.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringletsSecurityAuthenticationConfiguration {

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SpringletsSecurityInMemoryAuthenticationConfigurer inMemoryAuthenticationConfiguration() {
    return new SpringletsSecurityInMemoryAuthenticationConfigurer();
  }
}
