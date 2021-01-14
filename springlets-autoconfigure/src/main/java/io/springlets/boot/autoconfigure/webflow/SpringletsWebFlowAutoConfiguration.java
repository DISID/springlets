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
package io.springlets.boot.autoconfigure.webflow;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.binding.convert.ConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.webflow.executor.FlowExecutor;

import io.springlets.webflow.binding.convert.StringToNullConverter;
import io.springlets.webflow.config.EnableSpringletsWebFlow;
import io.springlets.webflow.config.SpringletsWebFlowConfiguration;

/**
 * @{@link EnableSpringletsWebFlow Auto-configuration} to setup the Spring MVC artifacts 
 * for Spring WebFlow
 * 
 * * WebFlow {@link ConversionService} that delegates on the default Spring MVC  
 *   {@link org.springframework.core.convert.ConversionService}.
 * * Set the default Spring MVC {@link Validator} as the default Spring WebFlow Validator.
 * * Secure {@link FlowExecutor}.
 * * Scans the application classpath to load the flow definition XML files automatically.
 * * Auto register the {@link StringToNullConverter}.
 * * Enable/disable flow and views cache (cache enabled by default).
 * * Adds compatibility with AJAX-based events (redirects) in Spring WebFlow.
 * * Set the Spring MVC request handler artifacts for Spring WebFlow.
 * * Register an {@link LocaleChangeInterceptor interceptor} for changing the 
 *   current locale on every request.
 *  
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Configuration
@ConditionalOnClass(SpringletsWebFlowConfiguration.class)
@ConditionalOnWebApplication
@EnableSpringletsWebFlow
public class SpringletsWebFlowAutoConfiguration {

}
