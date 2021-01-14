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
package io.springlets.data.jpa.repository.support;

import static org.mockito.Mockito.when;

import com.querydsl.core.types.Path;
import com.querydsl.jpa.JPQLQuery;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Unit tests for the {@link QueryDslRepositorySupportExt} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class QueryDslRepositorySupportExtTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private Pageable pageable;

  @Mock
  private Sort sort;

  @Mock
  private Iterator<Sort.Order> iterator;

  private QueryDslRepositorySupportExt<Object> support;

  /**
   * Test setup, creates a new {@link QueryDslRepositorySupportExt} to use in the test methods.
   */
  @Before
  public void setUp() throws Exception {
    support = new QueryDslRepositorySupportExt<Object>(Object.class) {
      @Override
      protected JPQLQuery<Object> applyPagination(Pageable pageable, JPQLQuery<Object> query) {
        return query;
      }
    };
  }

  /**
   * Test method for {@link io.springlets.data.jpa.repository.support.QueryDslRepositorySupportExt#applyPagination(org.springframework.data.domain.Pageable, com.querydsl.jpa.JPQLQuery, java.util.Map)}.
   */
  @Test
  public void applyPaginationWithPageableHavingAPropertyNotIncludedInTheAttributeMappingShouldNotFail() {
    // Prepare
    Sort.Order order = new Sort.Order("test");

    Map<String, Path<?>[]> attributeMapping = new HashMap<>();
    when(pageable.getSort()).thenReturn(sort);
    when(pageable.getPageSize()).thenReturn(1);
    when(sort.iterator()).thenReturn(iterator);
    when(iterator.hasNext()).thenReturn(true).thenReturn(false);
    when(iterator.next()).thenReturn(order).thenThrow(new NoSuchElementException());

    // Exercise & verify
    support.applyPagination(pageable, null, attributeMapping);
  }

  /**
   * Test method for {@link io.springlets.data.jpa.repository.support.QueryDslRepositorySupportExt#applyPagination(org.springframework.data.domain.Pageable, com.querydsl.jpa.JPQLQuery, java.util.Map)}.
   */
  @Test
  public void applyPaginationWithPageableHavingAnEmptySortShouldNotFail() {
    // Prepare
    Map<String, Path<?>[]> attributeMapping = new HashMap<>();
    when(pageable.getSort()).thenReturn(sort);
    when(sort.iterator()).thenReturn(iterator);
    when(iterator.hasNext()).thenReturn(false);

    // Exercise & verify
    support.applyPagination(pageable, null, attributeMapping);
  }

  /**
   * Test method for {@link io.springlets.data.jpa.repository.support.QueryDslRepositorySupportExt#applyPagination(org.springframework.data.domain.Pageable, com.querydsl.jpa.JPQLQuery, java.util.Map)}.
   */
  @Test
  public void applyPaginationWithNullPageableShouldNotFail() {
    // Prepare
    Map<String, Path<?>[]> attributeMapping = new HashMap<>();
    /*
    when(pageable.getSort()).thenReturn(sort);
    when(sort.iterator()).thenReturn(iterator);
    when(iterator.hasNext()).thenReturn(false);
     */

    // Exercise & verify
    support.applyPagination(null, null, attributeMapping);
  }
}
