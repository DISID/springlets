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

import io.springlets.web.mvc.util.ControllerMethodLinkBuilderFactory;
import io.springlets.web.mvc.util.MethodLinkBuilderFactory;
import io.springlets.web.mvc.util.MethodLinkFactory;
import io.springlets.web.mvc.util.MethodLinkFactoryRegistry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple implementation of the {@link ControllerMethodLinkBuilderFactory}, as
 * well as the {@link MethodLinkFactoryRegistry}. 
 * It allows to register implementations of {@link MethodLinkFactory} instances
 * linked to controllers, allowing to use them to create 
 * {@link MethodLinkBuilderFactory} instances for those controllers. 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SimpleControllerMethodLinkBuilderFactory
    implements ControllerMethodLinkBuilderFactory, MethodLinkFactoryRegistry {

  private final Map<Class<?>, MethodLinkFactory<?>> registry =
      new HashMap<Class<?>, MethodLinkFactory<?>>();

  @Override
  public void register(MethodLinkFactory<?> factory) {
    registry.put(factory.getControllerClass(), factory);
  }

  @Override
  public <T> MethodLinkBuilderFactory<T> of(Class<T> controller) {
    @SuppressWarnings("unchecked")
    MethodLinkFactory<T> linkFactory = (MethodLinkFactory<T>) registry.get(controller);
    if (linkFactory != null) {
      return new SimpleMethodLinkBuilderFactory<T>(linkFactory);
    }
    throw new IllegalArgumentException(
        "No MethodLinkBuilderFactory registered for controller: " + controller);
  }

  @Override
  public MethodLinkBuilderFactory<?> of(String controllerClassName) {
    for (Iterator<Class<?>> iterator = registry.keySet().iterator(); iterator.hasNext();) {
      Class<?> clazz = iterator.next();
      if (clazz.getSimpleName().equals(controllerClassName)) {
        return new SimpleMethodLinkBuilderFactory<>(registry.get(clazz));
      }
    }
    throw new IllegalArgumentException(
        "No MethodLinkBuilderFactory registered for controller with name: " + controllerClassName);
  }
}
