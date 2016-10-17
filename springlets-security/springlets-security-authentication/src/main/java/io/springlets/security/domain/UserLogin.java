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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * = _UserLogin_ Entity
 *
 * JPA Entity that models the user identification data and whose
 * instances can be loaded and stored in a database. 
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Entity
@Table(name = "${springlets.security.domain.users-login.table-name:USERS_LOGIN}")
public class UserLogin {

  @Column(name = "${springlets.security.domain.users-login.id-column-name:USER_LOGIN_ID}")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userLoginGen")
  @Id
  @SequenceGenerator(name = "userLoginGen",
      sequenceName = "${springlets.security.domain.users-login.sequence-name:SEQ_USERS_LOGIN}")
  private Long id;

  @Column(name = "${springlets.security.domain.users-login.version-column-name:VERSION}")
  @Version
  private long version;

  @Column(name = "${springlets.security.domain.users-login.username-column-name:USERNAME}",
      unique = true)
  @NotNull
  @Size(max = 30)
  @Pattern(regexp = "^[^<>\\\\'\"&;%]*$")
  private String username;

  @Column(name = "${springlets.security.domain.users-login.password-column-name:PASSWORD}")
  @NotNull
  @Size(max = 255)
  private String password;

  @Column(name = "${springlets.security.domain.users-login.from-date-column-name:FROM_DATE}")
  @DateTimeFormat(pattern = "${springlets.security.domain.users-login.from-date-pattern:dd/MM/yyyy}")
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar fromDate;

  @Column(name = "${springlets.security.domain.users-login.thru-date-column-name:THRU_DATE}")
  @DateTimeFormat(pattern = "${springlets.security.domain.users-login.thru-date-pattern:dd/MM/yyyy}")
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar thruDate;

  @Column(
      name = "${springlets.security.domain.users-login.need-change-password-column-name:NEED_CHANGE_PASSWORD}")
  private Boolean needChangePassword;

  @Column(name = "${springlets.security.domain.users-login.locked-column-name:LOCKED}")
  private Boolean locked;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "userLogin")
  private Set<UserLoginRole> userLoginRoles = new HashSet<UserLoginRole>();

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getVersion() {
    return this.version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Calendar getFromDate() {
    return this.fromDate;
  }

  public void setFromDate(Calendar fromDate) {
    this.fromDate = fromDate;
  }

  public Calendar getThruDate() {
    return this.thruDate;
  }

  public void setThruDate(Calendar thruDate) {
    this.thruDate = thruDate;
  }

  public Set<UserLoginRole> getUserLoginRoles() {
    return this.userLoginRoles;
  }

  public void setUserLoginRoles(Set<UserLoginRole> userLoginRoles) {
    this.userLoginRoles = userLoginRoles;
  }

  public Boolean getNeedChangePassword() {
    return needChangePassword;
  }

  public void setNeedChangePassword(Boolean needChangePassword) {
    this.needChangePassword = needChangePassword;
  }

  public Boolean getLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  @Override
  public boolean equals(Object obj) {

    if (id == null) {
      return super.equals(obj);
    }

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    UserLogin other = (UserLogin) obj;
    if (!id.equals(other.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    if (id == null) {
      return super.hashCode();
    }
    final int prime = 31;
    int result = 17;
    result = prime * result + id.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "UserLogin {" + "id='" + id + '\'' + ", username='" + username + '\'' + ", fromDate='"
        + fromDate + '\'' + ", thruDate='" + thruDate + '\'' + ", needChangePassword='"
        + needChangePassword + '\'' + ", locked='" + locked + '\'' + ", version='" + version + '\''
        + "} " + super.toString();
  }


}
