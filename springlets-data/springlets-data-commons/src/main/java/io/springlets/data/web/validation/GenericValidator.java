package io.springlets.data.web.validation;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;

/**
 * = GenericValidator
 * <p>
 * Implementation of the {@link Validator} interface to be able to validate
 * all the entities during the Binding process.
 *
 * @param <T>
 * @author Juan Carlos García
 */
public class GenericValidator<T> implements Validator {

    /**
     * The class that supports the validation
     */
    private Class<?> clazz;

    /**
     * The service used to validate
     */
    private ValidatorService validatorService;

    /**
     * Default constructor that receives the service to be able
     * to execute necessary operations during validation process.
     *
     * @param validatorService
     */
    public GenericValidator(Class<?> clazz, T validatorService) {
        Assert.notNull(clazz, "ERROR: You must provide a valid Class type");
        Assert.notNull(validatorService, "ERROR: You must provide a valid Validator service.");
        this.clazz = clazz;
        this.validatorService = (ValidatorService) validatorService;
    }

    /**
     * This Validator validates the type of class defined in
     * the constructor.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }

    /**
     * The validation process of this element
     *
     * @param obj
     * @param errors
     */
    @Override
    public void validate(Object obj, Errors errors) {
        Map<String, List<MessageI18n>> messages = validatorService.validate(obj);
        for (Map.Entry<String, List<MessageI18n>> element : messages.entrySet()) {
            for (MessageI18n message : element.getValue()) {
                errors.rejectValue(element.getKey(), message.label, message.values, "");
            }
        }
    }
}
