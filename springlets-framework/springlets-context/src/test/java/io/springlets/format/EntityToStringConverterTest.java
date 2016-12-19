/**
 * Copyright (c) 2016 DISID Corporation S.L. All rights reserved.
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
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
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

  private EntityToStringConverter converter;


  @Before
  public void setUp() throws Exception {
    converter = new EntityToStringConverter(parser, context, messageSource);
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
