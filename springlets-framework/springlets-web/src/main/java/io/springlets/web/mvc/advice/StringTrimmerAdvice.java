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
package io.springlets.web.mvc.advice;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Customizes the data binding to trim request parameter values.
 * 
 * Optionally allows transforming an empty string into a null value.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@ControllerAdvice
public class StringTrimmerAdvice {

  private boolean emptyAsNull = false;

  private String charsToDelete = null;


  public boolean isEmptyAsNull() {
    return emptyAsNull;
  }


  public void setEmptyAsNull(boolean emptyAsNull) {
    this.emptyAsNull = emptyAsNull;
  }


  public String getCharsToDelete() {
    return charsToDelete;
  }


  public void setCharsToDelete(String charsToDelete) {
    this.charsToDelete = charsToDelete;
  }

  /**
   * Registers the {@link StringTrimmerEditor}
   *
   * @param webDataBinder
   */
  @InitBinder
  public void initBinder(WebDataBinder webDataBinder) {
    StringTrimmerEditor trimmer =
        new StringTrimmerEditor(this.getCharsToDelete(), this.isEmptyAsNull());
    webDataBinder.registerCustomEditor(String.class, trimmer);
  }

}
