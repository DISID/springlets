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
package io.springlets.web.mvc.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * Indicates to MappingJackson2HttpMessageConverter that it should serialize the response
 * with JSONP format.
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice{

  public JsonpAdvice(String[] jsonpQueryParamNames) {
   super(jsonpQueryParamNames);
  }
}
