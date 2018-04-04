package io.springlets.web.mvc.util.concurrency;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * = ConcurrencyManager
 * <p>
 * Interface that defines all the operations that should be implemented by a controller that wants to
 * manage concurrency.
 *
 * @param <T> The entity managed by the annotated controller.
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public interface ConcurrencyManager<T> {

    /**
     * All the controllers that implements ConcurrencyManager must implement a method that returns a String with the
     * name of the entity used in the model. Ex:
     * <p>
     * model.addAttribute(getModelName(), entity);
     *
     * @return
     */
    String getModelName();

    /**
     * All the controllers that implements ConcurrencyManager must implement a method that returns a String with the
     * path of the edit view. Ex:
     * <p>
     * "entities/edit"
     *
     * @return
     */
    String getEditViewPath();

    /**
     * All the controllers that implements ConcurrencyManager must implement a method that returns the last version of
     * the provided record.
     *
     * @param record
     * @return
     */
    Integer getLastVersion(T record);

    /**
     * All the controllers that implements ConcurrencyManager must implement a populateForm method to ensure that the
     * populateConcurrencyForm method is able to populate a complete edit form.
     *
     * @param model
     */
    void populateForm(Model model);

    /**
     * All the controllers that implements ConcurrencyManager could implement a populateConcurrencyForm method to ensure
     * that the edit form is populate correctly when a Concurrency Exception appears.
     * <p>
     * This method has a default implementation for all the controllers that implements the ConcurrencyManager interface,
     * so if you want the default behavior when a Concurrency Exception appears, you don't need to implement it.
     * <p>
     * If you want to modify this implementation and populate your form with your custom attributes, you must implement
     * the method again in your controller.
     *
     * @param entity
     * @return
     */
    ModelAndView populateAndGetFormView(T entity, Model model);
}
