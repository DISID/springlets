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
package io.springlets.format;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.Parser;

import java.text.ParseException;
import java.util.Locale;

/**
 * A {@link Parser} for entities which is able to get the entity from its identifier in String form.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class EntityParser<T, ID> implements Parser<T> {

  private EntityResolver<T, ID> entityService;
  private ConversionService conversionService;

  /**
   * Creates a new {@link EntityParser}.
   * @param resolver used to find entities by its identifier
   * @param conversionService used to convert from String to the identifier type
   */
  public EntityParser(EntityResolver<T, ID> resolver, ConversionService conversionService) {
    this.entityService = resolver;
    this.conversionService = conversionService;
  }

  @Override
  public T parse(String text, Locale locale) throws ParseException {
    ID idValue = conversionService.convert(text, entityService.getIdType());

    return entityService.findOne(idValue);
  }

}
