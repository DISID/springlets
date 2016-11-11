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
package io.springlets.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import io.springlets.http.converter.json.BindingResultModule;
import io.springlets.http.converter.json.BindingResultSerializer;
import io.springlets.http.converter.json.FieldErrorSerializer;

/**
 * Configuration class to register the following beans for usage with Spring MVC:
 *
 * * {@link BindingResultModule}: Jackson module to register the serializers below:
 * ** {@link BindingResultSerializer}: to serialize {@link BindingResult} 
 *    objects.
 * ** {@link FieldErrorSerializer}: to serialize {@link FieldError} objects
 * * {@link Hibernate5Module}: Jackson module to support JSON serialization and deserialization 
 *   of Hibernate datatypes and properties.
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
public class SpringletsWebJacksonConfiguration {

  @Bean
  public BindingResultModule bindingResultModule() {
    return new BindingResultModule();
  }

  @Bean
  public Hibernate5Module hibernate5Module() {
    return new Hibernate5Module();
  }

}
