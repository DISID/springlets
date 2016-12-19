/**
 * Copyright (c) 2016 DISID Corporation S.L. All rights reserved.
 */
package io.springlets.format.config;

import io.springlets.format.EntityFormat;
import io.springlets.format.EntityFormatAnnotationFormatterFactory;

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
  }

}
