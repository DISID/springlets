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
package io.springlets.boot.autoconfigure.web.mvc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.springlets.web.mvc.advice.StringTrimmerAdvice;
import io.springlets.web.mvc.config.SpringletsWebMvcSettings;

/**
 * {@link ConfigurationProperties} for Springlets Web MVC.
 * 
 * Based on DevToolsProperties.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@ConfigurationProperties(prefix = "springlets.mvc.advices")
public class SpringletsWebMvcAdvicesProperties {

  private StringTrimmerAdviceProperties trimeditor = new StringTrimmerAdviceProperties();

  public StringTrimmerAdviceProperties getTrimeditor() {
    return trimeditor;
  }

  public void setTrimeditor(StringTrimmerAdviceProperties trimeditorProperties) {
    this.trimeditor = trimeditorProperties;
  }

  /**
   * Sets configuration items from the Spring Boot `springlets.mvc.advices` 
   * namespace to Springlets Web MVC settings.
   * 
   * @param configuration
   */
  public void applyTo(SpringletsWebMvcSettings configuration) {
    SpringletsWebMvcSettings.StringTrimmerAdviceSettings trimmerAdviceSettings =
        new SpringletsWebMvcSettings.StringTrimmerAdviceSettings();

    trimmerAdviceSettings.setEmptyAsNull(this.getTrimeditor().isEmptyAsNull());
    trimmerAdviceSettings.setCharsToDelete(this.getTrimeditor().getCharsToDelete());

    configuration.setTrimmerAdviceSettings(trimmerAdviceSettings);
  }

  /**
   * `springlets.mvc.advices.trimeditor` properties.
   */
  public static class StringTrimmerAdviceProperties {

    /** `true` set up the {@link StringTrimmerAdvice} */
    private boolean enabled = true;

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

}
