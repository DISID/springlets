/**
 * 
 */
package io.springlets.security.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringletsSecurityConfiguration {

  private SpringletsSecurityProperties securityProperties;

  @Autowired(required = false)
  public void setSpringletsSecurityProperties(SpringletsSecurityProperties properties) {
    this.securityProperties = properties;
  }

  /**
   * Create and register a {@link GlobalAuthenticationConfigurerAdapter} bean configured 
   * with {@link #securityProperties}.
   * 
   * Note that {@link #securityProperties} is null when the property 
   * `springlets.security.auth.in-memory.enabled` is false, in that case this method return null.
   * 
   * By returning null causes that the method {@link AnnotationConfigWebApplicationContext#getBean(Class)}
   * returns null in spite of throwing the {@link BeansException} exception.
   * 
   * @return the SpringletsSecurityInMemoryAuthenticationConfigurer
   */
  @Bean
  public SpringletsSecurityInMemoryAuthenticationConfigurer inMemoryAuthenticationConfigurer() {
    if(this.securityProperties == null) {
      return null;
    }
    return new SpringletsSecurityInMemoryAuthenticationConfigurer(securityProperties);
  }
}
