/*
 * Copyright 2017 the original author or authors.
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.springlets.data.jpa.domain.EmbeddableImage;
import io.springlets.web.mvc.converters.SpringletsImageFileConverter;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.persistence.Embedded;

/**
 * = SpringletsImageFileConverterAutoConfigurationTest
 * 
 * Test class to check if the SpringletsImageFileConverter it's beeing registered
 * correctly into the Spring MVC context.
 * 
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsImageFileConverterAutoConfigurationTest {

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
   * The default value for `springlets.image.management` is false, so
   * check that if it is not defined the SpringletsImageFileConverter is not
   * registered.
   *
   * @throws Exception
   */
  @Test(expected = NoSuchBeanDefinitionException.class)
  public void defaultConfiguration() throws Exception {

    // Setup
    registerAndRefreshContext();

    // Exercise
    this.context.getBean(SpringletsImageFileConverter.class);
  }

  /**
   * Checks that if `springlets.image.management` is true the
   * SpringletsImageFileConverter is registered.
   *
   * @throws Exception
   */
  @Test
  public void enableSpringletsImageFileConverter() {

    // Setup
    registerAndRefreshContext("springlets.image.management:true");

    // Exercise
    SpringletsImageFileConverter converter =
        this.context.getBean(SpringletsImageFileConverter.class);

    // Verify
    assertThat(converter).isNotNull();
  }

  /**
   * Perform a POST request to check the {@link SpringletsImageFileConverter} works.
   *
   * Only the needed autoconfiguration is loaded in order to create
   * the Spring Web MVC artifacts to handle the HTTP request.
   *
   * @see MockServletContext
   * @see MockMvc
   */
  @Test
  public void checkConverter() throws Exception {
    EnvironmentTestUtils.addEnvironment(this.context, "springlets.image.management:true");
    this.context.setServletContext(new MockServletContext());
    this.context.register(TestConfiguration.class);
    this.context.refresh();

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

    // Mock a multipart file to be sended
    MockMultipartFile imageFile =
        new MockMultipartFile("image", "image1.jpg", "image/jpg", "image1.jpg".getBytes());

    mockMvc
        .perform(MockMvcRequestBuilders.fileUpload("/persons").file(imageFile)
            .param("name", "TESTNAME").param("surname", "TESTSURNAME"))
        .andExpect(status().isOk()).andDo(print());
  }

  /**
   * Process the {@link SpringletsWebMvcAutoConfiguration} class by the
   * {@link ApplicationContext}. After the processing the {@link SpringletsImageFileConverter}
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
  @RequestMapping("/persons")
  protected static class PersonsController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> create(Person person) {
      return new ResponseEntity<Person>(person, HttpStatus.OK);
    }
  }

  /**
   * Dummy domain class for request binding.
   */
  protected static class Person {
    private String name;
    private String surname;

    @Embedded
    private EmbeddableImage image;

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

    public EmbeddableImage getImage() {
      return image;
    }

    public void setImage(EmbeddableImage image) {
      this.image = image;
    }

  }

}
