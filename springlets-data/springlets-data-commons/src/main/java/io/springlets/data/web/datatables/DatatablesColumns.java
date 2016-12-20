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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Information about Datatables columns configuration: name, if it is searchable, ...
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @see https://datatables.net/manual/server-side
 */
public class DatatablesColumns {

  private final Map<Integer, InternalColumn> columns;

  /**
   * Creates a new instance.
   */
  public DatatablesColumns() {
    columns = new TreeMap<>();
  }

  /**
   * Returns the list of column configurations. 
   * @return the list of columns
   */
  public Iterable<Column> getColumns() {
    List<Column> values = new ArrayList<>(columns.size());
    values.addAll(columns.values());
    return values;
  }

  /**
   * Sets the data attribute of a column.
   * @param index the index of the column
   * @param data the data value
   */
  public void setData(int index, String data) {
    getColumn(index).setData(data);
  }

  /**
   * Sets the name attribute of a column
   * @param index the index of the column
   * @param name the name value
   */
  public void setName(int index, String name) {
    getColumn(index).setName(name);
  }

  /**
   * Sets the 'searchable' attribute of a column
   * @param index the index of the column
   * @param searchable the searchable value
   */
  public void setSearchable(int index, boolean searchable) {
    getColumn(index).setSearchable(searchable);
  }

  /**
   * Sets the 'orderable' attribute of a column
   * @param index the index of the column
   * @param orderable the orderable value
   */
  public void setOrderable(int index, boolean orderable) {
    getColumn(index).setOrderable(orderable);
  }

  /**
   * Sets the search with regex attribute of a column
   * @param index the index of the column
   * @param regexp the search using regex value
   */
  public void setSearchRegexp(int index, boolean regexp) {
    getColumn(index).setSearchRegex(regexp);
  }

  /**
   * Sets the name attribute of a column
   * @param index the index of the column
   * @param search
   */
  public void setSearch(int index, String search) {
    getColumn(index).setSearch(search);
  }

  @Override
  public String toString() {
    return "DatatablesColumns [columns=" + columns + "]";
  }

  private InternalColumn getColumn(int index) {
    InternalColumn column = columns.get(index);
    if (column == null) {
      column = new InternalColumn(index);
      columns.put(index, column);
    }
    return column;
  }

  /**
   * Configuration of a column in datatables.
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  public static interface Column {
    /**
     * The zero-based position of the column in the table.
     * @return the column index
     */
    int getIndex();

    /**
     * Returns the property data name of the column. 
     * @return the property data name
     */
    String getData();

    /**
     * Returns the name of the column.
     * @return the column name
     */
    String getName();

    /**
     * Returns if the column is searchable.
     * @return if the column is searchable
     */
    boolean isSearchable();

    /**
     * Returns if the column is orderable.
     * @return if the column is orderable
     */
    boolean isOrderable();

    /**
     * Returns if the search by column is regexp based.
     * @return if the search by column is regexp based 
     */
    boolean isSearchRegex();

    /**
     * Returns the search by column text.
     * @return the text to search in the column text
     */
    String getSearch();
  }

  /**
   * Internal implementation of {@link Column}.
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  private static class InternalColumn implements Column {

    private final int index;
    private String data;
    private String name;
    private boolean searchable;
    private boolean orderable;
    private boolean searchRegex;
    private String search;

    public InternalColumn(int index) {
      this.index = index;
    }

    public int getIndex() {
      return index;
    }

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean isSearchable() {
      return searchable;
    }

    public void setSearchable(boolean searchable) {
      this.searchable = searchable;
    }

    public boolean isOrderable() {
      return orderable;
    }

    public void setOrderable(boolean orderable) {
      this.orderable = orderable;
    }

    public boolean isSearchRegex() {
      return searchRegex;
    }

    public void setSearchRegex(boolean searchRegex) {
      this.searchRegex = searchRegex;
    }

    public String getSearch() {
      return search;
    }

    public void setSearch(String search) {
      this.search = search;
    }

    @Override
    public String toString() {
      return "InternalColumn [index=" + index + ", data=" + data + ", name=" + name
          + ", searchable=" + searchable + ", orderable=" + orderable + ", searchRegex="
          + searchRegex + ", search=" + search + "]";
    }

  }

}
