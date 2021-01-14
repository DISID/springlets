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
package io.springlets.web.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.mockito.Mockito.when;

import io.springlets.web.mvc.util.MethodLinkBuilder;
import io.springlets.web.mvc.util.MethodLinkFactory;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.util.UriComponents;

import java.util.Map;

/**
 * Unit tests for the {@link SimpleMethodLinkBuilder} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleMethodLinkBuilderTest {

  private static final String THEURI = "theuri";

  private static final String VARIABLE_VALUE = "a value";

  private static final String VARIABLE_NAME = "var";

  private static final String METHOD_NAME = "test";

  private MethodLinkBuilder<?> builder;

  @Mock
  private MethodLinkFactory<?> linkFactory;

  @Mock
  private UriComponents uriMethod;

  @Mock
  private UriComponents uriMethodParams;

  @Mock
  private UriComponents uriMethodVars;

  @Mock
  private UriComponents uriMethodParamsVars;

  @Before
  public void setupContext() {
    builder = new SimpleMethodLinkBuilder<>(linkFactory, METHOD_NAME, null, null);
  }

  /**
   * Test method for {@link io.springlets.web.mvc.support.SimpleMethodLinkBuilder#toUri()}.
   */
  @Test
  public void testToUri() {
    // Setup
    when(linkFactory.toUri(eq(METHOD_NAME), isNull(),
        anyMap())).thenReturn(uriMethod);

    when(linkFactory.toUri(eq(METHOD_NAME), argThat(isParametersOfOneElement()),
        anyMap())).thenReturn(uriMethodParams);

    when(linkFactory.toUri(eq(METHOD_NAME), isNull(),
        argThat(isPathVariablesOfVariable(VARIABLE_NAME, VARIABLE_VALUE))))
            .thenReturn(uriMethodVars);

    when(linkFactory.toUri(eq(METHOD_NAME), argThat(isParametersOfOneElement()),
        argThat(isPathVariablesOfVariable(VARIABLE_NAME, VARIABLE_VALUE))))
            .thenReturn(uriMethodParamsVars);

    // Exercise
    UriComponents uri1 = builder.toUri();
    UriComponents uri2 = builder.arg(0, new Object()).toUri();
    UriComponents uri3 = builder.with(VARIABLE_NAME, VARIABLE_VALUE).toUri();
    UriComponents uri4 = builder.arg(0, new Object())
        .with(VARIABLE_NAME, VARIABLE_VALUE).toUri();

    // Verify
    assertThat(uri1).isNotNull().isEqualTo(uriMethod);
    assertThat(uri2).isNotNull().isEqualTo(uriMethodParams);
    assertThat(uri3).isNotNull().isEqualTo(uriMethodVars);
    assertThat(uri4).isNotNull().isEqualTo(uriMethodParamsVars);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMethodNameThrowsException() {
    new SimpleMethodLinkBuilder<>(linkFactory, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullLinkFactoryThrowsException() {
    new SimpleMethodLinkBuilder<>(null, METHOD_NAME, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void argWithNegativeIndexShouldThrowException() {
    builder.arg(-1, new Object());
  }

  @Test
  public void toUriStringShouldCallSameMethodInUriComponents() {
    // Setup
    when(linkFactory.toUri(eq(METHOD_NAME), isNull(),
        anyMap())).thenReturn(uriMethod);

    when(uriMethod.toUriString()).thenReturn(THEURI);

    // Exercise
    String uri = builder.toUriString();

    // Verify
    assertThat(uri).isNotNull().isEqualTo(THEURI);
  }

  @Test
  public void toPathShouldCallSameMethodInUriComponents() {
    // Setup
    when(linkFactory.toUri(eq(METHOD_NAME), isNull(),
        anyMap())).thenReturn(uriMethod);

    when(uriMethod.getPath()).thenReturn(THEURI);

    // Exercise
    String path = builder.toPath();

    // Verify
    assertThat(path).isNotNull().isEqualTo(THEURI);
  }

  @Test
  public void toStringShouldReturnToPathValue() {
    // Setup
    when(linkFactory.toUri(eq(METHOD_NAME), isNull(),
        anyMap())).thenReturn(uriMethod);

    when(uriMethod.getPath()).thenReturn(THEURI);

    // Exercise
    String path = builder.toString();

    // Verify
    assertThat(path).isNotNull().isEqualTo(THEURI);
  }

  private Matcher<Object[]> isParametersOfOneElement() {
    return new BaseMatcher<Object[]>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("one element");
      }
      @Override
      public boolean matches(Object item) {
        return item != null && ((Object[]) item).length == 1;
      }
    };
  }

  private Matcher<Map<String, Object>> isPathVariablesOfVariable(final String variable,
      final String value) {
    return new BaseMatcher<Map<String, Object>>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("is path variables of variable");
      }

      @SuppressWarnings("unchecked")
      @Override
      public boolean matches(Object arg) {
        Map<String, Object> item = (Map<String, Object>)arg;
        if (item.size() == 1) {
          Object argVariableValue = item.get(variable);
          return value.equals(argVariableValue);
        }
        return false;
      }
    };
  }

}
