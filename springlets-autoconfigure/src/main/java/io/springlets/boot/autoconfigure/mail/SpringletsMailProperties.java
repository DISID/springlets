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
package io.springlets.boot.autoconfigure.mail;

import io.springlets.mail.config.SpringletsMailSettings;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for Springlets Mail.
 *
 * Based on DevToolsProperties.
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
@ConfigurationProperties(prefix = "springlets.mail.receiver")
public class SpringletsMailProperties {

	/**
	 * Server host.
	 */
	private String host = "";

	/**
	 * Server port.
	 */
	private String port = "";

	/**
	 * Protocol used by server to receive emails.
	 */
	private String protocol = "";

	/**
	 * Login user of the server.
	 */
	private String username = "";

	/**
	 * Login password of the server.
	 */
	private String password = "";

	/**
	 * Enables the use of the STARTTLS
	 */
	private String starttlsEnabled = "";

	/**
	 * JNDI name. When set, takes precedence to others mail settings.
	 */
	private String jndiName = "";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStarttlsEnabled() {
		return starttlsEnabled;
	}

	public void setStarttlsEnabled(String starttlsEnabled) {
		this.starttlsEnabled = starttlsEnabled;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * Sets configuration items from the Spring Boot `springlets.mail.receiver`
	 * namespace to Springlets Mail settings.
	 *
	 * @param configuration
	 */
	public void applyTo(SpringletsMailSettings configuration) {
		configuration.setHost(this.host);
		configuration.setJndiName(this.jndiName);
		configuration.setPassword(this.password);
		configuration.setPort(this.port);
		configuration.setProtocol(this.protocol);
		configuration.setStarttlsEnabled(this.starttlsEnabled);
		configuration.setUsername(this.username);
	}

}
