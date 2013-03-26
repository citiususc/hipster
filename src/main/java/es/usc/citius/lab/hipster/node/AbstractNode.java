package es.usc.citius.lab.hipster.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements the basic operations for a serch node:
 * <ul>
 * <li> obtains the path from current state to the begin
 * <li> obtains the previous node to current
 * <li> obtains the {@link Transition} to following state
 * </ul>
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public abstract class AbstractNode<S> implements Node<S> {

    protected Transition<S> state;
    protected Node<S> previousNode = null;

    /**
     * Default constructor: Requires the previous {@link Node} and the
     * transition done to access this new one.
     *
     * @param transition incoming transition
     * @param previousNode previous node
     */
    public AbstractNode(Transition<S> transition, Node<S> previousNode) {
        this.previousNode = previousNode;
        this.state = transition;
    }

    public List<Node<S>> path() {
        List<Node<S>> path = new ArrayList<Node<S>>();
        Node<S> current = this;
        while (current != null) {
            path.add(current);
            current = current.previousNode();
        }
        Collections.reverse(path);
        return path;
    }

    public Node<S> previousNode() {
        return this.previousNode;
    }

    public Transition<S> transition() {
        return this.state;
    }
}
