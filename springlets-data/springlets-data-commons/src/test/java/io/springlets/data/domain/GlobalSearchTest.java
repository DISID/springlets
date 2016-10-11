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
package io.springlets.data.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pruebas unitarias de la clase {@link GlobalSearch}.
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class GlobalSearchTest {

  /**
   * Test method for {@link es.msssi.fwmvnje6.northwindjee.model.GlobalSearch#matches(java.lang.String)}.
   */
  @Test
  public void matchingSearchTextWorks() {
    GlobalSearch search = new GlobalSearch("test");

    assertTrue(search.matches("This is a test string"));
    assertTrue(search.matches("test string"));
    assertTrue(search.matches("This is a test"));
    assertTrue(search.matches("test"));
    assertFalse(search.matches("Not"));
  }

  /**
   * Test method for {@link es.msssi.fwmvnje6.northwindjee.model.GlobalSearch#matches(java.lang.String)}.
   */
  @Test
  public void matchingRegexpWorks() {
    GlobalSearch search = new GlobalSearch(".*test.?", true);

    assertTrue(search.matches("This is a test"));
    assertTrue(search.matches("testX"));
    assertTrue(search.matches("This is a testZ"));
    assertTrue(search.matches("test"));
    assertFalse(search.matches("testABC"));
  }

}
