/**
 * Copyright (c) 2016 DISID Corporation S.L. All rights reserved.
 */
package io.springlets.data.web.datatables;

import io.springlets.data.web.datatables.Datatables.ColumnParamType;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Resolves controller method parameters of type {@link DatatablesColumns}.
 * 
 * It has a limit in the number of columns to process to avoid tampering with the parameters.
 * Any column whose index is greater than the limit will be ignored. 
 *
 * See {@link https://datatables.net/manual/server-side}
 *  
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class DatatablesColumnsHandlerMethodArgumentResolver
    implements HandlerMethodArgumentResolver {

  private static final int MAX_COLUMNS = 50;

  private final int maxColumns;

  /**
   * Creates a new instance with the default maximum number of columns (50). 
   */
  public DatatablesColumnsHandlerMethodArgumentResolver() {
    this(MAX_COLUMNS);
  }

  /**
   * Creates a new instance with a maximum number of columns to process. 
   * @param maxColumns the maximum number of columns to process. 
   */
  public DatatablesColumnsHandlerMethodArgumentResolver(int maxColumns) {
    this.maxColumns = maxColumns;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return DatatablesColumns.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {

    DatatablesColumns columns =
        new ColumnsParametersParser(maxColumns, request.getParameterMap()).getColumns();

    return columns;
  }

  /**
   * Parses datatables columns configuration parameters. 
   * 
   * The expected parameters are the ones whose name starts with "columns[index]".
   * 
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  static class ColumnsParametersParser {

    private int maxColumns;
    private Map<String, String[]> parameters;

    /**
     * Creates a new parser instance.
     * @param maxColumns the maximum number of columns to process
     * @param parameterMap the parameters to parse
     */
    public ColumnsParametersParser(int maxColumns, Map<String, String[]> parameterMap) {
      this.maxColumns = maxColumns;
      this.parameters = parameterMap;
    }

    /**
     * Parses the parameters and creates a new {@link DatatablesColumns} instance with the 
     * information provided.
     * @return a {@link DatatablesColumns} with the parameters values
     */
    public DatatablesColumns getColumns() {
      DatatablesColumns columns = new DatatablesColumns();

      Set<Entry<String, String[]>> entrySet = parameters.entrySet();

      for (Entry<String, String[]> entry : entrySet) {
        String parameter = entry.getKey();
        String value = getParameter(parameter);
        if (!StringUtils.isEmpty(value) && Datatables.isColumn(parameter)) {
          addColumnValue(columns, parameter, value);
        }
      }

      return columns;
    }

    private void addColumnValue(DatatablesColumns columns, String parameter, String value) {
      ColumnParamType type = Datatables.columnParameterType(parameter);
      int index = Datatables.columnIndex(parameter);
      if (type != null && isValidIndex(index)) {
        switch (type) {
          case DATA:
            columns.setData(index, value);
            break;
          case NAME:
            columns.setName(index, value);
            break;
          case ORDERABLE:
            columns.setOrderable(index, Boolean.valueOf(value));
            break;
          case REGEX:
            columns.setSearchRegexp(index, Boolean.valueOf(value));
            break;
          case SEARCH:
            columns.setSearch(index, value);
            break;
          case SEARCHABLE:
            columns.setSearchable(index, Boolean.valueOf(value));
            break;
          default:
            break;
        }
      }
    }

    private boolean isValidIndex(int index) {
      return index > -1 && index < maxColumns;
    }

    private String getParameter(String name) {
      String[] values = parameters.get(name);
      return (values == null || values.length == 0) ? null : values[0];
    }
  }
}
