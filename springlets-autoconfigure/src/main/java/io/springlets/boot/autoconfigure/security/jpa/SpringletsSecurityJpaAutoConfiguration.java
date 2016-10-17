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
package io.springlets.boot.autoconfigure.security.jpa;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import io.springlets.security.jpa.config.EnableSpringletsSecurityJpa;
import io.springlets.security.jpa.config.SpringletsSecurityJpaConfiguration;

/**
 * {@link ConfigurationProperties} for Springlets Web MVC.
 * 
 * Based on DevToolsProperties.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
//@AutoConfigureBefore(AuthenticationConfiguration.class)
@ConditionalOnClass(SpringletsSecurityJpaConfiguration.class)
@ConfigurationProperties(prefix = "springlets.security.jpa")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableSpringletsSecurityJpa
//@EntityScan("io.springlets.security")
//@Import({PropertyPlaceholderAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class SpringletsSecurityJpaAutoConfiguration {


}
