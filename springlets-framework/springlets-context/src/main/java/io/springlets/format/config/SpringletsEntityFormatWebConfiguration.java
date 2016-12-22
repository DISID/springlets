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
package io.springlets.format.config;

import io.springlets.format.EntityFormat;
import io.springlets.format.EntityFormatAnnotationFormatterFactory;
import io.springlets.format.EnumToMessageConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuration class to register entity formatters and converter based on the
 * {@link EntityFormat} annotation.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
public class SpringletsEntityFormatWebConfiguration extends WebMvcConfigurerAdapter {

  private final MessageSource messageSource;

  private final ApplicationContext applicationContext;

  /**
   * Creates a new configuration of entity formatters and converters for Spring MVC.
   * @param messageSource to get i18n messages from
   * @param applicationContext current Spring app context
   */
  @Autowired
  public SpringletsEntityFormatWebConfiguration(MessageSource messageSource,
      ApplicationContext applicationContext) {
    super();
    this.messageSource = messageSource;
    this.applicationContext = applicationContext;
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    super.addFormatters(registry);
    EntityFormatAnnotationFormatterFactory factory = new EntityFormatAnnotationFormatterFactory(
        messageSource, applicationContext, (FormattingConversionService) registry);
    registry.addFormatterForFieldAnnotation(factory);

    registry.addConverter(factory.getToStringConverter());
    registry.addConverter(new EnumToMessageConverter(messageSource));
  }

}
