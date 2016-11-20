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
package io.springlets.security.config;

/**
 * Springlets Security configuration.
 *
 * This configuration is independent of any specific 3rd party library
 * like the Spring Boot.
 * 
 * To bind the property values from *Spring Boot* auto-configuration use
 * the http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-3rd-party-configuration[Spring Boot third-party configuration].
 *
 * The inner classes inside this class makes easier the value binding
 * from nested properties, for example 
 * http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#configuration-metadata-nested-properties[Spring Boot Nested properties].
 * 
 * This class must manage the properties `springlets.security`. It hosts the following nested properties:
 * 
 * * `auth.in-memory`
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsSecurityProperties {

  /**
   * `springlets.security.auth` properties.
   */
  private AuthProperties auth = new AuthProperties();

  public AuthProperties getAuth() {
    return auth;
  }

  public void setAuth(AuthProperties authProperties) {
    this.auth = authProperties;
  }

  /**
   * `springlets.security.auth` properties.
   */
  public static class AuthProperties {

    /**
     * `springlets.security.auth.in-memory` properties.
     */
    private InMemoryProperties inMemory = new InMemoryProperties();

    public InMemoryProperties getInMemory() {
      return inMemory;
    }

    public void setInMemory(InMemoryProperties properties) {
      this.inMemory = properties;
    }
  }

  /**
   * `springlets.security.auth.in-memory` properties.
   */
  public static class InMemoryProperties {

    /**
     * `springlets.security.auth.in-memory.enabled` property.
     */
    private boolean enabled;

    /**
     * `springlets.security.auth.in-memory.erase-credentials` property.
     */
    private boolean eraseCredentials = false;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean on) {
      this.enabled = on;
    }

    public boolean isEraseCredentials() {
      return eraseCredentials;
    }

    public void setEraseCredentials(boolean on) {
      this.eraseCredentials = on;
    }
  }

}
