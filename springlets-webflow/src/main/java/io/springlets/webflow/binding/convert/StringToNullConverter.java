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
package io.springlets.webflow.binding.convert;

import org.springframework.binding.convert.converters.Converter;
import org.springframework.util.StringUtils;

/**
 * {@link Converter} implementation to transform empty String into null values.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class StringToNullConverter implements Converter {

  @Override
  public Class<?> getSourceClass() {
    return String.class;
  }

  @Override
  public Class<?> getTargetClass() {
    return String.class;
  }

  @Override
  public Object convertSourceToTargetClass(Object source, Class<?> targetClass) throws Exception {
    if (source == null) {
      return null;
    } else if (source instanceof String) {
      if (StringUtils.isEmpty(source)) {
        return null;
      } else {
        return source;
      }
    }
    return null;
  }

}

