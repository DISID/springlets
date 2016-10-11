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

}
