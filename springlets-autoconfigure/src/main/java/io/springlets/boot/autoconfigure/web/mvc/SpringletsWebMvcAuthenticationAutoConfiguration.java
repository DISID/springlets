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

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;

import io.springlets.boot.autoconfigure.security.jpa.SpringletsSecurityJpaAuthenticationAutoConfiguration;
import io.springlets.security.web.config.EnableSpringletsWebSecurity;

/**
 * {@link EnableAutoConfiguration Auto-configuration} to setup Spring Security
 * authentication.
 *  
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
//@Configuration
//@ConditionalOnWebApplication
//@ConditionalOnBean(SpringletsSecurityJpaAuthenticationAutoConfiguration.class)
//@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
//@EnableSpringletsWebSecurity
public class SpringletsWebMvcAuthenticationAutoConfiguration {

}
