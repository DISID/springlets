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
package io.springlets.validation;

import java.util.Collection;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Spring {@link Validator} that iterates over the elements of a {@link Collection} and run the 
 * validation process for each of them individually.
 * 
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class CollectionValidator implements Validator {

  /**
   * TODO Auto-generated attribute documentation
   */
  private final Validator validator;

  /**
   * TODO Auto-generated constructor documentation
   *
   * @param validatorFactory
   */ 
  public CollectionValidator(LocalValidatorFactoryBean validatorFactory) {
    this.validator = validatorFactory;
  }

  /**
   * TODO Auto-generated method documentation
   *
   * @param clazz
   * @return boolean
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return Collection.class.isAssignableFrom(clazz);
  }

  /**
   * Validate each element inside the supplied {@link Collection}.
   * 
   * The supplied errors instance is used to report the validation errors.
   * 
   * @param target the collection that is to be validated
   * @param errors contextual state about the validation process
   */
  @Override
  @SuppressWarnings("rawtypes")
  public void validate(Object target, Errors errors) {
    Collection collection = (Collection) target;
    int index = 0;

    for (Object object : collection) {
      BeanPropertyBindingResult elementErrors = new BeanPropertyBindingResult(object,
          errors.getObjectName());
      elementErrors.setNestedPath("[".concat(Integer.toString(index++)).concat("]."));
      ValidationUtils.invokeValidator(validator, object, elementErrors);

      errors.addAllErrors(elementErrors);
    }
  }

}
