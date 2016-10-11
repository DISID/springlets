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
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

/**
 * Tests unitarios de la clase {@link DatatablesPageable}.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class DatatablesPageableTest {

  @Mock
  private Pageable pageable;

  /**
   * Checks that a {@link DatatablesPageable} is created from the data of a default Pageable.
   */
  @Test
  public void validDatatablesPageableFromPageable() {
    // Prepare
    when(pageable.getPageNumber()).thenReturn(20);
    when(pageable.getPageSize()).thenReturn(10);

    // Exercise
    DatatablesPageable dtPageable = new DatatablesPageable(pageable);

    // Validate
    assertThat(dtPageable.getPageNumber()).as("Valid page number").isEqualTo(2);
    assertThat(dtPageable.getPageSize()).as("Valid page size").isEqualTo(10);
  }

}
