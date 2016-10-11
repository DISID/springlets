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
package io.springlets.security.domain;

import java.util.Calendar;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * = _UserLoginDetails_ DTO
 * 
 * Data Transfer Object used to load the necessary user data during authentication and
 * authorization process.
 *
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class UserLoginDetails {

  private final Long id;

  private final String username;

  private final String password;

  @DateTimeFormat(
      pattern = "${springlets.security.domain.users-login.from-date-pattern:dd/MM/yyyy}")
  private final Calendar fromDate;

  @DateTimeFormat(
      pattern = "${springlets.security.domain.users-login.thru-date-pattern:dd/MM/yyyy}")
  private final Calendar thruDate;

  private final Boolean locked;

  private final Set<String> userLoginRoles;

  public UserLoginDetails(Long id, String username, String password, Calendar fromDate,
      Calendar thruDate, Boolean locked, Set<String> userLoginRoles) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.fromDate = fromDate;
    this.thruDate = thruDate;
    this.locked = locked;
    this.userLoginRoles = userLoginRoles;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Calendar getFromDate() {
    return fromDate;
  }

  public Calendar getThruDate() {
    return thruDate;
  }

  public Boolean getLocked() {
    return locked;
  }

  public Set<String> getUserLoginRoles() {
    return userLoginRoles;
  }

  public boolean isEnabled() {
    return (locked || fromDate == null) ? false : fromDate.before(now());
  }

  public boolean isExpired() {
    return thruDate == null ? false : thruDate.before(now());
  }

  private Calendar now() {
    return Calendar.getInstance();
  }


}
