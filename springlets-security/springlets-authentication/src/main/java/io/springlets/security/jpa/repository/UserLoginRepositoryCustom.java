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
package io.springlets.security.jpa.repository;

import io.springlets.security.jpa.domain.UserLogin;
import io.springlets.security.jpa.domain.UserLoginInfo;

/**
 * Interface that defines dynamic queries for repository related with 
 * {@link UserLogin} entity.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public interface UserLoginRepositoryCustom {

  /**
   * Obtains a DTO with the authentication and authorization data
   * associated to an user by the provided username.
   * 
   * @param username
   * @return
   */
  UserLoginInfo findDetailsByName(String username);

}
