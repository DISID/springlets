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

import org.springframework.core.convert.ConversionService;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.util.StringUtils;

public class EntityExpressionSupport {

  private final ExpressionParser parser;
  private final TemplateParserContext templateParserContext;
  private final String defaultExpression;
  private final ConversionService conversionService;

  public EntityExpressionSupport(ExpressionParser parser,
      TemplateParserContext templateParserContext) {
    this(parser, templateParserContext, null, "#{toString()}");
  }

  public EntityExpressionSupport(ExpressionParser parser,
      TemplateParserContext templateParserContext, ConversionService conversionService) {
    this(parser, templateParserContext, conversionService, "#{toString()}");
  }

  public EntityExpressionSupport(ExpressionParser parser,
      TemplateParserContext templateParserContext, String defaultExpression) {
    this(parser, templateParserContext, null, defaultExpression);
  }

  public EntityExpressionSupport(ExpressionParser parser,
      TemplateParserContext templateParserContext, ConversionService conversionService,
      String defaultExpression) {
    this.parser = parser;
    this.templateParserContext = templateParserContext;
    this.defaultExpression = defaultExpression;
    this.conversionService = conversionService;
  }

  /**
   * Returns the default expression to use when the provided one is null or empty.
   * @return the default expression
   */
  protected String getDefaultExpression() {
    return defaultExpression;
  }

  /**
   * Returns the expression, or the default one if it is empty or null.
   * @return the expression to use
   */
  protected String getExpressionOrDefault(String expression) {
    return StringUtils.isEmpty(expression) ? getDefaultExpression() : expression;
  }

  /**
   * Returns a {@link ExpressionParser} instance to parse the expression
   * @return the parser for the expression
   */
  private ExpressionParser getParser() {
    return parser;
  }

  /**
   * Returns the {@link TemplateParserContext} to use to parse the expression.
   * @return the templateParserContext
   */
  private TemplateParserContext getTemplateParserContext() {
    return templateParserContext;
  }

  /**
   * Parses the given expression.
   * @param expression text of the expression to parse
   * @return the parsed expression
   */
  protected Expression parseExpression(String expression) {
    Expression parsedExpression =
        getParser().parseExpression(expression, getTemplateParserContext());

    registerConversionServiceInSpelExpressions(parsedExpression);

    return parsedExpression;
  }

  private void registerConversionServiceInSpelExpressions(Expression parsedExpression) {
    if (conversionService == null) {
      return;
    }
    StandardTypeConverter converter = new StandardTypeConverter(conversionService);
    StandardEvaluationContext context = new StandardEvaluationContext();
    context.setTypeConverter(converter);
    setContextIfSpelExpression(parsedExpression, context);

    if (parsedExpression instanceof CompositeStringExpression) {
      CompositeStringExpression composite = (CompositeStringExpression) parsedExpression;
      for (Expression childExpresion : composite.getExpressions()) {
        setContextIfSpelExpression(childExpresion, context);
      }
    }
  }

  private void setContextIfSpelExpression(Expression expression,
      StandardEvaluationContext context) {
    if (expression instanceof SpelExpression) {
      ((SpelExpression) expression).setEvaluationContext(context);
    }
  }

  /**
   * @param entity
   * @param expressionTxt
   * @return
   */
  protected String convertToString(Object entity, String expressionTxt) {
    return parseExpression(expressionTxt).getValue(entity, String.class);
  }

}
