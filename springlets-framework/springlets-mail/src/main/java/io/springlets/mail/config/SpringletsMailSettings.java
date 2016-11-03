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
package io.springlets.mail.config;

/**
 * Springlets Mail configuration.
 *
 * This class allows for pluggable apply of configuration values, allowing
 * Springlets Mail to remain independent of any specific 3rd party library
 * like the Spring Boot that loads the configuration values via properties
 * files.
 *
 * @author Manuel Iborra at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsMailSettings {

	private String host = "";

	private String port = "";

	private String protocol = "";

	private String username = "";

	private String password = "";

	private String starttlsEnabled = "";

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
}
