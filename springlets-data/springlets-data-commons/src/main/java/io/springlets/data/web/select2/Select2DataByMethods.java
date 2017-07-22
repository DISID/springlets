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
package io.springlets.data.web.select2;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

/**
 * Response data for data requests performed by a select2 javascript component. 
 * This class will be converted to JSON, so the property names must follow the
 * name of the properties expected by select2.
 * 
 * This class uses the two functional interfaces and the {@link ConversionService} 
 * to convert the data to String. This allows to use _method reference Java 8
 * feature_ to get required data without use expressions. 
 * 
 * @author Jose Manuel Vivó at http://www.disid.com[DISID Corporation S.L.]
 * @see https://select2.github.io/examples.html#data-ajax
 *
 * @param <T> Response data type
 */
public class Select2DataByMethods<T> extends Select2DataSupport<T> {

  private final ConversionService conversionService;
  private boolean includeEntireElement;
  private IdProvider<T> idProvider;
  private CaptionProvider<T> captionProvider;

  /**
   * Create a response for select2 with data obtained from a request.
   *
   * @param page the data to show
   * @param idProvieder instance of {@link IdProvider} which can return identifier of target instance
   * @param captionProvieder instance of {@link CaptionProvider} which can return caption of target instance
   * @param conversionService to convert the data to String
   */
  public Select2DataByMethods(Page<T> page,IdProvider<T> idProvider,
      CaptionProvider<T> captionProvider,
      ConversionService conversionService) {
    super(page);
    Assert.notNull(page, "A ConversionService is required");

    this.conversionService = conversionService;
    this.idProvider = idProvider;
    this.captionProvider = captionProvider;
    // By default, the entire element will not be included in the response
    this.includeEntireElement = false;
  }

  /**
   * Create a response for select2 with data obtained from a request and indicates
   * if the entire element should be included in the response.
   *
   * @since 1.2.0
   *
   * @param page the data to show
   * @param idProvieder instance of {@link IdProvider} which can return identifier of target instance
   * @param captionProvieder instance of {@link CaptionProvider} which can return caption of target instance
   * @param conversionService to convert the data to String
   * @param includeEntireElement boolean that indicates if the JSON response must contain the
   *  entire element or only include the `id` and `text` select2 default fields. DEFAULT: false
   */
  public Select2DataByMethods(Page<T> page, IdProvider<T> idProvider,
      CaptionProvider<T> captionProvider, ConversionService conversionService,
      boolean includeEntireElement) {
    this(page, idProvider, captionProvider, conversionService);
    this.includeEntireElement = includeEntireElement;
  }


  @Override
  protected Data<T> createData(T element) {
    String id = getIdAsString(element);
    String text = getAsString(element);

    if (includeEntireElement) {
      return new Data<T>(id, text, element);
    }

    return new Data<T>(id, text);
  }

  @Override
  protected String getAsString(T element) {
    return conversionService.convert(captionProvider.getCaption(element), String.class);
  }

  @Override
  protected String getIdAsString(T element) {
    return conversionService.convert(idProvider.getId(element), String.class);
  }

  /**
   * = _IdProvider_
   *
   * Provides the identifier of target object 
   *
   * @author Jose Manuel Vivó at http://www.disid.com[DISID Corporation S.L.]
   *
   * @param <T>
   */
  @FunctionalInterface
  public static interface IdProvider<T> {
    Object getId(T object);
  }

  /**
   * = _CaptionProvider_
   *
   * Provides the caption of target objet
   *
   * @author Jose Manuel Vivó at http://www.disid.com[DISID Corporation S.L.]
   *
   * @param <T>
   */
  @FunctionalInterface
  public static interface CaptionProvider<T> {
    Object getCaption(T object);
  }
}


