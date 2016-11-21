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

/**
 * Factory to build links to the methods of a controller. It creates a 
 * {@link MethodLinkBuilderFactory} as the starting point to set the
 * method name and any optional method parameters and path variables
 * needed. 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public interface ControllerMethodLinkBuilderFactory {

  /**
   * Returns a {@link MethodLinkBuilderFactory} to create links to methods of
   * the given controller
   * @param controller the create links to
   * @return the factory
   */
  <T> MethodLinkBuilderFactory<T> of(Class<T> controller);

  /**
   * Returns a {@link MethodLinkBuilderFactory} to create links to methods of
   * the given controller
   * @param controller the class name of the controller to create links to
   * @return the factory
   */
  MethodLinkBuilderFactory<?> of(String controllerClassName);

}
