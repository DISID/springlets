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
package io.springlets.web.mvc.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.springlets.web.mvc.advice.StringTrimmerAdvice;

/**
 * Configuration class to register the following beans:
 * 
 * * {@link StringTrimmerAdvice}.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
public class SpringletsWebMvcConfiguration implements InitializingBean {

  /**
   * Each SpringletsWebMvcConfigurer will configure a feature of the Springlets Web MVC,
   * i.e. SpringBootSpringletsWebMvcAdvicesConfigurer configures the 
   * StringTrimmerAdvice.
   */
  @Autowired(required = false)
  List<SpringletsWebMvcConfigurer> configurers = Collections.emptyList();

  private SpringletsWebMvcConfigurerDelegate configurerDelegate;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.configurerDelegate = new SpringletsWebMvcConfigurerDelegate(configurers);
  }

  @Bean
  public StringTrimmerAdvice stringTrimmerAdvice() {
    SpringletsWebMvcSettings properties = config();

    StringTrimmerAdvice trimmerAdvice = new StringTrimmerAdvice();
    trimmerAdvice.setCharsToDelete(properties.getTrimmerAdviceSettings().getCharsToDelete());
    trimmerAdvice.setEmptyAsNull(properties.getTrimmerAdviceSettings().isEmptyAsNull());
    return trimmerAdvice;
  }

  /**
   * Main configuration for the Springlets Web MVC.
   */
  @Bean
  public SpringletsWebMvcSettings config() {

    SpringletsWebMvcSettings webMvcSettings = new SpringletsWebMvcSettings();

    configurerDelegate.configureSpringletsWebMvcSettings(webMvcSettings);

    return webMvcSettings;
  }

}
