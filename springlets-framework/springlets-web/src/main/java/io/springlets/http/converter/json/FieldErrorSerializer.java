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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link FieldError} Jackson Serializer for {@link FieldError} objects.
 * @author http://www.disid.com[DISID Corporation S.L.]
 */
public class FieldErrorSerializer extends JsonSerializer<FieldError> {

  @Override
  public void serialize(FieldError result, JsonGenerator gen, SerializerProvider serializers)
      throws IOException, JsonProcessingException {

    Map<String, Object> bindingResult = new HashMap<String, Object>();
    bindingResult.put("defaultMessage", result.getDefaultMessage());
    bindingResult.put("field", result.getField());
    bindingResult.put("rejectedValue", result.getRejectedValue());
    bindingResult.put("error-code", result.getCode());

    gen.writeObject(bindingResult);
  }
}
