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
package io.springlets.boot.autoconfigure.data.web;

import io.springlets.data.web.GlobalSearchHandlerMethodArgumentResolver;
import io.springlets.data.web.config.EnableSpringletsDataWebSupport;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Springlets Data's web support.
 * 
 * When in effect, the auto-configuration is the equivalent of enabling Springlets Data's web
 * support through the {@link EnableSpringletsDataWebSupport} annotation.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableSpringletsDataWebSupport
@ConditionalOnWebApplication
@ConditionalOnClass({GlobalSearchHandlerMethodArgumentResolver.class,
    WebMvcConfigurerAdapter.class})
@ConditionalOnMissingBean(GlobalSearchHandlerMethodArgumentResolver.class)
@AutoConfigureAfter(SpringDataWebAutoConfiguration.class)
public class SpringletsDataWebAutoConfiguration {

}
