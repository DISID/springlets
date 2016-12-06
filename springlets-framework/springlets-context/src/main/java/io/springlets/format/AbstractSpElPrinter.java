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

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.format.Printer;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * A base abstract Printer for objects values using a SpEl expression to build a String
 * with the object properties and methods.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public abstract class AbstractSpElPrinter implements Printer<Object> {

  private String expression;
  private ExpressionParser parser;
  private TemplateParserContext templateParserContext;
  private String defaultExpression = "#{toString()}";

  /**
   * Creates a new instance with the given expression in SpEL format.
   * @param expression expression to generate a String from the provided objects
   */
  public AbstractSpElPrinter(String expression) {
    this(expression, new SpelExpressionParser());
  }

  /**
   * Creates a new instance with the given expression and expression parser.
   * @param expression expression to generate a String from the provided objects
   * @param parser to parse the expression
   */
  public AbstractSpElPrinter(String expression, ExpressionParser parser) {
    this(expression, parser, new TemplateParserContext());
  }

  /**
   * Creates a new instance with the given expression and expression parser.
   * @param expression expression to generate a String from the provided objects
   * @param parser to parse the expression
   * @param templateParserContext context to use to parse the expression
   */
  public AbstractSpElPrinter(String expression, ExpressionParser parser,
      TemplateParserContext templateParserContext) {
    this.expression = expression;
    this.parser = parser;
    this.templateParserContext = templateParserContext;
  }

  /**
   * Returns the expression to generate the String
   * @return the expression
   */
  public String getExpression() {
    return expression;
  }

  /**
   * Returns the default expression to use when the provided one is null or empty.
   * @return the default expression
   */
  public String getDefaultExpression() {
    return defaultExpression;
  }

  /**
   * Sets the default expression to use when the provided one is null or empty.
   * @param defaultExpression the defaultExpression to set
   */
  public void setDefaultExpression(String defaultExpression) {
    this.defaultExpression = defaultExpression;
  }

  /**
   * Returns the expression, or the default one if it is empty or null.
   * @return the expression to use
   */
  public String getExpressionOrDefault() {
    return StringUtils.isEmpty(getExpression()) ? getDefaultExpression() : getExpression();
  }

  /**
   * Returns a {@link ExpressionParser} instance to parse the expression
   * @return the parser for the expression
   */
  public ExpressionParser getParser() {
    return parser;
  }

  /**
   * Returns the {@link TemplateParserContext} to use to parse the expression.
   * @return the templateParserContext
   */
  public TemplateParserContext getTemplateParserContext() {
    return templateParserContext;
  }

  @Override
  public String print(Object object, Locale locale) {
    return parseExpression(locale).getValue(object, String.class);
  }

  /**
   * Parses the given expression.
   * @param expression text of the expression to parse
   * @return the parsed expression
   */
  protected Expression parseExpression(String expression) {
    return getParser().parseExpression(expression, getTemplateParserContext());
  }

  /**
   * Parses the expression to apply based on the given {@link Locale}.
   * @param locale to get the expression for
   * @return the parsed expression
   */
  protected abstract Expression parseExpression(Locale locale);
}
