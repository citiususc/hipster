package es.usc.citius.lab.hipster.node;

/**
 * Interface defining the builder to create instances
 * of {@link Node}.
 * 
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @param <N> class defining the node
 * @since 26/03/2013
 * @version 1.0
 */
public interface NodeBuilder<S, N extends Node<S>> {

    /**
     * Builds a node from the current one and the incoming action
     * to reach it.
     * @param from incoming node
     * @param transition incoming transition
     * @return 
     */
    public N node(N from, Transition<S> transition);
}
