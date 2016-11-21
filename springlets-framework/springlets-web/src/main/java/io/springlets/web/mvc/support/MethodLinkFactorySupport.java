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

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import io.springlets.web.mvc.util.MethodLinkFactory;

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

/**
 * Base class for easier implementation of {@link MethodLinkFactory} classes.
 * @param <T> the class of the Controller to create links to
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public abstract class MethodLinkFactorySupport<T> implements MethodLinkFactory<T> {

  private final Class<T> controllerClass;

  /**
   * Creates an instance linked to the given controller class.
   * @param controllerClass class of the controller to create links to
   */
  public MethodLinkFactorySupport(Class<T> controllerClass) {
    this.controllerClass = controllerClass;
  }

  @Override
  public Class<T> getControllerClass() {
    return controllerClass;
  }

  /**
   * Utility method to simplify calling the
   * {@link MvcUriComponentsBuilder#on(Class)} method with the current
   * controller class. 
   * It can be used to shorten the creation of {@link UriComponents} to
   * controller methods like in the following example (for a controller
   * with a 'list' method with a single parameter):
   * 
   * [source,java]
   * ----
   * fromMethodCall(onController().list(null)).build();
   * ----
   * 
   * @return a proxy instance of the Controller
   */
  protected T onController() {
    return on(getControllerClass());
  }

}
