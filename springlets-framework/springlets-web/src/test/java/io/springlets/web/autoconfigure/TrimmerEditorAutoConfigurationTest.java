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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import io.springlets.web.StringTrimmerAdvice;

/**
 * Tests for {@link rimmerEditorAutoConfiguration}
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class TrimmerEditorAutoConfigurationTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private AnnotationConfigWebApplicationContext context =
      new AnnotationConfigWebApplicationContext();

  @Before
  public void setupContext() {
    //    this.context.setServletContext(new MockServletContext());
  }

  @After
  public void close() {
    LocaleContextHolder.resetLocaleContext();
    if (this.context != null) {
      this.context.close();
    }
  }

  /**
   * Check if "springlets.mvc.trimeditor.enabled" is not defined the
   * StringTrimmerAdvice is not registered.
   * 
   * @throws Exception
   */
  @Test
  public void defaultConfiguration() throws Exception {
    this.thrown.expect(NoSuchBeanDefinitionException.class);
    this.thrown.expectMessage("No qualifying bean of type [io.springlets.web.StringTrimmerAdvice]");
    registerAndRefreshContext();

    this.context.getBean(StringTrimmerAdvice.class);
  }

  /**
   * Enable the {@link StringTrimmerAdvice} and check it is in the
   * {@link ApplicationContext}.
   */
  @Test
  public void enableAdvice() {
    registerAndRefreshContext("springlets.mvc.trimeditor.enabled:true");
    assertThat(this.context.getBean(StringTrimmerAdvice.class)).isNotNull();
  }

  /**
   * Check the environment properties are used to setup the {@link StringTrimmerAdvice}
   * 
   * @throws Exception
   */
  @Test
  public void overrideProperties() throws Exception {
    registerAndRefreshContext("springlets.mvc.trimeditor.enabled:true",
        "springlets.mvc.trimeditor.chars-to-delete:abc",
        "springlets.mvc.trimeditor.empty-as-null:true");

    StringTrimmerAdvice advice = this.context.getBean(StringTrimmerAdvice.class);
    assertThat(advice.getCharsToDelete()).isEqualTo("abc");
    assertThat(advice.isEmptyAsNull()).isEqualTo(true);
  }

  /**
   * Manually process the {@link TrimmerEditorAutoConfiguration} class by the
   * {@link ApplicationContext}. After the processing the {@link StringTrimmerAdvice}
   * must be in the ApplicationContext.
   * 
   * Additionally add (high priority) values to the {@link Environment} owned by the
   * {@link ApplicationContext}.
   * 
   * @param env Strings following the pattern "property-name:property-value"
   * @see EnvironmentTestUtils#addEnvironment(String, org.springframework.core.env.ConfigurableEnvironment, String...)
   */
  private void registerAndRefreshContext(String... env) {
    EnvironmentTestUtils.addEnvironment(this.context, env);
    this.context.register(TrimmerEditorAutoConfiguration.class);
    this.context.refresh();
  }

}
