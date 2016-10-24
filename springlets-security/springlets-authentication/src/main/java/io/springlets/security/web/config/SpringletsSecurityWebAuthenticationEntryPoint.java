/**
 * 
 */
package io.springlets.security.web.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/**
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsSecurityWebAuthenticationEntryPoint implements AuthenticationEntryPoint {

  // Don't make final to allow test cases faking them
  private static String LOGIN_FORM_URL = "/login";

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

//    if (LOG.isDebugEnabled()) {
//      LOG.debug("Redirigiendo a pantalla de login: " + LOGIN_FORM_URL);
//    }

    ContentNegotiationStrategy negotiationStrategy = new HeaderContentNegotiationStrategy();
    MediaTypeRequestMatcher matcher =
        new MediaTypeRequestMatcher(negotiationStrategy, MediaType.TEXT_HTML);
    matcher.setUseEquals(false);

    if (matcher.matches(request)) {
      DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
      redirectStrategy.setContextRelative(false);
      redirectStrategy.sendRedirect(request, response, LOGIN_FORM_URL);
    } else {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }
}
