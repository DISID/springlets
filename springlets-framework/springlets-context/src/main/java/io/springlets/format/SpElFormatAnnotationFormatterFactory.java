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
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Formats fields annotated with the {@link SpElFormat} annotation using
 * a {@link SpElPrinter}.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpElFormatAnnotationFormatterFactory extends EmbeddedValueResolutionSupport
    implements AnnotationFormatterFactory<SpElFormat> {

  private static final Set<Class<?>> FIELD_TYPES;

  private static final SpelExpressionParser PARSER;

  private static final TemplateParserContext PARSER_CONTEXT;

  private final MessageSource messageSource;

  static {
    Set<Class<?>> fieldTypes = new HashSet<Class<?>>(1);
    fieldTypes.add(Object.class);
    FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
    PARSER = new SpelExpressionParser();
    PARSER_CONTEXT = new TemplateParserContext();
  }

  /**
   * Creates a new factory with the given {@link MessageSource} to get messages
   * depending on the locale.
   * @param messageSource to get i18n messages from
   */
  public SpElFormatAnnotationFormatterFactory(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public Set<Class<?>> getFieldTypes() {
    return FIELD_TYPES;
  }

  @Override
  public Printer<?> getPrinter(SpElFormat annotation, Class<?> fieldType) {
    String messageCode = getExpressionMessageCode(annotation, fieldType);
    if (StringUtils.isEmpty(messageCode)) {
      String expression = getExpression(annotation, fieldType);
      return new SpElPrinter(expression, PARSER, PARSER_CONTEXT);
    }

    return new SpElMessagePrinter(messageCode, messageSource, PARSER, PARSER_CONTEXT);
  }

  private String getExpressionMessageCode(SpElFormat annotation, Class<?> fieldType) {
    SpElFormat processedAnnotation = AnnotationUtils.getAnnotation(annotation, SpElFormat.class);
    String message = processedAnnotation.message();
    if (StringUtils.isEmpty(message)) {
      SpElFormat classFormatAnnotation =
          AnnotatedElementUtils.findMergedAnnotation(fieldType, SpElFormat.class);
      if (classFormatAnnotation != null) {
        message = classFormatAnnotation.expression();
      }
    }
    return message;
  }

  private String getExpression(SpElFormat annotation, Class<?> fieldType) {
    SpElFormat processedAnnotation = AnnotationUtils.getAnnotation(annotation, SpElFormat.class);
    String expression = processedAnnotation.expression();
    if (StringUtils.isEmpty(expression)) {
      SpElFormat classFormatAnnotation =
          AnnotatedElementUtils.findMergedAnnotation(fieldType, SpElFormat.class);
      if (classFormatAnnotation != null) {
        expression = classFormatAnnotation.expression();
      }
    }
    return expression;
  }

  @Override
  public Parser<?> getParser(SpElFormat annotation, Class<?> fieldType) {
    throw new UnsupportedOperationException("Parsing of SpEl format expressions is not supported");
  }

}
