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

import io.springlets.security.domain.UserLogin;
import io.springlets.security.domain.UserLoginDetails;
import io.springlets.security.repository.UserLoginRepository;
import io.springlets.security.service.api.UserLoginService;

/**
 * = Implementation if {@link UserLoginService} API 
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Service
@Transactional(readOnly = true)
public class UserLoginServiceImpl implements UserLoginService {

  private final UserLoginRepository repository;

  @Autowired
  public UserLoginServiceImpl(@Lazy UserLoginRepository repo) {
    this.repository = repo;
  }

  // === CRUD Methods

  @Override
  @Transactional
  public UserLogin save(UserLogin userLogin) {
    return repository.save(userLogin);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.delete(id);
  }

  // === Batch CRUD Methods

  @Override
  @Transactional
  public List<UserLogin> save(Iterable<UserLogin> userLogins) {
    return repository.save(userLogins);
  }

  @Override
  @Transactional
  public void delete(Iterable<Long> ids) {
    List<UserLogin> toDelete = repository.findAll(ids);
    repository.deleteInBatch(toDelete);
  }

  // === Finders

  @Override
  public List<UserLogin> findAll() {
    return repository.findAll();
  }

  @Override
  public List<UserLogin> findAll(Iterable<Long> ids) {
    return repository.findAll(ids);
  }

  @Override
  public UserLogin findOne(Long id) {
    return repository.findOne(id);
  }

  @Override
  public UserLogin findByUsername(String username) {
    return repository.findByUsername(username);
  }

  @Override
  public UserLogin findByActiveUsername(String username) {
    return repository.findByActiveUsername(username);
  }

  @Override
  public UserLoginDetails findDetailsByUsername(String username) {
    return repository.findDetailsByName(username);
  }

  @Override
  @Transactional
  public UserLogin lock(String username) {
    UserLogin userLogin = repository.findByUsername(username);
    userLogin.setLocked(true);
    return repository.save(userLogin);
  }

  @Override
  public Long countByName(String username) {
    return repository.countByName(username);
  }


}
