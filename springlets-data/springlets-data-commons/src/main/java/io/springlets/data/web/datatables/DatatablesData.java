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

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Response data for data requests performed by adatatables component. 
 * This class will be converted to JSON, so the property names must follow the
 * name of the properties expected by datatables.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @see https://datatables.net/manual/server-side
 *
 * @param <T> Response data type
 */
public class DatatablesData<T> {

  private List<T> data;
  private Long recordsTotal;
  private Long recordsFiltered;
  private Integer draw;
  private String error;

  /**
   * Create a response for datatables with data obtained from a previous request.
   *
   * @param data the data to show
   * @param recordsTotal the total number of available data
   * @param recordsFiltered the number of data after filtering
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   */
  public DatatablesData(List<T> data, Long recordsTotal, Long recordsFiltered, Integer draw) {
    this(data, recordsTotal, recordsFiltered, draw, null);
  }

  /**
   * Create an error response to datatables.
   * 
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   * @param error the error produced to inform the user
   */
  public DatatablesData(Integer draw, String error) {
    this(null, null, null, draw, error);
  }

  /**
   * Create an answer for datatables with data obtained from a previous request.
   *
   * @param data the data to show
   * @param recordsTotal the total number of available data
   * @param recordsFiltered the number of data after filtering
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   * @param error (optional) the produced error to inform the user
   */
  public DatatablesData(List<T> data, Long recordsTotal, Long recordsFiltered, Integer draw,
      String error) {
    this.data = data;
    this.recordsTotal = recordsTotal;
    this.recordsFiltered = recordsFiltered;
    this.draw = draw;
    this.error = error;
  }

  /**
   * Create an answer for datatables with data obtained from a previous request.
   *
   * @param dataPage the page of the obtained data
   * @param recordsTotal the total number of data with no filter and no pagination
   * @param draw counts datatables requests. It must be sent by datatables value 
   * in the data request.
   */
  public DatatablesData(Page<T> dataPage, Long recordsTotal, Integer draw) {
    this(dataPage.getContent(), recordsTotal, dataPage.getTotalElements(), draw);
  }

  public List<T> getData() {
    return data;
  }

  public Long getRecordsTotal() {
    return recordsTotal;
  }

  public Long getRecordsFiltered() {
    return recordsFiltered;
  }

  public Integer getDraw() {
    return draw;
  }

  public String getError() {
    return error;
  }
}
