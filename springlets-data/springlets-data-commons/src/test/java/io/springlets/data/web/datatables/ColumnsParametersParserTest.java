/**
 * Copyright (c) 2016 DISID Corporation S.L. All rights reserved.
 */
package io.springlets.data.web.datatables;

import static org.assertj.core.api.Assertions.assertThat;

import io.springlets.data.web.datatables.DatatablesColumns.Column;
import io.springlets.data.web.datatables.DatatablesColumnsHandlerMethodArgumentResolver.ColumnsParametersParser;

import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for the {@link ColumnsParametersParser} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class ColumnsParametersParserTest {

  private static final int MAXIMUM_COLUMN_COUNT = 3;
  private ColumnsParametersParser parser;

  @Test
  public void checkColumnsParametersAreParsed() {
    // Prepare
    parser = createParser(
        new String[] {"columns[0][data]", "columns[0][name]", "columns[0][orderable]",
            "columns[0][searchable]", "columns[0][search][value]", "columns[0][search][regex]",
            "columns[1][data]", "columns[1][name]", "columns[1][orderable]",
            "columns[1][searchable]", "columns[1][search][value]", "columns[1][search][regex]"},
        new String[] {"data0", "name0", "true", "true", "search0", "true", "data1", "name1",
            "false", "false", "search1", "false"});

    // Exercise
    DatatablesColumns columns = parser.getColumns();

    // Validate
    assertThat(columns.getColumns()).areExactly(2, new Condition<Column>() {

      @Override
      public boolean matches(Column value) {
        switch (value.getIndex()) {
          case 0:
            return value.getData().equals("data0") && value.getName().equals("name0")
                && value.isOrderable() && value.isSearchable()
                && value.getSearch().equals("search0") && value.isSearchRegex();
          case 1:
            return value.getData().equals("data1") && value.getName().equals("name1")
                && !value.isOrderable() && !value.isSearchable()
                && value.getSearch().equals("search1") && !value.isSearchRegex();
          default:
            return false;
        }
      }

    });
  }

  @Test
  public void checkMaxColumnsAreIgnored() {
    // Prepare
    parser = createParser(new String[] {"columns[0][data]", "columns[100][data]"},
        new String[] {"data0", "data50"});

    // Exercise
    DatatablesColumns columns = parser.getColumns();

    // Validate
    assertThat(columns.getColumns()).areExactly(1, new Condition<Column>() {

      @Override
      public boolean matches(Column value) {
        switch (value.getIndex()) {
          case 0:
            return true;
          default:
            return false;
        }
      }
    });
  }

  private ColumnsParametersParser createParser(String[] paramNames, String[] paramValues) {
    Map<String, String[]> params = createParameters(paramNames, paramValues);
    return new ColumnsParametersParser(MAXIMUM_COLUMN_COUNT, params);
  }

  private Map<String, String[]> createParameters(String[] paramNames, String[] paramValues) {
    HashMap<String, String[]> map = new HashMap<String, String[]>(paramNames.length);

    for (int i = 0; i < paramNames.length; i++) {
      String[] value = paramValues == null ? null : new String[] {paramValues[i]};
      map.put(paramNames[i], value);
    }

    return map;
  }

}
