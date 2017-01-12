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
package io.springlets.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * {@link JpaRepository} extension to allow to get detached entities.
 * 
 * This is useful if you want
 * to make changes to an entity but you don't want those changes to be automatically persisted.
 * 
 * For example, if you have the open session in view option active (by default in Spring Boot), 
 * and you want to use the JPA optimistic locking through the version field, coming from a form
 * for example, and you want the binding to be performed on a loaded entity, it has to be a detached
 * one.
 * 
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@NoRepositoryBean
public interface DetachableJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

  /**
   * Retrieves an entity by its id and detaches it from the EntityManager.
   * 
   * @param id must not be {@literal null}.
   * @return the entity with the given id or {@literal null} if none found
   * @throws IllegalArgumentException if {@code id} is {@literal null}
   */
  T findOneDetached(ID id);

}
