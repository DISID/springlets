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
package io.springlets.data.domain;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * {@link AuditorAware} implementation to get the current user's name from
 * the Spring Security's authentication context.
 * 
 * @author http://www.disid.com[DISID Corporation S.L.]
 */
public class AuthenticationAuditorAware implements AuditorAware<String> {

  /**
   * Returns the object which represents the selected element for identifying
   * the user who modifies registers of an entity.
   * 
   * @return object which represents the user or null if the user is not logged.
   */
  @Override
  public String getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    return authentication.getName();
  }

}
