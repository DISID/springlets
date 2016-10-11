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
package io.springlets.security.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.JPQLQuery;

import io.springlets.security.domain.QUserLogin;
import io.springlets.security.domain.UserLogin;
import io.springlets.security.domain.UserLoginDetails;
import io.springlets.security.domain.UserLoginRole;

/**
 * Implementation of dynamic queries defiend on {@link UserLoginRepositoryCustom} interface
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Transactional(readOnly = true)
public class UserLoginRepositoryImpl extends QueryDslRepositorySupport
    implements UserLoginRepositoryCustom {

  public UserLoginRepositoryImpl() {
    super(UserLogin.class);
  }

  @Override
  public UserLoginDetails findDetailsByName(String username) {

    QUserLogin user = QUserLogin.userLogin;
    UserLogin userLogin = from(user).where(user.username.eq(username)).fetchOne();
    UserLoginDetails details = null;
    if (userLogin != null) {
      Set<UserLoginRole> userLoginRoles = userLogin.getUserLoginRoles();
      Set<String> roleNames = null;
      if (userLoginRoles != null) {
        roleNames = new HashSet<String>(userLoginRoles.size());
        for (UserLoginRole userLoginRole : userLoginRoles) {
          roleNames.add(userLoginRole.getLoginRole().getName());
        }
      }
      details =
          new UserLoginDetails(userLogin.getId(), userLogin.getUsername(), userLogin.getPassword(),
              userLogin.getFromDate(), userLogin.getThruDate(), userLogin.getLocked(), roleNames);
    }

    return details;
  }

  @Override
  public Long countByName(String username) {
    QUserLogin user = QUserLogin.userLogin;
    JPQLQuery query = from(user).where(user.username.eq(username));
    return query.fetchCount();
  }


}
