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
package io.springlets.security.web.config;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import io.springlets.security.jpa.config.SpringletsSecurityJpaAuthenticationConfiguration;

/**
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({EnableSpringletsWebSecurity.SpringletsWebSecurityImportSelector.class})
public @interface EnableSpringletsWebSecurity {

  /**
   * Import selector to register {@link SpringletsSecurityJpaAuthenticationConfiguration} as configuration class.
   * 
   * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
   */
  static class SpringletsWebSecurityImportSelector
      implements BeanFactoryAware, ImportSelector {

    /** Owning BeanFactory */
    private BeanFactory beanFactory;

    /**
     * {@inheritDoc}
     * 
     * Due to Springlets is independent from Spring Boot the ConditionalOnBean annotation
     * is not used, however this method offers the same feature by using the 
     * BeanFactoryAware interface.
     * 
     * The @{@link Configuration} classes that should be imported are:
     * 
     * * {@link SpringletsWebSecurityConfiguration}, if no bean of the given type was found
     * * {@link SpringletsWebSecurityConfiguration}, if no bean of the given type was found
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
      List<String> imports = new ArrayList<String>();

      // Import SpringletsWebSecurityConfiguration
      boolean webConfigPresent;
      try {
        webConfigPresent =
            this.beanFactory.getBean(SpringletsWebSecurityConfiguration.class) != null;
      } catch (NoSuchBeanDefinitionException ex) {
        webConfigPresent = false;
      }

      if (!webConfigPresent) {
        imports.add(SpringletsWebSecurityConfiguration.class.getName());
      }

      // Import SpringletsSecurityWebMvcConfiguration
      boolean webMvcConfigPresent;
      try {
        webMvcConfigPresent =
            this.beanFactory.getBean(SpringletsSecurityWebMvcConfiguration.class) != null;
      } catch (NoSuchBeanDefinitionException ex) {
        webMvcConfigPresent = false;
      }

      if (!webMvcConfigPresent) {
        imports.add(SpringletsSecurityWebMvcConfiguration.class.getName());
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
