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
package io.springlets.security.jpa.service.api;

import java.util.List;

import io.springlets.security.jpa.domain.LoginRole;

/**
 * 
 * = API of the service related with the entity {@link LoginRole}
 *
 * Interface that defines the operations related with the entity {@link LoginRole}.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public interface LoginRoleService {

  //=== CRUD Methods

  LoginRole save(LoginRole loginRole);

  void delete(Long id);

  // === Batch CRUD Methods

  List<LoginRole> save(Iterable<LoginRole> loginRoles);

  void delete(Iterable<Long> ids);

  // === Finders

  List<LoginRole> findAll();

  List<LoginRole> findAll(Iterable<Long> ids);

  LoginRole findOne(Long id);

  LoginRole findByName(String name);

}
