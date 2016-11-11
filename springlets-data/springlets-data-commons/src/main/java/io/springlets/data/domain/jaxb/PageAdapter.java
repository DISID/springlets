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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.Page;

import io.springlets.data.domain.jaxb.SpringletsDataJaxb.PageDto;

/**
 * JAXB {@link XmlAdapter} to convert Spring Data `Page` instance  
 * into a {@link PageDto} and vice versa.
 * 
 * Use it in JAX-WS SEI interfaces.
 * 
 * Based on the Spring Data's `data.domain.jaxb` classes.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class PageAdapter extends XmlAdapter<PageDto, Page<Object>> {

  @Override
  public PageDto marshal(Page<Object> source) {

    if (source == null) {
      return null;
    }

    PageDto dto = new PageDto();
    dto.content = source.getContent();

    return dto;
  }

  @Override
  public Page<Object> unmarshal(PageDto v) {
    return null;
  }

}
