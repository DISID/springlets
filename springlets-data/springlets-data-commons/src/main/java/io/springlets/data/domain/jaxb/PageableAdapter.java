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

import java.util.Collections;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SortAdapter;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.data.domain.jaxb.SpringDataJaxb.SortDto;

import io.springlets.data.domain.jaxb.SpringletsDataJaxb.OrderDto;
import io.springlets.data.domain.jaxb.SpringletsDataJaxb.PageRequestDto;

/**
 * JAXB {@link XmlAdapter} to convert Spring Data `Pageable` instance  
 * into a {@link PageRequestDto} and vice versa.
 * 
 * Use it in JAX-WS SEI interfaces.
 * 
 * Based on the Spring Data's `data.domain.jaxb` classes.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class PageableAdapter extends XmlAdapter<PageRequestDto, Pageable> {

  @Override
  public PageRequestDto marshal(Pageable request) {

    PageRequestDto dto = new PageRequestDto();
    dto.orders = request.getSort() == null ? Collections.<OrderDto>emptyList()
        : SpringDataJaxb.marshal(request.getSort(), OrderAdapter.INSTANCE);
    dto.page = request.getPageNumber();
    dto.size = request.getPageSize();

    return dto;
  }

  @Override
  public Pageable unmarshal(PageRequestDto v) {

    if (v.orders.isEmpty()) {
      return new PageRequest(v.page, v.size);
    }

    SortDto sortDto = new SortDto();
    Sort sort = SortAdapter.INSTANCE.unmarshal(sortDto);

    return new PageRequest(v.page, v.size, sort);
  }
}
