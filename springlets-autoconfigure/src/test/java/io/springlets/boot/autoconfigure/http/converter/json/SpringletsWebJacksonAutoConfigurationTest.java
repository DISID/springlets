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
package io.springlets.boot.autoconfigure.http.converter.json;

import static org.assertj.core.api.Assertions.assertThat;

import io.springlets.http.converter.json.BindingResultModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Tests for {@link SpringletsWebJacksonAutoConfiguration}
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsWebJacksonAutoConfigurationTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

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

  @Test
  public void bindindResultModuleBeanAvailable() throws Exception {

    // Setup
    registerAndRefreshContext();

    // Exercise
    BindingResultModule module = this.context.getBean(BindingResultModule.class);

    // Verify
    assertThat(module).isNotNull();
  }

  /**
   * Process the {@link SpringletsWebJacksonAutoConfiguration} class by the
   * {@link ApplicationContext}. After the processing the {@link BindingResultModule}
   * must be in the ApplicationContext.
   * 
   * @see EnvironmentTestUtils#addEnvironment(String, org.springframework.core.env.ConfigurableEnvironment, String...)
   */
  private void registerAndRefreshContext(String... env) {
    EnvironmentTestUtils.addEnvironment(this.context, env);
    this.context.register(SpringletsWebJacksonAutoConfiguration.class);
    this.context.refresh();
  }
}
