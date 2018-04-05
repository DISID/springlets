package io.springlets.web.mvc.util.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.ui.Model;

/**
 * = ConcurrencyTemplate
 * <p>
 * Template class that simplifies programmatic concurrency demarcation and
 * concurrency exception handling.
 *
 * @param <T> Generic type that indicates the type of element that should be returned after
 *            {@link #execute(ConcurrencyCallback)} is called.
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class ConcurrencyTemplate<T> {

    // Logger to log possible concurrency exceptions
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrencyTemplate.class);

    /**
     * The element that will manage the concurrency behavior if some
     * concurrency exception appears.
     */
    private ConcurrencyManager<T> manager;

    /**
     * Default constructor that receives all the necessary parameters.
     *
     * @param manager The element that will manage the concurrency behavior if some concurrency
     *                exception appears.
     */
    public ConcurrencyTemplate(ConcurrencyManager<T> manager) {
        this.manager = manager;
    }

    /**
     * Executes the provided action. If something goes wrong and a {@link ConcurrencyException} appears during
     * the process, it delegates in the provided {@link ConcurrencyManager} to manage the concurrency behaviour.
     *
     * @param action The action that should be executed and that could produce a Concurrency Exception.
     * @return An object with the same type as the specified in the ConcurrencyTemplate constructor
     * @throws ConcurrencyTemplateException if some exception different of {@link ObjectOptimisticLockingFailureException}
     *          is throwed during concurrency management.
     */
    public T execute(T record, Model model, ConcurrencyCallback<T> action) {
        try {
            // Execute the provided action and return the result
            return action.doInConcurrency(record);
        } catch (ObjectOptimisticLockingFailureException ex) {
            // If some Concurrency Exception appears, log the error as debug level
            // and throws a custom exception that contains all the information about
            // the view layer.
            LOGGER.debug(ex.getLocalizedMessage());
            throw new ConcurrencyException(this.manager, record, model, ex);
        } catch(Exception e){
            throw new ConcurrencyTemplateException(e);
        }
    }

    /**
     * Custom exception to manage concurrency template errors
     */
    public static class ConcurrencyTemplateException extends RuntimeException{

        /**
       *
       */
      private static final long serialVersionUID = -2010317519711402983L;

        /**
         * Default constructor
         *
         * @param e
         */
        public ConcurrencyTemplateException(Exception e) {
            super(e);
        }

        public Throwable getEx() {
        	return getCause();
        }
    }
}
