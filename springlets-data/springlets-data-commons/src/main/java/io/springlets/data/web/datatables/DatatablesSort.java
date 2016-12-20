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

import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Extends {@link Sort} to be used with the datatables sorting parameters.
 * This way you can use this information to create Spring Data queries.
 * See {@link https://datatables.net/manual/server-side}
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class DatatablesSort extends Sort {

  private static final long serialVersionUID = 5938901146261470479L;

  /**
   * Creates an ordering by columns parameter for Datatables.
   * @param orders the list of column orderings to apply
   */
  public DatatablesSort(List<Order> orders) {
    super(orders);
  }

}
