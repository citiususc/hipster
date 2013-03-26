package es.usc.citius.lab.hipster.node;

/**
 * Simplest node that can be used in search processes.
 * 
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public class SimpleNode<S> extends AbstractNode<S> {

    /**
     * Basic constructor for this node.
     * @param transition incoming transition
     * @param previousNode parent node
     */
    public SimpleNode(Transition<S> transition, Node<S> previousNode) {
        super(transition, previousNode);
    }

}
