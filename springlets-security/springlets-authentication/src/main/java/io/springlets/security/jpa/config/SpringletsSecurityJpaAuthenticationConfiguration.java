/**
 * 
 */
package io.springlets.security.jpa.config;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.support.ClasspathScanningPersistenceUnitPostProcessor;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ResourceUtils;

import io.springlets.security.jpa.JpaUserDetailsService;
import io.springlets.security.jpa.domain.UserLogin;
import io.springlets.security.jpa.repository.UserLoginRepository;
import io.springlets.security.jpa.repository.UserLoginRepositoryCustom;
import io.springlets.security.jpa.repository.UserLoginRepositoryImpl;
import io.springlets.security.jpa.service.api.UserLoginService;
import io.springlets.security.jpa.service.impl.UserLoginServiceImpl;

/**
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@EnableJpaRepositories("io.springlets.security")
public class SpringletsSecurityJpaAuthenticationConfiguration
    implements ApplicationContextAware { //, ImportAware {

  protected static String SPRINGLETS_SECURITY_BASE_PACKAGE = "io.springlets.security";

//  @Autowired UserLoginRepository userLoginRepository;

  @Autowired DataSource dataSource;
  @Autowired ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider;
  @Autowired ObjectProvider<PersistenceUnitManager> persistenceUnitManagerProvider;
  @Autowired JpaVendorAdapter vendor;
  @Autowired JpaProperties jpaProperties;

  //  BeanFactory beanFactory;
  ApplicationContext applicationContext;

  AnnotationAttributes enableSpringletsSecurityAnnotationAttributes;

  //  @Override
  //  public void setBeanFactory(BeanFactory bf) throws BeansException {
  //    this.beanFactory = bf;
  //  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.applicationContext = ac;
  }

//  @Override
//  public void setImportMetadata(AnnotationMetadata importMetadata) {
//    Map<String, Object> annotationAttributes = importMetadata
//        .getAnnotationAttributes(EnableSpringletsSecurityJpaAuthentication.class.getName());
//    this.enableSpringletsSecurityAnnotationAttributes = AnnotationAttributes.fromMap(annotationAttributes);
//  }

  @Bean
  public UserLoginService userLoginService() {
    return new UserLoginServiceImpl(userLoginRepository());
//      return new UserLoginServiceImpl(this.userLoginRepository);
  }


  @Bean
  public UserLoginRepository userLoginRepository() {
    //  public JpaRepositoryFactoryBean<UserLoginRepository, UserLogin, Long> userLoginRepositoryFactory() {
    JpaRepositoryFactoryBean<UserLoginRepository, UserLogin, Long> factory =
        new JpaRepositoryFactoryBean<UserLoginRepository, UserLogin, Long>();
    EntityManagerFactory entityManagerFactory = entityManagerFactory().getObject();
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    factory.setRepositoryInterface(UserLoginRepository.class);
    factory.setCustomImplementation(new UserLoginRepositoryImpl());
    factory.setEntityManager(entityManager);
    factory.setMappingContext(
        new JpaMetamodelMappingContext(Collections.singleton(entityManager.getMetamodel())));
    factory.setBeanFactory(this.applicationContext);
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();

    entityManagerFactoryBean
        .setPersistenceUnitManager(persistenceUnitManagerProvider.getIfAvailable());

    entityManagerFactoryBean.setJpaVendorAdapter(vendor);

    if (jtaTransactionManagerProvider.getIfAvailable() != null) {
      entityManagerFactoryBean.setJtaDataSource(dataSource);
    } else {
      entityManagerFactoryBean.setDataSource(dataSource);
    }

    //    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    //    scanner.setEnvironment(this.applicationContext.getEnvironment());
    //    scanner.setResourceLoader(this.applicationContext);
    //    scanner.addIncludeFilter(new AnnotationTypeFilter());


    entityManagerFactoryBean.setPackagesToScan(SPRINGLETS_SECURITY_BASE_PACKAGE, "com.springsource.petclinic");
//        this.enableSpringletsSecurityAnnotationAttributes.getString("basePackages"));

    //    entityManagerFactoryBean.setMappingResources("classpath:springlets-authentication-orm.xml");
    //
    //    entityManagerFactoryBean.setPersistenceUnitPostProcessors(classpathScanningPersistenceUnitPostProcessor());

    entityManagerFactoryBean.getJpaPropertyMap().putAll(jpaProperties.getProperties());

    URL rootLocation = determinePersistenceUnitRootLocation();
    if (rootLocation != null) {
      entityManagerFactoryBean.setPersistenceUnitRootLocation(rootLocation.toString());
    }

    return entityManagerFactoryBean;
  }

  private URL determinePersistenceUnitRootLocation() {
    Class<?> source = getClass();
    try {
      URL url = source.getProtectionDomain().getCodeSource().getLocation();
      return ResourceUtils.extractJarFileURL(url);
    } catch (Exception ex) {
      // TODO
    }
    return null;
  }


  //  @Bean
  //  public BeanPostProcessor springletsSecurityDomainRegistrar() {
  //    SpringletsSecurityDomainRegistrar postProcessor = 
  //        new SpringletsSecurityDomainRegistrar();
  //    return postProcessor;
  //  }

  //  @Bean
  //  @Order(Ordered.HIGHEST_PRECEDENCE)
  //  protected PersistenceUnitPostProcessor classpathScanningPersistenceUnitPostProcessor() {
  //    ClasspathScanningPersistenceUnitPostProcessor postProcessor = 
  //        new ClasspathScanningPersistenceUnitPostProcessor("io.springlets.security.domain");
  //    postProcessor.setMappingFileNamePattern("classpath:springlets-authentication-orm.xml");
  //    return postProcessor;
  //  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SpringletsSecurityJpaAuthenticationConfigurer springletsSecurityJpaConfigurerAdapter() {
    return new SpringletsSecurityJpaAuthenticationConfigurer(
        new JpaUserDetailsService(userLoginService()));
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SpringletsSecurityInMemoryAuthenticationConfigurer inMemoryAuthenticationConfiguration() {
    return new SpringletsSecurityInMemoryAuthenticationConfigurer();
  }
}
