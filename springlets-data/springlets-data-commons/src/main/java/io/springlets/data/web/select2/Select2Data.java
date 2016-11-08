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
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Response data for data requests performed by a select2 javascript component. 
 * This class will be converted to JSON, so the property names must follow the
 * name of the properties expected by select2.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @see https://select2.github.io/examples.html#data-ajax
 *
 * @param <T> Response data type
 */
public class Select2Data<T> {

  private final List<Data> results;
  private final Pagination pagination;

  /**
   * Create a response for select2 with data obtained from a request.
   * Uses SpEL expression templates 
   * (http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html#expressions-templating)
   * to create the select2 data properties
   * ('id' and 'text') from the attributes of the data bean to return  
   *
   * @param page the data to show
   * @param idExpression the SpEl expression for the id field
   * @param textExpression the SpEl expression for the text field
   */
  public Select2Data(Page<T> page, String idExpression, String textExpression) {
    Assert.notNull(page, "The results list is required");

    List<T> content = page.getContent();
    this.results = new ArrayList<Data>(content.size());

    TemplateParserContext templateParserContext = new TemplateParserContext();
    ExpressionParser parser = new SpelExpressionParser();
    Expression parseIdExpression = parser.parseExpression(idExpression, templateParserContext);
    Expression parseTextExpression = parser.parseExpression(textExpression, templateParserContext);

    for (int i = 0; i < content.size(); i++) {
      Data data = createData(content.get(i), parseIdExpression, parseTextExpression);
      results.add(data);
    }

    this.pagination = new Pagination(!page.isLast());
  }

  protected Data createData(T element, Expression parseIdExpression,
      Expression parseTextExpression) {
    String id = parseIdExpression.getValue(element, String.class);
    String text = parseTextExpression.getValue(element, String.class);
    return createData(element, id, text);
  }

  protected Data createData(T element, String id, String text) {
    return new Data(id, text);
  }

  /**
   * Returns the data to return to a select2 component.
   * @return the data
   */
  public List<Data> getResults() {
    return results;
  }

  /**
   * Returns the pagination information for the response.
   * @return the pagination information
   */
  public Pagination getPagination() {
    return pagination;
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
   * Pagination information for the response data for a select2 component.
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
}
