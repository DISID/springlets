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
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.format.Printer;

import java.util.Set;

/**
 * Unit tests for the {@link SpElFormatAnnotationFormatterFactory} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class SpElFormatAnnotationFormatterFactoryTest {

  @Rule
  public ExpectedException thown = ExpectedException.none();

  @Mock
  private MessageSource messageSource;

  @Mock
  private SpElFormat format;

  private SpElFormatAnnotationFormatterFactory factory =
      new SpElFormatAnnotationFormatterFactory(messageSource);

  /**
   * Test method for {@link io.springlets.format.SpElFormatAnnotationFormatterFactory#getFieldTypes()}.
   */
  @Test
  public void shouldReturnObjectFieldType() {
    // Exercise
    Set<Class<?>> fieldTypes = factory.getFieldTypes();

    // Verify
    assertThat(fieldTypes).isNotEmpty();
    assertThat(fieldTypes.size()).isEqualTo(1);
    assertThat(fieldTypes.iterator().next()).isEqualTo(Object.class);
  }

  /**
   * Test method for {@link io.springlets.format.SpElFormatAnnotationFormatterFactory#getPrinter(io.springlets.format.SpElFormat, java.lang.Class)}.
   */
  @Test
  @Ignore("Whe have to find a way to mock AnnotationUtils")
  public void shouldReturnASpElPrinter() {
    // Prepare
    when(format.message()).thenReturn(null);
    when(format.expression()).thenReturn("expression");

    // Exercise
    Printer<?> printer = factory.getPrinter(format, Object.class);

    // Verify
    assertThat(printer).isInstanceOf(SpElPrinter.class);
  }

  /**
   * Test method for {@link io.springlets.format.SpElFormatAnnotationFormatterFactory#getPrinter(io.springlets.format.SpElFormat, java.lang.Class)}.
   */
  @Test
  @Ignore("Whe have to find a way to mock AnnotationUtils")
  public void shouldReturnASpElMessagePrinter() {
    // Prepare
    when(format.message()).thenReturn("messageCode");
    when(format.expression()).thenReturn("expression");

    // Exercise
    Printer<?> printer = factory.getPrinter(format, Object.class);

    // Verify
    assertThat(printer).isInstanceOf(SpElMessagePrinter.class);
  }

  /**
   * Test method for {@link io.springlets.format.SpElFormatAnnotationFormatterFactory#getParser(io.springlets.format.SpElFormat, java.lang.Class)}.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowException() {
    // Exercise
    factory.getParser(null, null);
  }

}
