package es.usc.citius.lab.hipster.function;

import es.usc.citius.lab.hipster.node.Transition;

/**
 * Interface defining a transition function, who obtains from a given state the
 * set of transitions that allows to access any other.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public interface TransitionFunction<S> {

    /**
     * Obtains the set of actions that performed over the current state reaching
     * any other one; that is: the neighborhood.
     *
     * @param current current state
     * @return set of available transitions to other states
     */
    public Iterable<Transition<S>> from(S current);
}
