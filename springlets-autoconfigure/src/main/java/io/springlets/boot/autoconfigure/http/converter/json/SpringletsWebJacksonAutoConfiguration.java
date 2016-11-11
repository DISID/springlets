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
package io.springlets.boot.autoconfigure.http.converter.json;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import io.springlets.http.converter.json.BindingResultModule;
import io.springlets.web.config.EnableSpringletsWebJacksonSupport;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Springlets Web Jackson support.
 * 
 * When in effect, the auto-configuration is the equivalent of enabling Springlets Web
 * Jackson support through the {@link EnableSpringletsWebJacksonSupport} annotation.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableSpringletsWebJacksonSupport
@ConditionalOnWebApplication
@ConditionalOnClass({BindingResultModule.class, ObjectMapper.class, Hibernate5Module.class})
@ConditionalOnMissingBean(BindingResultModule.class)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class SpringletsWebJacksonAutoConfiguration {

}
