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

import io.springlets.web.mvc.config.SpringletsWebMvcSettings;

import org.springframework.boot.context.properties.ConfigurationProperties;

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

	private JsonpAdviceProperties jsonp = new JsonpAdviceProperties();

	public StringTrimmerAdviceProperties getTrimeditor() {
		return trimeditor;
	}

	public void setTrimeditor(StringTrimmerAdviceProperties trimeditorProperties) {
		this.trimeditor = trimeditorProperties;
	}

	public JsonpAdviceProperties getJsonp() {
		return jsonp;
	}

	public void setTrimeditor(JsonpAdviceProperties jsonpProperties) {
		this.jsonp = jsonpProperties;
	}

	/**
	 * Sets configuration items from the Spring Boot `springlets.mvc.advices`
	 * namespace to Springlets Web MVC settings.
	 *
	 * @param configuration
	 */
	public void applyTo(SpringletsWebMvcSettings configuration) {

		// StringTrimmer properties
		SpringletsWebMvcSettings.StringTrimmerAdviceSettings trimmerAdviceSettings = new SpringletsWebMvcSettings.StringTrimmerAdviceSettings();

		trimmerAdviceSettings.setEmptyAsNull(this.getTrimeditor().isEmptyAsNull());
		trimmerAdviceSettings.setCharsToDelete(this.getTrimeditor().getCharsToDelete());

		configuration.setTrimmerAdviceSettings(trimmerAdviceSettings);

		// Jsonp properties
		SpringletsWebMvcSettings.JsonpAdviceSettings jsonpAdviceSettings = new SpringletsWebMvcSettings.JsonpAdviceSettings();
		jsonpAdviceSettings.setJsonpQueryParamNames(this.getJsonp().getQueryParamNames());

		configuration.setJsonpAdviceSettings(jsonpAdviceSettings);
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
		private String[] queryParamNames = { "callback" };

		public String[] getQueryParamNames() {
			return queryParamNames;
		}

		public void setQueryParamNames(String[] queryParamNames) {
			this.queryParamNames = queryParamNames;
		}

	}

}
