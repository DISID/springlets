/*
 * Copyright 2017 the original author or authors.
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
package io.springlets.web.mvc.converters;

import java.io.IOException;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import io.springlets.data.jpa.domain.EmbeddableImage;
import io.springlets.data.jpa.domain.EmbeddableImageException;

/**
 * = SpringletsImageFileConverter
 *
 * <p>
 * Converter that will convert the received MultipartFiles to a EmbeddableImage type
 * </p>
 *
 * <p>
 * This converter will be active only if the <code>${springlets.image.management}</code>
 * property has been registered in the {@link Environment} and if it has been setted with
 * <code>true</code> value.
 * </p>
 *
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsImageFileConverter implements Converter<MultipartFile, EmbeddableImage> {

  /**
   * Transforms the received MultipartFile to a EmbeddableImage file
   */
  @Override
  public EmbeddableImage convert(MultipartFile source) {

    try {
      // First of all, obtain the content type
      String contentType = source.getContentType();

      // If the provided MultipartFile is an image, create a new
      // Image item using the source bytes
      if (contentType.startsWith("image/")) {
        return new EmbeddableImage(source.getBytes());
      }

    } catch (IOException e) {
      throw new EmbeddableImageException("Error loading image", e);
    }
    // If is not an image, this converter
    // could not convert the provided MultipartFile
    return null;

  }



}
