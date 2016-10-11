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

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.springlets.web.StringTrimmerAdvice;

/**
 * = TrimmerEditorAutoConfiguration
 *
 * {@link ConfigurationProperties} for configuring {@link StringTrimmerEditor}.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Configuration
@ConditionalOnProperty(prefix = "springlets.mvc.trimeditor", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnWebApplication
@EnableConfigurationProperties(TrimmerEditorProperties.class)
public class TrimmerEditorAutoConfiguration {

  private final TrimmerEditorProperties properties;

  public TrimmerEditorAutoConfiguration(TrimmerEditorProperties properties) {
    this.properties = properties;
  }

  @Bean
  @ConditionalOnMissingBean
  public StringTrimmerAdvice stringTrimmerAdvice() {
    StringTrimmerAdvice trimmerAdvice = new StringTrimmerAdvice();
    trimmerAdvice.setCharsToDelete(this.properties.getCharsToDelete());
    trimmerAdvice.setEmptyAsNull(this.properties.isEmptyAsNull());
    return trimmerAdvice;
  }

}
