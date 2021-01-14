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
package io.springlets.webflow.config;

import java.util.Arrays;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.engine.impl.FlowExecutionImplFactory;
import org.springframework.webflow.execution.factory.ConditionalFlowExecutionListenerLoader;
import org.springframework.webflow.execution.factory.FlowExecutionListenerCriteria;
import org.springframework.webflow.execution.factory.FlowExecutionListenerCriteriaFactory;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.executor.FlowExecutorImpl;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
import org.springframework.webflow.security.SecurityFlowExecutionListener;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.FlowAjaxThymeleafView;

import io.springlets.webflow.binding.convert.StringToNullConverter;

/**
 * Setup Spring MVC artifacts for Spring WebFlow.
 * 
 * * Secure {@link #flowExecutor()} bean.
 * * 
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
@Configuration
public class SpringletsWebFlowConfiguration extends AbstractFlowConfiguration {

  private static final String UTF8 = "UTF-8";

  /** Default service for type conversion */
  @Autowired
  private ConversionService conversionService;

  /** Default object validator */
  @Autowired
  private Validator validator;

  /** Default interceptor for changing the current locale on every request */
  @Autowired
  private LocaleChangeInterceptor localeChangeInterceptor;

  /** Thymeleaf TemplateEngine for Spring MVC applications */
  @Autowired
  private SpringTemplateEngine templateEngine;

  /** In development mode, flows auto-refresh on change */
  @Value("${springlets.webflow.devtools.enabled:true}")
  private boolean development;

  /** Pass flow output to the Spring MVC flash scope */
  @Value("${springlets.webflow.flash.scope.save-output:true}")
  private boolean saveOutputToflashScope;

  /**
   * {@link FlowExecutor} bean configured with the default flow definitions provided 
   * by {@link #flowRegistry()}.
   * 
   * If you want to replace the default FlowExecutor completely, define 
   * a @{@link Bean} of that type and mark it as @{@link Primary}.
   * 
   * @return the flow executor
   */
  @Bean
  public FlowExecutor flowExecutor() {
    return getFlowExecutorBuilder(flowRegistry()).build();
  }

  /**
   * The default flow definitions container that scans the application classpath to 
   * load the flow definition XML files automatically.
   * 
   * The registry base path is `classpath:templates`, that is, the flow definitions must be
   * the templates folder.
   * 
   * The flow location pattern used to register the flows is `/ ** / *-flow.xml`. To learn
   * more read http://docs.spring.io/spring-webflow/docs/current/reference/htmlsingle/#flow-registry-base-path[flow location base path].
   */
  @Bean
  public FlowDefinitionRegistry flowRegistry() {
    return getFlowDefinitionRegistryBuilder(flowBuilderServices())
        .setBasePath("classpath:templates").addFlowLocationPattern("/**/*-flow.xml").build();
  }

  /**
   * Creates the {@link FlowBuilderServices} and configures it as follows:
   * 
   * * Set the WebFlow {@link org.springframework.binding.convert.ConversionService} 
   *   that delegates on the default Spring MVC {@link ConversionService}.
   * * Set the default Spring MVC {@link Validator} as the default Spring WebFlow Validator.
   * * Register the {@link StringToNullConverter}.
   * * Enable/disable flow and views cache (cache enabled by default).
   * * View factories creator that resolve their views by loading flow-relative resources.
   * 
   * @return the FlowBuilderServices
   */
  @Bean
  public FlowBuilderServices flowBuilderServices() {

    DefaultConversionService webFlowConversionService =
        new DefaultConversionService(this.conversionService);

    webFlowConversionService.addConverter(new StringToNullConverter());

    return getFlowBuilderServicesBuilder().setViewFactoryCreator(mvcViewFactoryCreator())
        .setConversionService(webFlowConversionService).setValidator(this.validator)
        .setDevelopmentMode(!this.development).build();
  }

  // === Spring MVC request handler artifacts for Spring WebFlow

  /**
   * Creates the native Spring MVC-based views using the {@link #ajaxThymeleafViewResolver()}.
   *
   * @return the Spring MVC ViewFactoryCreator
   */
  @Bean
  public MvcViewFactoryCreator mvcViewFactoryCreator() {
    MvcViewFactoryCreator mvcViewFactoryCreator = new MvcViewFactoryCreator();
    mvcViewFactoryCreator
        .setViewResolvers(Arrays.<ViewResolver>asList(ajaxThymeleafViewResolver()));
    return mvcViewFactoryCreator;
  }

  /**
   * Spring MVC {@link HandlerMapping} that manages URL path mappings that matches the <i>ids</i> 
   * of registered {@link FlowDefinition flow definitions}.
   *
   * Additionally register an {@link LocaleChangeInterceptor interceptor} for changing the 
   * current locale on every request 
   *
   * @return the Spring MVC HandlerMapping for web flows
   */
  @Bean
  public FlowHandlerMapping flowHandlerMapping() {
    FlowHandlerMapping handlerMapping = new FlowHandlerMapping();

    handlerMapping.setOrder(-1);
    handlerMapping.setFlowRegistry(this.flowRegistry());

    Object[] localChangeInterceptors = {this.localeChangeInterceptor};
    handlerMapping.setInterceptors(localChangeInterceptors);

    return handlerMapping;
  }

  /**
   * Spring MVC {@link HandlerAdapter} that encapsulates the generic workflow associated 
   * with executing flows.
   * 
   * Set the handler adapter to pass flow output to the Spring MVC flash scope. 
   *
   * @return the Spring MVC HandlerAdapter for web flows
   */
  @Bean
  public FlowHandlerAdapter flowHandlerAdapter() {
    FlowHandlerAdapter handlerAdapter = new FlowHandlerAdapter();
    handlerAdapter.setFlowExecutor(this.flowExecutor());

    handlerAdapter.setSaveOutputToFlashScopeOnRedirect(this.saveOutputToflashScope);
    return handlerAdapter;
  }

  /** 
   * Add compatibility with AJAX-based events (redirects) in Spring WebFlow
   * 
   * @return the ThymeleafViewResolver
   */
  @Bean
  public AjaxThymeleafViewResolver ajaxThymeleafViewResolver() {
    AjaxThymeleafViewResolver resolver = new AjaxThymeleafViewResolver();
    resolver.setViewClass(FlowAjaxThymeleafView.class);
    resolver.setTemplateEngine(this.templateEngine);
    resolver.setCharacterEncoding(UTF8);
    resolver.setCache(!this.development);
    return resolver;
  }

  /**
   * An {@link SmartInitializingSingleton} is a callback interface triggered at the end of the 
   * singleton pre-instantiation phase during BeanFactory bootstrap.
   * 
   * This interface is indicated for performing some initialization after the 
   * regular singleton instantiation algorithm, avoiding side effects with accidental early 
   * initialization.
   * 
   * This implementation will autowire the {@link SecurityFlowExecutionListener}
   * into the {@link FlowExecutor} when the Spring Security dependency is on the classpath 
   * by checking whether the `EnableWebSecurity` is present and can be loaded.
   * 
   * If present, the {@link FlowExecutor} bean will be configured with the 
   * security enabled by {@link #securityFlowExecutionListener()}.
   * 
   * @return the flow executor
   * @see ClassUtils
   */
  protected static class SecurityConfigurationListener implements SmartInitializingSingleton {

    @Autowired
    private FlowExecutor flowExecutor;

    @Override
    public void afterSingletonsInstantiated() {
      if (ClassUtils.isPresent(
          "org.springframework.security.config.annotation.web.configuration.EnableWebSecurity",
          null)) {
        configureSecurity();
      }
    }

    /**
     * Setup the {@link FlowExecutor} with the {@link SecurityFlowExecutionListener}.
     */
    private void configureSecurity() {
      if (flowExecutor instanceof FlowExecutorImpl) {
        FlowExecutorImpl flowImpl = (FlowExecutorImpl) flowExecutor;
        FlowExecutionImplFactory exeFactory =
            (FlowExecutionImplFactory) flowImpl.getExecutionFactory();
        exeFactory.setExecutionListenerLoader(getFlowExecutionListener());
      }
    }

    /**
     * Create the {@link SecurityFlowExecutionListener} for all flows.
     * @return
     */
    private ConditionalFlowExecutionListenerLoader getFlowExecutionListener() {
      // * means "all flows"
      FlowExecutionListenerCriteria listenerCriteria =
          new FlowExecutionListenerCriteriaFactory().getListenerCriteria("*");

      ConditionalFlowExecutionListenerLoader listenerLoader =
          new ConditionalFlowExecutionListenerLoader();
      listenerLoader.addListener(new SecurityFlowExecutionListener(), listenerCriteria);

      return listenerLoader;
    }

  }

}
