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
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import io.springlets.web.mvc.advice.JsonpAdvice;
import io.springlets.web.mvc.advice.StringTrimmerAdvice;

/**
 * Tests for {@link SpringletsJsonpAdviceAutoConfigurationTest}
 *
 * @author Manuel Iboora at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsJsonpAdviceAutoConfigurationTest {

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
   * The default value for `springlets.mvc.advices.enabled` is true, so
   * check that if it is not defined the JsonpAdvice is registered.
   *
   * @throws Exception
   */
  @Test
  public void defaultConfiguration() throws Exception {

    // Setup
    registerAndRefreshContext();

    // Exercise
    JsonpAdvice jsonpAdvice = this.context.getBean(JsonpAdvice.class);

    // Verify
    assertThat(jsonpAdvice).isNotNull();
  }

  /**
   * Check if `springlets.mvc.advices.enabled` is false the
   * JsonpAdvice is not registered.
   *
   * @throws Exception
   */
  @Test
  public void disabledAdvice() {

    // Setup
    registerAndRefreshContext("springlets.mvc.advices.enabled:false");

    // Exercise
    JsonpAdvice advice = this.context.getBean(JsonpAdvice.class);

    // Verify
    assertNull(advice);
  }

  /**
   * Enable the {@link StringTrimmerAdvice} and check if it is
   * in the {@link ApplicationContext}.
   */
  @Test
  public void enableAdvice() {
    registerAndRefreshContext("springlets.mvc.advices.enabled:true");
    assertThat(this.context.getBean(JsonpAdvice.class)).isNotNull();
  }

  /**
   * Perform a GET request to check the {@link JsonpAdvice} works.
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
        "springlets.mvc.advices.jsonp.query-param-names:callback1,callback2");
    this.context.setServletContext(new MockServletContext());
    this.context.register(TestConfiguration.class);
    this.context.refresh();

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

    // only returns the invocation related with the first parameter
    mockMvc.perform(get("/persons").param("callback1", "functionJs1").param("callback2", "functionJs2"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("functionJs1({\"name\":\"name\",\"surname\":\"surname\"})")))
        .andDo(print());

    mockMvc.perform(get("/persons").param("callback2", "functionJs2"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("functionJs2({\"name\":\"name\",\"surname\":\"surname\"})")))
        .andDo(print());

    mockMvc.perform(get("/persons"))
    .andExpect(status().isOk())
    .andExpect(content().string(containsString("{\"name\":\"name\",\"surname\":\"surname\"}")))
    .andDo(print());
  }

  /**
   * Perform a GET request to check the {@link JsonpAdvice} works
   * with default values.
   *
   * Only the needed autoconfiguration is loaded in order to create
   * the Spring Web MVC artifacts to handle the HTTP request.
   *
   * @see MockServletContext
   * @see MockMvc
   */
  @Test
  public void registerAdviceDefaultValues() throws Exception {
    EnvironmentTestUtils.addEnvironment(this.context,
        "springlets.mvc.advices.enabled:true");
    this.context.setServletContext(new MockServletContext());
    this.context.register(TestConfiguration.class);
    this.context.refresh();

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    mockMvc.perform(get("/persons").param("callback", "functionJs"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("functionJs({\"name\":\"name\",\"surname\":\"surname\"})")))
        .andDo(print());
  }

  /**
   * Process the {@link SpringletsWebMvcAutoConfiguration} class by the
   * {@link ApplicationContext}. After the processing the {@link JsonpAdvice}
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
    this.context.register(SpringletsWebMvcAutoConfiguration.class);
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
      HttpMessageConvertersAutoConfiguration.class, SpringletsWebMvcAutoConfiguration.class})
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

    @GetMapping(value="/persons", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPerson() {
    	return new ResponseEntity<Person>(new Person(), HttpStatus.OK);
    }
  }

  /**
   * Dummy domain class for request binding.
   */
  protected static class Person {
    private String name;
    private String surname;

    public Person() {
	    this.name = "name";
	    this.surname = "surname";
	}

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
