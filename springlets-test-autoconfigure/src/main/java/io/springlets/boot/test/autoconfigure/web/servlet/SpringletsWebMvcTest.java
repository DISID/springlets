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
package io.springlets.boot.test.autoconfigure.web.servlet;

import io.springlets.data.web.config.EnableSpringletsDataWebSupport;
import io.springlets.web.config.EnableSpringletsWebJacksonSupport;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@WebMvcTest
@EnableSpringDataWebSupport
@EnableSpringletsDataWebSupport
@EnableSpringletsWebJacksonSupport
@AutoConfigureSpringletsWebMvc
@TypeExcludeFilters(SpringletsWebMvcExcludeFilter.class)
public @interface SpringletsWebMvcTest {

  /**
   * Specifies the controllers to test. This is an alias of {@link #controllers()} which
   * can be used for brevity if no other attributes are defined. See
   * {@link #controllers()} for details.
   * @see #controllers()
   * @return the controllers to test
   */
  @AliasFor(annotation = WebMvcTest.class)
  Class<?>[] value() default {};

  /**
   * Specifies the controllers to test. May be left blank if all {@code @Controller}
   * beans should be added to the application context.
   * @see #value()
   * @return the controllers to test
   */
  @AliasFor(annotation = WebMvcTest.class)
  Class<?>[] controllers() default {};

  /**
   * Determines if default filtering should be used with
   * {@link SpringBootApplication @SpringBootApplication}. By default only
   * {@code @Controller} (when no explicit {@link #controllers() controllers} are
   * defined), {@code @ControllerAdvice} and {@code WebMvcConfigurer} beans are
   * included.
   * @see #includeFilters()
   * @see #excludeFilters()
   * @return if default filters should be used
   */
  @AliasFor(annotation = WebMvcTest.class)
  boolean useDefaultFilters() default true;

  /**
   * A set of include filters which can be used to add otherwise filtered beans to the
   * application context.
   * @return include filters to apply
   */
  @AliasFor(annotation = WebMvcTest.class)
  Filter[] includeFilters() default {};

  /**
   * A set of exclude filters which can be used to filter beans that would otherwise be
   * added to the application context.
   * @return exclude filters to apply
   */
  @AliasFor(annotation = WebMvcTest.class)
  Filter[] excludeFilters() default {};

  /**
   * If Spring Security's {@link MockMvc} support should be auto-configured when it is
   * on the classpath. Defaults to {@code true}.
   * @return if Spring Security's MockMvc support is auto-configured
   */
  @AliasFor(annotation = WebMvcTest.class)
  boolean secure() default true;

}
