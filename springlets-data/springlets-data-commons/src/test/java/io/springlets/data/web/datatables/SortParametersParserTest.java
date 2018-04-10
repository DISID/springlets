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

import io.springlets.data.web.datatables.DatatablesSortHandlerMethodArgumentResolver.SortParametersParser;

import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for the class {@link DatatablesSortParametersParser}.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class SortParametersParserTest {

  private static final int MAXIMUM_COLUMN_COUNT = 10;
  private SortParametersParser parser;

  @Test
  public void checkExpectedColumnCountWithOneColumnIsExpected() {
    // Prepare
    parser = createParser(new String[] {"order[0][column]"});

    // Exercise
    int columnCount = parser.getColumnCount();

    // Verify
    assertThat(columnCount).as("Comprobando que se devuelve el número esperado de columnas")
        .isEqualTo(1);
  }

  @Test
  public void checkExpectedColumnCountWithManyColumnsIsExpected() {
    // Prepare
    parser = createParser(new String[] {"order[0][column]", "order[1][column]", "order[2][column]",
        "order[3][column]", "order[4][column]"});

    // Exercise
    int columnCount = parser.getColumnCount();

    // Verify
    assertThat(columnCount).as("Comprobando que se devuelve el número esperado de columnas")
        .isEqualTo(5);
  }

  @Test
  public void checkMaximumColumnCountIsUsed() {
    // Prepare
    parser = createParser(new String[] {"order[0][column]", "order[1][column]", "order[2][column]",
        "order[999][column]"});

    // Exercise
    int columnCount = parser.getColumnCount();

    // Verify
    assertThat(columnCount).as("Comprobando que se devuelve el número máximo "
        + "de columnas cuando este valor se sobrepasa").isEqualTo(MAXIMUM_COLUMN_COUNT);
  }

  @Test
  public void checkValidColumnPositionIsReturned() {
    // Exercise
    int columnPosition = SortParametersParser.getColumnPosition("order[3][column]");

    // Verify
    assertThat(columnPosition)
        .as("Comprobando que la posición del parámetro 'order[3][column]' es correcto")
        .isEqualTo(3);
  }

  @Test
  public void checkZeroIsReturnedForBadColumnPosition() {
    // Exercise
    int columnPosition = SortParametersParser.getColumnPosition("order[a][column]");

    // Verify
    assertThat(columnPosition)
        .as("Comprobando que para una posición de columna incorrecta se devuelve cero")
        .isEqualTo(-1);
  }

  @Test
  public void checkZeroIsReturnedForNotAColumnsParameter() {
    // Exercise
    int columnPosition = SortParametersParser.getColumnPosition("bla");

    // Verify
    assertThat(columnPosition).as("Comprobando que ante un parámetro diferente se devuelve cero")
        .isEqualTo(-1);
  }

  @Test
  public void orderedColumnIsFoundInTheOrderingPosition() {
    // Prepare: ordering first with property3 and then with property1
    parser = createParser(
        new String[] {"order[0][column]", "order[1][column]", "columns[0][data]",
            "columns[1][data]", "columns[2][data]", "columns[3][data]"},
        new String[] {"3", "1", "property0", "property1", "property2", "property3"});

    // Exercise & Verify
    assertThat(parser.getPropertyNameInOrderPosition(0))
        .as("Comprobando que el nombre de la propiedad en la posición de ordenación 0 es correcta")
        .isEqualTo("property3");
    assertThat(parser.getPropertyNameInOrderPosition(1))
        .as("Comprobando que el nombre de la propiedad en la posición de ordenación 1 es correcta")
        .isEqualTo("property1");
    assertThat(parser.getPropertyNameInOrderPosition(2))
        .as("Comprobando que no se obtiene propiedad en la posición de ordenación 2").isNull();
  }

  @Test
  public void orderDirectionDetected() {
    // Prepare
    parser = createParser(new String[] {"order[0][dir]", "order[1][dir]", "order[2][dir]"},
        new String[] {"asc", "desc", ""});

    // Exercise & Verify
    assertThat(parser.getOrderDirection(0))
        .as("Comprobando que se devuelve la dirección esperada de ordenación de "
            + "la propiedad en posición de ordenación 0")
        .isEqualTo(Direction.ASC);
    assertThat(parser.getOrderDirection(1))
        .as("Comprobando que se devuelve la dirección esperada de ordenación de "
            + "la propiedad en posición de ordenación 1")
        .isEqualTo(Direction.DESC);
    assertThat(parser.getOrderDirection(0))
        .as("Comprobando que se devuelve la dirección ASC cuando no se especifica correctamente")
        .isEqualTo(Direction.ASC);
  }

  @Test
  public void orderLoadedFromPosition() {
    // Prepare: ordering first with property3 and then with property1
    parser = createParser(
        new String[] {"order[0][column]", "order[1][column]", "order[0][dir]", "order[1][dir]",
            "columns[0][data]", "columns[1][data]", "columns[2][data]", "columns[3][data]"},
        new String[] {"3", "1", "asc", "desc", "property0", "property1", "property2", "property3"});

    // Exercise
    Order order0 = parser.getOrderInPosition(0);
    Order order1 = parser.getOrderInPosition(1);

    // Verify
    assertThat(order0.getProperty()).as("Nombre de propiedad ordenada correcta")
        .isEqualTo("property3");
    assertThat(order0.getDirection()).as("Dirección de ordenación de propiedad correcta")
        .isEqualTo(Direction.ASC);
    assertThat(order1.getProperty()).as("Nombre de propiedad ordenada correcta")
        .isEqualTo("property1");
    assertThat(order1.getDirection()).as("Dirección de ordenación de propiedad correcta")
        .isEqualTo(Direction.DESC);
  }

  @Test
  public void validSortParsed() {
    // Prepare: ordering first with property3 and then with property1
    parser = createParser(
        new String[] {"order[0][column]", "order[1][column]", "order[0][dir]", "order[1][dir]",
            "columns[0][data]", "columns[1][data]", "columns[2][data]", "columns[3][data]"},
        new String[] {"3", "1", "asc", "desc", "property0", "property1", "property2", "property3"});

    // Exercise
    Sort sort = parser.getSort();
    Order order0 = sort.getOrderFor("property3");
    Order order1 = sort.getOrderFor("property1");

    // Verify
    assertThat(order0.getProperty()).as("Nombre de propiedad ordenada correcta")
        .isEqualTo("property3");
    assertThat(order0.getDirection()).as("Dirección de ordenación de propiedad correcta")
        .isEqualTo(Direction.ASC);
    assertThat(order1.getProperty()).as("Nombre de propiedad ordenada correcta")
        .isEqualTo("property1");
    assertThat(order1.getDirection()).as("Dirección de ordenación de propiedad correcta")
        .isEqualTo(Direction.DESC);
  }

  private SortParametersParser createParser(String[] paramNames) {
    return createParser(paramNames, null);
  }

  private SortParametersParser createParser(String[] paramNames, String[] paramValues) {
    Map<String, String[]> params = createParameters(paramNames, paramValues);
    return new SortParametersParser(MAXIMUM_COLUMN_COUNT, params);
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
