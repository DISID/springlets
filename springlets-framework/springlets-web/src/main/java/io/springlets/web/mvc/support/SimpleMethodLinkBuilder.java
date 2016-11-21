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

import io.springlets.web.mvc.util.MethodLinkBuilder;
import io.springlets.web.mvc.util.MethodLinkFactory;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponents;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Simple {@link MethodLinkBuilder} implementation which delegates the creation
 * of {@link UriComponents} instances to its Controller methods through
 * a {@link MethodLinkFactory} of the same Controller. 
 * @param <T> the class of the Controller to create links to
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SimpleMethodLinkBuilder<T> implements MethodLinkBuilder<T> {

  private final String methodName;
  private final Set<ParameterIndex> parameters;
  private final Map<String, Object> pathVariables;
  private final MethodLinkFactory<T> linkFactory;

  /**
   * Creates a builder for a Controller's method with the given options.
   * @param linkFactory to use to create the links
   * @param methodName the name of the Controller's method
   * @param parameters of the method
   * @param pathVariables path variables defined in the the
   * {@link RequestMapping} of the method to be replaced 
   */
  public SimpleMethodLinkBuilder(MethodLinkFactory<T> linkFactory, String methodName,
      Set<ParameterIndex> parameters,
      Map<String, Object> pathVariables) {
    this.linkFactory = linkFactory;
    Assert.notNull(methodName, "Method name can't be null");
    Assert.notNull(linkFactory, "LinkFactory can't be null");
    this.methodName = methodName;

    if (parameters == null) {
      this.parameters = Collections.emptySet();
    } else {
      this.parameters = parameters;
    }

    if (pathVariables == null) {
      this.pathVariables = Collections.emptyMap();
    } else {
      this.pathVariables = pathVariables;
    }
  }

  @Override
  public MethodLinkBuilder<T> arg(int index, Object parameter) {
    Assert.isTrue(index >= 0, "Parameter index must be equal or greater than zero");

    Set<ParameterIndex> allParameters = new HashSet<ParameterIndex>(this.parameters);
    allParameters.add(new ParameterIndex(index, allParameters));

    return new SimpleMethodLinkBuilder<T>(this.linkFactory, this.methodName, allParameters,
        this.pathVariables);
  }

  @Override
  public MethodLinkBuilder<T> with(String variable, Object value) {
    Assert.notNull(variable, "Path variable name can't be null");

    Map<String, Object> allVariables = new HashMap<String, Object>(pathVariables);
    allVariables.put(variable, value);

    return new SimpleMethodLinkBuilder<T>(this.linkFactory, this.methodName, this.parameters,
        allVariables);
  }

  @Override
  public UriComponents toUri() {
    return linkFactory.toUri(methodName, indexedParameters(), pathVariables);
  }

  private Object[] indexedParameters() {
    Object[] indexedParams = null;
    int size = 0;
    for (ParameterIndex param : parameters) {
      if (param.getIndex() >= size) {
        size = param.getIndex() + 1;
      }
    }
    if (size > 0) {
      indexedParams = new Object[size];
      for (ParameterIndex param : parameters) {
        indexedParams[param.getIndex()] = param.getParameter();
      }
    }
    return indexedParams;
  }

  @Override
  public String toUriString() {
    return toUri().toUriString();
  }

  @Override
  public String toPath() {
    return toUri().getPath();
  }

  @Override
  public String toString() {
    return toPath();
  }

  private static class ParameterIndex {

    private final int index;
    private final Object parameter;

    /**
     * @param index
     * @param parameter
     */
    public ParameterIndex(int index, Object parameter) {
      this.index = index;
      this.parameter = parameter;
    }

    public int getIndex() {
      return index;
    }

    public Object getParameter() {
      return parameter;
    }
  }
}
