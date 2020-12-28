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
package io.springlets.data.jpa.repository.support;

import io.springlets.data.jpa.repository.DetachableJpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;

import javax.persistence.EntityManager;

/**
 * {@link JpaRepository} extension to allow to get detached entities using the 
 * {@link EntityManager#detach(Object)} method.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class DetachableJpaRepositoryImpl<T, ID extends Serializable>
    extends SimpleJpaRepository<T, ID> implements DetachableJpaRepository<T, ID> {

  private final EntityManager entityManager;

  /**
   * Creates a new {@link DetachableJpaRepositoryImpl} to manage objects of the given
   * {@link JpaEntityInformation}.
   * 
   * @param entityInformation must not be {@literal null}.
   * @param entityManager must not be {@literal null}.
   */
  public DetachableJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  /**
   * Creates a new {@link DetachableJpaRepositoryImpl} to manage objects of the given domain type.
   * 
   * @param domainClass must not be {@literal null}.
   * @param em must not be {@literal null}.
   */
  public DetachableJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
    super(domainClass, em);
    this.entityManager = em;
  }

  @Override
  public T findOneDetached(ID id) {
    T entity = getOne(id);
    if (entity != null) {
      entityManager.detach(entity);
    }
    return entity;
  }

}
