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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * JPA Entity that models the user roles.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Entity
@Table(name = "LOGIN_ROLE")
public class LoginRole {

  @Column(name = "LOGIN_ROLE_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loginRoleGen")
  @Id
  @SequenceGenerator(name = "loginRoleGen", sequenceName = "SEQ_LOGIN_ROLE")
  private Long id;

  @Column(name = "VERSION")
  @Version
  private long version;

  @Column(name = "NAME", unique = true)
  @NotNull
  @Size(max = 20)
  private String name;

  @Column(name = "DESCRIPTION")
  @NotNull
  @Size(max = 50)
  private String description;

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

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
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

    LoginRole other = (LoginRole) obj;
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
    return "LoginRole {" + "id='" + id + '\'' + ", name='" + name + '\'' + ", description='"
        + description + '\'' + ", version='" + version + '\'' + "} " + super.toString();
  }

}
