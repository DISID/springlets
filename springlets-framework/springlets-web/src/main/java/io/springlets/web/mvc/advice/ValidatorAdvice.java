/*
 * Copyright 2016-2017 the original author or authors.
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
package io.springlets.web.mvc.advice;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import io.springlets.validation.CollectionValidator;

/**
 * Controller advice that adds the {@link CollectionValidator} to the {@link WebDataBinder}.
 * 
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@ControllerAdvice
public class ValidatorAdvice {

  private final CollectionValidator collectionValidator;

  public ValidatorAdvice(LocalValidatorFactoryBean validatorFactory) {
    collectionValidator = new CollectionValidator(validatorFactory);
  }

  /**
   * Adds the {@link CollectionValidator} to the supplied {@link WebDataBinder}
   * 
   * @param binder web data binder.
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    Object target = binder.getTarget();
    if (target != null && collectionValidator.supports(target.getClass())) {
      binder.addValidators(collectionValidator);
    }
  }
}