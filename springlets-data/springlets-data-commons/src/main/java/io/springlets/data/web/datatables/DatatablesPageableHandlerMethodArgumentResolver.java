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

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resolves controller method parameters of type {@link DatatablesPageable}.
 * 
 * It extends the Spring Data's {@link PageableHandlerMethodArgumentResolver}
 * to set the parameter names used by the datatables components, and creates
 * a {@link DatatablesPageable} pageable.
 * 
 * See {@link https://datatables.net/manual/server-side}
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class DatatablesPageableHandlerMethodArgumentResolver
    extends PageableHandlerMethodArgumentResolver {

  /**
   * Creates a new instance, providing the datatables paging
   * parameter names.
   */
  public DatatablesPageableHandlerMethodArgumentResolver() {
    super(new DatatablesSortHandlerMethodArgumentResolver());
    setPageParameterName(Datatables.PARAMETER_START);
    setSizeParameterName(Datatables.PARAMETER_LENGTH);
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return DatatablesPageable.class.equals(parameter.getParameterType());
  }

  @Override
  public DatatablesPageable resolveArgument(MethodParameter methodParameter,
      ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Pageable pageable =
        super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

    return new DatatablesPageable(pageable);
  }

}
