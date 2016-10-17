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
package io.springlets.web.mvc.config;

/**
 * Springlets Web MVC configuration.
 * 
 * This class allows for pluggable apply of configuration values, allowing 
 * Springlets Web MVC to remain independent of any specific 3rd party library
 * like the Spring Boot that loads the configuration values via properties 
 * files.
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsWebMvcSettings {

  private StringTrimmerAdviceSettings trimmerAdviceSettings = new StringTrimmerAdviceSettings();

  public StringTrimmerAdviceSettings getTrimmerAdviceSettings() {
    return trimmerAdviceSettings;
  }

  public void setTrimmerAdviceSettings(StringTrimmerAdviceSettings trimmerAdviceSettings) {
    this.trimmerAdviceSettings = trimmerAdviceSettings;
  }

  /**
   * StringTrimmerAdvice settings.
   */
  public static class StringTrimmerAdviceSettings {

    /** `true` if an empty parameter value is to be transformed into `null` */
    private boolean emptyAsNull = false;

    /** 
     * Set of characters to delete, in addition to trimming the parameter value. 
     * Useful for deleting unwanted line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
     */
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
  }
}
