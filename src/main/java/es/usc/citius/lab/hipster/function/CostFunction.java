package es.usc.citius.lab.hipster.function;

import es.usc.citius.lab.hipster.node.Transition;

/**
 * Interface that defines the function cost used to evaluate the cost of a
 * successor.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * @since 26/03/2013
 * @version 1.0
 */
public interface CostFunction<S, T> {

    /**
     * Calculates the cost of moving from the current state to the goal using a
     * given<code>Transition</code>.
     *
     * @param transition action to perform
     * @return cost of the change of state
     */
    public T evaluate(Transition<S> transition);
}
