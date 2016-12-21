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
package io.springlets.format;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Converter of entities to String using a SpEL expression. This converter will be applied to 
 * any class with the {@link EntityFormat} annotation at the class level.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class EntityToStringConverter extends EntityExpressionSupport
    implements ConditionalGenericConverter {

  private static final Set<ConvertiblePair> CONVERTIBLE_TYPES;
  private final MessageSource messageSource;

  static {
    ConvertiblePair pair = new ConvertiblePair(Object.class, String.class);
    CONVERTIBLE_TYPES = Collections.singleton(pair);
  }

  /**
   * Creates a new converter 
   * @param parser
   * @param templateParserContext
   * @param messageSource
   */
  public EntityToStringConverter(ExpressionParser parser,
      TemplateParserContext templateParserContext, MessageSource messageSource) {
    super(parser, templateParserContext);
    this.messageSource = messageSource;
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return CONVERTIBLE_TYPES;
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (source == null) {
      return null;
    }
    EntityFormat format = getAnnotation(sourceType.getType());

    String expressionTxt = getDefaultExpression();
    if (format != null) {
      if (!StringUtils.isEmpty(format.message())) {
        expressionTxt = messageSource.getMessage(format.message(), null, null, getCurrentLocale());
      } else if (!StringUtils.isEmpty(format.expression())) {
        expressionTxt = format.expression();
      }
    }

    return convertToString(source, expressionTxt);
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return (String.class.equals(targetType.getType()) && hasAnnotation(sourceType.getType()));
  }

  private Locale getCurrentLocale() {
    return LocaleContextHolder.getLocale();
  }

  private boolean hasAnnotation(Class<?> clazz) {
    return AnnotatedElementUtils.hasAnnotation(clazz, EntityFormat.class);
  }

  private EntityFormat getAnnotation(Class<?> clazz) {
    return AnnotatedElementUtils.findMergedAnnotation(clazz, EntityFormat.class);
  }

}
