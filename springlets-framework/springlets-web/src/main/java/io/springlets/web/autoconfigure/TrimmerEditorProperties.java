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
package io.springlets.web.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.springlets.web.StringTrimmerAdvice;

/**
 * = TrimmerEditorProperties
 *
 * {@link ConfigurationProperties} for configuring {@link StringTrimmerEditor}.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@ConfigurationProperties(prefix = "springlets.mvc.trimeditor")
public class TrimmerEditorProperties {

  /** `true` set up the {@link StringTrimmerAdvice} */
  private boolean enabled = false;

  /** `true` if an empty parameter value is to be transformed into `null` */
  private boolean emptyAsNull = false;
  
  /** 
   * Set of characters to delete, in addition to trimming the parameter value. 
   * Useful for deleting unwanted line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
   */
  private String charsToDelete = null;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

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
  
}
