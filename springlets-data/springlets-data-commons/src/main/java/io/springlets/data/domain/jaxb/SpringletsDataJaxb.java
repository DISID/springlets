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
package io.springlets.data.domain.jaxb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.util.Assert;

import io.springlets.data.domain.GlobalSearch;

/**
 * Spring Data {@link SpringDataJaxb} clone to use public {@link PageableAdapter} class
 * inside SEI interfaces.
 *
 * Contains utility methods to implement JAXB {@link XmlAdapter}s as well as the
 * Springlets DTO types to be marshalled by JAXB.
 *
 * Based on the Spring Data's class {@link SpringDataJaxb}.
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public abstract class SpringletsDataJaxb {

  private SpringletsDataJaxb() {}

  /**
   * The mutable DTO for {@link GlobalSearch}s.
   */
  @XmlRootElement(name = "search-request")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class GlobalSearchDto {

    @XmlAttribute
    String searchText;

    @XmlAttribute
    boolean regexp;
  }

  /**
   * The DTO for {@link Pageable}s/{@link PageRequest}s.
   */
  @XmlRootElement(name = "page-request")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class PageRequestDto {

    @XmlAttribute
    int page;

    @XmlAttribute
    int size;

    @XmlElement(name = "order")
    List<OrderDto> orders = new ArrayList<OrderDto>();
  }

  /**
   * The DTO for {@link Page}.
   *
   * @author Oliver Gierke
   */
  @XmlRootElement(name = "page")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class PageDto {

    @XmlAnyElement
    @XmlElementWrapper(name = "content")
    List<Object> content;
  }

  /**
   * The DTO for {@link Order}.
   *
   * @author Oliver Gierke
   */
  @XmlRootElement(name = "order")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class OrderDto {

    @XmlAttribute
    String property;

    @XmlAttribute
    Direction direction;
  }

  /**
   * Marshals each of the elements of the given {@link Iterable} using the given {@link XmlAdapter}.
   *
   * @param source
   * @param adapter must not be {@literal null}.
   * @return
   * @throws Exception
   */
  public static <T, S> List<S> marshal(Iterable<T> source, XmlAdapter<S, T> adapter) {

    Assert.notNull(adapter, "[Assertion failed] - this argument is required; it must not be null");

    if (source == null) {
      return Collections.emptyList();
    }

    List<S> result = new ArrayList<S>();

    for (T element : source) {
      try {
        result.add(adapter.marshal(element));
      } catch (Exception ex) {
        throw new IllegalStateException(ex);
      }
    }

    return result;
  }
}
