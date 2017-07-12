package io.springlets.web.mvc.converters;

import io.springlets.data.jpa.domain.EmbeddableImage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
      e.printStackTrace();
    }
    // If is not an image, this converter
    // could not convert the provided MultipartFile
    return null;

  }

}
