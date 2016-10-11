/*
 * Copyright (c) 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad de
 * España. Todos los derechos reservados.
 *
 * Proyecto : northwindjee
 */
package io.springlets.data.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.springlets.data.domain.GlobalSearch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Unit tests for the class {@link GlobalSearchHandlerMethodArgumentResolver}.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class GlobalSearchHandlerMethodArgumentResolverTest {

  private GlobalSearchHandlerMethodArgumentResolver resolver;

  @Mock
  private MethodParameter methodParameter;

  @Mock
  private NativeWebRequest request;

  @Before
  public void setUp() throws Exception {
    // This is the way to be able to return a Class object
    Mockito.<Class<?>>when(methodParameter.getParameterType()).thenReturn(GlobalSearch.class);

    resolver = new GlobalSearchHandlerMethodArgumentResolver();
  }

  @Test
  public void validateSupportedParameterTypeIsGlobalSearch() {
    // Exercise & Verify
    assertThat(resolver.supportsParameter(methodParameter));
  }

  @Test
  public void checkValidGlobalSearchNonRegexpIsResolved() throws Exception {
    // Prepare
    when(request.getParameter(resolver.getSearchValueParameter())).thenReturn("test");
    when(request.getParameter(resolver.getRegexpParameter())).thenReturn("false");

    // Exercise
    GlobalSearch search = resolver.resolveArgument(methodParameter, null, request, null);

    // Verify
    assertThat(search.getText()).as("Texto de búsqueda según parámetro").isEqualTo("test");
    assertThat(search.isRegexp()).as("Búsqueda por expresiones regulares no activa")
        .isEqualTo(false);
  }

}
