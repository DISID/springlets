/*
 * Copyright (c) 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad de
 * España. Todos los derechos reservados.
 *
 * Proyecto : MSSSI northwindjee
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
  public void init(FilterConfig filterConfig) throws ServletException {}

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
  public void destroy() {}
}

