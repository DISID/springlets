package io.springlets.web.mvc.util.concurrency;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

/**
 * = ConcurrencyException
 * <p>
 * Exception that extends the {@link ObjectOptimisticLockingFailureException} including some extra information
 * to be able to manage concurrency in the view layer.
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
public class ConcurrencyException extends ObjectOptimisticLockingFailureException
{

  private static final long serialVersionUID = -5653262866014061733L;

  /**
     * The controller that has throw the exception. This is necessary because the controller
     * is the only one who knows how to populate the edit form.
     */
  @SuppressWarnings( "rawtypes" )
  private final transient ConcurrencyManager manager;

  /**
   * The record that has caused the concurrency exception.
   */
  private final transient Object record;

  /**
   * The model of the view layer where all the attributes should be loaded to be able to populate
   * a correct edit form view.
   */
  private final transient Model model;


  /**
   * Default an unique constructor. It must provide valid values for Controller, record and model. Also, it receives
   * the {@link ObjectOptimisticLockingFailureException} that has thrown this exception to maintain all the provided
   * information.
   *
   * @param manager
   * @param record
   * @param model
   * @param ex
   */
  public <T> ConcurrencyException( ConcurrencyManager<T> manager, T record, Model model,
      ObjectOptimisticLockingFailureException ex )
  {
    super( ex.getPersistentClass(), ex.getIdentifier(), ex.getMessage(), ex.getCause() );
    Assert.notNull( manager, "ERROR: You must provide a not null controller to throw this exception." );
    Assert.notNull( record, "ERROR: You must provide a not null record to throw this exception." );
    Assert.notNull( model, "ERROR: You must provide a not null model to throw this exception." );
    this.manager = manager;
    this.record = record;
    this.model = model;
  }

  @SuppressWarnings( "unchecked" )
  public ModelAndView populateAndGetFormView()
  {
    return manager.populateAndGetFormView( record, model );
  }
}
