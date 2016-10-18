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
package io.springlets.data.jpa.config;

import io.springlets.data.domain.AuthenticationAuditorAware;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to automatically register the following beans for usage with Spring Data:
 * 
 * * {@link AuthenticationAuditorAware}: default {@link AuditorAware} implementation
 * integrated with Spring Security's authentication context. 
 *
 * @see SpringletsDataJpaAuthenticationAuditorConfiguration
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({SpringletsDataJpaAuthenticationAuditorConfiguration.class})
public @interface EnableSpringletsDataJpaAuthenticationAuditorSupport {

}
