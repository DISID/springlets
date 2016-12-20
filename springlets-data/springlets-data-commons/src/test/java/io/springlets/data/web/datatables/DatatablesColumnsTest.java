/**
 * Copyright (c) 2016 DISID Corporation S.L. All rights reserved.
 */
package io.springlets.data.web.datatables;

import static org.assertj.core.api.Assertions.assertThat;

import io.springlets.data.web.datatables.DatatablesColumns.Column;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link DatatablesColumns} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class DatatablesColumnsTest {

  private DatatablesColumns columns;

  @Before
  public void setUp() throws Exception {
    columns = new DatatablesColumns();
  }

  /**
   * Test method for {@link io.springlets.data.web.datatables.DatatablesColumns#getColumns()}.
   */
  @Test
  public void checkEmptyColumnsByDefault() {
    assertThat(columns.getColumns()).isEmpty();
  }

  /**
   * Test method for {@link io.springlets.data.web.datatables.DatatablesColumns#getColumns()}.
   */
  @Test
  public void checkAddingDataReturnsValidColums() {
    assertThat(columns.getColumns()).isEmpty();

    columns.setData(0, "data0");
    columns.setOrderable(0, false);
    columns.setSearch(0, "search0");

    columns.setData(1, "data1");
    columns.setSearchable(1, true);

    assertThat(columns.getColumns()).areExactly(2, new Condition<Column>() {

      @Override
      public boolean matches(Column value) {
        switch (value.getIndex()) {
          case 0:
            return value.getData().equals("data0") && !value.isOrderable()
                && value.getSearch().equals("search0");
          case 1:
            return value.getData().equals("data1") && value.isSearchable();
          default:
            return false;
        }
      }

    });
  }

}
