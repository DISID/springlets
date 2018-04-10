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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some datatables utilities and constant values.
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class Datatables {

  public static final String MEDIA_TYPE = "application/vnd.datatables+json";

  public static final String PARAMETER_DRAW = "draw";

  public static final String PARAMETER_LENGTH = "length";

  public static final String PARAMETER_START = "start";

  private static final String PARAM_COLUMN_PREFIX = "columns[";

  private static final String PARAM_COLUMN_NAME_SUFIX = "][data]";

  private static final String PARAM_ORDER_DIR_SUFIX = "][dir]";

  private static final String PARAM_ORDER_PREFIX = "order[";

  private static final String PARAM_ORDER_COLUMN_SUFIX = "][column]";

  private static final String PARAM_SEARCH_TYPE = "search";

  private static final Pattern COLUMN_INDEX_PATTERN =
      Pattern.compile("columns\\[([0-9]{1,3})?\\]");

  private static final Pattern COLUMN_TYPE_PATTERN =
      Pattern.compile("columns\\[([0-9]{1,3})?\\]\\[(data|name|searchable|orderable|search|regex)?\\]");


  /**
   * Returns the name of the parameter which provides the index of the column
   * in the list of columns of the datatables.
   * @param index position in the list of columns to order by
   * @return the name of the property
   */
  public static String orderColumnIndexParameter(int index) {
    return PARAM_ORDER_PREFIX + index + PARAM_ORDER_COLUMN_SUFIX;
  }

  /**
   * Returns the parameter name with the order direction
   * in the given ordering position.
   * @param index position in the list of order by columns
   * @return the name of the property
   */
  public static String orderDirectionParameter(int index) {
    return PARAM_ORDER_PREFIX + index + PARAM_ORDER_DIR_SUFIX;
  }

  /**
   * Returns the parameter name with the name of the column
   * in the provided position in the datatables.
   * @param columnPosition position in the list of datatables columns
   * @return the name of the property
   */
  public static String columnNameParameter(String columnPosition) {
    return PARAM_COLUMN_PREFIX + columnPosition + PARAM_COLUMN_NAME_SUFIX;
  }

  /**
   * Returns if the given parameter is a datatables column one.
   * @param parameter to check
   * @return if it is a column parameter
   */
  public static boolean isColumn(String parameter) {
    return parameter != null && parameter.startsWith(PARAM_COLUMN_PREFIX);
  }

  /**
   * Returns the index of the column referenced in the parameter
   * @param parameter the column parameter
   * @return the column index
   */
  public static int columnIndex(String parameter) {
    if (isColumn(parameter)) {
      Matcher matcher = COLUMN_INDEX_PATTERN.matcher(parameter);
      while (matcher.find()) {
        try {
          return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ex) {
          // Ignore number, it has a format error or its is not a number
        }
      }
    }
    return -1;
  }

  /**
   * Returns the type of column parameter or null if it is not a valid column parameter
   * @param parameter the column parameter
   * @return the column parameter type
   */
  public static ColumnParamType columnParameterType(String parameter) {
    if (isColumn(parameter)) {
      Matcher matcher = COLUMN_TYPE_PATTERN.matcher(parameter);
      while (matcher.find()) {
        String type = matcher.group(2);
        if (PARAM_SEARCH_TYPE.equals(type) && parameter.endsWith("[search][regex]")) {
          return ColumnParamType.REGEX;
        }
        try {
          return ColumnParamType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
          // Ignore type, it has a format error or its is not a valid column type
        }
      }
    }
    return null;
  }

  /**
   * Enumeration of datatables column types
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  public enum ColumnParamType {
    DATA, NAME, SEARCHABLE, ORDERABLE, SEARCH, REGEX
  }
}
