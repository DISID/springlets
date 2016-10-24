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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * = Spring Data JPA repository for {@link UserLogin} entity
 * 
 * To get more info about Spring Data JPA repositories:
 * 
 * * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories
 *
 * @see JPARepository
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Transactional(readOnly = true)
public interface UserLoginRepository
    extends JpaRepository<UserLogin, Long>, UserLoginRepositoryCustom {

  /**
   * Obtains an UserLogin by its username
   * 
   * @param username name of the user
   * @return UserLogin entity or null if there's not exists any UserLogin with the
   *         provided username
   */
  UserLogin findByUsername(String username);

  /**
   * Obtains an UserLogin by its username only if is active
   * 
   * @param username name of the user
   * @return UserLogin entity or null if there's not exists any active UserLogin with the
   *         provided username
   */
  @Query("SELECT u FROM UserLogin u WHERE u.username = :username AND u.locked = false AND u.fromDate <= CURRENT_DATE AND (u.thruDate > CURRENT_DATE OR u.thruDate IS NULL)")
  UserLogin findByActiveUsername(@Param("username") String username);

  /**
   * Counts all UserLogin with the provided username
   * 
   * @param username name of the user
   * @return number of users with the given name
   */
  Long countByUsername(String username);
}
