/**
 * 
 */
package io.springlets.security.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.support.ClasspathScanningPersistenceUnitPostProcessor;

import io.springlets.security.domain.UserLogin;
import io.springlets.security.jpa.userdetails.JpaUserDetailsService;
import io.springlets.security.repository.UserLoginRepository;
import io.springlets.security.repository.UserLoginRepositoryCustom;
import io.springlets.security.repository.UserLoginRepositoryImpl;
import io.springlets.security.service.api.UserLoginService;
import io.springlets.security.service.impl.UserLoginServiceImpl;

/**
 * @author eruiz
 * 
 */
@Configuration
//@EnableJpaRepositories("io.springlets.security")
public class SpringletsSecurityJpaConfiguration {

  @Autowired
  UserLoginRepository userLoginRepository;

  @Bean
  public UserLoginService userLoginService() {
    return new UserLoginServiceImpl(this.userLoginRepository);
  }

  @Bean
  public JpaRepositoryFactoryBean<UserLoginRepository, UserLogin, Long> userLoginRepository() {
    JpaRepositoryFactoryBean<UserLoginRepository, UserLogin, Long> factory = 
        new JpaRepositoryFactoryBean<UserLoginRepository, UserLogin, Long>();
    factory.setRepositoryInterface(UserLoginRepository.class);
    return factory;
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public BeanPostProcessor springletsSecurityDomainRegistrar() {
    SpringletsSecurityDomainRegistrar postProcessor = 
        new SpringletsSecurityDomainRegistrar();
    return postProcessor;
  }

//  @Bean
//  @Order(Ordered.HIGHEST_PRECEDENCE)
//  public ClasspathScanningPersistenceUnitPostProcessor classpathScanningPersistenceUnitPostProcessor() {
//    ClasspathScanningPersistenceUnitPostProcessor postProcessor = 
//        new ClasspathScanningPersistenceUnitPostProcessor("io.springlets.security.domain");
//    postProcessor.setMappingFileNamePattern("classpath:springlets-authentication-orm.xml");
//    return postProcessor;
//  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SpringletsSecurityJpaConfigurerAdapter springletsSecurityJpaConfigurerAdapter() {
    return new SpringletsSecurityJpaConfigurerAdapter(
        new JpaUserDetailsService(userLoginService()));
  }
}
