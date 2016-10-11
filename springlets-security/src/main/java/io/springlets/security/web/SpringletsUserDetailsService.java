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
package io.springlets.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.springlets.security.domain.UserLoginDetails;
import io.springlets.security.service.api.UserLoginService;

/**
 * = Integration of Spring Security
 * 
 * UserDetailsService implementation of Spring Security that loads data
 * authentication and user authorization from the domain model.
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Service
public class SpringletsUserDetailsService implements UserDetailsService {

  private final UserLoginService userLoginService;

  @Autowired
  public SpringletsUserDetailsService(UserLoginService userLoginService) {
    this.userLoginService = userLoginService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserLoginDetails details = userLoginService.findDetailsByUsername(username);
    if (details == null) {
      throw new UsernameNotFoundException(username);
    }
    return new SpringletsUserDetails(details);
  }


}
