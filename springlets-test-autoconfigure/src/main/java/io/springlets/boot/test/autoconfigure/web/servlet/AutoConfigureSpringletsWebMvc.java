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
package io.springlets.boot.test.autoconfigure.web.servlet;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ImportAutoConfiguration Auto-configuration imports} for typical Springlets Spring MVC
 * tests. Most tests should consider using {@link SpringletsWebMvcTest @SpringletsWebMvcTest} 
 * rather than using this annotation directly.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration
public @interface AutoConfigureSpringletsWebMvc {

}