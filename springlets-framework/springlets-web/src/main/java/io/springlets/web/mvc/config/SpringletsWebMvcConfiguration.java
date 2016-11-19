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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.springlets.web.mvc.advice.JsonpAdvice;
import io.springlets.web.mvc.advice.StringTrimmerAdvice;

/**
 * Configuration class to register the following beans:
 *
 * * {@link StringTrimmerAdvice}.
 * * {@link JsonpAdvice}.
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
public class SpringletsWebMvcConfiguration { // implements InitializingBean {

  @Autowired(required = false)
  private SpringletsWebMvcProperties properties;
  
  @Bean
  public StringTrimmerAdvice stringTrimmerAdvice() {
    StringTrimmerAdvice trimmerAdvice = new StringTrimmerAdvice();
    trimmerAdvice.setCharsToDelete(properties.getAdvices().getTrimeditor().getCharsToDelete());
    trimmerAdvice.setEmptyAsNull(properties.getAdvices().getTrimeditor().isEmptyAsNull());
    return trimmerAdvice;
  }

  @Bean
  public JsonpAdvice jsonpAdvice() {
    JsonpAdvice jsonpAdvice = new JsonpAdvice(
        properties.getAdvices().getJsonp().getQueryParamNames());
    return jsonpAdvice;
  }
}
