package io.springlets.security.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class SpringletsSecurityWebMvcConfigurer extends WebMvcConfigurerAdapter {

      @Override
      public void addViewControllers(ViewControllerRegistry registry) {
        // forward the request for "/login" to a view called "login"
        registry.addViewController("/login").setViewName("login");
      }

}
