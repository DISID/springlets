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
package io.springlets.data.web.datatables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.HashMap;

/**
 * Unit tests for the class {@link DatatablesSortHandlerMethodArgumentResolver}.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class DatatablesSortHandlerMethodArgumentResolverTest {

  private DatatablesSortHandlerMethodArgumentResolver resolver;

  @Mock
  private MethodParameter methodParameter;

  @Mock
  private NativeWebRequest request;

  @Before
  public void setUp() throws Exception {
    resolver = new DatatablesSortHandlerMethodArgumentResolver();
  }

  /**
   * Unit test for the method {@link es.msssi.fwmvnje6.northwindjee.datatables.DatatablesSortHandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)}.
   */
  @Test
  public void validateSupportedParameterTypeIsSort() {
    // Prepare
    Mockito.<Class<?>>when(methodParameter.getParameterType()).thenReturn(Sort.class);

    // Exercise & Verify
    assertThat(resolver.supportsParameter(methodParameter)).isTrue();
  }

  /**
   * Unit test for the method {@link es.msssi.fwmvnje6.northwindjee.datatables.DatatablesSortHandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)}.
   */
  @Test
  public void checkValidSortIsResolved() throws Exception {
    // Prepare
    String[] paramNames = new String[] {"order[0][column]", "order[0][dir]", "columns[0][data]",
        "columns[1][data]", "columns[2][data]"};
    String[] paramValues = new String[] {"1", "asc", "property0", "property1", "property2"};

    HashMap<String, String[]> map = new HashMap<String, String[]>(paramNames.length);

    for (int i = 0; i < paramNames.length; i++) {
      String[] value = paramValues == null ? null : new String[] {paramValues[i]};
      map.put(paramNames[i], value);
    }

    when(request.getParameterMap()).thenReturn(map);

    // Exercise
    Sort sort = resolver.resolveArgument(methodParameter, null, request, null);

    // Verify
    Order order0 = sort.getOrderFor("property1");
    assertThat(order0.getProperty()).as("Valid property name to order by")
        .isEqualTo("property1");
    assertThat(order0.getDirection()).as("Valir ordering direction for the property")
        .isEqualTo(Direction.ASC);
  }

}
