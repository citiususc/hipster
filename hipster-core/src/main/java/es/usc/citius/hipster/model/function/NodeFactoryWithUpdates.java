package es.usc.citius.hipster.model.function;

/**
 * Instantiator of nodes with the functionality of updating them as well.
 * New nodes are obtained from the current state, the parent and he transition between them.
 * Existing nodes can be updated within this class.
 *
 * @param <A> action type
 * @param <S> state type
 * @param <N> node type
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface NodeFactoryWithUpdates<A, S, N> extends NodeFactory<A, S, N>{

    /**
     * Allows updating the information of a node without creating a new one.
     *
     * @param node node to be updated
     */
    public void updateNode(N node);

}
