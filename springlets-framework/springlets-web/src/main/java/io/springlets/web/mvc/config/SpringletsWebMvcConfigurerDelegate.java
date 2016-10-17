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

import org.springframework.util.Assert;

/**
 * Delegating implementation of {@link SpringletsWebMvcConfigurer} that will forward 
 * all calls to configuration methods to all registered {@link SpringletsWebMvcConfigurer}.
 * 
 * Based on https://github.com/spring-projects/spring-data-rest[Spring Data REST] project.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
class SpringletsWebMvcConfigurerDelegate implements SpringletsWebMvcConfigurer {

	private final Iterable<SpringletsWebMvcConfigurer> delegates;

	/**
	 * Creates a new {@link RepositoryRestConfigurerDelegate} for the given 
	 * {@link RepositoryRestConfigurer}s.
	 * 
	 * @param delegates must not be {@literal null}.
	 */
	public SpringletsWebMvcConfigurerDelegate(Iterable<SpringletsWebMvcConfigurer> delegates) {

		Assert.notNull(delegates, "SpringletsWebMvcConfigurers must not be null!");

		this.delegates = delegates;
	}

	@Override
	public void configureSpringletsWebMvcSettings(SpringletsWebMvcSettings config) {
		for (SpringletsWebMvcConfigurer configurer : delegates) {
			configurer.configureSpringletsWebMvcSettings(config);
		}
	}

}
