package es.usc.citius.lab.hipster.node;

import java.util.List;

/**
 * Basic search sctructure: adds search information to the state.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public interface Node<S> {

    /**
     * Builds the path from the begining state to the current one.
     *
     * @return list of nodes that forms the path
     */
    public List<Node<S>> path();

    /**
     * Returns the previous node to the current
     *
     * @return instance of {@link Node}
     */
    public Node<S> previousNode();

    /**
     * Returns the transition to access this node
     *
     * @return instance of {@link Transition}
     */
    public Transition<S> transition();
}
