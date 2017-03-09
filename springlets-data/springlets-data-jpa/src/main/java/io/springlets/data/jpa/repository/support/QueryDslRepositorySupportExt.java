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

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.OrderSpecifier.NullHandling;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;

import io.springlets.data.domain.GlobalSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Base class with additional utilities for implementing repositories using QueryDsl library.
 *
 * @param <T> the entity class to manage in the repository
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
public class QueryDslRepositorySupportExt<T> extends QueryDslRepositorySupport {

  private static final Logger LOG = LoggerFactory.getLogger(QueryDslRepositorySupportExt.class);

  private final Class<T> domainClass;
  private PathBuilder<Object> entityIdPath = null;

  /**
   * Creates a new {@link QueryDslRepositorySupport} instance for the given domain type.
   *
   * @param domainClass must not be {@literal null}.
   */
  public QueryDslRepositorySupportExt(Class<T> domainClass) {
    super(domainClass);
    this.domainClass = domainClass;
  }

  /**
   * Applies the given {@link Pageable} to the given {@link JPQLQuery}.
   *
   * @param pageable the ordering and paging information
   * @param query the query to apply to
   * @return the updated query
   */
  protected JPQLQuery<T> applyPagination(Pageable pageable, JPQLQuery<T> query) {
    return getQuerydsl().applyPagination(pageable, query);
  }

  /**
   * Applies the given {@link Pageable} to the given {@link JPQLQuery}.
   * Allows to map the attributes to order as provided in the {@link Pageable}
   * to real entity attributes. This might be used to work with projections
   * or DTOs whose attributes don't have the same name as the entity ones.
   *
   * It allows to map to more than one entity attribute. As an example, if
   * the DTO used to create the {@link Pageable} has a fullName attribute, you
   * could map that attribute to two entity attributes: name and surname.
   * In this case, the {@link Pageable} defines an order by a fullName
   * attribute, but que query will order by name and surname instead.
   *
   * @param pageable the ordering and paging
   * @param query
   * @param mapping definition of a mapping of order attribute names to
   *        real entity ones
   * @return the updated query
   */
  protected JPQLQuery<T> applyPagination(Pageable pageable, JPQLQuery<T> query,
      AttributeMappingBuilder mapping) {
    return applyPagination(pageable, query, mapping.asMap());
  }

  /**
   * Applies the given {@link Pageable} to the given {@link JPQLQuery}.
   * Allows to map the attributes to order as provided in the {@link Pageable}
   * to real entity attributes. This might be used to work with projections
   * or DTOs whose attributes don't have the same name as the entity ones.
   *
   * It allows to map to more than one entity attribute. As an example, if
   * the DTO used to create the {@link Pageable} has a fullName attribute, you
   * could map that attribute to two entity attributes: name and surname.
   * In this case, the {@link Pageable} defines an order by a fullName
   * attribute, but que query will order by name and surname instead.
   *
   * @param pageable the ordering and paging
   * @param query
   * @param attributeMapping definition of a mapping of order attribute names
   *        to real entity ones
   * @return the updated query
   */
  protected JPQLQuery<T> applyPagination(Pageable pageable, JPQLQuery<T> query,
      Map<String, Path<?>[]> attributeMapping) {

    if (pageable == null) {
      return query;
    }

    Pageable mappedPageable = null;
    Sort sort = pageable.getSort();
    if (sort != null) {
      List<Sort.Order> mappedOrders = new ArrayList<Sort.Order>();
      for (Sort.Order order : sort) {
        if (!attributeMapping.containsKey(order.getProperty())) {
          LOG.warn(
              "The property (%1) is not included in the attributeMapping, will order "
                  + "using the property as it is",
              order.getProperty());
          mappedOrders.add(order);
        } else {
          Path<?>[] paths = attributeMapping.get(order.getProperty());
          for (Path<?> path : paths) {
            Sort.Order mappedOrder =
                new Sort.Order(order.getDirection(), preparePropertyPath(path));
            mappedOrders.add(mappedOrder);
          }
        }
      }
      if (mappedOrders.isEmpty()) {
        // No properties to order by are available, so don't apply ordering and return the query
        // as it is
        return query;
      }
      mappedPageable =
          new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(mappedOrders));
      return applyPagination(mappedPageable, query);
    } else {
      return applyPagination(pageable, query);
    }

  }

  /**
   * Creates a factory to easily build an attribute mapping to
   * real entity attribute names.
   * @return the mapping factory
   */
  protected AttributeMappingBuilder buildMapper() {
    return new AttributeMappingBuilder();
  }

  /**
   * Adds to a query an order by the entity identifier related to this repository.
   * This is useful as the default last order in queries where pagination is
   * applied, so you have always an absolute order. Otherwise, the order
   * of the results depends on the database criteria, which might change
   * even between pages, returning confusing results for the user.
   * @param query
   * @return the updated query
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected JPQLQuery<T> applyOrderById(JPQLQuery<T> query) {
    PathBuilder<Object> idPath = getEntityId();

    return query.orderBy(new OrderSpecifier(Order.ASC, idPath, NullHandling.NullsFirst));
  }

  /**
   * Returns the path of entity identifier field
   *
   * @return path of entity Identifier
   */
  protected PathBuilder<Object> getEntityId() {
    if (entityIdPath == null) {
      EntityType<T> entity = getEntityMetaModel();
      SingularAttribute<?, ?> id = entity.getId(entity.getIdType().getJavaType());
      entityIdPath = getBuilder().get(id.getName());
    }
    return entityIdPath;
  }

  private EntityType<T> getEntityMetaModel() {
    Metamodel metamodel = getEntityManager().getMetamodel();
    EntityType<T> entity = metamodel.entity(domainClass);
    return entity;
  }

  /**
   * Adds a global contains text filter on the provided attributes.
   * WARNING: this creates a very inefficient query. If you have many entity
   * instances to query, use instead an indexed text search solution for better
   * performance.
   * @param text the text to look for
   * @param query
   * @param globalSearchAttributes the list of attributes to perform the
   *        filter on
   * @return the updated query
   */
  protected JPQLQuery<T> applyGlobalSearch(String text, JPQLQuery<T> query,
      Path<?>... globalSearchAttributes) {
    if (text != null && !StringUtils.isEmpty(text) && globalSearchAttributes.length > 0) {
      BooleanBuilder searchCondition = new BooleanBuilder();
      for (int i = 0; i < globalSearchAttributes.length; i++) {
        Path<?> path = globalSearchAttributes[i];
        if (path instanceof StringPath) {
          StringPath stringPath = (StringPath) path;
          searchCondition.or(stringPath.containsIgnoreCase(text));
        } else if (path instanceof NumberExpression) {
          searchCondition.or(((NumberExpression<?>) path).like("%".concat(text).concat("%")));
        }
      }
      return query.where(searchCondition);
    }
    return query;
  }

  /**
   * Adds a global contains text filter on the provided attributes.
   * WARNING: this creates a very inefficient query. If you have many entity
   * instances to query, use instead an indexed text search solution for better
   * performance.
   * @param globalSearch Contains the text to look for
   * @param query
   * @param globalSearchAttributes the list of attributes to perform the
   *        filter on
   * @return the updated query
   */
  protected JPQLQuery<T> applyGlobalSearch(GlobalSearch globalSearch, JPQLQuery<T> query,
      Path<?>... globalSearchAttributes) {
    if (globalSearch != null) {
      String txt = globalSearch.getText();
      return applyGlobalSearch(txt, query, globalSearchAttributes);
    }
    return query;
  }

  /**
   * Loads a page of data with the provided pagination criteria. It allows to
   * load full entities as well as projections.
   *
   * TODO: the current implementation expects the query to have already applied
   * the paging and sorting criteria, which is not what one could expect from
   * the method signature.
   *
   * Sample loading entities:
   *
   * <pre class="code">
   * loadPage(query, pageable, QEmployee.employee);
   * </pre>
   *
   * Sample with a projection:
   *
   * <pre class="code">
   * loadPage(query, pageable, Projections.constructor(EmployeeInfo.class,
   *    employee.id, employee.firstName, employee.lastName, employee.phone, employee.extension,
   *    employee.supervisor.id, employee.supervisor.firstName, employee.supervisor.lastName));
   * </pre>
   *
   * @param <M> the data type to load, usually a JPA Entity or a projection bean
   * @param query the query with the pagination and ordering criteria already applied
   * @param pageable the already applied pagination and ordering criteria
   * @param expression the entity or projection to build with the query data
   * @return the loaded data page
   */
  protected <M> Page<M> loadPage(JPQLQuery<T> query, Pageable pageable, Expression<M> expression) {
    long totalFound = query.fetchCount();
    List<M> results = query.select(expression).fetch();
    return new PageImpl<M>(results, pageable, totalFound);
  }

  /**
   * Recursively creates a dot-separated path for the property path.
   *
   * @param path must not be {@literal null}.
   * @return
   */
  private static String preparePropertyPath(Path<?> path) {

    Path<?> root = path.getRoot();

    return root == null || path.equals(root) ? path.toString()
        : path.toString().substring(root.toString().length() + 1);
  }

  /**
   * Factory to create a mapping between simulated attributes and the entity
   * real ones.
   */
  public static class AttributeMappingBuilder {

    private final Map<String, Path<?>[]> attrMapping = new HashMap<String, Path<?>[]>();

    /**
     * Adds a mapping between a virtual attribute and one or more
     * real ones.
     * As an example, if there is a DTO with a {@code fullName} attribute,
     * you could map it to the entity {@code name} and {@code surname}
     * ones.
     * @param attribute the virtual attribe
     * @param realAttributes the list of real entity attributes to order by instead
     * @return the mapping factory
     */
    public AttributeMappingBuilder map(String attribute, Path<?>... realAttributes) {
      attrMapping.put(attribute, realAttributes);
      return this;
    }

    /**
     * Returns a {@link Map} with the attribute mappings added to the factory.
     * @return the attribute mappings
     */
    public Map<String, Path<?>[]> asMap() {
      return Collections.unmodifiableMap(attrMapping);
    }
  }
}
