/**
 * 
 */
package io.springlets.security.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import io.springlets.security.jpa.JpaUserDetailsService;
import io.springlets.security.jpa.repository.LoginRoleRepository;
import io.springlets.security.jpa.repository.UserLoginRepository;
import io.springlets.security.jpa.repository.UserLoginRoleRepository;
import io.springlets.security.jpa.service.api.LoginRoleService;
import io.springlets.security.jpa.service.api.UserLoginRoleService;
import io.springlets.security.jpa.service.api.UserLoginService;
import io.springlets.security.jpa.service.impl.LoginRoleServiceImpl;
import io.springlets.security.jpa.service.impl.UserLoginRoleServiceImpl;
import io.springlets.security.jpa.service.impl.UserLoginServiceImpl;

/**
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringletsSecurityJpaAuthenticationConfiguration {

  @Autowired UserLoginRepository userLoginRepository;
  @Autowired LoginRoleRepository loginRoleRepository;
  @Autowired UserLoginRoleRepository userLoginRoleRepository;

  @Bean
  public UserLoginService userLoginService() {
      return new UserLoginServiceImpl(this.userLoginRepository);
  }

  @Bean
  public LoginRoleService loginRoleService() {
      return new LoginRoleServiceImpl(this.loginRoleRepository);
  }

  @Bean
  public UserLoginRoleService userLoginRoleService() {
      return new UserLoginRoleServiceImpl(this.userLoginRoleRepository);
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SpringletsSecurityJpaAuthenticationConfigurer springletsSecurityJpaConfigurerAdapter() {
    return new SpringletsSecurityJpaAuthenticationConfigurer(
        new JpaUserDetailsService(userLoginService()));
  }

}
