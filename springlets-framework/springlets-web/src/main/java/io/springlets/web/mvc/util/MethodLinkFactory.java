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
package io.springlets.web.mvc.util;

import org.springframework.web.util.UriComponents;

import java.util.Map;

/**
 * Factory to create {@link UriComponents} instances to a controller method.
 * @param <T> the class of the Controller to create links to
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public interface MethodLinkFactory<T> {

  /**
   * Returns the class of the controller to create method links to.
   * @return the controller's class
   */
  Class<T> getControllerClass();

  /**
   * Creates a {@link UriComponents} for the controller returned in the
   * {@link #getControllerClass()} with the given parameters.
   * @param methodName the method to create the uri to
   * @param parameters the method parameters
   * @param pathVariables the path variables defined in the method
   * RequestMapping
   * @return the uri to the controller's method
   */
  UriComponents toUri(String methodName, Object[] parameters,
      Map<String, Object> pathVariables);
}
