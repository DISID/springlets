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
package io.springlets.boot.autoconfigure.data.web;

import static org.assertj.core.api.Assertions.assertThat;

import io.springlets.data.web.GlobalSearchHandlerMethodArgumentResolver;
import io.springlets.data.web.datatables.DatatablesPageableHandlerMethodArgumentResolver;
import io.springlets.data.web.datatables.DatatablesSortHandlerMethodArgumentResolver;
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
 * Tests for {@link SpringletsDataArgumentResolversAutoConfiguration}
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsDataArgumentResolversAutoConfigurationTest {

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
  public void globalSearchResolverAvailable() throws Exception {

    // Setup
    registerAndRefreshContext();

    // Exercise
    GlobalSearchHandlerMethodArgumentResolver resolver =
        this.context.getBean(GlobalSearchHandlerMethodArgumentResolver.class);

    // Verify
    assertThat(resolver).isNotNull();
  }

  @Test
  public void datatablesSortResolverAvailable() throws Exception {

    // Setup
    registerAndRefreshContext();

    // Exercise
    DatatablesSortHandlerMethodArgumentResolver resolver =
        this.context.getBean(DatatablesSortHandlerMethodArgumentResolver.class);

    // Verify
    assertThat(resolver).isNotNull();
  }

  @Test
  public void datatablesPageableResolverAvailable() throws Exception {

    // Setup
    registerAndRefreshContext();

    // Exercise
    DatatablesPageableHandlerMethodArgumentResolver resolver =
        this.context.getBean(DatatablesPageableHandlerMethodArgumentResolver.class);

    // Verify
    assertThat(resolver).isNotNull();
  }

  /**
   * Process the {@link SpringletsDataArgumentResolversAutoConfiguration} class by the
   * {@link ApplicationContext}. After the processing the {@link BindingResultModule}
   * must be in the ApplicationContext.
   * 
   * @see EnvironmentTestUtils#addEnvironment(String, org.springframework.core.env.ConfigurableEnvironment, String...)
   */
  private void registerAndRefreshContext(String... env) {
    EnvironmentTestUtils.addEnvironment(this.context, env);
    this.context.register(SpringletsDataArgumentResolversAutoConfiguration.class);
    this.context.refresh();
  }
}
