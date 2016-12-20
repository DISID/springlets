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

import static org.assertj.core.api.Assertions.assertThat;

import io.springlets.data.web.datatables.Datatables.ColumnParamType;

import org.junit.Test;

/**
 * Unit tests for the {@link Datatables} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class DatatablesTest {

  /**
   * Test method for {@link io.springlets.data.web.datatables.Datatables#orderColumnIndexParameter(int)}.
   */
  @Test
  public void testOrderColumnIndexParameter() {
    assertThat(Datatables.orderColumnIndexParameter(0)).as("Valid order column index parameter")
        .isEqualTo("order[0][column]");
  }

  /**
   * Test method for {@link io.springlets.data.web.datatables.Datatables#orderDirectionParameter(int)}.
   */
  @Test
  public void testOrderDirectionParameter() {
    assertThat(Datatables.orderDirectionParameter(0)).as("Valid order direction parameter")
        .isEqualTo("order[0][dir]");
  }

  /**
   * Test method for {@link io.springlets.data.web.datatables.Datatables#columnNameParameter(java.lang.String)}.
   */
  @Test
  public void testColumnNameParameter() {
    assertThat(Datatables.columnNameParameter("0")).as("Valid column name parameter")
        .isEqualTo("columns[0][data]");
  }

  /**
   * Test method for {@link io.springlets.data.web.datatables.Datatables#isColumn(String)}.
   */
  @Test
  public void testIsColumn() {
    assertThat(Datatables.isColumn("columns[1][data]")).as("Is a column parameter").isEqualTo(true);
    assertThat(Datatables.isColumn("columns[1][name]")).as("Is a column parameter").isEqualTo(true);
    assertThat(Datatables.isColumn("other[1][data]")).as("Is not a column parameter")
        .isEqualTo(false);
  }

  /**
   * Test method for {@link io.springlets.data.web.datatables.Datatables#columnIndex(String)}.
   */
  @Test
  public void testColumnIndex() {
    assertThat(Datatables.columnIndex("columns[1][data]")).as("Column parameter with index")
        .isEqualTo(1);
    assertThat(Datatables.columnIndex("columns[3][name]")).as("Column parameter with index")
        .isEqualTo(3);
    assertThat(Datatables.columnIndex("other[1][data]")).as("Not a column parameter").isEqualTo(-1);
    assertThat(Datatables.columnIndex("columns[badnumber][name]"))
        .as("Column parameter with invalid index").isEqualTo(-1);
  }

  /**
   * Test method for {@link io.springlets.data.web.datatables.Datatables#columnParameterType(String)}.
   */
  @Test
  public void testColumnParameterType() {
    assertThat(Datatables.columnParameterType("columns[1][data]")).as("Column data parameter")
        .isEqualTo(ColumnParamType.DATA);
    assertThat(Datatables.columnParameterType("columns[1][name]")).as("Column name parameter")
        .isEqualTo(ColumnParamType.NAME);
    assertThat(Datatables.columnParameterType("columns[1][orderable]"))
        .as("Column is orderable parameter").isEqualTo(ColumnParamType.ORDERABLE);
    assertThat(Datatables.columnParameterType("columns[1][searchable]"))
        .as("Column is searchable parameter").isEqualTo(ColumnParamType.SEARCHABLE);
    assertThat(Datatables.columnParameterType("columns[1][search][value]"))
        .as("Column search value parameter").isEqualTo(ColumnParamType.SEARCH);
    assertThat(Datatables.columnParameterType("columns[1][search][regex]"))
        .as("Column search is regex parameter").isEqualTo(ColumnParamType.REGEX);
    assertThat(Datatables.columnParameterType("columns[1][other]")).as("Column data parameter")
        .isNull();
  }

}
