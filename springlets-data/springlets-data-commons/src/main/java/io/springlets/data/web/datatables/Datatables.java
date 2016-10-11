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

/**
 * Some datatables utility constant values.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class Datatables {

  public static final String MEDIA_TYPE = "application/vnd.datatables+json";

  public static final String PARAMETER_DRAW = "draw";

  public static final String PARAMETER_LENGTH = "length";

  public static final String PARAMETER_START = "start";

  /**
   * Returns the name of the parameter which provides the index of the column
   * in the list of columns of the datatables. 
   * @param index position in the list of columns to order by
   * @return the name of the property
   */
  public static String orderColumnIndexParameter(int index) {
    return "order[" + index + "][column]";
  }

  /**
   * Returns the parameter name with the order direction
   * in the given ordering position. 
   * @param index position in the list of order by columns
   * @return the name of the property
   */
  public static String orderDirectionParameter(int index) {
    return "order[" + index + "][dir]";
  }

  /**
   * Returns the parameter name with the name of the column
   * in the provided position in the datatables. 
   * @param columnPosition position in the list of datatables columns
   * @return the name of the property
   */
  public static String columnNameParameter(String columnPosition) {
    return "columns[" + columnPosition + "][data]";
  }

}
