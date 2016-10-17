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
package io.springlets.http.converter.json;

import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Module to configure the serialization of BindingResult objects.
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Component
public class BindingResultModule extends SimpleModule {

  private static final long serialVersionUID = 6297913040418497316L;

  public BindingResultModule() {
    addSerializer(BindingResult.class, new BindingResultSerializer());
    addSerializer(FieldError.class, new FieldErrorSerializer());
  }
}
