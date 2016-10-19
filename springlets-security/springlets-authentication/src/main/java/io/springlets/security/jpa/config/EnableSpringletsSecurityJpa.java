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
package io.springlets.security.jpa.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Annotation to automatically import the {@link SpringletsSecurityJpaConfiguration}
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({EnableSpringletsSecurityJpa.SpringletsSecurityJpaImportSelector.class})
public @interface EnableSpringletsSecurityJpa {
//
//  /**
//   * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
//   * declarations e.g.: {@code @EntityScan("org.my.pkg")} instead of
//   * {@code @EntityScan(basePackages="org.my.pkg")}.
//   * @return the base packages to scan
//   */
//  @AliasFor("basePackages")
//  String[] value() default "io.springlets.security.domain";
//
//  /**
//   * Base packages to scan for entities. {@link #value()} is an alias for (and mutually
//   * exclusive with) this attribute.
//   * <p>
//   * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
//   * package names.
//   * @return the base packages to scan
//   */
//  @AliasFor("value")
//  String[] basePackages() default {};

  /**
   * Import selector to register {@link SpringletsSecurityJpaConfiguration} as configuration class.
   * 
   * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
   */
  static class SpringletsSecurityJpaImportSelector implements BeanFactoryAware, ImportSelector {

    /** Owning BeanFactory */
    private BeanFactory beanFactory;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
      boolean jpaConfigurerPresent;
      List<String> imports = new ArrayList<String>();

      try {
        jpaConfigurerPresent =
            this.beanFactory.getBean(SpringletsSecurityJpaConfiguration.class) != null;
      } catch (NoSuchBeanDefinitionException ex) {
        jpaConfigurerPresent = false;
      }

      if (!jpaConfigurerPresent) {
        imports.add(SpringletsSecurityJpaConfiguration.class.getName());
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
  
//  @Order(Ordered.HIGHEST_PRECEDENCE)
//  static class SpringletsSecurityModelDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
//
//    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;
//
//    private static String[] addPackageNames(
//        ConstructorArgumentValues constructorArguments,
//        Collection<String> packageNames) {
//      String[] existing = (String[]) constructorArguments
//          .getIndexedArgumentValue(0, String[].class).getValue();
//      Set<String> merged = new LinkedHashSet<String>();
//      merged.addAll(Arrays.asList(existing));
//      merged.addAll(packageNames);
//      return merged.toArray(new String[merged.size()]);
//    }
//
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata metadata,
//        BeanDefinitionRegistry registry) {
//      Assert.notNull(registry, "Registry must not be null");
//      List packageNames = Arrays.asList(getPackagesToScan(metadata));
//
//      Class beanClass = EntityRegistry.class;
//
//      if (registry.containsBeanDefinition(beanClass.getName())) {
//        BeanDefinition beanDefinition = registry.getBeanDefinition(beanClass.getName());
//        ConstructorArgumentValues constructorArguments = beanDefinition
//            .getConstructorArgumentValues();
//        constructorArguments.addIndexedArgumentValue(0,
//            addPackageNames(constructorArguments, packageNames));
//      }
//      else {
//        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
//        beanDefinition.setBeanClass(beanClass);
//        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0,
//            packageNames.toArray(new String[packageNames.size()]));
//        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
//        registry.registerBeanDefinition(beanClass.getName(), beanDefinition);
//      }
//
//    }
//
//    /**
//     * Get the packages to scan value in the annotation @EnableSpringletsSecurityJpa
//     * of the importing @Configuration class.
//     *  
//     * @param metadata
//     * @return
//     */
//    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
//      AnnotationAttributes attributes = AnnotationAttributes.fromMap(
//          metadata.getAnnotationAttributes(EnableSpringletsSecurityJpa.class.getName()));
//
//      String[] basePackages = attributes.getStringArray("basePackages");
//
//      Set<String> packagesToScan = new LinkedHashSet<String>();
//      packagesToScan.addAll(Arrays.asList(basePackages));
//
//      if (packagesToScan.isEmpty()) {
//        String packageName = ClassUtils.getPackageName(metadata.getClassName());
//        Assert.state(!StringUtils.isEmpty(packageName),
//            "@EnableSpringletsSecurityJpa cannot be used with the default package");
//        return Collections.singleton(packageName);
//      }
//      return packagesToScan;
//    }
//  }
//
//  static class EntityRegistry {
//
//    private final List<String> packageNames;
//
//    EntityRegistry(String... packageNames) {
//      List<String> packages = new ArrayList<String>();
//      for (String name : packageNames) {
//        if (StringUtils.hasText(name)) {
//          packages.add(name);
//        }
//      }
//      this.packageNames = Collections.unmodifiableList(packages);
//    }
//
//    /**
//     * Return the package names specified from all {@link EntityScan @EntityScan}
//     * annotations.
//     * @return the entity scan package names
//     */
//    public List<String> getPackageNames() {
//      return this.packageNames;
//    }
//  }

}
