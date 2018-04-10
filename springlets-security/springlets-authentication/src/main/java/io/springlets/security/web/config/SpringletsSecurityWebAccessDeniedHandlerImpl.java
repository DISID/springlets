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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/**
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsSecurityWebAccessDeniedHandlerImpl extends AccessDeniedHandlerImpl {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    ContentNegotiationStrategy negotiationStrategy = new HeaderContentNegotiationStrategy();
    MediaTypeRequestMatcher matcher =
        new MediaTypeRequestMatcher(negotiationStrategy, MediaType.TEXT_HTML);
    matcher.setUseEquals(false);

    if (matcher.matches(request)) {
      DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
      redirectStrategy.setContextRelative(false);
      redirectStrategy.sendRedirect(request, response, "/errores/403");
    } else {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);

    }

  }
}
