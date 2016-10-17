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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import io.springlets.web.mvc.advice.StringTrimmerAdvice;
import io.springlets.web.mvc.config.SpringletsWebMvcConfiguration;
import io.springlets.web.mvc.config.SpringletsWebMvcConfigurer;
import io.springlets.web.mvc.config.SpringletsWebMvcSettings;
import io.springlets.web.mvc.config.SpringletsWebMvcSettings.StringTrimmerAdviceSettings;

/**
 * Tests for {@link SpringletsWebMvcConfiguration}
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsWebMvcConfigurationTest {

  private AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

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
   * 
   * @throws Exception
   */
  @Test
  public void defaultConfiguration() throws Exception {

    // Setup
    this.context.register(SpringletsWebMvcConfiguration.class);
    this.context.refresh();

    // Exercise
    StringTrimmerAdvice advice = this.context.getBean(StringTrimmerAdvice.class);

    // Verify
    assertThat(this.context.getBean(SpringletsWebMvcSettings.class)).isNotNull();
    assertThat(advice).isNotNull();
    assertThat(advice.isEmptyAsNull()).isEqualTo(true);
    assertThat(advice.getCharsToDelete()).isNull();
  }

  /**
   * Configure the {@link StringTrimmerAdvice} and check it has the right
   * settings.
   */
  @Test
  public void configureAdvice() {

    // Setup
    this.context.register(DummyConfigurer.class);
    this.context.register(SpringletsWebMvcConfiguration.class);
    this.context.refresh();

    // Exercise
    StringTrimmerAdvice advice = this.context.getBean(StringTrimmerAdvice.class);

    // Verify
    assertThat(this.context.getBean(SpringletsWebMvcSettings.class)).isNotNull();
    assertThat(advice).isNotNull();
    assertThat(advice.isEmptyAsNull()).isEqualTo(false);
    assertThat(advice.getCharsToDelete()).isEqualTo("abc");
  }

  /**
   * Test Configurer.
   */
  protected static class DummyConfigurer implements SpringletsWebMvcConfigurer {
    public boolean emptyAsNull = false;
    public String charsToDelete = "abc";

    public DummyConfigurer() {
    }

    @Override
    public void configureSpringletsWebMvcSettings(SpringletsWebMvcSettings config) {
      StringTrimmerAdviceSettings settings = new StringTrimmerAdviceSettings();
      settings.setCharsToDelete(this.charsToDelete);
      settings.setEmptyAsNull(this.emptyAsNull);
      config.setTrimmerAdviceSettings(settings);
    }
  }
}
