package io.springlets.data.web.validation;

import java.util.List;
import java.util.Map;

/**
 * = ValidatorService
 * <p>
 * API that defines operations to validate entities
 *
 * @param <T> Entity that will be validated
 * @author Juan Carlos García
 */
public interface ValidatorService<T> {

    /**
     * Defines an operation to validate the provided entity.
     *
     * @param entity
     * @return
     */
    Map<String, List<MessageI18n>> validate(T entity);
}