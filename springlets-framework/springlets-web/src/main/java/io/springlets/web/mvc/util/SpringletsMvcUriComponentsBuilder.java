/*
 * Copyright 2017 the original author or authors.
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
package io.springlets.web.mvc.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This class is a custom implementation of the {@link MvcUriComponentsBuilder}
 * class provided by the Spring MVC. 
 * 
 * We only need to overwrite the private method `applyContributors`. The problem is
 * that all methods of this class are statics, and we need to re-implement them to be
 * able to use our customized method.  
 * 
 * This customization is necessary to be able to solve the following issue
 * https://jira.spring.io/browse/SPR-14890. 
 * 
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 * @since 1.1.0
 */
public class SpringletsMvcUriComponentsBuilder extends MvcUriComponentsBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringletsMvcUriComponentsBuilder.class);
	
	private static final PathMatcher pathMatcher = new AntPathMatcher();

	private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	private static final CompositeUriComponentsContributor defaultUriComponentsContributor;

	static {
		defaultUriComponentsContributor = new CompositeUriComponentsContributor(
				new PathVariableMethodArgumentResolver(), new RequestParamMethodArgumentResolver(false));
	}

	protected SpringletsMvcUriComponentsBuilder(UriComponentsBuilder baseUrl) {
		super(baseUrl);
	}


	public static UriComponentsBuilder fromMethodCall(Object info) {
		Assert.isInstanceOf(MethodInvocationInfo.class, info);
		MethodInvocationInfo invocationInfo = (MethodInvocationInfo) info;
		Class<?> controllerType = invocationInfo.getControllerType();
		Method method = invocationInfo.getControllerMethod();
		Object[] arguments = invocationInfo.getArgumentValues();
		return fromMethodInternal(null, controllerType, method, arguments);
	}

	private static UriComponentsBuilder fromMethodInternal(UriComponentsBuilder baseUrl,
			Class<?> controllerType, Method method, Object... args) {

		baseUrl = getBaseUrlToUse(baseUrl);
		String typePath = getTypeRequestMapping(controllerType);
		String methodPath = getMethodRequestMapping(method);
		String path = pathMatcher.combine(typePath, methodPath);
		baseUrl.path(path);
		UriComponents uriComponents = applyContributors(baseUrl, method, args);
		return UriComponentsBuilder.newInstance().uriComponents(uriComponents);
	}

	private static UriComponentsBuilder getBaseUrlToUse(UriComponentsBuilder baseUrl) {
		if (baseUrl != null) {
			return baseUrl.cloneBuilder();
		}
		else {
			return ServletUriComponentsBuilder.fromCurrentServletMapping();
		}
	}

	private static String getTypeRequestMapping(Class<?> controllerType) {
		Assert.notNull(controllerType, "'controllerType' must not be null");
		RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerType, RequestMapping.class);
		if (requestMapping == null) {
			return "/";
		}
		String[] paths = requestMapping.path();
		if (ObjectUtils.isEmpty(paths) || StringUtils.isEmpty(paths[0])) {
			return "/";
		}
		if (paths.length > 1 && logger.isWarnEnabled()) {
			logger.warn("Multiple paths on controller " + controllerType.getName() + ", using first one");
		}
		return paths[0];
	}

	private static String getMethodRequestMapping(Method method) {
		Assert.notNull(method, "'method' must not be null");
		RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
		if (requestMapping == null) {
			throw new IllegalArgumentException("No @RequestMapping on: " + method.toGenericString());
		}
		String[] paths = requestMapping.path();
		if (ObjectUtils.isEmpty(paths) || StringUtils.isEmpty(paths[0])) {
			return "/";
		}
		if (paths.length > 1 && logger.isWarnEnabled()) {
			logger.warn("Multiple paths on method " + method.toGenericString() + ", using first one");
		}
		return paths[0];
	}

	private static UriComponents applyContributors(UriComponentsBuilder builder, Method method, Object... args) {
		CompositeUriComponentsContributor contributor = getConfiguredUriComponentsContributor();
		if (contributor == null) {
			logger.debug("Using default CompositeUriComponentsContributor");
			contributor = defaultUriComponentsContributor;
		}

		int paramCount = method.getParameterTypes().length;
		int argCount = args.length;
		if (paramCount != argCount) {
			throw new IllegalArgumentException("Number of method parameters " + paramCount +
					" does not match number of argument values " + argCount);
		}

		final Map<String, Object> uriVars = new HashMap<String, Object>();
		for (int i = 0; i < paramCount; i++) {
			MethodParameter param = new SynthesizingMethodParameter(method, i);
			param.initParameterNameDiscovery(parameterNameDiscoverer);
			contributor.contributeMethodArgument(param, args[i], builder, uriVars);
		}
		
		// Custom implementation to remove uriVar if the value is null
		removeUriVarsWithNullValue(uriVars);
		
		// We may not have all URI var values, expand only what we have
		return builder.build().expand(new UriComponents.UriTemplateVariables() {
			@Override
			public Object getValue(String name) {
				return uriVars.containsKey(name) ? uriVars.get(name) : UriComponents.UriTemplateVariables.SKIP_VALUE;
			}
		});
	}

	/**
	 * Custom implementation. This method removes all the variables that contains a 
	 * null value from the provided uriVars map.
	 * 
	 * @param uriVars map that contains the uri variables.
	 */
	private static void removeUriVarsWithNullValue(Map<String, Object> uriVars) {
		List<String> uriVarsToRemove = new ArrayList<String>();
		for(Entry<String, Object> uriVar : uriVars.entrySet()){
			if(uriVar.getValue() == null){
				uriVarsToRemove.add(uriVar.getKey());
			}
		}
		for(String uriVarToRemove : uriVarsToRemove){
			uriVars.remove(uriVarToRemove);
		}
	}


	private static CompositeUriComponentsContributor getConfiguredUriComponentsContributor() {
		WebApplicationContext wac = getWebApplicationContext();
		if (wac == null) {
			return null;
		}
		try {
			return wac.getBean(MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME, CompositeUriComponentsContributor.class);
		}
		catch (NoSuchBeanDefinitionException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("No CompositeUriComponentsContributor bean with name '" +
						MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME + "'");
			}
			return null;
		}
	}

	private static WebApplicationContext getWebApplicationContext() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			logger.debug("No request bound to the current thread: is DispatcherSerlvet used?");
			return null;
		}

		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		if (request == null) {
			logger.debug("Request bound to current thread is not an HttpServletRequest");
			return null;
		}

		String attributeName = DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE;
		WebApplicationContext wac = (WebApplicationContext) request.getAttribute(attributeName);
		if (wac == null) {
			logger.debug("No WebApplicationContext found: not in a DispatcherServlet request?");
			return null;
		}
		return wac;
	}

}
