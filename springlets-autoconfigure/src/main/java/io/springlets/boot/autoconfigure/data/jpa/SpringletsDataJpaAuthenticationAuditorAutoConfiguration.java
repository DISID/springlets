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
package io.springlets.boot.autoconfigure.data.jpa;

import io.springlets.data.domain.AuthenticationAuditorAware;
import io.springlets.data.jpa.config.EnableSpringletsDataJpaAuthenticationAuditorSupport;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Springlets Data's 
 * authentication auditor.
 * 
 * When in effect, the auto-configuration is the equivalent of enabling 
 * Springlets Data's authentication auditor
 * support through the 
 * {@link EnableSpringletsDataJpaAuthenticationAuditorSupport} annotation.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
@EnableSpringletsDataJpaAuthenticationAuditorSupport
@ConditionalOnWebApplication
@ConditionalOnClass({AuthenticationAuditorAware.class, EnableJpaAuditing.class, AuditorAware.class,
    SecurityContext.class})
@ConditionalOnMissingBean(AuditorAware.class)
@AutoConfigureAfter(SpringDataWebAutoConfiguration.class)
public class SpringletsDataJpaAuthenticationAuditorAutoConfiguration {

}
