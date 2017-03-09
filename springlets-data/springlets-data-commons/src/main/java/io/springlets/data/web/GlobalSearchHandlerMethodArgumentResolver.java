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
package io.springlets.data.web;

import io.springlets.data.domain.GlobalSearch;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Extracts global search text information from web requests and thus allows 
 * injecting {@link GlobalSearch} instances into controller methods. 
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class GlobalSearchHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String DEFAULT_REGEXP_PARAMETER = "search[regex]";

  private static final String DEFAULT_SEARCH_VALUE_PARAMETER = "search[value]";

  private String searchValueParameter = DEFAULT_SEARCH_VALUE_PARAMETER;

  private String regexpParameter = DEFAULT_REGEXP_PARAMETER;

  public String getSearchValueParameter() {
    return searchValueParameter;
  }

  public void setSearchValueParameter(String searchValueParameter) {
    this.searchValueParameter = searchValueParameter;
  }

  public String getRegexpParameter() {
    return regexpParameter;
  }

  public void setRegexpParameter(String regexpParameter) {
    this.regexpParameter = regexpParameter;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return GlobalSearch.class.equals(parameter.getParameterType());
  }

  @Override
  public GlobalSearch resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

    String searchValue = webRequest.getParameter(getSearchValueParameter());
    if (StringUtils.isEmpty(searchValue)) {
      return null;
    }
    String regexp = webRequest.getParameter(getRegexpParameter());
    if ("true".equalsIgnoreCase(regexp)) {
      return new GlobalSearch(searchValue, true);
    } else if ("false".equalsIgnoreCase(regexp)) {
      return new GlobalSearch(searchValue, false);
    }

    return new GlobalSearch(searchValue);
  }

}
