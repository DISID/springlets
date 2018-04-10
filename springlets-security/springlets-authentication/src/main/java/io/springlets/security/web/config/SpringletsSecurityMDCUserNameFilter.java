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
package io.springlets.security.web.config;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * = Set authenticated username in {@link MDC}
 *
 * This allows to show log messages with the authenticated username.
 *
 */
public class SpringletsSecurityMDCUserNameFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Nothing to do here
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      MDC.put("username", authentication.getName());
    }
    try {
      chain.doFilter(req, resp);
    } finally {
      if (authentication != null) {
        MDC.remove("userName");
      }
    }
  }

  @Override
  public void destroy() {
    // Nothing to do here
  }
}

