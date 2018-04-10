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
package io.springlets.security.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * Entity that represents relations between users and roles.
 *
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Entity
@Table(name = "USER_LOGIN_ROLE")
public class UserLoginRole {

  @Id
  @Column(name = "USER_LOGIN_ROLE_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userLoginRoleGen")
  @SequenceGenerator(name = "userLoginRoleGen", sequenceName = "SEQ_USER_LOGIN_ROLES")
  private Long id;

  @Column(name = "VERSION")
  @Version
  private long version;

  @JoinColumn(name = "USER_LOGIN")
  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private UserLogin userLogin;

  @JoinColumn(name = "LOGIN_ROLE")
  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private LoginRole loginRole;

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

  public UserLogin getUserLogin() {
    return this.userLogin;
  }

  public void setUserLogin(UserLogin userLogin) {
    this.userLogin = userLogin;
  }

  public LoginRole getLoginRole() {
    return this.loginRole;
  }

  public void setLoginRole(LoginRole loginRole) {
    this.loginRole = loginRole;
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

    UserLoginRole other = (UserLoginRole) obj;
    return id.equals(other.id);
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
    return "UserLoginRole {" + "id='" + id + '\'' + ", version='" + version + '\'' + "} "
        + super.toString();
  }



}
