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
package io.springlets.boot.autoconfigure.web.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import io.springlets.web.mvc.advice.StringTrimmerAdvice;

/**
 * Tests for {@link SpringletsWebMvcAdvicesAutoConfiguration}
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsWebMvcAdvicesAutoConfigurationTest {

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

  /**
   * Check if `springlets.mvc.advices.enabled` is not defined the
   * StringTrimmerAdvice is not registered.
   * 
   * @throws Exception
   */
  @Test
  public void defaultConfiguration() throws Exception {

    // Verify
    this.thrown.expect(NoSuchBeanDefinitionException.class);
    this.thrown.expectMessage("No qualifying bean of type [");

    // Setup
    registerAndRefreshContext();

    // Exercise
    this.context.getBean(StringTrimmerAdvice.class);
  }

  /**
   * Check if `springlets.mvc.advices.enabled` is false the
   * StringTrimmerAdvice is not registered.
   * 
   * @throws Exception
   */
  @Test
  public void disabledAdvice() {

    // Verify
    this.thrown.expect(NoSuchBeanDefinitionException.class);
    this.thrown.expectMessage("No qualifying bean of type [");

    // Setup
    registerAndRefreshContext("springlets.mvc.advices.enabled:false");

    // Exercise
    this.context.getBean(StringTrimmerAdvice.class);
  }

  /**
   * Enable the {@link StringTrimmerAdvice} and check it is in the
   * {@link ApplicationContext}.
   */
  @Test
  public void enableAdvice() {
    registerAndRefreshContext("springlets.mvc.advices.enabled:true");
    assertThat(this.context.getBean(StringTrimmerAdvice.class)).isNotNull();
  }

  /**
   * Check the environment properties are used to setup the {@link StringTrimmerAdvice}
   * 
   * @throws Exception
   */
  @Test
  public void overrideProperties() throws Exception {
    registerAndRefreshContext("springlets.mvc.advices.enabled:true",
        "springlets.mvc.advices.trimeditor.chars-to-delete:abc",
        "springlets.mvc.advices.trimeditor.empty-as-null:true");

    StringTrimmerAdvice advice = this.context.getBean(StringTrimmerAdvice.class);
    assertThat(advice.getCharsToDelete()).isEqualTo("abc");
    assertThat(advice.isEmptyAsNull()).isEqualTo(true);
  }

  /**
   * Perform a POST request to check the {@link StringTrimmerAdvice} works.
   * 
   * Only the needed autoconfiguration is loaded in order to create 
   * the Spring Web MVC artifacts to handle the HTTP request. 
   * 
   * @see MockServletContext
   * @see MockMvc
   */
  @Test
  public void registerAdvice() throws Exception {
    EnvironmentTestUtils.addEnvironment(this.context, 
        "springlets.mvc.advices.enabled:true",
        "springlets.mvc.advices.trimeditor.chars-to-delete:YOUR-",
        "springlets.mvc.advices.trimeditor.empty-as-null:true");
    this.context.setServletContext(new MockServletContext());
    this.context.register(TestConfiguration.class);
    this.context.refresh();

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    mockMvc.perform(post("/persons").param("name", "YOUR-NAME").param("surname", "   "))
        .andExpect(status().isOk())
        .andExpect(model().attribute("name", is("NAME")))
        .andExpect(model().attribute("surname", isNull()))
        .andDo(print());
  }

  /**
   * Process the {@link SpringletsWebMvcAdvicesAutoConfiguration} class by the
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
    this.context.register(SpringletsWebMvcAdvicesAutoConfiguration.class);
    this.context.refresh();
  }

  /**
   * Load needed Spring Web MVC artifacts.
   * 
   * @see WebMvcAutoConfiguration
   * @see HttpMessageConvertersAutoConfiguration
   */
  @Configuration
  @ImportAutoConfiguration({WebMvcAutoConfiguration.class,
      HttpMessageConvertersAutoConfiguration.class, SpringletsWebMvcAdvicesAutoConfiguration.class})
  protected static class TestConfiguration {

    @Bean
    public PersonsController controller() {
      return new PersonsController();
    }
  }

  /**
   * Test Controller.
   */
  @Controller
  protected static class PersonsController {

    @PostMapping("/persons")
    public ModelAndView user(Person person) {
      ModelAndView model = new ModelAndView("person/show");
      model.addObject("name", person.getName());
      model.addObject("surname", person.getSurname());
      return model;
    }

  }

  /**
   * Dummy domain class for request binding.
   */
  protected static class Person {
    private String name;
    private String surname;

    public String getName() {
      return name;
    }

    public void setName(String n) {
      this.name = n;
    }

    public String getSurname() {
      return surname;
    }

    public void setSurname(String sn) {
      this.surname = sn;
    }
  }
}
