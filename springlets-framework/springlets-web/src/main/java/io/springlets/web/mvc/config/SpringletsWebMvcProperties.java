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
 * This configuration is independent of any specific 3rd party library
 * like the Spring Boot.
 * 
 * To bind the property values from *Spring Boot* auto-configuration use
 * the http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-3rd-party-configuration[Spring Boot third-party configuration].
 *
 * The inner classes inside this class makes easier the value binding
 * from nested properties, for example 
 * http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#configuration-metadata-nested-properties[Spring Boot Nested properties].
 * 
 * This class must manage the properties `springlets.mvc`. It hosts the following nested properties:
 * 
 * * `advices.trimeditor`
 * * `advices.jsonp`
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsWebMvcProperties {

  /**
   * `springlets.mvc.advices` properties.
   */
  private AdvicesProperties advices = new AdvicesProperties();

  public AdvicesProperties getAdvices() {
    return advices;
  }

  public void setAdvices(AdvicesProperties advicesProperties) {
    this.advices = advicesProperties;
  }

  /**
   * `springlets.mvc.advices` properties.
   */
  public static class AdvicesProperties {

    /**
     * `springlets.mvc.advices.enabled` property
     */
    private boolean enabled;

    /**
     * `springlets.mvc.advices.trimeditor` properties
     */
    private StringTrimmerAdviceProperties trimeditor = new StringTrimmerAdviceProperties();

    /**
     * `springlets.mvc.advices.jsonp` properties
     */
    private JsonpAdviceProperties jsonp = new JsonpAdviceProperties();

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean on) {
      this.enabled = on;
    }

    public StringTrimmerAdviceProperties getTrimeditor() {
      return trimeditor;
    }

    public void setTrimeditor(StringTrimmerAdviceProperties trimeditorProperties) {
      this.trimeditor = trimeditorProperties;
    }

    public JsonpAdviceProperties getJsonp() {
      return jsonp;
    }

    public void setJsonp(JsonpAdviceProperties jsonpProperties) {
      this.jsonp = jsonpProperties;
    }
  }

  /**
   * `springlets.mvc.advices.trimeditor` properties.
   */
  public static class StringTrimmerAdviceProperties {

    /**
     * `true` if an empty parameter value is to be transformed into `null`
     */
    private boolean emptyAsNull = true;

    /**
     * Set of characters to delete, in addition to trimming the parameter
     * value. Useful for deleting unwanted line breaks: e.g. "\r\n\f" will
     * delete all new lines and line feeds in a String.
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

  /**
   * `springlets.mvc.advices.jsonp` properties.
   */
  public static class JsonpAdviceProperties {

    /** Jsonp parameters name. `callback` parameter name by default */
    private String[] queryParamNames = {"callback"};

    public String[] getQueryParamNames() {
      return queryParamNames;
    }

    public void setQueryParamNames(String[] queryParamNames) {
      this.queryParamNames = queryParamNames;
    }
  }
}
