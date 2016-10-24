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
package io.springlets.security.jpa;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.springlets.security.jpa.domain.UserLoginInfo;
import io.springlets.security.jpa.service.api.UserLoginService;

/**
 * UserDetailsService implementation that loads user data credentials
 * represented as JPA entities via a service layer plus Spring Data Repositories.
 * 
 * This service has no other function other to load that data for use by other 
 * components within the authentication process. It is not responsible for authenticating 
 * the user. Authenticating a user with a username/password combination is most commonly 
 * performed by the {@link JpaAuthenticationProvider}, which is injected with a 
 * UserDetailsService to allow it to load the password (and other data) for a user in order 
 * to compare it with the submitted value. 
 *  
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class JpaUserDetailsService implements UserDetailsService {

  private final UserLoginService userLoginService;

  public JpaUserDetailsService(UserLoginService userLoginService) {
    this.userLoginService = userLoginService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserLoginInfo details = userLoginService.findDetailsByUsername(username);
    if (details == null) {
      throw new UsernameNotFoundException(username);
    }
    return new JpaUserDetails(details);
  }


}
