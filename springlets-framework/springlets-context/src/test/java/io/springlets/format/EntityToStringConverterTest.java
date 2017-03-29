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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;

import java.util.Set;

/**
 * @author cordin at http://www.disid.com[DISID Corporation S.L.]
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityToStringConverterTest {

  @Rule
  public ExpectedException thown = ExpectedException.none();

  @Mock
  private MessageSource messageSource;

  @Mock
  private ExpressionParser parser;

  @Mock
  private TemplateParserContext context;

  @Mock
  private TypeDescriptor sourceType;

  @Mock
  private TypeDescriptor targetType;

  private ConversionService conversionService = new DefaultConversionService();

  private EntityToStringConverter converter;


  @Before
  public void setUp() throws Exception {
    converter = new EntityToStringConverter(parser, context, messageSource, conversionService);
  }

  /**
   * Test method for {@link io.springlets.format.EntityToStringConverter#getConvertibleTypes()}.
   */
  @Test
  public void testGetConvertibleTypes() {
    // Prepare

    // Exercise
    Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();

    // Validate
    assertThat(convertibleTypes).isNotEmpty()
        .containsExactly(new ConvertiblePair(Object.class, String.class));
  }

  /**
   * Test method for {@link io.springlets.format.EntityToStringConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)}.
   */
  @Test
  @Ignore("Whe have to find a way to mock AnnotatedElementUtils")
  public void testConvert() {
    // Prepare

    // Exercise
    Object converted = converter.convert("test", sourceType, targetType);

    // Validate
    assertThat(converted).isEqualTo("test");
  }

  /**
   * Test method for {@link io.springlets.format.EntityToStringConverter#matches(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)}.
   */
  @Test
  @Ignore("Whe have to find a way to mock AnnotatedElementUtils")
  public void testMatches() {
    // Prepare
    // This is the way to be able to return a Class object
    Mockito.<Class<?>>when(targetType.getType()).thenReturn(String.class);

    // Exercise
    boolean matches = converter.matches(sourceType, targetType);

    // Validate
    assertThat(matches).isTrue();
  }

}
