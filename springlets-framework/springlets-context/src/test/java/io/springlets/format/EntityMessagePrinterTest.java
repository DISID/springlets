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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Locale;

/**
 * Unit tests for the {@link EntityMessagePrinter} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityMessagePrinterTest {

  private static final String TO_STRING_EXPRESSION = "#{toString()}";

  private static final String TO_STRING_VALUE = "A string";

  private EntityMessagePrinter printer;
  private ExpressionParser parser = new SpelExpressionParser();
  private TemplateParserContext context = new TemplateParserContext();
  private TestObject testObject = new TestObject();
  private Locale locale = Locale.getDefault();

  @Mock
  private MessageSource messageSource;

  @Rule
  public ExpectedException thown = ExpectedException.none();

  @Test
  public void shouldPrintToStringWithNullMessageCode() {
    // Prepare
    printer = new EntityMessagePrinter(null, messageSource, parser, context, TO_STRING_EXPRESSION);

    // Exercise
    String result = printer.print(testObject, locale);

    // Validate
    assertThat(result).isNotEmpty().isEqualTo(TO_STRING_VALUE);
  }

  @Test
  public void shouldPrintToStringWithEmptyMessage() {
    // Prepare
    when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), eq(locale)))
        .thenReturn(TO_STRING_EXPRESSION);
    printer =
        new EntityMessagePrinter("empty", messageSource, parser, context, TO_STRING_EXPRESSION);

    // Exercise
    String result = printer.print(testObject, locale);

    // Validate
    assertThat(result).isNotEmpty().isEqualTo(TO_STRING_VALUE);
  }

  @Test
  public void shouldPrintUsingExpression() {
    // Prepare
    when(messageSource.getMessage("message", null, null, locale))
        .thenReturn("#{field1} - #{field2}");
    printer =
        new EntityMessagePrinter("message", messageSource, parser, context, TO_STRING_EXPRESSION);

    // Exercise
    String result = printer.print(testObject, locale);

    // Validate
    assertThat(result).isNotEmpty().isEqualTo(testObject.field1 + " - " + testObject.field2);
  }

  private class TestObject {
    public String field1 = "value1";
    public Long field2 = Long.valueOf(2);

    @Override
    public String toString() {
      return TO_STRING_VALUE;
    }
  }

}
