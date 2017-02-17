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

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

/**
 * Response data for data requests performed by a select2 javascript component. 
 * This class will be converted to JSON, so the property names must follow the
 * name of the properties expected by select2.
 * 
 * This class uses the {@link ConversionService} to convert the data to String.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @see https://select2.github.io/examples.html#data-ajax
 *
 * @param <T> Response data type
 */
public class Select2DataWithConversion<T> extends Select2DataSupport<T> {

  private final ConversionService conversionService;
  private final Expression parseIdExpression;
  private boolean includeEntireElement;

  /**
   * Create a response for select2 with data obtained from a request.
   *
   * @param page the data to show
   * @param idExpression the SpEl expression for the id field
   * @param conversionService to convert the data to String
   */
  public Select2DataWithConversion(Page<T> page, String idExpression,
      ConversionService conversionService) {
    super(page);
    Assert.notNull(page, "A ConversionService is required");

    this.conversionService = conversionService;
    TemplateParserContext templateParserContext = new TemplateParserContext();
    ExpressionParser parser = new SpelExpressionParser();
    parseIdExpression = parser.parseExpression(idExpression, templateParserContext);
    // By default, the entire element will not be included in the response
    this.includeEntireElement = false;
  }
  
  /**
   * Create a response for select2 with data obtained from a request and indicates
   * if the entire element should be included in the response.
   *
   * @since 1.2.0
   *
   * @param page the data to show
   * @param idExpression the SpEl expression for the id field
   * @param conversionService to convert the data to String
   * @param includeEntireElement boolean that indicates if the JSON response must contain the
   *  entire element or only include the `id` and `text` select2 default fields. DEFAULT: false
   */
  public Select2DataWithConversion(Page<T> page, String idExpression,
	      ConversionService conversionService, boolean includeEntireElement) {
	  this(page, idExpression, conversionService);
	  this.includeEntireElement = includeEntireElement;
  }
  
  
  @Override
  protected Data<T> createData(T element) {
	String id = getIdAsString(element);
	String text = getAsString(element);
	
	if(includeEntireElement){
		return new Data<T>(id, text, element);
	}
	
	return new Data<T>(id, text);
  }

  @Override
  protected String getAsString(T element) {
    return conversionService.convert(element, String.class);
  }

  @Override
  protected String getIdAsString(T element) {
    return parseIdExpression.getValue(element, String.class);
  }
}
