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
package io.springlets.data.web.select2;

import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract parent response data for data requests performed by a select2 javascript component. 
 * This class will be converted to JSON, so the property names must follow the
 * name of the properties expected by select2.
 * 
 * The select2 component expects a JSON object with two properties:
 * * _results_: the array of data, each element with an _id_ property and a _text_ property.
 * * _pagination_: an object with a _more_ property which is true if more pages of data are 
 * available. 
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @see https://select2.github.io/examples.html#data-ajax
 *
 * @param <T> Response data type
 */
public abstract class Select2DataSupport<T> {

  private final Page<T> page;

  /**
   * Create a response for select2 with data obtained from a request.
   * Uses SpEL expression templates 
   * (http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html#expressions-templating)
   * to create the select2 _id_ data property.
   * Leaves the creation of the _text_ property to the child implementations.  
   *
   * @param page the data to show
   */
  public Select2DataSupport(Page<T> page) {
    this.page = page;
    Assert.notNull(page, "The results list is required");
  }

  /**
   * Returns the data to return to a select2 component.
   * @return the data
   */
  public List<Data> getResults() {
    List<T> content = page.getContent();
    List<Data> results = new ArrayList<Data>(content.size());
    for (int i = 0; i < content.size(); i++) {
      Data data = createData(content.get(i));
      results.add(data);
    }
    return results;
  }

  /**
   * Returns the pagination information for the response.
   * @return the pagination information
   */
  public Pagination getPagination() {
    return new Pagination(!page.isLast());
  }

  /**
   * Data to be returned to the 
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  protected static class Data {
    private final String id;
    private final String text;

    public Data(String id, String text) {
      this.id = id;
      this.text = text;
    }

    public String getId() {
      return id;
    }

    public String getText() {
      return text;
    }
  }

  /**
   * Pagination information for a select2 component response data.
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  protected static class Pagination {
    private final boolean more;

    /**
     * Creates a new {@link Pagination}.
     * @param more if there is more data available in following pages
     */
    public Pagination(boolean more) {
      this.more = more;
    }

    /**
     * If there is more data available in following pages.
     * @return if there is more data available in following pages
     */
    public boolean isMore() {
      return more;
    }
  }

  protected Data createData(T element) {
    String id = getIdAsString(element);
    String text = getAsString(element);
    return new Data(id, text);
  }

  protected abstract String getAsString(T element);

  /**
   * @param element
   * @param parseIdExpression
   * @return
   */
  protected abstract String getIdAsString(T element);
}
