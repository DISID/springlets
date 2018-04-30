package io.springlets.data.web.validation;

/**
 * = MessageI18n
 * <p>
 * Structure which holds a message which should be use with a message resolver
 *
 * @author Juan Carlos García
 */
public final class MessageI18n {

    /**
     * The label that will be used in the message
     */
    public final String label;

    /**
     * The possible values that will be replaced by the label
     * values.
     */
    public final Object[] values;

    /**
     * Default constructor
     *
     * @param label
     * @param values
     */
    public MessageI18n(String label, Object... values) {
        this.label = label;
        this.values = values;
    }

    /**
     * When some MessageI18n is represented in an String, the label
     * will be displayed.
     *
     * @return
     */
    @Override
    public String toString() {
        return label;
    }
}