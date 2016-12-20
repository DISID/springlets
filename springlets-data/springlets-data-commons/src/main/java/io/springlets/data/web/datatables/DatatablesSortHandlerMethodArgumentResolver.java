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
package io.springlets.data.web.datatables;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves controller method parameters of type {@link DatatablesSort}.
 * 
 * It extends the Spring Data's {@link SortHandlerMethodArgumentResolver}
 * to set the parameter names used by the datatables components, 
 * (*order[i]* and *columns[i]*) and creates a {@link DatatablesSort} instance.
 *
 * If those parameters are not available, it delegates in the default
 * Spring Data resolver implementation.
 * See {@link https://datatables.net/manual/server-side}
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class DatatablesSortHandlerMethodArgumentResolver extends SortHandlerMethodArgumentResolver {

  private static final int MAX_ORDERED_COLUMNS = 5;

  private final int maxOrderedColumns;

  /**
   * Creates a new instance with default maximum number of columns to order by.
   */
  public DatatablesSortHandlerMethodArgumentResolver() {
    this(MAX_ORDERED_COLUMNS);
  }

  /**
   * Creates a new instance.
   * @param maxOrderedColumns maximum number of columns to order by
   */
  public DatatablesSortHandlerMethodArgumentResolver(int maxOrderedColumns) {
    this.maxOrderedColumns = maxOrderedColumns;
  }

  @Override
  public DatatablesSort resolveArgument(MethodParameter parameter,
      ModelAndViewContainer mavContainer, NativeWebRequest request,
      WebDataBinderFactory binderFactory) {

    DatatablesSort sort =
        new SortParametersParser(maxOrderedColumns, request.getParameterMap()).getSort();

    return sort;
  }

  /**
   * Parses datatables sorting parameters to create a {@link DatatablesSort}
   * with the information available in those parameters.
   * The expected parameter format is that they start with *order[i]*
   * and *columns[i]*.
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  static class SortParametersParser {

    private static final Pattern PATTERN = Pattern.compile("order\\[([0-9]*)?\\]\\[column\\]");

    private final int maxColumnCount;
    private Map<String, String[]> parameters;

    /**
     * Crea una instancia con un número máximo de columnas soportadas para
     * ordenación, para evitar errores o un uso malicioso de los parámetros
     * que provoque un mal comportamiento de la aplicación o un uso excesivo
     * de recursos.
     *
     * @param maxOrderedColumns número máximo de columnas de ordenación soportadas
     * @param map
     */
    public SortParametersParser(int maxOrderedColumns, Map<String, String[]> parameters) {
      this.maxColumnCount = maxOrderedColumns;
      this.parameters = parameters;
    }

    /**
     * Returns the number of columns to order by from a list of parameters 
     * names.
     * 
     * From the list, only will be taken into account parameters that
     * begin with *columns[i]*, being _i_ the position of the column
     * in the datatables. Then a count of columns is not performed, 
     * but it takes into account datatables returns a list, so the 
     * maximum value will be obtained.
     * If the value is bigger than the maximum number of values supported,
     * the second value will be returned instead to avoid parameter abuse.
     *
     * @return the number of columns to order by
     */
    int getColumnCount() {

      if (parameters == null || parameters.isEmpty()) {
        return 0;
      }

      int columnCount = -1;
      for (String paramName : parameters.keySet()) {
        int columnNumber = getColumnPosition(paramName);
        if (columnNumber > columnCount) {
          columnCount = columnNumber;
        }
      }

      // The column array is zero based
      columnCount++;

      // Just in case there is an error or someone is tampering with the parameters
      return columnCount > maxColumnCount ? maxColumnCount : columnCount;
    }

    /**
     * Return the position given in a column order parameter from datatable.
     * The parameter must begin with *order[i]*, and then return the numeric
     * value of devolviendo el valor numérico de *i*.
     *
     * @param paramName the name of the column to analyze
     * @return the position given in the column order, or -1 if it is not
     * a valid number or the format is not the expected one 
     */

    static int getColumnPosition(String paramName) {
      Matcher matcher = PATTERN.matcher(paramName);
      while (matcher.find()) {
        try {
          return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ex) {
          // Ignore number, it has a format error or its is not a number
        }
      }
      return -1;
    }
    /**
     * Returns the name of the property whose datatables column is available
     * into the list of the columns to order by, in the given position.
     * 
     * Returns null if there is no ordering by the given column
     *
     * @param pos the column position into the ordering list
     * @return the name of the property
     */
    String getPropertyNameInOrderPosition(int pos) {
      String columnPosition = getParameter(Datatables.orderColumnIndexParameter(pos));

      if (columnPosition == null) {
        return null;
      }

      return getParameter(Datatables.columnNameParameter(columnPosition));
    }

    /**
     * Returns the ordering {@link Direction} to apply to the property
     * in the given ordering position.
     * @param pos the ordering position
     * @return the ordering {@link Direction}
     */
    Direction getOrderDirection(int pos) {
      String direction = getParameter(Datatables.orderDirectionParameter(pos));
      if ("desc".equals(direction)) {
        return Direction.DESC;
      }
      return Direction.ASC;
    }

    /**
     * Returns an {@link Order} from the datatables ordering parameters
     * in the given position.
     * @param pos the ordering position to get
     * @return the {@link Order} definition
     */
    Order getOrderInPosition(int pos) {
      String propertyName = getPropertyNameInOrderPosition(pos);
      if (propertyName == null) {
        return null;
      }
      Direction direction = getOrderDirection(pos);
      return new Order(direction, propertyName);
    }

    /**
     * Returns a full ordering criteria from the datatables parameters.
     * @return the ordering criteria
     */
    public DatatablesSort getSort() {
      int columnCount = getColumnCount();

      if (columnCount <= 0) {
        return null;
      }

      List<Order> orderList = new ArrayList<Order>(columnCount);

      for (int i = 0; i < columnCount; i++) {
        Order order = getOrderInPosition(i);
        if (order != null) {
          orderList.add(order);
        }
      }

      if (orderList.size() == 0) {
        return null;
      }

      return new DatatablesSort(orderList);
    }

    private String getParameter(String name) {
      String[] values = parameters.get(name);
      return (values == null || values.length == 0) ? null : values[0];
    }
  }

}
