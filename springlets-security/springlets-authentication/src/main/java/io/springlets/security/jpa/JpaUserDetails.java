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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.springlets.security.jpa.domain.UserLoginInfo;

/**
 * Models core user information retrieved by a JpaUserDetailsService.
 * 
 * Developers may use this class directly, subclass it, or write their own UserDetails 
 * implementation from scratch. 
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class JpaUserDetails implements UserDetails, CredentialsContainer {

  private static final long serialVersionUID = 60483409069826371L;
  private Long id;
  private String username;
  private String password;
  private boolean enabled;
  private boolean expired;
  private Collection<? extends GrantedAuthority> roles;

  public JpaUserDetails(UserLoginInfo user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.enabled = user.isEnabled();
    this.expired = user.isExpired();

    if (user.getUserLoginRoles() == null) {
      this.roles = Collections.emptySet();
    } else {
      Set<SimpleGrantedAuthority> roleSet =
          new HashSet<SimpleGrantedAuthority>(user.getUserLoginRoles().size());
      for (String role : user.getUserLoginRoles()) {
        roleSet.add(new SimpleGrantedAuthority(role));
      }
      this.roles = roleSet;
    }
  }

  public Long getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !expired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void eraseCredentials() {
    this.password = null;
  }

  @Override
  public boolean equals(Object otherUser) {
    if (otherUser instanceof JpaUserDetails) {
      return this.username.equals(((JpaUserDetails) otherUser).getUsername());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }


}
