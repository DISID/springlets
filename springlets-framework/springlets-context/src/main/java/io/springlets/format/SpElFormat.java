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
package io.springlets.format;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

/**
 * Declares that a field should be formatted using a SpEL expression.
 *
 * Can be applied to any Java Object value type. If no value is provided at
 * method or field level, it looks for the value applied at the value type
 * class level. If no value is provided at any level, it defaults to use the 
 * _#{toString()}_ expression.
 *
 * Example:
 * 
 * [source,java]
 * ----
 * @SpElFormat("#{firstName} #{lastName}")
 * public class User {
 *   
 *   private String firstName;
 *   private String lastName;
 *   
 *   // Constructor, getters, setters, methods...
 * }
 * 
 * public class Order {
 *   
 *   @SpElFormat()
 *   private User customer;
 * }
 * 
 * public class CreditCard {
 *   
 *   @SpElFormat("#{lastName}, #{firstName} ")
 *   private User customer;
 * }
 * ----
 *
 * In the example, the _Order.customer_ property will apply the format defined
 * in the _User_ class level _SpElFormat_ annotation. In the
 * _CreditCard.customer_ property, the format applied will be the one provided
 * in the property annotation.
 * 
 * To apply an expression based on the current locale, use the {@link #message()}
 * parameter. If both {@link #expression()} and {@link #message()} parameters are
 * provided, the {@link #message()} expression for the current or the default
 * locale will be used. If not available, the {@link #expression()} will be applied
 * instead.
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE})
public @interface SpElFormat {

  /**
   * Expression format in SpEl to apply to a field, parameter or method return
   * value. If given at the class level, it will be used as the default
   * expression to apply at the other levels.
   * @return the value of the value/expression annotation attribute
   */
  @AliasFor("value")
  String expression() default "";

  /**
   * Alias for the {@link #expression()} attribute.
   * @return the value of the value/expression annotation attribute
   */
  @AliasFor("expression")
  String value() default "";

  /**
   * Returns the message code to use to get an internationalized expression
   * to apply based on the current {@link Locale}.
   * @return the i18n message code for the expression to apply
   */
  String message() default "";

}
