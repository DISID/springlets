/**
 * Copyright (c) 2016 DISID Corporation S.L. All rights reserved.
 */
package io.springlets.data.web.datatables;

import io.springlets.data.web.datatables.DatatablesColumns.Column;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.Page;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Response data for data requests performed by datatables component. 
 * 
 * The data is converted through a {@link ConversionService} to a Map of String values with each
 * object bean property as the key. 
 * 
 * It may also take into account the columns parameters sent
 * by the datatables to convert only the needed properties.
 * 
 * The current {@link Locale} is also taken into account, so this class allows to send data to
 * a datatables using the same conversion which is used in the application pages.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class ConvertedDatatablesData<T> extends DatatablesData<Map<String, Object>> {

  private static final TypeDescriptor TYPE_STRING = TypeDescriptor.valueOf(String.class);

  /**
   * Create a response for datatables with data obtained from a previous request.
   *
   * @param data the data to show
   * @param recordsTotal the total number of available data
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   * @param conversionService to convert the data values to String
   */
  public ConvertedDatatablesData(Page<T> data, Long recordsTotal, Integer draw,
      ConversionService conversionService) {
    this(data.getContent(), recordsTotal, data.getTotalElements(), draw, conversionService, null);
  }

  /**
   * Create a response for datatables with data obtained from a previous request.
   *
   * @param data the data to show
   * @param recordsTotal the total number of available data
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   * @param conversionService to convert the data values to String
   * @param columns parameters sent by datatables
   */
  public ConvertedDatatablesData(Page<T> data, Long recordsTotal, Integer draw,
      ConversionService conversionService, DatatablesColumns columns) {
    this(data.getContent(), recordsTotal, data.getTotalElements(), draw, conversionService,
        columns);
  }

  /**
   * Create a response for datatables with data obtained from a previous request.
   *
   * @param data the data to show
   * @param recordsTotal the total number of available data
   * @param recordsFiltered the number of data after filtering
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   * @param conversionService to convert the data values to String
   */
  public ConvertedDatatablesData(List<T> data, Long recordsTotal, Long recordsFiltered,
      Integer draw, ConversionService conversionService) {
    this(data, recordsTotal, recordsFiltered, draw, conversionService, null);
  }

  /**
   * Create a response for datatables with data obtained from a previous request.
   *
   * @param data the data to show
   * @param recordsTotal the total number of available data
   * @param recordsFiltered the number of data after filtering
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   * @param conversionService to convert the data values to String
   * @param columns parameters sent by datatables
   */
  public ConvertedDatatablesData(List<T> data, Long recordsTotal, Long recordsFiltered,
      Integer draw, ConversionService conversionService, DatatablesColumns columns) {
    super(convertAll(data, conversionService, columns), recordsTotal, recordsFiltered, draw, null);
  }

  private static List<Map<String, Object>> convertAll(List<?> data,
      ConversionService conversionService, DatatablesColumns columns) {

    if (data == null) {
      return null;
    }

    List<Map<String, Object>> converted = new ArrayList<>(data.size());

    for (Object value : data) {
      Map<String, Object> convertedValue = columns == null ? convert(value, conversionService)
          : convert(value, conversionService, columns);
      converted.add(convertedValue);
    }

    return converted;
  }

  private static Map<String, Object> convert(Object value, ConversionService conversionService) {

    BeanWrapper bean = new BeanWrapperImpl(value);
    PropertyDescriptor[] properties = bean.getPropertyDescriptors();
    Map<String, Object> convertedValue = new HashMap<>(properties.length);

    for (int i = 0; i < properties.length; i++) {
      String name = properties[i].getName();
      Object propertyValue = bean.getPropertyValue(name);
      if (propertyValue != null
          && conversionService.canConvert(propertyValue.getClass(), String.class)) {
        TypeDescriptor source = bean.getPropertyTypeDescriptor(name);
        String convertedPropertyValue =
            (String) conversionService.convert(propertyValue, source, TYPE_STRING);
        convertedValue.put(name, convertedPropertyValue);
      }
    }

    return convertedValue;
  }

  private static Map<String, Object> convert(Object value, ConversionService conversionService,
      DatatablesColumns columns) {

    BeanWrapper bean = new BeanWrapperImpl(value);
    Map<String, Object> convertedValue = new HashMap<>();

    for (Column column : columns.getColumns()) {
      String property = column.getData();
      convertedValue.put(property,
          convertProperty(bean, property, conversionService, convertedValue));
    }

    return convertedValue;
  }

  private static Object convertProperty(BeanWrapper parentBean, String property,
      ConversionService conversionService, Map<String, Object> parentValue) {

    TypeDescriptor source = parentBean.getPropertyTypeDescriptor(property);
    Object propertyValue = parentBean.getPropertyValue(property);

    int dotIndex = property.indexOf('.');
    if (dotIndex > 0) {
      String baseProperty = property.substring(0, dotIndex);
      String childProperty = property.substring(dotIndex + 1);
      Map<String, Object> childValue = getChildValue(parentValue, baseProperty, childProperty);
      BeanWrapper childBean = new BeanWrapperImpl(parentBean.getPropertyValue(baseProperty));
      Object convertedProperty =
          convertProperty(childBean, childProperty, conversionService, childValue);
      childValue.put(childProperty, convertedProperty);
      return childValue;
    } else {
      String convertedPropertyValue =
          (String) conversionService.convert(propertyValue, source, TYPE_STRING);
      return convertedPropertyValue;
    }
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> getChildValue(Map<String, Object> parentValue,
      String baseProperty, String childProperty) {
    Map<String, Object> childValue = null;

    if (parentValue.containsKey(childProperty)) {
      Object value = parentValue.get(baseProperty);
      if (value instanceof Map<?, ?>) {
        childValue = (Map<String, Object>) value;
      }
    }

    if (childValue == null) {
      childValue = new HashMap<>();
      parentValue.put(baseProperty, childValue);
    }

    return childValue;
  }

}
