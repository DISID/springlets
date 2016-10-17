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
package io.springlets.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.springlets.security.domain.UserLoginRole;
import io.springlets.security.repository.UserLoginRoleRepository;
import io.springlets.security.service.api.UserLoginRoleService;

/**
 * = {@link UserLoginRoleService} implementation 
 * 
 * {@link UserLoginRoleService} default implementation used and configured by
 * the Springlets Boot Autoconfiguration to load and store UserLoginRole 
 * from repositories.
 * 
 * CRUD methods are transactional by default. For reading operations the 
 * transaction configuration readOnly flag is set to true, all others are 
 * configured with a plain {@link Transactional} so that default transaction 
 * configuration applies. 
 * 
 * Developers may use this class directly, subclass it, or write their own 
 * UserLoginRoleService implementation from scratch.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Service
@Transactional(readOnly = true)
public class UserLoginRoleServiceImpl implements UserLoginRoleService {

  private final UserLoginRoleRepository repository;

  @Autowired
  public UserLoginRoleServiceImpl(@Lazy UserLoginRoleRepository repo) {
    this.repository = repo;
  }

  //=== CRUD Methods

  @Override
  @Transactional
  public UserLoginRole save(UserLoginRole userLoginRole) {
    return repository.save(userLoginRole);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.delete(id);
  }

  //=== Batch CRUD Methods

  @Override
  @Transactional
  public List<UserLoginRole> save(Iterable<UserLoginRole> userLoginRoles) {
    return repository.save(userLoginRoles);
  }

  @Override
  @Transactional
  public void delete(Iterable<Long> ids) {
    List<UserLoginRole> toDelete = repository.findAll(ids);
    repository.deleteInBatch(toDelete);
  }

  //=== Finders

  @Override
  public List<UserLoginRole> findAll() {
    return repository.findAll();
  }

  @Override
  public List<UserLoginRole> findAll(Iterable<Long> ids) {
    return repository.findAll(ids);
  }

  @Override
  public UserLoginRole findOne(Long id) {
    return repository.findOne(id);
  }


}
