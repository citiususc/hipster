package es.usc.citius.lab.hipster.function;

/**
 * Interface that must implement all heuristic functions to be used in search
 * processes.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * @since 26/03/2013
 * @version 1.0
 */
public interface HeuristicFunction<S, T> {

    /**
     * Obtains the minimum estimted cost of traversing from current state to any
     * goal.
     *
     * @param state current state
     * @return cost estimation
     */
    public T estimate(S state);
}
