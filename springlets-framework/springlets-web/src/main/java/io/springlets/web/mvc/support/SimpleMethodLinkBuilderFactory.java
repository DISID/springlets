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
import io.springlets.web.mvc.util.MethodLinkBuilderFactory;
import io.springlets.web.mvc.util.MethodLinkFactory;

/**
 * Simple {@link MethodLinkBuilderFactory} implementation which creates
 * instances of {@link MethodLinkBuilder} using the
 * {@link SimpleMethodLinkBuilder} class.
 * @param <T> the class of the Controller to create links to
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SimpleMethodLinkBuilderFactory<T> implements MethodLinkBuilderFactory<T> {

  private final MethodLinkFactory<T> linkFactory;

  /**
   * Creates a factory of {@link MethodLinkBuilder} objects based on the given
   * {@link MethodLinkFactory} related to a Controller
   * @param linkFactory to use to create links to the Controller
   */
  public SimpleMethodLinkBuilderFactory(MethodLinkFactory<T> linkFactory) {
    this.linkFactory = linkFactory;
  }

  @Override
  public MethodLinkBuilder<T> to(String methodName) {
    return new SimpleMethodLinkBuilder<T>(linkFactory, methodName, null, null);
  }

}
