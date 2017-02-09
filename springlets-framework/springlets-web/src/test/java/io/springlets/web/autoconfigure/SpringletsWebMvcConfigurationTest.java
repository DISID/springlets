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
package io.springlets.web.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import io.springlets.web.mvc.advice.JsonpAdvice;
import io.springlets.web.mvc.advice.StringTrimmerAdvice;
import io.springlets.web.mvc.advice.ValidatorAdvice;
import io.springlets.web.mvc.config.SpringletsWebMvcConfiguration;
import io.springlets.web.mvc.config.SpringletsWebMvcProperties;

/**
 * Tests for {@link SpringletsWebMvcConfiguration}
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsWebMvcConfigurationTest {

  private AnnotationConfigWebApplicationContext context =
      new AnnotationConfigWebApplicationContext();

  @Before
  public void setupContext() {}

  @After
  public void close() {
    LocaleContextHolder.resetLocaleContext();
    if (this.context != null) {
      this.context.close();
    }
  }

  /**
   * Check if "springlets.mvc.advices.enabled" is not defined the
   * StringTrimmerAdvice is registered and empty-as-null is true.
   * JsonpAdvice is registered with 'callback' as jsonp query parameter
   *
   * @throws Exception
   */
  @Test
  public void defaultConfiguration() throws Exception {

    // Setup
    this.context.register(SpringletsWebMvcProperties.class);
    this.context.register(SpringletsWebMvcConfiguration.class);
    this.context.refresh();

    // Exercise
    StringTrimmerAdvice advice = this.context.getBean(StringTrimmerAdvice.class);
    JsonpAdvice jsonpAdvice = this.context.getBean(JsonpAdvice.class);
    ValidatorAdvice validatorAdvice = this.context.getBean(ValidatorAdvice.class);

    // Verify
    assertThat(this.context.getBean(SpringletsWebMvcProperties.class)).isNotNull();
    assertThat(advice).isNotNull();
    assertThat(advice.isEmptyAsNull()).isEqualTo(true);
    assertThat(advice.getCharsToDelete()).isNull();
    assertThat(jsonpAdvice).isNotNull();
    assertThat(validatorAdvice).isNotNull();
  }

  /**
   * Configure the {@link StringTrimmerAdvice}, the {@link JsonpAdvice} and the
   * {@link ValidatorAdvice} and check if they have the right settings.
   */
  @Test
  public void configureAdvice() {

    // Setup
    this.context.register(DummyPropertiesConfiguration.class);
    this.context.register(SpringletsWebMvcConfiguration.class);
    this.context.refresh();

    // Exercise
    StringTrimmerAdvice advice = this.context.getBean(StringTrimmerAdvice.class);
    JsonpAdvice jsonpAdvice = this.context.getBean(JsonpAdvice.class);
    ValidatorAdvice validatorAdvice = this.context.getBean(ValidatorAdvice.class);

    // Verify
    assertThat(this.context.getBean(SpringletsWebMvcProperties.class)).isNotNull();
    assertThat(advice).isNotNull();
    assertThat(advice.isEmptyAsNull()).isEqualTo(false);
    assertThat(advice.getCharsToDelete()).isEqualTo("abc");
    assertThat(jsonpAdvice).isNotNull();
    assertThat(validatorAdvice).isNotNull();
  }

  /**
   * Test Configuration.
   */
  @Configuration
  protected static class DummyPropertiesConfiguration {
    public boolean emptyAsNull = false;
    public String charsToDelete = "abc";

    @Bean
    public SpringletsWebMvcProperties springletsWebMvcProperties() {
      SpringletsWebMvcProperties properties = new SpringletsWebMvcProperties();
      properties.getAdvices().getTrimeditor().setCharsToDelete(this.charsToDelete);
      properties.getAdvices().getTrimeditor().setEmptyAsNull(this.emptyAsNull);
      return properties;
    }
  }
}
