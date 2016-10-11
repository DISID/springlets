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
package io.springlets.data.web.config;

import io.springlets.data.domain.GlobalSearch;
import io.springlets.data.web.GlobalSearchHandlerMethodArgumentResolver;
import io.springlets.data.web.datatables.DatatablesPageable;
import io.springlets.data.web.datatables.DatatablesPageableHandlerMethodArgumentResolver;
import io.springlets.data.web.datatables.DatatablesSort;
import io.springlets.data.web.datatables.DatatablesSortHandlerMethodArgumentResolver;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to automatically register the following beans for usage with Spring MVC:
 * 
 * * {@link GlobalSearchHandlerMethodArgumentResolver}: to allow injection of
 * {@link GlobalSearch} instances into controller methods automatically created
 * from request parameters.
 * * {@link DatatablesPageableHandlerMethodArgumentResolver}: to allow
 * injection of {@link DatatablesPageable} instances into controller methods 
 * automatically created from request parameters.
 * * {@link DatatablesSortHandlerMethodArgumentResolver}: to allow injection of
 * {@link DatatablesSort} instances into controller methods automatically created
 * from request parameters. 
 *
 * @see SpringletsDataWebConfiguration
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({EnableSpringletsDataWebSupport.SpringletsDataWebConfigurationImportSelector.class})
public @interface EnableSpringletsDataWebSupport {

  /**
   * Import selector to register {@link SpringletsDataWebConfiguration} as configuration class.
   * 
   * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
   */
  static class SpringletsDataWebConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
      return new String[] {SpringletsDataWebConfiguration.class.getName()};
    }
  }
}
