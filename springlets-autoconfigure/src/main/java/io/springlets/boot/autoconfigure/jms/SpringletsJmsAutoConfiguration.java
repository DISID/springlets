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
package io.springlets.boot.autoconfigure.jms;

import io.springlets.jms.config.EnableSpringletsJms;
import io.springlets.jms.config.SpringletsJmsConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
/**
 * {@link EnableAutoConfiguration Auto-configuration} for Springlets jms
 * integration.
 *
 * Activates when no {@link SpringletsJmsConfiguration} is found.
 *
 * Once in effect, the auto-configuration allows to configure
 * {@link SpringletsJmsConfiguration}
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
@AutoConfigureAfter(JmsAutoConfiguration.class)
@ConditionalOnClass(SpringletsJmsConfiguration.class)
@ConditionalOnBean({ConnectionFactory.class, Destination.class})
@Configuration
@EnableSpringletsJms
public class SpringletsJmsAutoConfiguration {

}
