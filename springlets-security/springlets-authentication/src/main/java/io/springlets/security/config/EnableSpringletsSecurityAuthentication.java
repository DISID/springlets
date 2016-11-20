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
package io.springlets.security.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import io.springlets.security.jpa.config.SpringletsSecurityJpaAuthenticationConfiguration;

/**
 * Annotation to automatically import the {@link SpringletsSecurityJpaAuthenticationConfiguration}
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({
    EnableSpringletsSecurityAuthentication.SpringletsSecurityJpaAuthenticationImportSelector.class})
public @interface EnableSpringletsSecurityAuthentication {

  /**
   * Import selector to register {@link SpringletsSecurityJpaAuthenticationConfiguration} as 
   * configuration class.
   * 
   * The developer can overwrite this default configuration by registering his own
   * {@link SpringletsSecurityJpaAuthenticationConfiguration} bean before this config.
   * 
   * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
   */
  static class SpringletsSecurityJpaAuthenticationImportSelector
      implements BeanFactoryAware, ImportSelector {

    /** Owning BeanFactory */
    private BeanFactory beanFactory;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
      boolean jpaConfigurerPresent;
      List<String> imports = new ArrayList<String>();

      // JPA authentication configuration
      try {
        jpaConfigurerPresent = this.beanFactory
            .getBean(SpringletsSecurityJpaAuthenticationConfiguration.class) != null;
      } catch (NoSuchBeanDefinitionException ex) {
        jpaConfigurerPresent = false;
      }

      if (!jpaConfigurerPresent) {
        imports.add(SpringletsSecurityJpaAuthenticationConfiguration.class.getName());
      }

      // In memory authentication configuration
      boolean inMemConfigurerPresent;

      try {
        inMemConfigurerPresent =
            this.beanFactory.getBean(SpringletsSecurityConfiguration.class) != null;
      } catch (NoSuchBeanDefinitionException ex) {
        inMemConfigurerPresent = false;
      }

      if (!inMemConfigurerPresent) {
        imports.add(SpringletsSecurityConfiguration.class.getName());
      }

      return imports.toArray(new String[imports.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * [IMPORTANT]
     * ====
     * {@link ImportSelector} guarantees the following Aware interfaces, and their respective
     * methods will be called prior to {@link ImportSelector#selectImports(AnnotationMetadata) selectImports}.
     * 
     * * EnvironmentAware
     * * BeanFactoryAware
     * * BeanClassLoaderAware
     * * ResourceLoaderAware
     * ==== 
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
    }
  }
}
