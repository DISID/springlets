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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Locale;

/**
 * Unit tests for the {@link SpElPrinter} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpElPrinterTest {

  private static final String TO_STRING = "A string";

  private SpElPrinter printer;
  private ExpressionParser parser = new SpelExpressionParser();
  private TemplateParserContext context = new TemplateParserContext();
  private TestObject testObject = new TestObject();

  @Rule
  public ExpectedException thown = ExpectedException.none();

  @Test
  public void shouldPrintToStringWithEmptyExpression() {
    // Prepare
    printer = new SpElPrinter("", parser, context);

    // Exercise
    String result = printer.print(testObject, Locale.getDefault());

    // Validate
    assertThat(result).isNotEmpty().isEqualTo(TO_STRING);
  }

  @Test
  public void shouldPrintToStringWithNullExpression() {
    // Prepare
    printer = new SpElPrinter(null, parser, context);

    // Exercise
    String result = printer.print(testObject, Locale.getDefault());

    // Validate
    assertThat(result).isNotEmpty().isEqualTo(TO_STRING);
  }

  @Test
  public void shouldPrintUsingExpression() {
    // Prepare
    printer = new SpElPrinter("#{field1} - #{field2}", parser, context);

    // Exercise
    String result = printer.print(testObject, Locale.getDefault());

    // Validate
    assertThat(result).isNotEmpty().isEqualTo(testObject.field1 + " - " + testObject.field2);
  }

  private class TestObject {
    public String field1 = "value1";
    public Long field2 = Long.valueOf(2);

    @Override
    public String toString() {
      return TO_STRING;
    }
  }

}
