/**
 * Copyright (c) 2016 DISID Corporation S.L. All rights reserved.
 */
package io.springlets.format;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Converts Enum type values to String. 
 * It looks for a i18n message with the following format: *enum_CLASS_NAME*, being *CLASS*
 * the enum simple class name, and name the enum value name.
 * If the message is not found, the enum name will be used instead.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class EnumToMessageConverter implements GenericConverter {

  private static final String SEPARATOR = "_";
  private static final String ENUM_MESSAGE_CODE_PREFIX = "enum_";
  private static final Set<ConvertiblePair> CONVERTIBLE_TYPES;

  static {
    ConvertiblePair pair = new ConvertiblePair(Enum.class, String.class);
    CONVERTIBLE_TYPES = Collections.singleton(pair);
  }

  private final MessageSource messageSource;

  /**
   * Creates a new enum message converter.
   * @param messageSource to look for the i18n messages.
   */
  public EnumToMessageConverter(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return CONVERTIBLE_TYPES;
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (source == null) {
      // Nothing to convert
      return null;
    }
    @SuppressWarnings("rawtypes")
    String name = ((Enum) source).name();
    String code = ENUM_MESSAGE_CODE_PREFIX + source.getClass().getSimpleName() + SEPARATOR + name;
    String message = messageSource.getMessage(code, null, name, getCurrentLocale());

    return message;
  }

  private Locale getCurrentLocale() {
    return LocaleContextHolder.getLocale();
  }

}
