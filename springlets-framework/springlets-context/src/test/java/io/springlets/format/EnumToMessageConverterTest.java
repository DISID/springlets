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
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

import java.util.Locale;
import java.util.Set;

/**
 * Unit tests for the {@link EnumToMessageConverter} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class EnumToMessageConverterTest {

  @Rule
  public ExpectedException thown = ExpectedException.none();

  private EnumToMessageConverter converter;

  @Mock
  private MessageSource messageSource;

  @Before
  public void setUp() throws Exception {
    converter = new EnumToMessageConverter(messageSource);
  }

  @Test
  public void checkValidConvertibleTypes() {
    // Prepare

    // Exercise
    Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();

    // Validate
    assertThat(convertibleTypes).isNotEmpty()
        .containsExactly(new ConvertiblePair(Enum.class, String.class));
  }

  @Test
  public void checkConvertsToEnumName() {
    // Prepare
    when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class)))
        .thenAnswer(new Answer<String>() {

          @Override
          public String answer(InvocationOnMock invocation) throws Throwable {
            return invocation.getArgumentAt(2, String.class);
          }

        });

    // Exercise
    Object converted = converter.convert(TestEnum.ONE, TypeDescriptor.valueOf(TestEnum.class),
        TypeDescriptor.valueOf(String.class));

    // Validate
    assertThat(converted).isEqualTo("ONE");
  }

  @Test
  public void checkConvertsToEnumMessage() {
    // Prepare
    when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class)))
        .thenAnswer(new Answer<String>() {

          @Override
          public String answer(InvocationOnMock invocation) throws Throwable {
            String name = invocation.getArgumentAt(2, String.class);
            return name.toLowerCase();
          }

        });

    // Exercise
    Object converted = converter.convert(TestEnum.ONE, TypeDescriptor.valueOf(TestEnum.class),
        TypeDescriptor.valueOf(String.class));

    // Validate
    assertThat(converted).isEqualTo("one");
  }

  @Test
  public void convertNullShouldReturnNull() {
    // Prepare
    // Exercise
    Object converted = converter.convert(null, TypeDescriptor.valueOf(TestEnum.class),
        TypeDescriptor.valueOf(String.class));

    // Validate
    assertThat(converted).isNull();
  }

  enum TestEnum {
    ONE, TWO
  }
}
