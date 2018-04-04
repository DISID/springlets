package io.springlets.web.mvc.util.concurrency;

/**
 * Callback interface for concurrency code. Used with {@link ConcurrencyTemplate}'s
 * {@code executeWithOcc} method, often as anonymous class within a method implementation.
 *
 * @param <T>
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public interface ConcurrencyCallback<T> {

    /**
     * Gets called by {@link ConcurrencyTemplate#execute(ConcurrencyCallback)}.
     *
     * @return a result object, or {@code null}
     */
    T doInConcurrency() throws Exception;
}
